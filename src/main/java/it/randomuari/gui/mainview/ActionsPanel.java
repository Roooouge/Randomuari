package it.randomuari.gui.mainview;

import it.randomuari.config.Config;
import it.randomuari.gui.GUIUtils;
import it.randomuari.players.Player;
import it.randomuari.teams.Team;
import it.randomuari.teams.TeamsManager;
import java.awt.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import javax.swing.border.LineBorder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ActionsPanel extends JPanel {

    public ActionsPanel() {
        setLayout(new BorderLayout());

        setBackground(GUIUtils.BACKGROUND);
        setForeground(GUIUtils.FOREGROUND);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, GUIUtils.FOREGROUND));
        setPreferredSize(new Dimension(1800,850));
    }

    /*
     *
     */

    private void setContent(Component component) {
        if (getComponentCount() > 0)
           remove(0);
        add(component, BorderLayout.CENTER);
        GUIUtils.repaint();
    }

    /*
     *
     */

    public void state() {
        JPanel panel = new JPanel(new GridLayout(1, TeamsManager.TEAMS.size()));

        int playersPerTeam = 25;
        for (Team team : TeamsManager.TEAMS) {
            JPanel teamPanel = new JPanel(new GridLayout(playersPerTeam + 5, 1));
            teamPanel.setBackground(null);

            JLabel nameLabel = new JLabel(team.getName(), SwingConstants.CENTER);
            nameLabel.setBorder(new LineBorder(GUIUtils.FOREGROUND));
            nameLabel.setFont(GUIUtils.FONT.deriveFont(Font.BOLD).deriveFont(20f));
            nameLabel.setBackground(GUIUtils.BACKGROUND);
            nameLabel.setForeground(GUIUtils.FOREGROUND);
            JPanel namePanel = new JPanel(new BorderLayout());
            namePanel.setBackground(null);
            namePanel.setForeground(null);
            namePanel.add(nameLabel, BorderLayout.CENTER);
            teamPanel.add(namePanel);

            int totalPrints = 0;
            for (int i = 0; i < playersPerTeam; i++) {
                Player player = team.getPlayerAt(i);
                boolean redacted = false;

                if (player != null)
                    redacted = player.isRedacted();

                if (totalPrints == 0 || totalPrints == 3 || totalPrints == 11 || totalPrints == 19) {
                    int total = 0;
                    int max = 0;

                    switch (totalPrints) {
                        case 0:
                            total = team.getPor().size();
                            max = team.MAX_POR;
                            break;
                        case 3:
                            total = team.getDif().size();
                            max = team.MAX_DIF;
                            break;
                        case 11:
                            total = team.getCen().size();
                            max = team.MAX_CEN;
                            break;
                        case 19:
                            total = team.getAtt().size();
                            max = team.MAX_ATT;
                            break;
                    }

                    JLabel label = new JLabel(total + "/" + max, SwingConstants.CENTER);
                    label.setFont(GUIUtils.FONT.deriveFont(12f));
                    label.setBackground(GUIUtils.BACKGROUND);
                    label.setForeground(GUIUtils.FOREGROUND);
                    teamPanel.add(label, BorderLayout.CENTER);
                }

                JLabel playerLabel = new JLabel("- " + (player == null ? "" : player.getLabel()));
                playerLabel.setHorizontalTextPosition(SwingConstants.CENTER);
                playerLabel.setFont(GUIUtils.FONT);
                if (redacted) {
                    playerLabel.setFont(playerLabel.getFont().deriveFont(Font.ITALIC));
                    playerLabel.setForeground(Color.gray);
                } else {
                    playerLabel.setForeground(GUIUtils.FOREGROUND);
                }
                playerLabel.setBackground(GUIUtils.BACKGROUND);

                teamPanel.add(playerLabel);

                totalPrints ++;
            }

            panel.add(teamPanel);
        }

        panel.setBackground(null);
        setContent(panel);
    }

    public void setMessage(String message) {
        JLabel msgLabel = new JLabel(message, SwingConstants.CENTER);
        msgLabel.setFont(GUIUtils.FONT.deriveFont(Font.BOLD).deriveFont(24f));
        msgLabel.setBackground(GUIUtils.BACKGROUND);
        msgLabel.setForeground(GUIUtils.FOREGROUND);
        JPanel msgPanel = new JPanel(new BorderLayout());
        msgPanel.setBackground(null);
        msgPanel.setForeground(null);
        msgPanel.add(msgLabel, BorderLayout.CENTER);

        setContent(msgPanel);
    }

    public void setRandomPlayerMessage(Player player, Team team, String label) {
        final String playerRole = player.getRole().name();
        final String teamName = team.getName();
        int playerDuration = new Random().nextInt(6000) + 2000;
        int teamDuration = new Random().nextInt(6000) + 2000;

        final long start = System.currentTimeMillis();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                String playerString;
                String teamString;

                long now = System.currentTimeMillis();

                if (now - start >= playerDuration)
                    playerString = player.getLabel() + " (" + playerRole + ")";
                else
                    playerString = randomString();

                if (now - start >= teamDuration)
                    teamString = teamName;
                else
                    teamString = randomString();

                setMessage(label + playerString + " goes to " + teamString);
            }
        }, 0, 50);


        long now;
        do {
            now = System.currentTimeMillis();
        } while (now - start < playerDuration || now - start < teamDuration);

        timer.cancel();
        setMessage(label + player.getLabel() + " (" + playerRole+ ") goes to " + teamName);
    }

    /*
     *
     */

    private String randomString() {
        String charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";

        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            randomString.append(charset.charAt((int) (Math.random() * charset.length())));
        }

        return randomString.toString();
    }

}
