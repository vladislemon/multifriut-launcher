package ntzw.mfl.json;

public class JsonNumber extends JsonObject {

    private double value;

    public JsonNumber(String name, double value) {
        super(name);
        this.value = value;
    }

    public JsonNumber(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int asInt() {
        return (int)value;
    }

    @Override
    public String getValueAsString() {
        double rounded = Math.round(value);
        if(Math.abs(rounded - value) <= Double.MIN_NORMAL)
            return Integer.toString((int)rounded);
        return Double.toString(value);
    }

    @Override
    public String toString() {
        String s = "";
        if(!name.isEmpty())
            s = "\"" + name + "\":";
        return s + getValueAsString();
    }
}
