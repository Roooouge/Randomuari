package it.randomuari.gui.mainview;

import it.randomuari.Randomuari;
import it.randomuari.gui.GUIUtils;
import it.randomuari.gui.templates.Content;
import java.awt.BorderLayout;
import javax.swing.JPanel;

public class MainViewContent extends Content {

    private CommandBar commandBar;

    /*
     *
     */

    @Override
    public void init() {
        add(center(), BorderLayout.CENTER);
        add(south(), BorderLayout.SOUTH);
    }

    /*
     *
     */

    private JPanel center() {
        return GUIUtils.ACTIONS_PANEL = new ActionsPanel();
    }

    private JPanel south() {
        commandBar = new CommandBar();
        return commandBar;
    }

    /*
     *
     */

    public void commandFocus() {
        commandBar.commandFocus();
    }

}
