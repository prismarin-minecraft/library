package in.prismar.library.common.repository;

import in.prismar.library.common.repository.Repository;
import in.prismar.library.common.repository.entity.StringRepositoryEntity;
import in.prismar.library.common.repository.impl.LocalRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class LocalRepositoryTest {

    RepositoryTestInterface repository = new RepositoryTestImpl();

    @Before
    public void setup() {
        repository.deleteAll();
        repository.create(new StringRepositoryEntity("Test"));
    }

    @Test
    public void testGet() {
        Assertions.assertEquals("Test", repository.findById("Test").getId());
    }

    @Test
    public void testGetAll() {
        Assertions.assertEquals(1, repository.findAll().size());
    }

    @Test
    public void testExists() {
        Assertions.assertEquals(true, repository.existsById("Test"));
        Assertions.assertEquals(false, repository.existsById("Nobody"));
    }

    @Test
    public void testDelete() {
        Assertions.assertEquals("Test", repository.deleteById("Test").getId());
    }

    @Test
    public void testCreate() {
        StringRepositoryEntity entity = new StringRepositoryEntity("Dummy");

        Assertions.assertEquals("Dummy", repository.create(entity).getId());
    }

    class RepositoryTestImpl extends LocalRepository<String, StringRepositoryEntity> implements RepositoryTestInterface {

        public RepositoryTestImpl() {
            super(false);
        }

        @Override
        public void deleteAll() {
            getLocal().clear();
        }
    }

    interface RepositoryTestInterface extends Repository<String, StringRepositoryEntity> {

        void deleteAll();
    }


}
