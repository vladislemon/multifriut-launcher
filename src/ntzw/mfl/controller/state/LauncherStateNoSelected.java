package ntzw.mfl.controller.state;

import ntzw.mfl.controller.MainController;

public class LauncherStateNoSelected extends LauncherState {
    public LauncherStateNoSelected(MainController controller) {
        super(controller);
    }

    @Override
    public void onAccountCreateAction(String username, String authServer) {
        controller.getMainFrame().onBlockedState();
        if (controller.createAccount(username, authServer)) {
            controller.setState(controller.READY_STATE);
            controller.getMainFrame().onReadyState();
        } else {
            controller.getMainFrame().onNoSelectedState();
        }
    }

    @Override
    public void onAccountSelected(int selectedAccountIndex) {
        if (selectedAccountIndex != -1) {
            controller.setState(controller.READY_STATE);
            controller.getMainFrame().onReadyState();
        }
    }

    @Override
    public void onLoginAction(int selectedAccountIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onDeleteAction(int selectedAccountIndex, boolean forced) {
        throw new UnsupportedOperationException();
    }
}
