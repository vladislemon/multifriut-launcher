package ntzw.mfl.gui.main.create;

import ntzw.mfl.Main;
import ntzw.mfl.gui.main.MainFrame;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CreateDialog extends Dialog {

    private final TextField usernameField;
    private final Choice authServerChoice;
    private final Button addButton;

    public CreateDialog(MainFrame owner) {
        super(owner, "New account", true);
        setSize(320, 110);
        setLocationRelativeTo(owner);
        setResizable(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ((Window) e.getSource()).dispose();
            }
        });

        Panel paramsPanel = new Panel(new BorderLayout());
        add(paramsPanel, BorderLayout.NORTH);

        Panel usernamePanel = new Panel(new BorderLayout());
        paramsPanel.add(usernamePanel, BorderLayout.NORTH);
        usernamePanel.add(new Label("Username:"), BorderLayout.WEST);
        usernamePanel.add(usernameField = new TextField(), BorderLayout.CENTER);

        Panel authServerPanel = new Panel(new BorderLayout());
        paramsPanel.add(authServerPanel, BorderLayout.SOUTH);
        authServerPanel.add(new Label("Auth service:"), BorderLayout.WEST);
        authServerPanel.add(authServerChoice = new Choice(), BorderLayout.CENTER);

        for (String key : Main.serverNameToUrl.keySet()) {
            authServerChoice.add(key);
        }

        add(addButton = new Button("Add"), BorderLayout.SOUTH);

        addButton.addActionListener(e -> owner.onAccountCreateAction(this, getUsername(), getAuthURL()));

        setVisible(true);
    }

    public Button getAddButton() {
        return addButton;
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getAuthURL() {
        return Main.serverNameToUrl.get(authServerChoice.getSelectedItem());
    }
}
