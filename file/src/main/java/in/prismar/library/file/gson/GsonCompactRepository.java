package in.prismar.library.file.gson;

import com.google.gson.reflect.TypeToken;
import in.prismar.library.common.repository.Repository;
import in.prismar.library.common.repository.entity.RepositoryEntity;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class GsonCompactRepository<E extends RepositoryEntity<String>>
        extends GsonFileWrapper<Map<String, E>> implements Repository<String, E> {

    public GsonCompactRepository(String filePath, TypeToken token) {
        super(filePath, token.getType());
        load();
        if(getEntity() == null) {
            setEntity(new HashMap<>());
        }
    }

    @Override
    public E create(E entity) {
        this.getEntity().put(entity.getId().toLowerCase(), entity);
        save(entity);
        return entity;
    }

    @Override
    public E findById(String id) {
        return getEntity().get(id.toLowerCase());
    }

    @Override
    public Optional<E> findByIdOptional(String id) {
        final String lowered = id.toLowerCase();
        if(getEntity().containsKey(lowered)) {
            return Optional.of(getEntity().get(lowered));
        }
        return Optional.empty();
    }

    @Override
    public Collection<E> findAll() {
        return getEntity().values();
    }

    @Override
    public boolean existsById(String id) {
        return getEntity().containsKey(id.toLowerCase());
    }

    @Override
    public E save(E entity) {
        this.save();
        return entity;
    }

    @Override
    public E delete(E entity) {
        return deleteById(entity.getId());
    }

    @Override
    public E deleteById(String id) {
        final String lowered = id.toLowerCase();
        if(getEntity().containsKey(lowered)) {
            E entity = getEntity().remove(lowered);
            save();
            return entity;
        }
        return null;
    }
}
