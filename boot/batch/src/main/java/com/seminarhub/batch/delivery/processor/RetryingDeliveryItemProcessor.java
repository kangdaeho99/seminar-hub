package com.seminarhub.batch.delivery.processor;

import com.seminarhub.batch.delivery.exception.DeliveryStatusUpdateException;
import com.seminarhub.domain.delivery.domain.Delivery;
import java.util.Map;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.retry.backoff.BackOffInterruptedException;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.Sleeper;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

public class RetryingDeliveryItemProcessor implements ItemProcessor<Delivery, Delivery> {

    private final ItemProcessor<Delivery, Delivery> delegate;
    private final RetryTemplate retryTemplate;

    public RetryingDeliveryItemProcessor(ItemProcessor<Delivery, Delivery> delegate) {
        this(delegate, Thread::sleep);
    }

    RetryingDeliveryItemProcessor(ItemProcessor<Delivery, Delivery> delegate, Sleeper sleeper) {
        this.delegate = delegate;

        ExponentialBackOffPolicy backOff = new ExponentialBackOffPolicy();
        backOff.setInitialInterval(200);
        backOff.setMultiplier(2);
        backOff.setMaxInterval(400);
        backOff.setSleeper(sleeper);

        retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(new SimpleRetryPolicy(
                3, Map.of(DeliveryStatusUpdateException.class, true)));
        retryTemplate.setBackOffPolicy(backOff);
    }

    @Override
    public Delivery process(Delivery delivery) throws Exception {
        try {
            return retryTemplate.execute(context -> {
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException("Delivery lookup interrupted");
                }

                try {
                    return delegate.process(delivery);
                } catch (Exception exception) {
                    // Unwrap interruption so DeliverySkipPolicy cannot mistake it for
                    // an ordinary DeliveryStatusUpdateException and skip the item.
                    for (Throwable cause = exception; cause != null; cause = cause.getCause()) {
                        if (cause instanceof InterruptedException interrupted) {
                            Thread.currentThread().interrupt();
                            throw interrupted;
                        }
                    }
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException("Delivery lookup interrupted");
                    }
                    throw exception;
                }
            });
        } catch (BackOffInterruptedException exception) {
            Thread.currentThread().interrupt();
            throw exception;
        }
    }
}
