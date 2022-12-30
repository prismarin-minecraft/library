package in.prismar.library.file;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.ConfigFormat;
import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.file.FileNotFoundAction;
import com.electronwill.nightconfig.core.io.IndentStyle;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.electronwill.nightconfig.toml.TomlParser;
import com.electronwill.nightconfig.toml.TomlWriter;
import in.prismar.library.file.toml.TomlConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class TomlTest {

    @Test
    public void test() {
        TomlConfig<TestObj> config = new TomlConfig<>("test.toml", TestObj.class);
        if (config.getEntity() == null) {
            config.setEntity(new TestObj("Test", new TestDetails(18, List.of("swimming"))));
            config.save();
        }
        Assertions.assertEquals("Test", config.getEntity().getName());
        config.getFile().delete();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestObj {
        private String name;
        private TestDetails details;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestDetails {
        private int age;
        private List<String> hobbies;
    }
}
