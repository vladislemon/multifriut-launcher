package ntzw.mfl.json;

public class JsonString extends JsonObject {

    private String value;

    public JsonString(String name, String value) {
        super(name);
        this.value = value;
    }

    public JsonString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getValueAsString() {
        return value;
    }

    @Override
    public String toString() {
        if(name.isEmpty())
            return "\"" + value + "\"";
        return "\"" + name + "\":\"" + addEscapeChars(value) + "\"";
    }

    private String addEscapeChars(String s) {
        if(s == null || s.isEmpty()) return s;
        StringBuilder sb = new StringBuilder();
        for(char c : s.toCharArray()) {
            sb.append(c);
            if(c == '\\') {
                sb.append('\\');
            }
        }
        return sb.toString();
    }
}
