package ntzw.mfl.auth.yggdrasil;

import ntzw.mfl.json.JsonBoolean;
import ntzw.mfl.json.JsonObject;
import ntzw.mfl.json.JsonUtil;

public class YggdrasilProfile {

    private String id;
    private String name;
    private boolean legacy;

    public YggdrasilProfile(String id, String name, boolean legacy) {
        this.id = id;
        this.name = name;
        this.legacy = legacy;
    }

    public YggdrasilProfile(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public YggdrasilProfile(JsonObject jsonObject) {
        this.id = JsonUtil.getStringIfPresent(jsonObject, "id");
        this.name = JsonUtil.getStringIfPresent(jsonObject, "name");
        JsonObject legacyField = jsonObject.getField("legacy");
        this.legacy = legacyField != null && ((JsonBoolean) legacyField).getValue();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLegacy() {
        return legacy;
    }

    public void setLegacy(boolean legacy) {
        this.legacy = legacy;
    }
}
