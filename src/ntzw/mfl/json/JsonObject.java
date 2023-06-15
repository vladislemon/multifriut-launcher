package ntzw.mfl.json;

import java.util.ArrayList;
import java.util.List;

public class JsonObject {

    protected String name;
    private JsonObject superObject;
    private List<JsonObject> fields;

    public JsonObject(String name) {
        if(name == null) {
            this.name = "";
        } else {
            this.name = name;
        }
        this.fields = new ArrayList<>(5);
    }

    public JsonObject() {
        this("");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonObject getSuperObject() {
        return superObject;
    }

    public void setSuperObject(JsonObject superObject) {
        this.superObject = superObject;
    }

    public JsonObject getField(String name) {
        for(JsonObject field : fields) {
            if(field.name.equals(name)) {
                return field;
            }
        }
        return null;
    }

    public List<JsonObject> getFields() {
        return fields;
    }

    public void addField(JsonObject field) {
        fields.add(field);
        field.setSuperObject(this);
    }

    public JsonObject removeField(String name) {
        for(JsonObject field : fields) {
            if(field.name.equals(name)) {
                fields.remove(field);
                return field;
            }
        }
        return null;
    }

    public String getValueAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for(int i = 0; i < fields.size(); i++) {
            sb.append(fields.get(i));
            if(i < fields.size() - 1) {
                sb.append(',');
            }
        }
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return name.isEmpty() ? super.hashCode() : name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof JsonObject ? ((JsonObject) obj).name.equals(name) : super.equals(obj);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(!name.isEmpty()) {
            sb.append('\"');
            sb.append(name);
            sb.append("\": ");
        }
        sb.append("{\n");
        for(int i = 0; i < fields.size(); i++) {
            sb.append(fields.get(i));
            if(i < fields.size() - 1) {
                sb.append(',');
            }
            sb.append('\n');
        }
        sb.append("}\n");
        return sb.toString();
    }
}
