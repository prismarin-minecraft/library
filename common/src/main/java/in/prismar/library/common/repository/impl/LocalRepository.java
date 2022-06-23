package in.prismar.library.common.repository.impl;

import in.prismar.library.common.repository.Repository;
import in.prismar.library.common.repository.entity.RepositoryEntity;
import lombok.Getter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class LocalRepository<ID, E extends RepositoryEntity<ID>> implements Repository<ID, E> {

    private Map<ID, E> local;

    public LocalRepository(boolean threadSafe) {
        this.local = threadSafe ? new ConcurrentHashMap<>() : new HashMap<>();
    }

    @Override
    public E create(E entity) {
        this.local.put(entity.getId(), entity);
        return entity;
    }


    @Override
    public E findById(ID id) {
        return this.local.get(id);
    }


    @Override
    public Optional<E> findByIdOptional(ID id) {
        if(this.local.containsKey(id)) {
            return Optional.of(this.local.get(id));
        }
        return Optional.empty();
    }

    @Override
    public Collection<E> findAll() {
        return this.local.values();
    }


    @Override
    public boolean existsById(ID id) {
        return this.local.containsKey(id);
    }

    @Override
    public E save(E entity) {
        return entity;
    }

    @Override
    public E delete(E entity) {
        return deleteById(entity.getId());
    }

    @Override
    public E deleteById(ID id) {
        return this.local.remove(id);
    }
}
