package com.seminarhub.batch.delivery.config;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Waits for queue capacity instead of rejecting work or running it on the
 * submitting thread.
 */
final class BlockingRejectedExecutionHandler implements RejectedExecutionHandler {

    private static final long SHUTDOWN_CHECK_INTERVAL_MILLIS = 100L;

    @Override
    public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {
        try {
            while (!executor.isShutdown()) {
                if (executor.getQueue().offer(
                        task,
                        SHUTDOWN_CHECK_INTERVAL_MILLIS,
                        TimeUnit.MILLISECONDS)) {
                    if (executor.isShutdown() && executor.remove(task)) {
                        throw new RejectedExecutionException(
                                "Executor shut down while waiting for queue capacity");
                    }
                    return;
                }
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new RejectedExecutionException(
                    "Interrupted while waiting for executor queue capacity",
                    exception);
        }

        throw new RejectedExecutionException("Executor is shut down");
    }
}
