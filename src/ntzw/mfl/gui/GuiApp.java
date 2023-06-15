package ntzw.mfl.gui;

import ntzw.mfl.AccountManager;
import ntzw.mfl.Main;
import ntzw.mfl.Settings;
import ntzw.mfl.controller.MainController;
import ntzw.mfl.gui.main.MainFrame;
import ntzw.mfl.json.JsonParser;

import java.nio.file.Paths;

public class GuiApp {

    public void run(Settings settings, JsonParser jsonParser) {
        AccountManager accountManager = new AccountManager();
        accountManager.loadAccounts(Paths.get(settings.getString(Main.ACCOUNT_STORAGE_PATH_SETTING_NAME)), jsonParser, true);

        MainThreadExecutor mainThreadExecutor = new MainThreadExecutor();
        MainFrame mainFrame = new MainFrame(accountManager);
        MainController controller = new MainController(accountManager, settings, mainThreadExecutor, mainFrame);
        mainFrame.setController(controller);

        mainFrame.setVisible(true);

        mainThreadExecutor.run();
    }
}
