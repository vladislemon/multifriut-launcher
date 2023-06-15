package ntzw.mfl.launch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LaunchArguments {

    private String nativesPath;
    private List<String> librariesList;
    private String mainClass;
    private String tweakClass;
    private String gameDir;
    private String assetsDir;
    private String version;
    private String assetIndex;
    private String username;
    private String uuid;
    private String accessToken;
    private String userProperties;

    public String getNativesPath() {
        return nativesPath;
    }

    public void setNativesPath(String nativesPath) {
        this.nativesPath = nativesPath;
    }

    public List<String> getLibrariesList() {
        return librariesList;
    }

    public void setLibrariesList(List<String> librariesList) {
        this.librariesList = librariesList;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public String getTweakClass() {
        return tweakClass;
    }

    public void setTweakClass(String tweakClass) {
        this.tweakClass = tweakClass;
    }

    public String getGameDir() {
        return gameDir;
    }

    public void setGameDir(String gameDir) {
        this.gameDir = gameDir;
    }

    public String getAssetsDir() {
        return assetsDir;
    }

    public void setAssetsDir(String assetsDir) {
        this.assetsDir = assetsDir;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAssetIndex() {
        return assetIndex;
    }

    public void setAssetIndex(String assetIndex) {
        this.assetIndex = assetIndex;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUserProperties() {
        return userProperties;
    }

    public void setUserProperties(String userProperties) {
        this.userProperties = userProperties;
    }

    public List<String> asList() {
        List<String> list = new ArrayList<>();
        list.add("-Djava.library.path=" + nativesPath);
        list.add("-cp");
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < librariesList.size(); i++) {
            sb.append(librariesList.get(i));
            if(i < librariesList.size() - 1)
                sb.append(File.pathSeparatorChar);
        }
        list.add(sb.toString());
        list.add(mainClass);
        if(tweakClass != null) {
            list.add("--tweakClass");
            list.add(tweakClass);
        }
        list.add("--gameDir");
        list.add(gameDir);
        list.add("--assetsDir");
        list.add(assetsDir);
        list.add("--version");
        list.add(version);
        list.add("--assetIndex");
        list.add(assetIndex);
        list.add("--username");
        list.add(username);
        list.add("--uuid");
        list.add(uuid);
        list.add("--accessToken");
        list.add(accessToken);
        list.add("--userProperties");
        list.add(userProperties == null ? "{}" : userProperties);

        return list;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(String s : asList()) {
            sb.append(s);
            sb.append(' ');
        }

        return sb.toString();
    }
}
