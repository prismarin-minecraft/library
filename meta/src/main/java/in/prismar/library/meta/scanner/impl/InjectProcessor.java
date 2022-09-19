package in.prismar.library.meta.scanner.impl;

import in.prismar.library.meta.MetaEntity;
import in.prismar.library.meta.MetaRegistry;
import in.prismar.library.meta.scanner.AbstractMetaProcessor;
import in.prismar.library.meta.scanner.MetaProcessorType;

import java.lang.reflect.Field;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class InjectProcessor extends AbstractMetaProcessor {


    public InjectProcessor(MetaRegistry registry) {
        super(registry, MetaProcessorType.LOAD);
    }

    @Override
    public void process(Class<?> target) throws Exception {
        MetaEntity self = getRegistry().getEntity(target);
        for(Field field : target.getDeclaredFields() ) {
            Class<?> type = field.getType();
            if(getRegistry().existsEntity(type)) {
                MetaEntity entity = getRegistry().getEntity(type);
                boolean success = field.trySetAccessible();
                if(success) {
                    field.set(self.getInstance(), entity.getInstance());
                }
            }
        }
    }
}
