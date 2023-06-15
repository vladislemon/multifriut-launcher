package ntzw.mfl;

import ntzw.mfl.launch.LaunchArguments;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MultiFruitLaunch {

    private static String nativesPath = Paths.get("caches", "natives", "1.7.10").toAbsolutePath().toString();
    private static String libsPath = Paths.get("caches", "libs").toAbsolutePath().toString();
    private static String gameJar = Paths.get("caches", "versions", "1.7.10.jar").toAbsolutePath().toString();
    private static String mainClass = "net.minecraft.launchwrapper.Launch";
    private static String tweakClass = "cpw.mods.fml.common.launcher.FMLTweaker";
    private static String gameDir = Paths.get("instance" ).toAbsolutePath().toString();
    private static String assetsDir = Paths.get("caches", "assets").toAbsolutePath().toString();
    private static String version = "1.7.10";
    private static String assetIndex = "1.7.10";
    private static String userProperties = "{}";

    private static List<File> listFiles(String dir) {
        List<File> list = new ArrayList<>();
        doListFiles(new File(dir), list);
        return list;
    }
    private static void doListFiles(File f, List<File> list) {
        for(File file : f.listFiles()) {
            if(file.isDirectory()) {
                doListFiles(file, list);
            } else if(file.isFile()) {
                list.add(file);
            }
        }
    }

    public static LaunchArguments buildArguments(String username, String uuid, String accessToken) {
        System.out.println(libsPath);
        List<String> libs = new ArrayList<>();
        for(File file : listFiles(libsPath)) {
            libs.add(file.getAbsolutePath());
        }
        libs.add(gameJar);
        LaunchArguments launchArguments = new LaunchArguments();
        launchArguments.setNativesPath(nativesPath);
        launchArguments.setLibrariesList(libs);
        launchArguments.setMainClass(mainClass);
        launchArguments.setTweakClass(tweakClass);
        launchArguments.setGameDir(gameDir);
        launchArguments.setAssetsDir(assetsDir);
        launchArguments.setVersion(version);
        launchArguments.setAssetIndex(assetIndex);
        launchArguments.setUserProperties(userProperties);

        launchArguments.setUsername(username);
        launchArguments.setUuid(uuid);
        launchArguments.setAccessToken(accessToken);

        return launchArguments;
    }

    public static String getNativesPath() {
        return nativesPath;
    }

    public static void setNativesPath(String nativesPath) {
        MultiFruitLaunch.nativesPath = nativesPath;
    }

    public static String getLibsPath() {
        return libsPath;
    }

    public static void setLibsPath(String libsPath) {
        MultiFruitLaunch.libsPath = libsPath;
    }

    public static String getGameJar() {
        return gameJar;
    }

    public static void setGameJar(String gameJar) {
        MultiFruitLaunch.gameJar = gameJar;
    }

    public static String getGameDir() {
        return gameDir;
    }

    public static void setGameDir(String gameDir) {
        MultiFruitLaunch.gameDir = gameDir;
    }

    public static String getAssetsDir() {
        return assetsDir;
    }

    public static void setAssetsDir(String assetsDir) {
        MultiFruitLaunch.assetsDir = assetsDir;
    }
}
