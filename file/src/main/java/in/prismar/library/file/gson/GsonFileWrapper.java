package in.prismar.library.file.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import in.prismar.library.common.exception.ExceptionMapper;
import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;

import java.io.*;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class GsonFileWrapper<T> {

    @Setter
    private T entity;
    private Class<?> type;
    private ExceptionMapper exceptionMapper;

    private Gson gson;
    private File file;

    public GsonFileWrapper(String filePath, Class<?> type) {
        this(filePath, type, null);
    }

    public GsonFileWrapper(String filePath, Class<?> type, ExceptionMapper mapper) {
        this(new File(filePath), type, mapper);
    }

    public GsonFileWrapper(File file, Class<?> type, ExceptionMapper exceptionMapper) {
        this.file = file;
        this.type = type;
        this.exceptionMapper = exceptionMapper;

        // Create directories
        File directory = new File(file.getParentFile().getAbsolutePath());
        directory.mkdirs();

        //Build default gson
        buildGson();
    }

    public void intercept(GsonBuilder builder) {}

    public void buildGson() {
        //Intercept
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        intercept(builder);
        this.gson = builder.create();
    }

    public void load() {
        try {
            if(exists()) {
                @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                this.entity = (T) gson.fromJson(reader, type);
            }
        }catch (Exception exception) {
            if(this.exceptionMapper != null) {
                this.exceptionMapper.map(exception);
            } else {
                exception.printStackTrace();
            }
        }
    }

    public void save() {
        try {
            if(!exists()) {
                this.file.createNewFile();
            }
            final String json = gson.toJson(entity);
            @Cleanup FileOutputStream fileOutputStream = new FileOutputStream(this.file);
            @Cleanup OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, "UTF-8");
            writer.write(json);
            writer.flush();
        }catch (Exception exception) {
            if(this.exceptionMapper != null) {
                this.exceptionMapper.map(exception);
            } else {
                exception.printStackTrace();
            }
        }
    }

    public boolean exists() {
        return this.file.exists();
    }
}
