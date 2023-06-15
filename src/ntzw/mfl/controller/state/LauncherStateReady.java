package ntzw.mfl.controller.state;

import ntzw.mfl.AccountManager;
import ntzw.mfl.AuthOperation;
import ntzw.mfl.auth.AuthStatus;
import ntzw.mfl.controller.MainController;
import ntzw.mfl.gui.main.MainFrame;
import ntzw.mfl.launch.ProcessMonitor;

public class LauncherStateReady extends LauncherState {
    public LauncherStateReady(MainController controller) {
        super(controller);
    }

    @Override
    public void onAccountCreateAction(String username, String authServer) {
        controller.getMainFrame().onBlockedState();
        controller.createAccount(username, authServer);
        controller.getMainFrame().onReadyState();
    }

    @Override
    public void onAccountSelected(int selectedAccountIndex) {
        if (selectedAccountIndex == -1) {
            controller.setState(controller.NO_SELECTED_STATE);
        }
    }

    @Override
    public void onLoginAction(int selectedAccountIndex) {
        if (selectedAccountIndex == -1) {
            return;
        }
        MainFrame mainFrame = controller.getMainFrame();
        AccountManager accountManager = controller.getAccountManager();

        mainFrame.onBlockedState();
        accountManager.selectAccount(selectedAccountIndex);
        String username = accountManager.getSelectedAccountName();
        AuthOperation authOperation = (retry) -> accountManager.authWithSelectedAccount(mainFrame.getPasswordSupplier(username, retry));
        AuthStatus authStatus = controller.tryAuthMultipleTimes(authOperation);

        controller.executeIfStatusIsOk(authStatus, () -> {
            ProcessMonitor processMonitor = controller.launchGame();
            controller.validateGame(processMonitor);
        });
        mainFrame.onReadyState();
    }

    @Override
    public void onDeleteAction(int selectedAccountIndex, boolean forced) {
        if (selectedAccountIndex == -1) {
            return;
        }
        MainFrame mainFrame = controller.getMainFrame();
        AccountManager accountManager = controller.getAccountManager();

        mainFrame.onBlockedState();

        boolean deleted = accountManager.removeAccount(selectedAccountIndex, forced);
        if (!forced && !deleted) {
            mainFrame.onForcedDeleteAction(selectedAccountIndex);
            return;
        }

        if (deleted) {
            mainFrame.onAccountDeleted(selectedAccountIndex);
            controller.setState(controller.NO_SELECTED_STATE);
            mainFrame.onNoSelectedState();
        } else {
            mainFrame.onReadyState();
        }
    }
}
