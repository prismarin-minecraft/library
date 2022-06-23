package in.prismar.library.common.registry;

import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class LocalMapRegistry<ID, E> implements MapRegistry<ID, E>  {

    private Map<ID, E> local;

    public LocalMapRegistry(boolean threadSafe, boolean sorted) {
        this.local = threadSafe ? (sorted ? Collections.synchronizedMap(new LinkedHashMap<>()) : new ConcurrentHashMap<>())
                : (sorted ? new LinkedHashMap<>() : new HashMap<>());
    }

    @Override
    public void register(ID id, E entity) {
        this.local.put(wrap(id), entity);
    }

    @Override
    public E unregister(ID id) {
        return this.local.remove(wrap(id));
    }

    @Override
    public boolean existsById(ID id) {
        return this.local.containsKey(wrap(id));
    }

    @Override
    public E getById(ID id) {
        return this.local.get(wrap(id));
    }

    @Override
    public void clear() {
        this.local.clear();
    }

    @Override
    public Collection<E> getAll() {
        return this.local.values();
    }


}
