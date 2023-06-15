package ntzw.mfl.auth.yggdrasil;

import ntzw.mfl.json.*;

import java.util.UUID;

public class YggdrasilAuthData {

    private String clientToken;
    private String accessToken;
    private String username;
    private String host;
    private YggdrasilAuthAgent agent;
    private YggdrasilProfile selectedProfile;

    public YggdrasilAuthData(String clientToken) {
        this.clientToken = clientToken;
    }

    public YggdrasilAuthData() {
        this.clientToken = UUID.randomUUID().toString().replaceAll("-", "");
    }

    public String getClientToken() {
        return clientToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public YggdrasilAuthAgent getAgent() {
        return agent;
    }

    public void setAgent(YggdrasilAuthAgent agent) {
        this.agent = agent;
    }

    public YggdrasilProfile getSelectedProfile() {
        return selectedProfile;
    }

    public void setSelectedProfile(YggdrasilProfile selectedProfile) {
        this.selectedProfile = selectedProfile;
    }

    public String getAccountName() {
        if(username != null && host != null)
            return username + " (" + host + ")";
        return null;
    }

    public String getPlayerName() {
        return selectedProfile != null ? selectedProfile.getName() : null;
    }

    public String getPlayerId() {
        return selectedProfile != null ? selectedProfile.getId() : null;
    }

    public void fillFromJson(JsonObject jsonObject) {
        clientToken = JsonUtil.getStringIfPresent(jsonObject, "clientToken");
        accessToken = JsonUtil.getStringIfPresent(jsonObject, "accessToken");
        username = JsonUtil.getStringIfPresent(jsonObject, "username");
        host = JsonUtil.getStringIfPresent(jsonObject, "host");
        JsonObject agentField = jsonObject.getField("agent");
        if(agentField != null) {
            agent = new YggdrasilAuthAgent(agentField);
        }
        JsonObject profileField = jsonObject.getField("selectedProfile");
        if(agentField != null) {
            selectedProfile = new YggdrasilProfile(profileField);
        }
    }

    public void writeIntoJson(JsonObject jsonObject) {
        JsonUtil.addStringIfNonNull(jsonObject, "clientToken", clientToken);
        JsonUtil.addStringIfNonNull(jsonObject, "accessToken", accessToken);
        JsonUtil.addStringIfNonNull(jsonObject, "username", username);
        JsonUtil.addStringIfNonNull(jsonObject, "host", host);
        if(agent != null) {
            JsonObject agentField = new JsonObject("agent");
            agentField.addField(new JsonString("name", agent.getName()));
            agentField.addField(new JsonNumber("version", agent.getVersion()));
            jsonObject.addField(agentField);
        }
        if(selectedProfile != null) {
            JsonObject selectedProfileField = new JsonObject("selectedProfile");
            selectedProfileField.addField(new JsonString("id", selectedProfile.getId()));
            selectedProfileField.addField(new JsonString("name", selectedProfile.getName()));
            selectedProfileField.addField(new JsonBoolean("legacy", selectedProfile.isLegacy()));
            jsonObject.addField(selectedProfileField);
        }
    }
}
