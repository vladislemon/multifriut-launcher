package ntzw.mfl.auth;

public class GameAuthData {

    private String playerId;
    private String playerName;
    private String accessToken;

    public GameAuthData(String playerId, String playerName, String accessToken) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.accessToken = accessToken;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
