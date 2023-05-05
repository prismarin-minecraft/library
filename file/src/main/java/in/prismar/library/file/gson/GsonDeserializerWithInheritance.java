package in.prismar.library.file.gson;

import com.google.gson.*;

import java.lang.reflect.Type;

public class GsonDeserializerWithInheritance<T> implements JsonDeserializer<T> {

    @Override
    public T deserialize(
            JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonPrimitive classNamePrimitive = (JsonPrimitive) jsonObject.get("type");

        String className = classNamePrimitive.getAsString();
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e.getMessage());
        }
        return context.deserialize(jsonObject, clazz);
    }
}