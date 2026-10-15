package com.seminarhub.batch.delivery.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.seminarhub.batch.delivery.exception.DeliveryStatusUpdateException;
import com.seminarhub.batch.delivery.policy.DeliverySkipPolicy;
import com.seminarhub.domain.delivery.domain.Delivery;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.springframework.retry.backoff.BackOffInterruptedException;

class RetryingDeliveryItemProcessorTest {

    private final Delivery delivery = Delivery.builder().id(1L).build();
    private final AtomicInteger calls = new AtomicInteger();
    private final List<Long> delays = new ArrayList<>();

    @Test
    void returnsFirstSuccessWithoutWaiting() throws Exception {
        RetryingDeliveryItemProcessor processor = new RetryingDeliveryItemProcessor(item -> {
            calls.incrementAndGet();
            return item;
        }, delays::add);

        assertThat(processor.process(delivery)).isSameAs(delivery);
        assertThat(calls.get()).isEqualTo(1);
        assertThat(delays).isEmpty();
    }

    @Test
    void retriesTwiceWithExponentialBackoffBeforeReturningSuccess() throws Exception {
        Thread caller = Thread.currentThread();
        RetryingDeliveryItemProcessor processor = new RetryingDeliveryItemProcessor(item -> {
            assertThat(Thread.currentThread()).isSameAs(caller);
            if (calls.incrementAndGet() < 3) {
                throw new DeliveryStatusUpdateException("transient failure");
            }
            return item;
        }, delays::add);

        assertThat(processor.process(delivery)).isSameAs(delivery);
        assertThat(calls.get()).isEqualTo(3);
        assertThat(delays).containsExactly(200L, 400L);
    }

    @Test
    void propagatesTheLastFailureAfterThreeAttempts() {
        List<DeliveryStatusUpdateException> failures = new ArrayList<>();
        RetryingDeliveryItemProcessor processor = new RetryingDeliveryItemProcessor(item -> {
            DeliveryStatusUpdateException failure =
                    new DeliveryStatusUpdateException("attempt " + calls.incrementAndGet());
            failures.add(failure);
            throw failure;
        }, delays::add);

        assertThatThrownBy(() -> processor.process(delivery))
                .satisfies(failure -> assertThat(failure).isSameAs(failures.get(2)));
        assertThat(calls.get()).isEqualTo(3);
        assertThat(delays).containsExactly(200L, 400L);
    }

    @Test
    void doesNotRetryUnexpectedExceptions() {
        IllegalStateException failure = new IllegalStateException("unexpected failure");
        RetryingDeliveryItemProcessor processor = new RetryingDeliveryItemProcessor(item -> {
            calls.incrementAndGet();
            throw failure;
        }, delays::add);

        assertThatThrownBy(() -> processor.process(delivery)).isSameAs(failure);
        assertThat(calls.get()).isEqualTo(1);
        assertThat(delays).isEmpty();
    }

    @Test
    void unwrapsInterruptionAndPreservesTheSignalWithoutRetryingOrSkipping() {
        InterruptedException interruption = new InterruptedException("lookup cancelled");
        RetryingDeliveryItemProcessor processor = new RetryingDeliveryItemProcessor(item -> {
            calls.incrementAndGet();
            throw new DeliveryStatusUpdateException("lookup failed",
                    new IllegalStateException(interruption));
        }, delays::add);

        try {
            assertThatThrownBy(() -> processor.process(delivery)).isSameAs(interruption);
            assertThat(Thread.currentThread().isInterrupted()).isTrue();
            assertThat(new DeliverySkipPolicy().shouldSkip(interruption, 0)).isFalse();
            assertThat(calls.get()).isEqualTo(1);
            assertThat(delays).isEmpty();
        } finally {
            Thread.interrupted();
        }
    }

    @Test
    void doesNotStartALookupOnAnInterruptedThread() {
        RetryingDeliveryItemProcessor processor = new RetryingDeliveryItemProcessor(item -> {
            calls.incrementAndGet();
            return item;
        }, delays::add);

        Thread.currentThread().interrupt();
        try {
            assertThatThrownBy(() -> processor.process(delivery)).isInstanceOf(InterruptedException.class);
            assertThat(Thread.currentThread().isInterrupted()).isTrue();
            assertThat(calls.get()).isZero();
            assertThat(delays).isEmpty();
        } finally {
            Thread.interrupted();
        }
    }

    @Test
    void stopsWhenInterruptedDuringBackoff() {
        RetryingDeliveryItemProcessor processor = new RetryingDeliveryItemProcessor(item -> {
            calls.incrementAndGet();
            throw new DeliveryStatusUpdateException("transient failure");
        }, delay -> {
            delays.add(delay);
            throw new InterruptedException("backoff cancelled");
        });

        try {
            assertThatThrownBy(() -> processor.process(delivery))
                    .isInstanceOf(BackOffInterruptedException.class)
                    .satisfies(failure -> assertThat(new DeliverySkipPolicy().shouldSkip(failure, 0)).isFalse());
            assertThat(Thread.currentThread().isInterrupted()).isTrue();
            assertThat(calls.get()).isEqualTo(1);
            assertThat(delays).containsExactly(200L);
        } finally {
            Thread.interrupted();
        }
    }
}
