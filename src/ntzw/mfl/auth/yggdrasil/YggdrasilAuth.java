package ntzw.mfl.auth.yggdrasil;

import ntzw.mfl.PasswordSupplier;
import ntzw.mfl.StringPair;
import ntzw.mfl.auth.AuthStatus;
import ntzw.mfl.http.HttpResponse;
import ntzw.mfl.http.HttpUtil;
import ntzw.mfl.json.JsonNumber;
import ntzw.mfl.json.JsonObject;
import ntzw.mfl.json.JsonParser;
import ntzw.mfl.json.JsonString;

import java.io.IOException;
import java.net.HttpURLConnection;

public class YggdrasilAuth {

    private YggdrasilAuthData authData;

    private JsonParser jsonParser;

    public YggdrasilAuth(YggdrasilAuthData authData, JsonParser jsonParser) {
        this.authData = authData;
        this.jsonParser = jsonParser;
    }

    public YggdrasilAuthData getAuthData() {
        return authData;
    }

    private HttpResponse doRequest(String endpoint, JsonObject payload) throws IOException {
        return HttpUtil.post(authData.getHost() + "/" + endpoint, payload.toString(),
                new StringPair("Content-Type", "application/json"));
    }

    public void authenticate(PasswordSupplier passwordSupplier) throws IOException, YggdrasilAuthException {
        JsonObject payload = new JsonObject();

        JsonObject agentObject = new JsonObject("agent");
        agentObject.addField(new JsonString("name", authData.getAgent().getName()));
        agentObject.addField(new JsonNumber("version", authData.getAgent().getVersion()));
        payload.addField(agentObject);

        payload.addField(new JsonString("username", authData.getUsername()));
        String password = passwordSupplier.getPassword();
        if(password == null || password.isEmpty())
            throw new IllegalArgumentException("Null or empty password");
        payload.addField(new JsonString("password", password));
        payload.addField(new JsonString("clientToken", authData.getClientToken()));

        HttpResponse response = doRequest("authenticate", payload);

        JsonObject responseJson = jsonParser.getRootObject(response.getPayload());
        if(response.getCode() != HttpURLConnection.HTTP_OK || responseJson.getField("error") != null)
            throw new YggdrasilAuthException(response.getCode(), responseJson);

        authData.setAccessToken(responseJson.getField("accessToken").getValueAsString());
        authData.setSelectedProfile(new YggdrasilProfile(responseJson.getField("selectedProfile")));
    }

    public void refresh() throws IOException, YggdrasilAuthException {
        JsonObject payload = new JsonObject();
        payload.addField(new JsonString("accessToken", authData.getAccessToken()));
        payload.addField(new JsonString("clientToken", authData.getClientToken()));

        HttpResponse response = doRequest("refresh", payload);

        JsonObject responseJson = jsonParser.getRootObject(response.getPayload());
        if(response.getCode() != HttpURLConnection.HTTP_OK || responseJson.getField("error") != null)
            throw new YggdrasilAuthException(response.getCode(), responseJson);

        authData.setAccessToken(responseJson.getField("accessToken").getValueAsString());
        authData.setSelectedProfile(new YggdrasilProfile(responseJson.getField("selectedProfile")));
    }

    public void validate() throws IOException, YggdrasilAuthException {
        JsonObject payload = new JsonObject();
        payload.addField(new JsonString("accessToken", authData.getAccessToken()));
        payload.addField(new JsonString("clientToken", authData.getClientToken()));

        HttpResponse response = doRequest("validate", payload);

        JsonObject responseJson = jsonParser.getRootObject(response.getPayload());
        if(response.getCode() != HttpURLConnection.HTTP_NO_CONTENT && response.getCode() != HttpURLConnection.HTTP_OK)
            throw new YggdrasilAuthException(response.getCode(), responseJson);
    }

    public void signout(PasswordSupplier passwordSupplier) throws IOException, YggdrasilAuthException {
        JsonObject payload = new JsonObject();
        payload.addField(new JsonString("username", authData.getUsername()));
        payload.addField(new JsonString("password", passwordSupplier.getPassword()));

        HttpResponse response = doRequest("signout", payload);

        JsonObject responseJson = jsonParser.getRootObject(response.getPayload());
        if(response.getCode() != HttpURLConnection.HTTP_OK || responseJson.getField("error") != null)
            throw new YggdrasilAuthException(response.getCode(), responseJson);

        authData.setAccessToken(null);
    }

    public void invalidate() throws IOException, YggdrasilAuthException {
        JsonObject payload = new JsonObject();
        payload.addField(new JsonString("accessToken", authData.getAccessToken()));
        payload.addField(new JsonString("clientToken", authData.getClientToken()));

        HttpResponse response = doRequest("invalidate", payload);

        JsonObject responseJson = jsonParser.getRootObject(response.getPayload());
        if(response.getCode() != HttpURLConnection.HTTP_OK || responseJson.getField("error") != null)
            throw new YggdrasilAuthException(response.getCode(), responseJson);

        authData.setAccessToken(null);
    }

    public AuthStatus fullAuth(PasswordSupplier passwordSupplier, boolean refreshEvenIfValid) {
        if(authData.getClientToken() == null || authData.getAgent() == null || authData.getUsername() == null ||
            authData.getUsername().isEmpty() || authData.getHost() == null || authData.getHost().isEmpty()) {
            throw new IllegalArgumentException("AuthData not filled");
        }
        if(authData.getAccessToken() != null) {
            try {
                validate();
                if(refreshEvenIfValid) {
                    try {
                        refresh();
                        return AuthStatus.OK;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        return AuthStatus.UNDERLYING_ERROR;
                    }
                }
                return AuthStatus.OK;
            } catch (IOException e) {
                e.printStackTrace();
                return AuthStatus.UNDERLYING_ERROR;
            } catch (YggdrasilAuthException e) {
                System.out.println(e.getMessage());
                System.out.println("Validation failed, trying refresh token");
                try {
                    refresh();
                    return AuthStatus.OK;
                } catch (IOException e1) {
                    e1.printStackTrace();
                    return AuthStatus.UNDERLYING_ERROR;
                } catch (YggdrasilAuthException e1) {
                    System.out.println(e.getMessage());
                    System.out.println("Refreshing failed, user must authenticate with password");
                }
            }
        }
        try {
            authenticate(passwordSupplier);
            return AuthStatus.OK;
        } catch (IOException e) {
            e.printStackTrace();
            return AuthStatus.UNDERLYING_ERROR;
        } catch (YggdrasilAuthException e) {
            System.out.println("Authentication failed");
            System.out.println(e.getMessage());
            return AuthStatus.INVALID_CREDENTIALS;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return AuthStatus.EMPTY_PASSWORD;
        }
    }
}
