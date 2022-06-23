package in.prismar.library.common.repository;

import java.util.Collection;
import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface Repository<ID, E> {

    E create(E entity);

    E findById(ID id);

    Optional<E> findByIdOptional(ID id);

    Collection<E> findAll();

    boolean existsById(ID id);

    E save(E entity);

    E delete(E entity);

    E deleteById(ID id);
}
