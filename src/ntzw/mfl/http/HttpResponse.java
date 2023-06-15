package ntzw.mfl.http;

import java.util.List;
import java.util.Map;

public class HttpResponse {

    private int code;
    private Map<String, List<String>> properties;
    private String payload;

    public HttpResponse(int code, Map<String, List<String>> properties, String payload) {
        this.code = code;
        this.properties = properties;
        this.payload = payload;
    }

    public int getCode() {
        return code;
    }

    public Map<String, List<String>> getProperties() {
        return properties;
    }

    public String getPayload() {
        return payload;
    }
}
