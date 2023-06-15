package ntzw.mfl.gui.main.password;

import java.awt.*;
import java.awt.event.*;

public class PasswordDialog extends Dialog {

    private TextField passwordField;

    private volatile boolean isOpen = true;
    private volatile String password;
    private final Object passwordLock = new Object();

    public PasswordDialog(Frame owner, String title) {
        super(owner, title, true);
        setSize(320, 100);
        setLocationRelativeTo(owner);
        setResizable(false);

        ActionListener enterActionListener = e -> {
            String text = passwordField.getText();
            if(!text.isEmpty()) {
                setPassword(text);
                dispose();
            }
        };

        add(passwordField = new TextField(), BorderLayout.NORTH);
        passwordField.setEchoChar('*');
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enterActionListener.actionPerformed(null);
                }
            }
        });

        Panel buttonPanel = new Panel();
        Button enterButton = new Button("Enter");
        buttonPanel.add(enterButton);
        add(buttonPanel, BorderLayout.SOUTH);
        enterButton.addActionListener(enterActionListener);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ((Window)e.getSource()).dispose();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                isOpen = false;
            }
        });
        repaint();
    }

    public boolean isOpen() {
        return isOpen;
    }

    public String getPassword() {
        synchronized (passwordLock) {
            return password;
        }
    }

    private void setPassword(String password) {
        synchronized (passwordLock) {
            this.password = password;
        }
    }
}
