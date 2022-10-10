package in.prismar.library.meta.processor.impl;

import in.prismar.library.meta.MetaEntity;
import in.prismar.library.meta.MetaRegistry;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.meta.processor.AbstractMetaProcessor;
import in.prismar.library.meta.processor.MetaProcessorPhase;

import java.lang.reflect.Method;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class SafeInitializeProcessor extends AbstractMetaProcessor {


    public SafeInitializeProcessor(MetaRegistry registry) {
        super(registry, MetaProcessorPhase.POST_INJECTION);
    }

    @Override
    public void process(Class<?> target) throws Exception {
        MetaEntity self = getRegistry().getEntity(target);
        for(Method method : target.getDeclaredMethods() ) {
            if(method.isAnnotationPresent(SafeInitialize.class)) {
                boolean success = method.trySetAccessible();
                if(success) {
                    method.invoke(self.getInstance());
                }
            }
        }
    }
}
