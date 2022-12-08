package in.prismar.library.spigot.scheduler;

import lombok.Getter;
import lombok.Setter;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public abstract class SchedulerRunnable implements Runnable {

    private boolean cancelled;

    @Setter
    private long currentTicks;

    public void cancel() {
        this.cancelled = true;
    }

}
