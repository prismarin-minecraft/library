package in.prismar.library.file.gson;

import com.google.gson.GsonBuilder;
import in.prismar.library.common.exception.ExceptionMapper;
import in.prismar.library.common.repository.entity.StringRepositoryEntity;
import in.prismar.library.common.repository.impl.AbstractAsyncRepository;
import in.prismar.library.file.gson.GsonFileWrapper;
import lombok.Getter;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class GsonRepository<E extends StringRepositoryEntity> extends AbstractAsyncRepository<String, E> {

    protected String directory;
    private Class<?> type;
    private ExceptionMapper mapper;
    private Map<String, GsonFileWrapper<E>> internalWrapperCache;
    private Map<String, E> cache;


    public GsonRepository(String directory, Class<?> type, String poolName, long poolDelay) {
        this(directory, type, null, poolName, poolDelay);
    }

    public GsonRepository(String directory, Class<?> type, ExceptionMapper mapper, String poolName, long poolDelay) {
        super(poolName, poolDelay);
        this.directory = directory;
        this.type = type;
        this.mapper = mapper;
        this.internalWrapperCache = new ConcurrentHashMap<>();
        this.cache = new ConcurrentHashMap<>();
    }

    public void loadAll() {
        loadAll(null);
    }

    public void loadAll(Consumer<GsonFileWrapper<E>> loadCallback) {
        File folder = new File(directory);
        if(folder.exists()) {
            for(File file : folder.listFiles()) {
                if(file.getName().endsWith(".json")) {
                    GsonFileWrapper<E> wrapper = createWrapper(file.getName().replace(".json", ""));
                    wrapper.load();
                    if(wrapper.getEntity() == null) {
                        continue;
                    }
                    internalWrapperCache.put(wrap(wrapper.getEntity().getId()), wrapper);
                    cache.put(wrap(wrapper.getEntity().getId()), wrapper.getEntity());
                    if(loadCallback != null) {
                        loadCallback.accept(wrapper);
                    }
                }
            }
        }
    }

    public void interceptEntry(GsonBuilder builder) {}

    public GsonFileWrapper<E> createWrapper(File file) {
        GsonFileWrapper<E> wrapper = new GsonFileWrapper<E>(file, type, mapper) {
            @Override
            public void intercept(GsonBuilder builder) {
                interceptEntry(builder);
            }
        };
        return wrapper;
    }

    public GsonFileWrapper<E> createWrapper(String id) {
        if(this.internalWrapperCache.containsKey(wrap(id))) {
            return this.internalWrapperCache.get(wrap(id));
        }
        GsonFileWrapper<E> wrapper = createWrapper(new File(directory + id + ".json"));
        this.internalWrapperCache.put(wrap(id), wrapper);
        return wrapper;
    }

    @Override
    public E create(E entity) {
        return save(entity);
    }


    @Override
    public E findById(String id) {
        if(this.cache.containsKey(wrap(id))) {
            return this.cache.get(wrap(id));
        }
        GsonFileWrapper<E> wrapper = createWrapper(id);
        return wrapper.getEntity();
    }

    @Override
    public Optional<E> findByIdOptional(String id) {
        if(this.cache.containsKey(wrap(id))) {
            return Optional.of(this.cache.get(wrap(id)));
        }
        GsonFileWrapper<E> wrapper = createWrapper(id);
        wrapper.load();
        if(wrapper.exists() && wrapper.getEntity() != null) {
            return Optional.of(wrapper.getEntity());
        }
        return Optional.empty();
    }

    @Override
    public Collection<E> findAll() {
        return cache.values();
    }

    @Override
    public boolean existsById(String id) {
        if(this.cache.containsKey(wrap(id))) {
            return true;
        }
        GsonFileWrapper<E> wrapper = createWrapper(id);
        return wrapper.exists() && wrapper.getEntity() != null;
    }

    @Override
    public E save(E entity) {
        GsonFileWrapper<E> wrapper = createWrapper(entity.getId());
        wrapper.setEntity(entity);
        wrapper.save();
        this.cache.put(wrap(entity.getId()), entity);
        return entity;
    }

    @Override
    public E delete(E entity) {
        return deleteById(entity.getId());
    }

    @Override
    public E deleteById(String id) {
        GsonFileWrapper<E> wrapper = createWrapper(id);
        if(wrapper.exists()) {
            wrapper.getFile().delete();
            cache.remove(wrap(id));
            internalWrapperCache.remove(wrap(id));
            return wrapper.getEntity();
        }
        return null;
    }

    public String wrap(String id) {
        return id.toLowerCase();
    }
}
