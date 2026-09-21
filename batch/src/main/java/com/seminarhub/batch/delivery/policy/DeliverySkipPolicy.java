package com.seminarhub.batch.delivery.policy;

import com.seminarhub.batch.delivery.exception.DeliveryStatusUpdateException;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

public class DeliverySkipPolicy implements SkipPolicy {

    @Override
    public boolean shouldSkip(Throwable t, long skipCount) throws SkipLimitExceededException {
        Throwable cause = t;
        while (cause != null) {
            if (cause instanceof DeliveryStatusUpdateException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }
}
