package in.prismar.library.spigot.location.copier;

import lombok.AllArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.CompletableFuture;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
public class CopierProcess extends BukkitRunnable {

    private Copier copier;
    private CompletableFuture<CopierResult> future;

    @Override
    public void run() {
        if(!copier.work()) {
            cancel();
            future.complete(new CopierResult(true));
        }
    }
}
