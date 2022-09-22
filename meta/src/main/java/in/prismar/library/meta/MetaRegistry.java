package in.prismar.library.meta;

import com.google.common.reflect.ClassPath;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.meta.processor.MetaProcessor;
import in.prismar.library.meta.processor.MetaProcessorType;
import in.prismar.library.meta.processor.impl.InjectProcessor;
import in.prismar.library.meta.processor.impl.ServiceProcessor;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class MetaRegistry {

    private Map<Class<?>, MetaEntity> entities;
    private final Map<Class<? extends Annotation>, MetaProcessor> processors;

    public MetaRegistry() {
        this.entities = new ConcurrentHashMap<>();
        this.processors = new ConcurrentHashMap<>();
        loadDefaultProcessors();
    }

    private void loadDefaultProcessors() {
        registerProcessor(Service.class, new ServiceProcessor(this));
        registerProcessor(Inject.class, new InjectProcessor(this));
    }

    public boolean existsEntity(Class<?> type) {
        return entities.containsKey(type);
    }

    public MetaEntity getEntity(Class<?> type) {
        return entities.get(type);
    }

    public MetaEntity registerEntity(Object instance) {
        MetaEntity entity = new MetaEntity(instance);
        entities.put(instance.getClass(), entity);
        return entity;
    }

    public MetaEntity registerEntity(Class<?> target) throws Exception {
        Constructor<?> constructor = target.getConstructors()[0];
        Object instance;
        if(constructor.getParameterCount() >= 1) {
            Object[] params = new Object[constructor.getParameterCount()];
            for (int i = 0; i < constructor.getParameterCount(); i++) {
                Parameter parameter = constructor.getParameters()[i];
                if(existsEntity(parameter.getType())) {
                    params[i] = getEntity(parameter.getType()).getInstance();
                }
            }
            instance = constructor.newInstance(params);
        } else {
            instance = constructor.newInstance();
        }
        return registerEntity(instance);
    }

    public void registerProcessor(Class<? extends Annotation> type, MetaProcessor processor) {
        this.processors.put(type, processor);
    }

    public void scan(ClassLoader loader, String packageName) {
        try {
            ClassPath classPath = ClassPath.from(loader);
            for(ClassPath.ClassInfo info : classPath.getTopLevelClasses()) {
                if(info.getName().startsWith(packageName.concat("."))) {
                    Class<?> target = info.load();
                    for(Map.Entry<Class<? extends Annotation>, MetaProcessor> processor : processors.entrySet()) {
                        if(processor.getValue().getType() == MetaProcessorType.SCAN) {
                            processor.getValue().process(target);
                        }
                    }
                }

            }
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void load() {
        try {
            for(Map.Entry<Class<?>, MetaEntity> entry : entities.entrySet()) {
                for(Map.Entry<Class<? extends Annotation>, MetaProcessor> processor : processors.entrySet()) {
                    if(processor.getValue().getType() ==  MetaProcessorType.LOAD) {
                        processor.getValue().process(entry.getKey());
                    }
                }
            }
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }



}
