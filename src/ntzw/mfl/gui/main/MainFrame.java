package ntzw.mfl.gui.main;

import ntzw.mfl.AccountManager;
import ntzw.mfl.PasswordSupplier;
import ntzw.mfl.auth.AuthStatus;
import ntzw.mfl.controller.MainController;
import ntzw.mfl.gui.main.create.CreateDialog;
import ntzw.mfl.gui.main.delete.ConfirmationDialog;
import ntzw.mfl.gui.main.password.DialogPasswordSupplier;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends Frame {
    private final ButtonPanel buttonPanel;
    private final AccountList accountList;
    private final InfoBar infoBar;

    private MainController controller;

    public MainFrame(AccountManager accountManager) {
        super("MultiFruit Launcher [v0.2]");

        setSize(400, 225);
        setLocationRelativeTo(null);

        add(buttonPanel = new ButtonPanel(), BorderLayout.NORTH);
        add(accountList = new AccountList(accountManager.getNameHostPairs(), accountManager.getSelectedAccountIndex()), BorderLayout.CENTER);
        add(infoBar = new InfoBar("Hello"), BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ((Window) e.getSource()).dispose();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                controller.onLauncherClosed();
            }
        });

        accountList.addItemListener(e -> controller.onAccountSelected(accountList.getSelectedIndex()));
        buttonPanel.getLoginButton().addActionListener(e -> controller.onLoginAction(accountList.getSelectedIndex()));
        buttonPanel.getCreateButton().addActionListener(e -> new CreateDialog(this));
        buttonPanel.getDeleteButton().addActionListener(e -> onDeleteAction(accountList.getSelectedIndex()));
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }

    public PasswordSupplier getPasswordSupplier(String username, boolean retry) {
        String message = retry
                ? "Invalid, try again (" + username + ")"
                : "Password for " + username;
        return new DialogPasswordSupplier(this, message);
    }

    public void onNoSelectedState() {
        buttonPanel.getLoginButton().setEnabled(false);
        buttonPanel.getCreateButton().setEnabled(true);
        buttonPanel.getDeleteButton().setEnabled(false);
        buttonPanel.getSettingsButton().setEnabled(true);
        accountList.setEnabled(true);
        infoBar.info("No selected account");
    }

    public void onReadyState() {
        buttonPanel.getLoginButton().setEnabled(true);
        buttonPanel.getCreateButton().setEnabled(true);
        buttonPanel.getDeleteButton().setEnabled(true);
        buttonPanel.getSettingsButton().setEnabled(true);
        accountList.setEnabled(true);
        infoBar.info("Ready");
    }

    public void onBlockedState() {
        buttonPanel.getLoginButton().setEnabled(false);
        buttonPanel.getCreateButton().setEnabled(false);
        buttonPanel.getDeleteButton().setEnabled(false);
        buttonPanel.getSettingsButton().setEnabled(false);
        accountList.setEnabled(false);
        infoBar.info("Blocked");
    }

    public void onInvalidCredentials() {
        infoBar.info("Invalid credentials");
    }

    public void onAuthIsOk() {
        infoBar.info("Auth OK! Launching game...");
    }

    public void onAuthIsNotOk(AuthStatus authStatus) {
        infoBar.info("Auth status: " + authStatus);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onGameLaunched() {
        for (int i = 5; i > 0; i--) {
            infoBar.info("Game started, self-destruction in " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        this.dispose();
    }

    public void onGameCrashed() {
        infoBar.info("Looks like game is crashed, please check logs");
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    public void onAccountCreateAction(CreateDialog createDialog, String username, String authServer) {
        controller.onAccountCreateAction(username, authServer);
        createDialog.dispose();
    }

    public void onDeleteAction(int selectedAccountIndex) {
        String username = accountList.getSelectedItem();
        ConfirmationDialog dialog = new ConfirmationDialog(this, "Delete account " + username + " ?");
        if (dialog.isUserAgreed()) {
            controller.onDeleteAction(selectedAccountIndex, false);
        }
    }

    public void onForcedDeleteAction(int selectedAccountIndex) {
        ConfirmationDialog dialog = new ConfirmationDialog(this, "Failed account invalidation, delete anyway?");
        if (dialog.isUserAgreed()) {
            controller.onDeleteAction(selectedAccountIndex, true);
        }
    }

    public void onAccountAdded(String username, String authServer, int selectedAccountIndex) {
        accountList.add(username, authServer);
        accountList.select(selectedAccountIndex);
    }

    public void onAccountDeleted(int selectedAccountIndex) {
        accountList.remove(selectedAccountIndex);
        accountList.select(-1);
    }
}
