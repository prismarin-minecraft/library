package in.prismar.library.file.toml;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.ConfigFormat;
import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.electronwill.nightconfig.core.file.FileNotFoundAction;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.electronwill.nightconfig.toml.TomlParser;
import com.electronwill.nightconfig.toml.TomlWriter;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class TomlConfig<T> {

    private File file;
    private ObjectConverter converter;
    private Config config;
    private TomlParser parser;
    private TomlWriter writer;

    @Setter
    private T entity;
    private Class<T> type;

    public TomlConfig(String path, Class<T> type) {
        this.file = new File(path);
        this.type = type;
        this.converter = new ObjectConverter();
        this.parser = TomlFormat.instance().createParser();
        this.writer = TomlFormat.instance().createWriter();
        this.writer.setIndent("");

        load();
    }

    @SneakyThrows
    public void load() {
        if(this.file.exists()) {
            this.config = this.parser.parse(file, (path, configFormat) -> false);
            this.entity = type.getConstructor().newInstance();
            converter.toObject(config, entity);
        } else {
            this.config = Config.inMemory();
        }
    }

    public void save() {
        if(entity == null) {
            return;
        }
        converter.toConfig(entity, config);
        writer.write(config, file, WritingMode.REPLACE);
    }
}
