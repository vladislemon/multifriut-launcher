package ntzw.mfl.json;

public class JsonArray extends JsonObject {

    public JsonArray(String name) {
        super(name);
    }

    public JsonArray() {}

    //List "fields" are used as internal array
    public JsonObject[] getArray() {
        return getFields().toArray(new JsonObject[]{});
    }

    @Override
    public String getValueAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for(int i = 0; i < getFields().size(); i++) {
            sb.append(getFields().get(i));
            if(i < getFields().size() - 1) {
                sb.append(',');
            }
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(!name.isEmpty()) {
            sb.append('\"');
            sb.append(name);
            sb.append("\":");
        }
        sb.append('[');
        for(int i = 0; i < getFields().size(); i++) {
            sb.append(getFields().get(i));
            if(i < getFields().size() - 1) {
                sb.append(',');
            }
        }
        sb.append(']');
        return sb.toString();
    }
}
