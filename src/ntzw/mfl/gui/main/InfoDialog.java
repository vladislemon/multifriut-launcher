package ntzw.mfl.gui.main;

import java.awt.*;
import java.awt.event.*;

public class InfoDialog extends Dialog {

    private Button okButton;

    public InfoDialog(Frame owner, String title, String text) {
        super(owner, title, true);
        setResizable(false);

        Panel textPanel = new Panel();
        add(textPanel, BorderLayout.NORTH);
        textPanel.add(new Label(text));

        Panel buttonPanel = new Panel();
        add(buttonPanel, BorderLayout.SOUTH);
        okButton = new Button("Ok");
        buttonPanel.add(okButton);

        pack();
        setLocationRelativeTo(owner);

        WindowListener closeListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ((Window)e.getSource()).dispose();
            }
        };
        addWindowListener(closeListener);
        okButton.addActionListener(e -> this.dispose());
    }

    public Button getOkButton() {
        return okButton;
    }
}
