package in.prismar.library.spigot.inventory;

import lombok.Data;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class FrameProperties {

    private final String title;
    private final int rows;
    private boolean async;

    public FrameProperties(String title, int rows) {
        this.title = title;
        this.rows = rows;
    }
}
