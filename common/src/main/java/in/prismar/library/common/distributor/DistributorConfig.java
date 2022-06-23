package in.prismar.library.common.distributor;

import lombok.Data;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class DistributorConfig {

    private int minWorkers = 4;
    private int maxWorkers = 10;

    private int scaleWorkersOnTaskSize = 50;
}
