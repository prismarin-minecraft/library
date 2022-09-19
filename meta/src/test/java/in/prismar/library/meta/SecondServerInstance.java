package in.prismar.library.meta;

import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.Service;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
public class SecondServerInstance {

    @Inject
    FirstServiceInstance instance;

    public String execute() {
        return instance.execute();
    }
}
