package ntzw.mfl.gui.main;

import java.awt.*;

public class InfoBar extends Panel {

    private Label statusLabel;

    public InfoBar(String initText) {
        super(new GridLayout(1, 0, 10, 10));
        add(statusLabel = new Label(initText));
    }

    public void info(String s) {
        statusLabel.setText(s);
    }
}
