package in.prismar.library.common.delayed;

import in.prismar.library.common.delayed.worker.DelayedOperationWorker;
import lombok.Getter;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class DelayedOperationExecutor<T extends DelayedOperation> {

    private final String name;
    private DelayedOperationWorker<T> worker;
    private ExecutorService threadPool;
    private DelayQueue<T> queue;

    public DelayedOperationExecutor(String name) {
        this.name = name;
        this.threadPool = Executors.newCachedThreadPool();
        this.queue = new DelayQueue<>();
        this.worker = new DelayedOperationWorker<>(this);
    }

    public void clear() {
        this.queue.clear();
    }

    public void executeTask(DelayedOperationTask task) {
        this.getThreadPool().execute(() -> task.execute());
    }

    public void executeAllTasks() {
        for(T operation : queue) {
            executeTask(operation.getTask());
        }
        queue.clear();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public boolean addTask(String uniqueId, long delay, DelayedOperationTask task) {
        DelayedOperation operation = new DelayedOperation(uniqueId, delay, task);
        return addTask((T) operation);
    }

    public boolean addTask(T operation) {
        if(hasTask(operation.getUniqueId())) {
            return false;
        }
        this.queue.add(operation);
        return true;
    }

    public boolean hasTask(String uniqueId) {
        for(T operation : queue) {
            if(operation.getUniqueId().equals(uniqueId)) {
                return true;
            }
        }
        return false;
    }

    public void shutdown() {
        this.worker.getRunning().set(false);
        this.threadPool.shutdown();
    }
}
