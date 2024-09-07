package it.randomuari.teams;

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
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

@UtilityClass
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

    /*
     *
     */

    public void random(int rounds) {
        new Thread(() -> {
            for (int i = 0; i < rounds && !allTeamsFull(); i++) {
                Team team = randomTeam();
                Roles role;

                do {
                    role = Roles.randomRole();
                } while (team.isFullRole(role));

                Player player = PlayersManager.random(role);
                System.out.println("[" + (i+1) + "/ " + rounds + "] " + role.name() + " - " + player.getName() + " to " + team.getName());

                GUIUtils.ACTIONS_PANEL.setRandomPlayerMessage(player, team, (i+1) + "/" + rounds + " - ");
//                EventsManager.handle(player, team, EventsManager.randomEvent(), false);

                team.addPlayer(player);

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            GUIUtils.ACTIONS_PANEL.state();
        }).start();
    }

    public void randomNoGUI(int rounds) {
        for (int i = 0; i < rounds && !allTeamsFull(); i++) {
            Team team = randomTeam();
            Roles role;

            do {
                role = Roles.randomRole();
            } while (team.isFullRole(role));

            Player player = PlayersManager.random(role);
            System.out.println("[" + (i+1) + "/ " + rounds + "] " + role.name() + " - Adding " + player.getName() + " to " + team.getName());
            team.addPlayer(player);
        }

        GUIUtils.ACTIONS_PANEL.state();
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
            team.addPlayer(PlayersManager.random(role));
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

    public void cancel() {
        resetTeams();
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
            playerElement.addAttribute("role", role.getKey());
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

                team.addPlayer(new Player(id, name, playerTeam, role));
            }

            TEAMS.add(team);
        }
    }


}
