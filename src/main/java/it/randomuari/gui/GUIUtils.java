package it.randomuari.gui;

import it.randomuari.gui.mainview.ActionsPanel;
import it.randomuari.gui.mainview.MainView;
import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingUtilities;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GUIUtils {


    public final Color BACKGROUND = Color.black;
    public final Color FOREGROUND = Color.decode("#00FF00");
    public final Color TRANSPARENT = new Color(0f,0f,0f,0f);

    public final Font FONT = new Font(Font.MONOSPACED, Font.BOLD, 16);

    public MainView MAIN_VIEW;
    public ActionsPanel ACTIONS_PANEL;

    /*
     *
     */

    public void repaint() {
        SwingUtilities.invokeLater(() -> {
            MAIN_VIEW.validate();
            MAIN_VIEW.repaint();
        });
    }
}
