package in.prismar.library.common.delayed;

import lombok.Getter;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class DelayedOperation implements Delayed {

    private String uniqueId;
    private long time;
    private DelayedOperationTask task;

    public DelayedOperation(String uniqueId, long delay, DelayedOperationTask task) {
        this.uniqueId = uniqueId;
        this.time = System.currentTimeMillis() + delay;
        this.task = task;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long diff = time - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed obj) {
        if (this.time < ((DelayedOperation)obj).getTime()) {
            return -1;
        }
        if (this.time > ((DelayedOperation)obj).getTime()) {
            return 1;
        }
        return 0;
    }
}
