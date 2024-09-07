package it.randomuari.gui.templates;

import javax.swing.JFrame;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class View extends JFrame {

    protected View(Class<? extends Content> clazz, String title) throws Exception {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(clazz.getDeclaredConstructor().newInstance());
        setResizable(true);
        pack();
        setLocationRelativeTo(null);
    }

    /*
     *
     */
}
