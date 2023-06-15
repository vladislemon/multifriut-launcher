package ntzw.mfl;

import ntzw.mfl.json.*;
import ntzw.mfl.util.ArrayUtil;

import java.io.IOException;
import java.nio.file.Path;

public class Settings {

    private Path path;
    private JsonObject settingsRoot;

    public Settings(Path path) {
        this.path = path;
    }

    public void load(JsonParser jsonParser) {
        try {
            settingsRoot = JsonUtil.fromFile(path, jsonParser, true);
        } catch (IOException e) {
            System.err.println("Unable to load settings file: " + path);
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            JsonUtil.toFile(settingsRoot, path);
        } catch (IOException e) {
            System.err.println("Unable to save settings file: " + path);
            e.printStackTrace();
        }
    }

    private JsonObject get(String first, String... more) {
        JsonObject jsonObject = settingsRoot.getField(first);
        if(more != null) {
            for(String s : more) {
                jsonObject = jsonObject.getField(s);
            }
        }
        return jsonObject;
    }

    public String getString(String first, String... more) {
        return get(first, more).getValueAsString();
    }

    public int getInt(String first, String... more) {
        return Integer.parseInt(getString(first, more));
    }

    public double getDouble(String first, String... more) {
        return Double.parseDouble(getString(first, more));
    }

    public boolean getBoolean(String first, String... more) {
        return Boolean.parseBoolean(getString(first, more));
    }

    private void set(JsonObject value, String first, String... more) {
        JsonObject jsonObject;
        if(more == null || more.length < 1) {
            jsonObject = settingsRoot;
        } else if(more.length < 2) {
            jsonObject = get(first);
        } else {
            jsonObject = get(first, (String[])ArrayUtil.subarray(more, 0, more.length - 1));
        }
        jsonObject.removeField(value.getName());
        jsonObject.addField(value);
    }

    public void setString(String value, String first, String... more) {
        String name = more != null && more.length > 0 ? more[more.length - 1] : first;
        set(new JsonString(name, value), first, more);
    }

    public void setInt(int value, String first, String... more) {
        String name = more != null && more.length > 0 ? more[more.length - 1] : first;
        set(new JsonNumber(name, value), first, more);
    }

    public void setDouble(double value, String first, String... more) {
        String name = more != null && more.length > 0 ? more[more.length - 1] : first;
        set(new JsonNumber(name, value), first, more);
    }

    public void setBoolean(boolean value, String first, String... more) {
        String name = more != null && more.length > 0 ? more[more.length - 1] : first;
        set(new JsonBoolean(name, value), first, more);
    }

    private void add(JsonObject value, String first, String... more) {
        JsonObject jsonObject;
        if(more == null || more.length < 1) {
            jsonObject = settingsRoot;
        } else if(more.length < 2) {
            jsonObject = get(first);
        } else {
            jsonObject = get(first, (String[])ArrayUtil.subarray(more, 0, more.length - 1));
        }
        if(jsonObject.getField(value.getName()) == null) {
            jsonObject.addField(value);
        }
    }

    public void addString(String value, String first, String... more) {
        String name = more != null && more.length > 0 ? more[more.length - 1] : first;
        add(new JsonString(name, value), first, more);
    }

    public void addInt(int value, String first, String... more) {
        String name = more != null && more.length > 0 ? more[more.length - 1] : first;
        add(new JsonNumber(name, value), first, more);
    }

    public void addDouble(double value, String first, String... more) {
        String name = more != null && more.length > 0 ? more[more.length - 1] : first;
        add(new JsonNumber(name, value), first, more);
    }

    public void addBoolean(boolean value, String first, String... more) {
        String name = more != null && more.length > 0 ? more[more.length - 1] : first;
        add(new JsonBoolean(name, value), first, more);
    }
}
