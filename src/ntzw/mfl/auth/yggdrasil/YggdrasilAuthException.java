package ntzw.mfl.auth.yggdrasil;

import ntzw.mfl.json.JsonObject;

public class YggdrasilAuthException extends Exception {

    private int responseCode;
    private JsonObject payload;

    public YggdrasilAuthException(int responseCode, JsonObject payload) {
        this.responseCode = responseCode;
        this.payload = payload;
    }

    @Override
    public String getMessage() {
        return responseCode + "; " + payload.getField("error") + "; " + payload.getField("errorMessage");
    }
}
