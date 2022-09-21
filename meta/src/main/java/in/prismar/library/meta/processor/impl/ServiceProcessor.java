package in.prismar.library.meta.processor.impl;

import in.prismar.library.meta.MetaRegistry;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.meta.processor.AbstractMetaProcessor;
import in.prismar.library.meta.processor.MetaProcessorType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;


/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class ServiceProcessor extends AbstractMetaProcessor {

    public ServiceProcessor(MetaRegistry registry) {
        super(registry, MetaProcessorType.SCAN);
    }

    @Override
    public void process(Class<?> target) throws Exception {
        if(target.isAnnotationPresent(Service.class)) {
            Service service = target.getAnnotation(Service.class);
            if(service.autoRegister()) {
                getRegistry().registerEntity(target);
            }
        }
    }


}
