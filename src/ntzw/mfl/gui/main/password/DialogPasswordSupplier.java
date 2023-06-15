package ntzw.mfl.gui.main.password;

import ntzw.mfl.PasswordSupplier;

import java.awt.*;

public class DialogPasswordSupplier implements PasswordSupplier {

    private final Frame owner;
    private final String username;
    private PasswordDialog dialog;

    public DialogPasswordSupplier(Frame owner, String username) {
        this.owner = owner;
        this.username = username;
    }

    @Override
    public String getPassword() {
        dialog = new PasswordDialog(owner, username);
        dialog.setVisible(true);
        while(dialog.isOpen()) {
            Thread.yield();
        }
        dialog.dispose();
        return dialog.getPassword();
    }

    @Override
    public boolean isSupplied() {
        return dialog.getPassword() != null;
    }

    public void close() {
        dialog.dispose();
    }
}
