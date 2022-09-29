package in.prismar.library.meta.processor.impl;

import in.prismar.library.meta.MetaEntity;
import in.prismar.library.meta.MetaRegistry;
import in.prismar.library.meta.anno.Config;
import in.prismar.library.meta.processor.AbstractMetaProcessor;
import in.prismar.library.meta.processor.MetaProcessorType;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class ConfigProcessor extends AbstractMetaProcessor {

    private final ConfigProvider provider;

    public ConfigProcessor(MetaRegistry registry, ConfigProvider provider) {
        super(registry, MetaProcessorType.LOAD);
        this.provider = provider;
    }

    @Override
    public void process(Class<?> target) throws Exception {
        MetaEntity self = getRegistry().getEntity(target);
        for(Field field : target.getDeclaredFields() ) {
            if(field.isAnnotationPresent(Config.class)) {
                Config anno = field.getAnnotation(Config.class);
                Object value = provider.getConfigValue(anno.value());
                field.set(self.getInstance(), value == null ? anno.defaultValue() : value);
            }
        }
    }

    public interface ConfigProvider {

        @Nullable
        Object getConfigValue(String key);

    }
}
