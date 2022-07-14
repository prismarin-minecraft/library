package in.prismar.library.spigot.hologram;

import in.prismar.library.spigot.hologram.line.HologramLine;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
public class HologramViewer {

    private final Player player;
    private boolean spawned;
    private List<HologramLine> lines;

    public HologramViewer(Player player) {
        this.player = player;
        this.lines = new ArrayList<>();
    }
}
