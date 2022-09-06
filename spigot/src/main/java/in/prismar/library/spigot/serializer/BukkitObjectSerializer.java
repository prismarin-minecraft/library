package in.prismar.library.spigot.serializer;

import in.prismar.library.common.serializer.Serializer;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Copyright (c) ReaperMaga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by ReaperMaga
 **/
public class BukkitObjectSerializer implements Serializer<String, Object> {

    private static final Serializer<String, Object> INSTANCE = new BukkitObjectSerializer();

    public static Serializer<String, Object> getSerializer() {
        return INSTANCE;
    }

    @Override
    @SneakyThrows
    public String serialize(Object object) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        @Cleanup BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
        dataOutput.writeObject(object);
        return Base64Coder.encodeLines(outputStream.toByteArray());
    }

    @Override
    @SneakyThrows
    public Object deserialize(String base64) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(base64));
        @Cleanup BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
        return dataInput.readObject();
    }
}