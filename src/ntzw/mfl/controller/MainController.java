package ntzw.mfl.controller;

import ntzw.mfl.AccountManager;
import ntzw.mfl.AuthOperation;
import ntzw.mfl.Main;
import ntzw.mfl.MultiFruitLaunch;
import ntzw.mfl.Settings;
import ntzw.mfl.auth.AuthStatus;
import ntzw.mfl.auth.GameAuthData;
import ntzw.mfl.controller.state.LauncherState;
import ntzw.mfl.controller.state.LauncherStateNoSelected;
import ntzw.mfl.controller.state.LauncherStateReady;
import ntzw.mfl.gui.MainThreadExecutor;
import ntzw.mfl.gui.main.MainFrame;
import ntzw.mfl.launch.Launcher;
import ntzw.mfl.launch.ProcessMonitor;

import java.io.IOException;

public class MainController {
    private final AccountManager accountManager;
    private final Settings settings;
    private final MainThreadExecutor mainThreadExecutor;
    private final MainFrame mainFrame;

    public final LauncherState NO_SELECTED_STATE = new LauncherStateNoSelected(this);
    public final LauncherState READY_STATE = new LauncherStateReady(this);

    private LauncherState state;

    public MainController(AccountManager accountManager, Settings settings, MainThreadExecutor mainThreadExecutor, MainFrame mainFrame) {
        this.accountManager = accountManager;
        this.settings = settings;
        this.mainThreadExecutor = mainThreadExecutor;
        this.mainFrame = mainFrame;
        initState();
    }

    private void initState() {
        if (accountManager.getSelectedAccountIndex() != -1) {
            setState(READY_STATE);
            mainFrame.onReadyState();
        } else {
            setState(NO_SELECTED_STATE);
            mainFrame.onNoSelectedState();
        }
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public void setState(LauncherState state) {
        this.state = state;
    }

    public void onAccountCreateAction(String username, String authServer) {
        mainThreadExecutor.execute(() -> state.onAccountCreateAction(username, authServer));
    }

    public void onAccountSelected(int selectedAccountIndex) {
        mainThreadExecutor.execute(() -> state.onAccountSelected(selectedAccountIndex));
    }

    public void onLoginAction(int selectedAccountIndex) {
        mainThreadExecutor.execute(() -> state.onLoginAction(selectedAccountIndex));
    }

    public void onDeleteAction(int selectedAccountIndex, boolean forced) {
        mainThreadExecutor.execute(() -> state.onDeleteAction(selectedAccountIndex, forced));
    }

    public void onLauncherClosed() {
        accountManager.saveAccounts();
        settings.save();
        mainThreadExecutor.stop();
    }

    public boolean createAccount(String username, String authServer) {
        if (username.isEmpty()) {
            return false;
        }
        AuthOperation authOperation = retry -> accountManager.addAccountByUserInput(username, authServer, mainFrame.getPasswordSupplier(username, retry));
        AuthStatus authStatus = tryAuthMultipleTimes(authOperation);
        executeIfStatusIsOk(authStatus, () -> mainFrame.onAccountAdded(username, authServer, accountManager.getSelectedAccountIndex()));
        return authStatus == AuthStatus.OK;
    }

    public void executeIfStatusIsOk(AuthStatus authStatus, Runnable runnable) {
        if (authStatus == AuthStatus.OK) {
            mainFrame.onAuthIsOk();
            runnable.run();
        } else {
            mainFrame.onAuthIsNotOk(authStatus);
        }
    }

    public AuthStatus tryAuthMultipleTimes(AuthOperation authOperation) {
        AuthStatus authStatus = authOperation.auth(false);
        while (authStatus == AuthStatus.INVALID_CREDENTIALS) {
            mainFrame.onInvalidCredentials();
            authStatus = authOperation.auth(true);
        }
        return authStatus;
    }

    public ProcessMonitor launchGame() {
        GameAuthData gameAuthData = accountManager.getGameAuthData();
        String javaPath = settings.getString(Main.JAVA_PATH_SETTING_NAME);
        String[] javaArgs = settings.getString(Main.ADDITIONAL_ARGS_SETTING_NAME).split("\\s+");
        String[] args = new String[javaArgs.length + 1];
        System.arraycopy(javaArgs, 0, args, 1, javaArgs.length);
        args[0] = "-Xmx" + settings.getString(Main.MAX_MEMORY_SETTING_NAME);
        Launcher launcher = new Launcher(MultiFruitLaunch.buildArguments(
                gameAuthData.getPlayerName(),
                gameAuthData.getPlayerId(),
                gameAuthData.getAccessToken()
        ), javaPath, args);
        try {
            return new ProcessMonitor(launcher.runMinecraft(settings.getBoolean(Main.REDIRECT_OUTPUT_SETTING_NAME)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void validateGame(ProcessMonitor processMonitor) {
        boolean gameIsOk = false;
        if (processMonitor != null) {
            try {
                if (processMonitor.wait(10)) {
                    gameIsOk = true;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (gameIsOk) {
            mainFrame.onGameLaunched();
        } else {
            mainFrame.onGameCrashed();
        }
    }
}
