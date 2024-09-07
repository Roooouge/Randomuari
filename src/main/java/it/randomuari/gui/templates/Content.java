package it.randomuari.gui.templates;

import it.randomuari.gui.GUIUtils;
import java.awt.BorderLayout;
import javax.swing.JPanel;

public abstract class Content extends JPanel {

    protected Content() {
        defaults();
        init();
    }

    /*
     *
     */

    public abstract void init();

    /*
     *
     */

    private void defaults() {
        setBackground(GUIUtils.BACKGROUND);
        setLayout(new BorderLayout());
    }
}
