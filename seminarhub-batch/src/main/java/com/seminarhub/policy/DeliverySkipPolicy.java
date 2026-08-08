package com.seminarhub.policy;

import com.seminarhub.exception.DeliveryStatusUpdateException;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

public class DeliverySkipPolicy implements SkipPolicy {

    @Override
    public boolean shouldSkip(Throwable t, long skipCount) throws SkipLimitExceededException {
        if (t instanceof DeliveryStatusUpdateException) {
            return true;
        }
        return false;
    }
}
