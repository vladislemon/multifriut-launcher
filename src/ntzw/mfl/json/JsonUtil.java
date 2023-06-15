package ntzw.mfl.json;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

public class JsonUtil {

    public static String getStringIfPresent(JsonObject jsonObject, String fieldName) {
        JsonObject field = jsonObject.getField(fieldName);
        if(field instanceof JsonString) {
            return ((JsonString) field).getValue();
        }
        return null;
    }

    public static UUID getUuidIfPresent(JsonObject jsonObject, String fieldName) {
        JsonObject field = jsonObject.getField(fieldName);
        if(field instanceof JsonString) {
            String value = ((JsonString) field).getValue();
            if(value != null) {
                return UUID.fromString(value);
            }
        }
        return null;
    }

    public static Double getNumberIfPresent(JsonObject jsonObject, String fieldName) {
        JsonObject field = jsonObject.getField(fieldName);
        if(field instanceof JsonNumber) {
            return ((JsonNumber) field).getValue();
        }
        return null;
    }

    public static Boolean getBooleanIfPresent(JsonObject jsonObject, String fieldName) {
        JsonObject field = jsonObject.getField(fieldName);
        if(field instanceof JsonBoolean) {
            return ((JsonBoolean) field).getValue();
        }
        return null;
    }

    public static void addStringIfNonNull(JsonObject jsonObject, String name, String value) {
        if(value != null) {
            jsonObject.addField(new JsonString(name, value));
        }
    }

    public static void addUuidIfNonNull(JsonObject jsonObject, String name, UUID value) {
        addStringIfNonNull(jsonObject, name, value.toString());
    }

    public static void toFile(JsonObject jsonObject, Path path) throws IOException {
        if(Files.notExists(path))
            Files.createFile(path);
        Files.write(path, jsonObject.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static JsonObject fromFile(Path path, JsonParser jsonParser, boolean create) throws IOException {
        if(Files.notExists(path)) {
            if(create) {
                Files.createFile(path);
            } else {
                return null;
            }
        }
        return jsonParser.getRootObject(new String(Files.readAllBytes(path), StandardCharsets.UTF_8));
    }
}
