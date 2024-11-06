package it.randomuari;

import it.randomuari.gui.GUIUtils;
import it.randomuari.gui.mainview.MainView;
import it.randomuari.players.PlayersManager;
import javax.swing.SwingUtilities;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Randomuari {

    public static final String VERSION = "1.0.0-SNAPSHOT";
    public static final String VERSION_LABEL = "Randomuari v" + VERSION;

    public static void main(String[] args) {
        try {
            PlayersManager.init();

            GUIUtils.MAIN_VIEW = new MainView();
            MainView mainView = GUIUtils.MAIN_VIEW;
            mainView.setVisible(true);
            SwingUtilities.invokeLater(mainView::commandFocus);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}