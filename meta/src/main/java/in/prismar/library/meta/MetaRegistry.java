package in.prismar.library.meta;

import com.google.common.reflect.ClassPath;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.meta.processor.MetaProcessor;
import in.prismar.library.meta.processor.MetaProcessorPhase;
import in.prismar.library.meta.processor.impl.InjectProcessor;
import in.prismar.library.meta.processor.impl.SafeInitializeProcessor;
import in.prismar.library.meta.processor.impl.ServiceProcessor;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;
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
    private final Map<Class<? extends Annotation>, List<MetaProcessor>> processors;

    private Set<Class<?>> scanned;

    public MetaRegistry() {
        this.entities = new ConcurrentHashMap<>();
        this.processors = new ConcurrentHashMap<>();
        this.scanned = new HashSet<>();
        loadDefaultProcessors();
    }

    private void loadDefaultProcessors() {
        registerProcessor(Service.class, new ServiceProcessor(this));
        registerProcessor(Inject.class, new InjectProcessor(this));
        registerProcessor(SafeInitialize.class, new SafeInitializeProcessor(this));
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

    public void registerProcessor(Class<? extends Annotation> type, MetaProcessor... processors) {
        if(!this.processors.containsKey(type)) {
            this.processors.put(type, new ArrayList<>());
        }
        this.processors.get(type).addAll(Arrays.asList(processors));
    }

    public void build(ClassLoader loader, String packageName) {
        try {
            ClassPath classPath = ClassPath.from(loader);
            for(ClassPath.ClassInfo info : classPath.getTopLevelClasses()) {
                if(info.getName().startsWith(packageName.concat("."))) {
                    try {
                        Class<?> target = info.load();
                        scanned.add(target);
                        run(MetaProcessorPhase.DISCOVERY, target);
                    } catch (NoClassDefFoundError err) {
                        System.out.println("Can't find class (Ignore if intended): " + err.getMessage());
                    }

                }
            }
            runPhase(MetaProcessorPhase.POST_DISCOVERY);
            runPhase(MetaProcessorPhase.INJECTION);
            runPhase(MetaProcessorPhase.POST_INJECTION);
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void runPhase(MetaProcessorPhase phase) throws Exception {
        for(Map.Entry<Class<?>, MetaEntity> entry : entities.entrySet()) {
            run(phase, entry.getKey());
        }
    }

    private void run(MetaProcessorPhase phase, Class<?> target) throws Exception{
        for(Map.Entry<Class<? extends Annotation>, List<MetaProcessor>> processorEntry : processors.entrySet()) {
            for(MetaProcessor processor : processorEntry.getValue()) {
                if(processor.getPhase() ==  phase) {
                    try {
                        processor.process(target);
                    }catch (NoClassDefFoundError error) {
                        System.out.println("Can't find class (Ignore if intended): " + error.getMessage());
                    }

                }
            }
        }
    }



}
