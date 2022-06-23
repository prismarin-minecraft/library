package in.prismar.library.common.distributor.task;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public abstract class DelayedDistributionTask implements DistributionTask{

    private long timestamp;
    private long waitTime;

    public DelayedDistributionTask(long waitTime) {
        this.waitTime = waitTime;
    }

    @Override
    public boolean execute() {
        if(System.currentTimeMillis() >= timestamp) {
            boolean success = delayedExecute();
            timestamp = System.currentTimeMillis() + waitTime;
            return success;
        }
        return false;
    }

    public abstract boolean delayedExecute();
}
