package in.prismar.library.common.registry;

import java.util.Collection;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface MapRegistry<ID, E> {

    void register(ID id, E entity);

    E unregister(ID id);

    boolean existsById(ID id);

    E getById(ID id);

    Collection<E> getAll();

    void clear();

    default ID wrap(ID id) {
        return id;
    }

}
