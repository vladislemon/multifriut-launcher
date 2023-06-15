package ntzw.mfl.controller.state;

import ntzw.mfl.controller.MainController;

public abstract class LauncherState {
    protected final MainController controller;

    protected LauncherState(MainController controller) {
        this.controller = controller;
    }

    public abstract void onAccountCreateAction(String username, String authServer);

    public abstract void onAccountSelected(int selectedAccountIndex);

    public abstract void onLoginAction(int selectedAccountIndex);

    public abstract void onDeleteAction(int selectedAccountIndex, boolean forced);
}
