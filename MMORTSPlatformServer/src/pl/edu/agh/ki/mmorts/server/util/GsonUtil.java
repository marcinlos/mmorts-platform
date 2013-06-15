package pl.edu.agh.ki.mmorts.server.util;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * Static class containing Gson utility elements.
 */
public class GsonUtil {

    /** 'Canonical' {@linkplain Gson} instance */
    public static final Gson gson;

    /** Class deserializer */
    private static class ClassDeserializer implements
            JsonDeserializer<Class<?>> {
        @Override
        public Class<?> deserialize(JsonElement json, Type type,
                JsonDeserializationContext ctx) throws JsonParseException {
            String name = json.getAsString();
            try {
                return Class.forName(name);
            } catch (ClassNotFoundException e) {
                throw new JsonParseException("Cannot load class", e);
            }
        }
    }

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Class.class, new ClassDeserializer());
        gson = builder.create();
    }

}
