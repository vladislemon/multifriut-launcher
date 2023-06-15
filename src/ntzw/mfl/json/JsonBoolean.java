package ntzw.mfl.json;

public class JsonBoolean extends JsonObject {

    private boolean value;

    public JsonBoolean(String name, boolean value) {
        super(name);
        this.value = value;
    }

    public JsonBoolean(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public String getValueAsString() {
        return Boolean.toString(value);
    }

    public String toString() {
        if(name.isEmpty())
            return Boolean.toString(value);
        return "\"" + name + "\":" + value;
    }
}
