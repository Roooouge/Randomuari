package it.randomuari.gui.mainview;

import it.randomuari.Randomuari;
import it.randomuari.gui.templates.View;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainView extends View {

    public MainView() throws Exception {
        super(MainViewContent.class, Randomuari.VERSION_LABEL);
    }

    /*
     *
     */

    public void commandFocus() {
        ((MainViewContent) getContentPane()).commandFocus();
    }

}
