package in.prismar.library.common.distributor;

import in.prismar.library.common.distributor.strategy.DistributionStrategy;
import in.prismar.library.common.distributor.strategy.SizeDistributionStrategy;
import in.prismar.library.common.distributor.task.DistributionTask;
import in.prismar.library.common.distributor.worker.DistributorWorker;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class Distributor<T extends DistributionTask> {

    private DistributorConfig config;
    private DistributionStrategy<T> strategy;
    private List<DistributorWorker<T>> workers;
    private ExecutorService threadPool;
    private ReentrantLock lock;


    public Distributor(DistributorConfig config) {
        this.config = config;
        this.workers = new ArrayList<>();
        this.lock = new ReentrantLock();
        this.strategy = new SizeDistributionStrategy<>(this);
        this.threadPool = Executors.newCachedThreadPool();

        for (int i = 0; i < config.getMinWorkers(); i++) {
            createWorker();
        }
    }

    public synchronized void scale(boolean increase) {
        int overAllSize = getTasksSize();
        int workerScaledSize = getConfig().getScaleWorkersOnTaskSize() * getWorkers().size();
        if(increase) {
            if(overAllSize >= workerScaledSize && getWorkers().size() < config.getMaxWorkers()) {
                lock.lock();
                createWorker();
                lock.unlock();
            }
        } else {
            if(getWorkers().size() <= getConfig().getMinWorkers()) {
                return;
            }
            workerScaledSize -= getConfig().getScaleWorkersOnTaskSize();
            if(overAllSize < workerScaledSize) {
                lock.lock();
                try {
                    DistributorWorker<T> worker = deleteWorker();
                    if(worker != null) {
                        for(Map.Entry<String, T> entry : worker.getTasks().entrySet()) {
                            addTask(entry.getKey(), entry.getValue());
                        }
                    }
                }finally {
                    lock.unlock();
                }
            }
        }
    }

    public DistributorWorker<T> createWorker() {
        DistributorWorker<T> worker = new DistributorWorker<>(this);
        this.workers.add(worker);
        threadPool.execute(worker);
        return worker;
    }


    public DistributorWorker<T> deleteWorker() {
        if(getWorkers().isEmpty()) {
            return null;
        }
        int index = getWorkers().size()-1;
        DistributorWorker<T> worker = getWorkers().remove(index);
        worker.setRunning(false);
        return worker;
    }

    public void addTask(String id, T task) {
        if(existsTask(id)) {
            return;
        }
        this.strategy.distribute(id, task);
    }

    public void removeTask(String id) {
        for(DistributorWorker<T> worker : workers) {
            removeTask(worker, id);
        }
    }

    public void removeTask(DistributorWorker<T> worker, String id) {
        if(worker.getTasks().containsKey(id)) {
            worker.getTasks().remove(id);
            scale(false);
        }

    }

    public boolean existsTask(String id) {
        for(DistributorWorker<T> worker : workers) {
            if(worker.getTasks().containsKey(id)) {
                return true;
            }
        }
        return false;
    }

    public int getTasksSize() {
        int size = 0;
        for(DistributorWorker<T> worker : workers) {
            size += worker.getTasksSize();
        }
        return size;
    }
}
