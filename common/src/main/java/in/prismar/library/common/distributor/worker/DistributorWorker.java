package in.prismar.library.common.distributor.worker;

import in.prismar.library.common.distributor.Distributor;
import in.prismar.library.common.distributor.task.DistributionTask;
import lombok.Getter;
import lombok.Setter;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class DistributorWorker<T extends DistributionTask> implements Runnable {

    private final Distributor<T> distributor;
    private Map<String, T> tasks;

    @Setter
    private boolean running;

    public DistributorWorker(Distributor<T> distributor) {
        this.distributor = distributor;
        this.tasks = new ConcurrentHashMap<>();
        this.running = true;
    }

    @Override
    public void run() {
        while (running) {
            Iterator<Map.Entry<String, T>> iterator = tasks.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, T> entry = iterator.next();
                boolean success = entry.getValue().execute();
                if(success) {
                    distributor.removeTask(this, entry.getKey());
                }
            }
        }
    }

    public int getTasksSize() {
        return this.tasks.size();
    }
}
