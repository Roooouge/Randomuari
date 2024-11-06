package it.randomuari.teams;

import it.randomuari.config.Config;
import it.randomuari.events.Events;
import it.randomuari.events.EventsManager;
import it.randomuari.gui.GUIUtils;
import it.randomuari.players.Player;
import it.randomuari.players.PlayersManager;
import it.randomuari.players.Roles;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

@UtilityClass
@Slf4j
public class TeamsManager {

    public final List<Team> TEAMS = new ArrayList<>();;

    /*
     *
     */

    public void newGame(String[] names) {
        TEAMS.clear();

        for (String name : names) {
            name = name.trim().replaceAll("_", " ");
            TEAMS.add(new Team(name));
        }
    }

    /*
     *
     */

    private Team randomTeam() {
        Random random = new Random();
        Team team = null;

        do {
            team = TEAMS.get(random.nextInt(TEAMS.size()));
        } while (team.isFullTeam());

        return team;
    }

    public Team getTeamByName(String name) {
        for (Team team : TEAMS) {
            if (team.getName().equals(name)) {
                return team;
            }
        }

        return null;
    }

    /*
     *
     */

    private Player randomPlayerForTeamFromRole(Team team, Roles role, boolean canBeRedacted) {
        Random random = new Random();
        int redactedBound = Integer.parseInt(Config.getConfig("//players/manager/redactedBound").getText());

        Player player = PlayersManager.random(role);
        boolean isRedacted = false;
        if (canBeRedacted && team.acceptNewRedacted()) {
            isRedacted = random.nextInt(redactedBound) == 0;
        }
        player.setRedacted(isRedacted);

        return player;
    }

    public void random(int rounds) {
        new Thread(() -> {
            String redactedString = Config.getConfig("//players/manager/redactedString").getText();

            for (int i = 0; i < rounds && !allTeamsFull(); i++) {
                Team team = randomTeam();
                Roles role;

                do {
                    role = Roles.randomRole();
                } while (team.isFullRole(role));

                Player player = randomPlayerForTeamFromRole(team, role, true);
                team.addPlayer(player);

                GUIUtils.ACTIONS_PANEL.setRandomPlayerMessage(player, team, (i+1) + "/" + rounds + " - ");

                String eventResult = handleEvent(EventsManager.randomEvent());
                if (eventResult != null && !eventResult.isEmpty()) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    log.debug(eventResult);
                    GUIUtils.ACTIONS_PANEL.setMessage(eventResult);
                }

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            GUIUtils.ACTIONS_PANEL.state();
        }).start();
    }

    private String handleEvent(Events event) {
        if (!event.equals(Events.NONE) && !event.validateCondition())
            return null;

        switch (event) {
            case SHAKE_UP -> {
                Team teamFrom = randomTeam();
                Team teamTo;
                do {
                    teamTo = randomTeam();
                } while (teamFrom.equals(teamTo));

                Player playerFrom = teamFrom.getRandomPlayer();
                Roles role = playerFrom.getRole();
                Player playerTo = teamTo.getRandomPlayerFromRole(role);

                teamFrom.removePlayer(playerFrom);
                teamTo.removePlayer(playerTo);
                teamFrom.addPlayer(playerTo);
                teamTo.addPlayer(playerFrom);

                return "Switching " + playerFrom.getLabel() + " (" + teamFrom.getName() + ") to " + playerTo.getName() + " (" + teamTo.getName() + ")";
            }
        }

        return null;
    }

    public void randomNoGUI(int rounds) {
        Random random = new Random();
        int redactedBound = Integer.parseInt(Config.getConfig("//players/manager/redactedBound").getText());
        String redactedString = Config.getConfig("//players/manager/redactedString").getText();

        for (int i = 0; i < rounds && !allTeamsFull(); i++) {
            Team team = randomTeam();
            Roles role;

            do {
                role = Roles.randomRole();
            } while (team.isFullRole(role));

            Player player = randomPlayerForTeamFromRole(team, role, true);
            team.addPlayer(player);

            System.out.println("[" + (i+1) + "/ " + rounds + "] " + role.name() + (player.isRedacted() ? " " + redactedString : "") + " - " + player.getName() + " to " + team.getName());
        }

        GUIUtils.ACTIONS_PANEL.state();
    }

    public void revealAll() {
        for (Team team : TEAMS) {
            team.revealAll();
        }
    }

    public void fillTeams() {
        for (Team team : TEAMS) {
            fillRole(team, Roles.POR);
            fillRole(team, Roles.DIF);
            fillRole(team, Roles.CEN);
            fillRole(team, Roles.ATT);
        }
    }

    public void fillRole(Team team, Roles role) {
        while (!team.isFullRole(role)) {
            team.addPlayer(randomPlayerForTeamFromRole(team, role, false));
        }
    }

    public boolean allTeamsFull() {
        for (Team team : TEAMS) {
            if (!team.isFullTeam())
                return false;
        }

        return true;
    }

    public void resetTeams() {
        for (Team team : TEAMS) {
            team.clearAll();
        }
    }

    public void clear() {
        resetTeams();
        PlayersManager.init();
    }

    public void cancel() {
        clear();
        TEAMS.clear();
    }

    //---

    public void save(File file) throws IOException {
        Document document = DocumentHelper.createDocument();
        Element teamsElement = document.addElement("teams");

        for (Team team : TEAMS) {
            Element teamElement = teamsElement.addElement("team");
            teamElement.addAttribute("name", team.getName());

            addPlayersWithRoleToTeam(teamElement, team.getPor(), Roles.POR);
            addPlayersWithRoleToTeam(teamElement, team.getDif(), Roles.DIF);
            addPlayersWithRoleToTeam(teamElement, team.getCen(), Roles.CEN);
            addPlayersWithRoleToTeam(teamElement, team.getAtt(), Roles.ATT);
        }

        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(new FileWriter(file), format);
        writer.write(document);
        writer.close();
    }

    private void addPlayersWithRoleToTeam(Element teamElement, List<Player> players, Roles role) {
        for (Player player : players) {
            Element playerElement = teamElement.addElement("player");
            playerElement.addAttribute("id", String.valueOf(player.getId()));
            playerElement.addAttribute("name", player.getName());
            playerElement.addAttribute("team", player.getTeam());
            playerElement.addAttribute("value", String.valueOf(player.getValue()));
            playerElement.addAttribute("role", role.getKey());
            playerElement.addAttribute("redacted", String.valueOf(player.isRedacted()));
        }
    }

    //---

    public void load(File file) throws IOException, DocumentException {
        Document document = new SAXReader().read(file);
        Element root = document.getRootElement();

        for (Element teamElement : root.elements("team")) {
            Team team = new Team(teamElement.attributeValue("name"));

            for (Element playerElement : teamElement.elements("player")) {
                int id = Integer.parseInt(playerElement.attributeValue("id"));
                String name = playerElement.attributeValue("name");
                String playerTeam = playerElement.attributeValue("team");
                String role = playerElement.attributeValue("role");
                int value = Integer.parseInt(playerElement.attributeValue("value"));
                boolean redacted = Boolean.parseBoolean(playerElement.attributeValue("redacted"));

                Player player = new Player(id, name, playerTeam, role, value, redacted);
                team.addPlayer(player);
                PlayersManager.removeAll(player);
            }

            TEAMS.add(team);
        }
    }


}
