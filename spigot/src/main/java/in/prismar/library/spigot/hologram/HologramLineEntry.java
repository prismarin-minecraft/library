package in.prismar.library.spigot.hologram;

import in.prismar.library.spigot.hologram.line.HologramLineType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
@Getter
@Setter
public class HologramLineEntry {

    private HologramLineType type;
    private Object content;
    private boolean small;

}
