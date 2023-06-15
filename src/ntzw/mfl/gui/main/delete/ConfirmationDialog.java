package ntzw.mfl.gui.main.delete;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ConfirmationDialog extends Dialog {
    private final Button yesButton;
    private final Button noButton;
    private boolean userAgreed;

    public ConfirmationDialog(Frame owner, String text) {
        super(owner, "Confirmation", true);
        setResizable(false);
        setAlwaysOnTop(true);

        Panel buttonsPanel = new Panel();
        add(buttonsPanel, BorderLayout.CENTER);
        yesButton = new Button("Yes");
        noButton = new Button("No");
        buttonsPanel.add(yesButton);
        buttonsPanel.add(noButton);

        Panel textPanel = new Panel();
        textPanel.add(new Label(text));
        add(textPanel, BorderLayout.NORTH);

        yesButton.addActionListener(e -> userAgreed = true);
        yesButton.addActionListener(e -> this.dispose());
        noButton.addActionListener(e -> this.dispose());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ((Window) e.getSource()).dispose();
            }
        });

        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    public boolean isUserAgreed() {
        return userAgreed;
    }
}
