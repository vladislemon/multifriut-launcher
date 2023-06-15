package ntzw.mfl.auth.yggdrasil;

import ntzw.mfl.json.JsonObject;
import ntzw.mfl.json.JsonUtil;

public class YggdrasilAuthAgent {

    private String name;
    private int version;

    public YggdrasilAuthAgent(String name, int version) {
        this.name = name;
        this.version = version;
    }

    public YggdrasilAuthAgent(JsonObject jsonObject) {
        this.name = JsonUtil.getStringIfPresent(jsonObject, "name");
        this.version = JsonUtil.getNumberIfPresent(jsonObject, "version").intValue();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
