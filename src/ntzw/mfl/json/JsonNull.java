package ntzw.mfl.json;

public class JsonNull extends JsonObject {

    @Override
    public String getValueAsString() {
        return "null";
    }

    public String toString() {
        if(name.isEmpty())
            return "null";
        return "\"" + name + "\":null";
    }
}
