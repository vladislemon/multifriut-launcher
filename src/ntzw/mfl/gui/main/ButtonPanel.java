package ntzw.mfl.gui.main;

import java.awt.*;

public class ButtonPanel extends Panel {

    private Button loginButton;
    private Button createButton;
    private Button deleteButton;
    private Button settingsButton;

    public ButtonPanel() {
        super();
        add("loginButton", loginButton = new Button("Login"));
        add("createButton", createButton = new Button("Create"));
        add("deleteButton", deleteButton = new Button("Delete"));
        add("settingsButton", settingsButton = new Button("Settings"));
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Button getCreateButton() {
        return createButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getSettingsButton() {
        return settingsButton;
    }
}
