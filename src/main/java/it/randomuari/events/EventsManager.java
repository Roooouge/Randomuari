package it.randomuari.events;

import it.randomuari.gui.GUIUtils;
import it.randomuari.players.Player;
import it.randomuari.teams.Team;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EventsManager {

    public Events randomEvent() {
        List<Events> values = Arrays.asList(Events.values());
        Collections.shuffle(values);
        Random rand = new Random();

        for (Events event : values) {
            if (event.equals(Events.NONE))
                continue;

            float percent = rand.nextFloat(100f);
            if (percent >= event.getMin() && percent < event.getMax())
                return event;
        }

        return Events.NONE;
    }

    //

    public void handle(Player player, Team team, Events event, boolean simulate) {
        System.out.println("- Checking " + event.name());

        switch (event) {
            case BLAME:
                blame(player, team, simulate);
                break;
        }
    }

    //

    public void blame(Player player, Team team, boolean simulate) {
        int choice = 0;

        if (!simulate) {
            JButton confirm = new JButton("Confirm");
            confirm.setBackground(GUIUtils.BACKGROUND);
            confirm.setForeground(GUIUtils.FOREGROUND);
            confirm.setBorder(BorderFactory.createLineBorder(confirm.getForeground()));

            JButton cancel = new JButton("Cancel");
            cancel.setBackground(GUIUtils.BACKGROUND);
            cancel.setForeground(Color.red);
            cancel.setBorder(BorderFactory.createLineBorder(cancel.getForeground()));

            JButton random = new JButton("Random");
            random.setBackground(GUIUtils.BACKGROUND);
            random.setForeground(Color.white);
            random.setBorder(BorderFactory.createLineBorder(random.getForeground()));

            JOptionPane.showOptionDialog(
                    GUIUtils.MAIN_VIEW,
                    buildBlamePanel(player, team, confirm, cancel, random),
                    "",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[] {confirm, cancel, random},
                    confirm
            );
        }

        switch (choice) {
            case -1:
                break;
            case 1:
                break;
        }
    }

    private JPanel buildBlamePanel(Player player, Team team, JButton... buttons) {
        JLabel label = new JLabel("Blame event for player " + player.getName() + " to team " + team.getName());
        label.setBackground(GUIUtils.BACKGROUND);
        label.setForeground(GUIUtils.FOREGROUND);
        label.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(15, 15, 15, 15),
                BorderFactory.createMatteBorder(0, 0, 2, 0, GUIUtils.FOREGROUND)
        ));


        JPanel buttonsPanel = new JPanel(new GridLayout(1, buttons.length));
        buttonsPanel.setBackground(GUIUtils.BACKGROUND);
        for (JButton button : buttons) {
            buttonsPanel.add(button);
        }


        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(GUIUtils.BACKGROUND);
        panel.add(label, BorderLayout.NORTH);
        panel.add(buttonsPanel, BorderLayout.CENTER);

        return panel;
    }

}
