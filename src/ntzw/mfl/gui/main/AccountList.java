package ntzw.mfl.gui.main;

import ntzw.mfl.Main;
import ntzw.mfl.StringPair;

import java.awt.*;

public class AccountList extends List {

    public AccountList(java.util.List<StringPair> initialEntries, int initialSelected) {
        super(1, false);
        for(StringPair pair : initialEntries) {
            add(pair.getFirst(), pair.getSecond());
        }
        if(initialSelected != -1) {
            select(initialSelected);
        }
    }

    public void add(String name, String url) {
        add(name + " ( " + Main.urlToServerName.get(url) + " )");
    }
}
