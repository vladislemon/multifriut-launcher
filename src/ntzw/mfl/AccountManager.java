package ntzw.mfl;

import ntzw.mfl.auth.AuthStatus;
import ntzw.mfl.auth.GameAuthData;
import ntzw.mfl.auth.yggdrasil.YggdrasilAuth;
import ntzw.mfl.auth.yggdrasil.YggdrasilAuthAgent;
import ntzw.mfl.auth.yggdrasil.YggdrasilAuthData;
import ntzw.mfl.auth.yggdrasil.YggdrasilAuthException;
import ntzw.mfl.json.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AccountManager {

    private JsonParser jsonParser;
    private List<YggdrasilAuthData> storage;
    private int selectedAccount = -1;
    private Path storageFilePath;

    private static final String ACCOUNTS_FIELD_NAME = "accounts";
    private static final String SELECTED_FIELD_NAME = "selectedAccount";

    public AccountManager() {
        storage = new ArrayList<>();
    }

    private void updateToken(YggdrasilAuthData authData) {
        YggdrasilAuth auth = new YggdrasilAuth(authData, jsonParser);
        try {
            auth.validate();
        } catch (YggdrasilAuthException validateException) {
            System.out.println("Invalid token for account: " + authData.getAccountName());
            System.out.println(validateException.getMessage());
            System.out.println("Refreshing...");
            try {
                auth.refresh();
            } catch (YggdrasilAuthException refreshException) {
                authData.setAccessToken(null); //to avoid redundant "validate" and "refresh" in future auth process
                System.out.println("Token can't be refreshed");
                System.out.println(refreshException.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateAllTokens() {
        for (YggdrasilAuthData authData : storage) {
            updateToken(authData);
        }
    }

    public void loadAccounts(Path path, JsonParser jsonParser, boolean updateTokens) {
        this.jsonParser = jsonParser;
        try {
            JsonObject jsonObject = JsonUtil.fromFile(path, jsonParser, false);
            if (jsonObject != null) {
                JsonArray accounts = (JsonArray) jsonObject.getField(ACCOUNTS_FIELD_NAME);
                for (JsonObject account : accounts.getArray()) {
                    YggdrasilAuthData authData = new YggdrasilAuthData();
                    authData.fillFromJson(account);
                    if (updateTokens) {
                        updateToken(authData);
                    }
                    storage.add(authData);
                }
                selectAccount(((JsonNumber) jsonObject.getField(SELECTED_FIELD_NAME)).asInt());
            }
        } catch (Exception e) {
            System.out.println("Unable to load accounts storage from file");
            e.printStackTrace();
        }
        storageFilePath = path;
    }

    public void saveAccounts() {
        JsonArray accounts = new JsonArray(ACCOUNTS_FIELD_NAME);
        for (YggdrasilAuthData authData : storage) {
            JsonObject account = new JsonObject();
            authData.writeIntoJson(account);
            accounts.addField(account);
        }
        JsonNumber selectedAccountNumber = new JsonNumber(SELECTED_FIELD_NAME, selectedAccount);
        JsonObject root = new JsonObject();
        root.addField(accounts);
        root.addField(selectedAccountNumber);
        try {
            JsonUtil.toFile(root, storageFilePath);
        } catch (IOException e) {
            System.out.println("Unable to save accounts storage into file");
            e.printStackTrace();
        }
    }

    private boolean contains(YggdrasilAuthData authData) {
        for (YggdrasilAuthData account : storage) {
            if (account.getAccountName().equals(authData.getAccountName()))
                return true;
        }
        return false;
    }

    public AuthStatus addAccountByUserInput(String username, String authServer, YggdrasilAuthAgent authAgent,
                                            PasswordSupplier passwordSupplier)
            throws IllegalArgumentException {
        if (username == null || username.isEmpty())
            throw new IllegalArgumentException("Username can't be null or empty");
        if (authServer == null || authServer.isEmpty())
            throw new IllegalArgumentException("Hostname can't be null or empty");
        YggdrasilAuthData authData = new YggdrasilAuthData();
        authData.setUsername(username);
        authData.setHost(authServer);
        authData.setAgent(authAgent);
        if (contains(authData))
            throw new IllegalArgumentException("Account already present");
        AuthStatus authStatus = new YggdrasilAuth(authData, jsonParser).fullAuth(passwordSupplier, false);
        if (authStatus == AuthStatus.OK) {
            storage.add(authData);
        }
        return authStatus;
    }

    public AuthStatus addAccountByUserInput(String username, String authServer, PasswordSupplier passwordSupplier) {
        return addAccountByUserInput(username, authServer, new YggdrasilAuthAgent("Minecraft", 1), passwordSupplier);
    }

    public int getSelectedAccountIndex() {
        return selectedAccount;
    }

    public List<String> getAccountNames() {
        List<String> names = new ArrayList<>();
        for (YggdrasilAuthData authData : storage) {
            names.add(authData.getAccountName());
        }
        return names;
    }

    public List<StringPair> getNameHostPairs() {
        List<StringPair> list = new ArrayList<>();
        for (YggdrasilAuthData authData : storage) {
            list.add(new StringPair(authData.getUsername(), authData.getHost()));
        }
        return list;
    }

    public String getSelectedAccountName() {
        return selectedAccount != -1 ? storage.get(selectedAccount).getAccountName() : null;
    }

    public String getSelectedUsername() {
        return selectedAccount != -1 ? storage.get(selectedAccount).getUsername() : null;
    }

    public String getSelectedHost() {
        return selectedAccount != -1 ? storage.get(selectedAccount).getHost() : null;
    }

    public int selectAccount(int number) {
        selectedAccount = number > -1 && number < storage.size() ? number : -1;
        return selectedAccount;
    }

    private int getAccountIndex(String username, String authServer) {
        for (int i = 0; i < storage.size(); i++) {
            YggdrasilAuthData authData = storage.get(i);
            if (authData.getUsername().equals(username) && authData.getHost().equals(authServer))
                return i;
        }
        return -1;
    }

    public int selectAccount(String username, String authServer) {
        selectedAccount = getAccountIndex(username, authServer);
        return selectedAccount;
    }

    private void doRemove(int index) {
        ArrayList<YggdrasilAuthData> newList = new ArrayList<>();
        for (int i = 0; i < storage.size(); i++) {
            if (i != index) {
                newList.add(storage.get(i));
            }
        }
        storage = newList;
        selectAccount(-1);
    }

    public boolean removeAccount(int index, boolean force) {
        if (!force) {
            YggdrasilAuthData authData = storage.get(index);
            if (authData.getAccessToken() != null) {
                try {
                    new YggdrasilAuth(authData, jsonParser).invalidate();
                    doRemove(index);
                    return true;
                } catch (IOException | YggdrasilAuthException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return false;
        } else {
            doRemove(index);
        }
        return true;
    }

    public boolean removeSelectedAccount(boolean force) {
        return removeAccount(selectedAccount, force);
    }

    public AuthStatus authWithSelectedAccount(PasswordSupplier passwordSupplier) {
        return new YggdrasilAuth(storage.get(selectedAccount), jsonParser).fullAuth(passwordSupplier, true);
    }

    public GameAuthData getGameAuthData() {
        YggdrasilAuthData authData = storage.get(selectedAccount);
        return new GameAuthData(
                authData.getPlayerId(),
                authData.getPlayerName(),
                authData.getAccessToken()
        );
    }
}
