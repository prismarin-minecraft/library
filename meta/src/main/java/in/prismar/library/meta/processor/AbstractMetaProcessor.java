package in.prismar.library.meta.processor;

import in.prismar.library.meta.MetaRegistry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@RequiredArgsConstructor
public abstract class AbstractMetaProcessor implements MetaProcessor {

    private final MetaRegistry registry;
    private final MetaProcessorType type;


}
