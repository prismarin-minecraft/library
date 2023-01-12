package in.prismar.library.common.delayed.worker;

import in.prismar.library.common.delayed.DelayedOperation;
import in.prismar.library.common.delayed.DelayedOperationExecutor;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class DelayedOperationWorker<T extends DelayedOperation> extends Thread {

    private final DelayedOperationExecutor<T> pool;
    private AtomicBoolean running;

    public DelayedOperationWorker(DelayedOperationExecutor<T> pool) {
        this.pool = pool;
        this.running = new AtomicBoolean(true);
        start();
    }

    @Override
    public void run() {
        while (running.get()) {
            try {
                T operation = pool.getQueue().take();
                pool.executeTask(operation.getTask());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
