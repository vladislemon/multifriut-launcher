package ntzw.mfl;

import ntzw.mfl.console.ConsoleApp;
import ntzw.mfl.gui.GuiApp;
import ntzw.mfl.json.JsonParser;
import ntzw.mfl.util.ArrayUtil;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        JsonParser jsonParser = new JsonParser();
        if(args != null && args.length > 1 && ArrayUtil.contains(args, "--nogui")) {
            String[] consoleArgs = new String[args.length - 1];
            int i = 0;
            for (String arg : args) {
                if (!"--nogui".equals(arg)) {
                    consoleArgs[i++] = arg;
                }
            }
            new ConsoleApp().run(loadSettings(jsonParser), jsonParser, consoleArgs);
            return;
        }
        new GuiApp().run(loadSettings(jsonParser), jsonParser);
    }

    public static final String ACCOUNT_STORAGE_PATH_SETTING_NAME = "accountStoragePath";
    public static final String ACCOUNT_STORAGE_PATH_SETTING_DEFAULT = "accounts.json";

    public static final String JAVA_PATH_SETTING_NAME = "javaPath";
    public static final String JAVA_PATH_SETTING_DEFAULT = "java";
    public static final String MAX_MEMORY_SETTING_NAME = "maxMemory";
    public static final String MAX_MEMORY_SETTING_DEFAULT = "3G";
    public static final String ADDITIONAL_ARGS_SETTING_NAME = "additionalJavaArgs";
    public static final String ADDITIONAL_ARGS_SETTING_DEFAULT = "";
    public static final String REDIRECT_OUTPUT_SETTING_NAME = "redirectOutput";
    public static final boolean REDIRECT_OUTPUT_SETTING_DEFAULT = true;

//    public static final String NATIVES_PATH_SETTING_NAME = "nativesPath";
//    public static final String LIBS_PATH_SETTING_NAME = "librariesPath";
//    public static final String GAME_JAR_PATH_SETTING_NAME = "gameJarPath";
//    public static final String GAME_DIR_PATH_SETTING_NAME = "gameDirectoryPath";
//    public static final String ASSETS_PATH_SETTING_NAME = "assetsPath";

    private static Settings loadSettings(JsonParser jsonParser) {
        Settings settings = new Settings(Paths.get("mfl_settings.json"));
        settings.load(jsonParser);

        settings.addString(ACCOUNT_STORAGE_PATH_SETTING_DEFAULT, ACCOUNT_STORAGE_PATH_SETTING_NAME);
        settings.addString(JAVA_PATH_SETTING_DEFAULT, JAVA_PATH_SETTING_NAME);
        settings.addString(MAX_MEMORY_SETTING_DEFAULT, MAX_MEMORY_SETTING_NAME);
        settings.addString(ADDITIONAL_ARGS_SETTING_DEFAULT, ADDITIONAL_ARGS_SETTING_NAME);
        settings.addBoolean(REDIRECT_OUTPUT_SETTING_DEFAULT, REDIRECT_OUTPUT_SETTING_NAME);
//        settings.addString(MultiFruitLaunch.getNativesPath(), NATIVES_PATH_SETTING_NAME);
//        settings.addString(MultiFruitLaunch.getLibsPath(), LIBS_PATH_SETTING_NAME);
//        settings.addString(MultiFruitLaunch.getGameJar(), GAME_JAR_PATH_SETTING_NAME);
//        settings.addString(MultiFruitLaunch.getGameDir(), GAME_DIR_PATH_SETTING_NAME);
//        settings.addString(MultiFruitLaunch.getAssetsDir(), ASSETS_PATH_SETTING_NAME);
//
//        MultiFruitLaunch.setNativesPath(settings.getString(NATIVES_PATH_SETTING_NAME));
//        MultiFruitLaunch.setLibsPath(settings.getString(LIBS_PATH_SETTING_NAME));
//        MultiFruitLaunch.setGameJar(settings.getString(GAME_JAR_PATH_SETTING_NAME));
//        MultiFruitLaunch.setGameDir(settings.getString(GAME_DIR_PATH_SETTING_NAME));
//        MultiFruitLaunch.setAssetsDir(settings.getString(ASSETS_PATH_SETTING_NAME));

        return settings;
    }

    public static HashMap<String, String> serverNameToUrl = new HashMap<>();
    public static HashMap<String, String> urlToServerName = new HashMap<>();
    static {
        serverNameToUrl.put("Mojang", "https://authserver.mojang.com");
        serverNameToUrl.put("Ely.by", "https://authserver.ely.by/auth");

        for(Map.Entry<String, String> entry : serverNameToUrl.entrySet()) {
            urlToServerName.put(entry.getValue(), entry.getKey());
        }
    }
}
