package in.prismar.library.file;

import in.prismar.library.common.repository.Repository;
import in.prismar.library.common.repository.entity.StringRepositoryEntity;
import in.prismar.library.file.gson.GsonRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import java.io.File;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class GsonRepositoryTest {

    RepositoryTestInterface repository = new RepositoryTestImpl();

    @Before
    public void setup() {

        repository.create(new StringRepositoryEntity("Test"));
    }

    @AfterEach
    public void after() {
        repository.deleteAll();
    }

    @After
    public void afterAll() {
        repository.deleteAll();
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

    class RepositoryTestImpl extends GsonRepository<StringRepositoryEntity> implements RepositoryTestInterface {

        public RepositoryTestImpl() {
            super("test_data/", StringRepositoryEntity.class, "Test", 5000);
        }

        @Override
        public void deleteAll() {
            File file = new File(getDirectory());
            delete(file);

        }

        void delete(File f)  {
            if (f.isDirectory()) {
                for (File c : f.listFiles())
                    delete(c);
            }
            f.delete();
        }
    }

    interface RepositoryTestInterface extends Repository<String, StringRepositoryEntity> {

        void deleteAll();
    }


}
