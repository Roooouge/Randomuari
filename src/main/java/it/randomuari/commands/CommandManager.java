package it.randomuari.commands;

import it.randomuari.gui.GUIUtils;
import it.randomuari.teams.TeamsManager;
import java.io.IOException;
import java.util.*;
import javax.swing.JFileChooser;
import lombok.experimental.UtilityClass;
import org.dom4j.DocumentException;

@UtilityClass
public class CommandManager {

    public final String OK_COMMAND = "OK";
    public final String KO_COMMAND = "KO";
    public final String UNKNOWN_COMMAND = "Unknown command";

    public final String NEW_GAME_KEYWORD = "newGame";
    public final String FULL_TEAMS_KEYWORD = "fullTeams";
    public final String RANDOM_KEYWORD = "random";
    public final String STATE_KEYWORD = "state";
    public final String CANCEL_KEYWORD = "cancel";
    public final String SAVE_KEYWORD = "save";
    public final String LOAD_KEYWORD = "load";

    private final List<String> KEYWORDS = new LinkedList<>();

    static {
        KEYWORDS.add(NEW_GAME_KEYWORD);
        KEYWORDS.add(STATE_KEYWORD);
        KEYWORDS.add(FULL_TEAMS_KEYWORD);
        KEYWORDS.add(RANDOM_KEYWORD);
        KEYWORDS.add(CANCEL_KEYWORD);
        KEYWORDS.add(SAVE_KEYWORD);
        KEYWORDS.add(LOAD_KEYWORD);
    }

    /*
     *
     */


    public CommandResult execCommand(String command) {
        String[] args = command.split(" ");
        String keyword = args[0];

        List<String> params = new ArrayList<>();
        Collections.addAll(params, args);
        params.remove(0);

        if (!KEYWORDS.contains(keyword))
            return CommandResult.ko(UNKNOWN_COMMAND);

        switch (keyword) {
            case NEW_GAME_KEYWORD:
                return newGame(params);
            case STATE_KEYWORD:
                return state(params);
            case FULL_TEAMS_KEYWORD:
                return fullTeams(params);
            case RANDOM_KEYWORD:
                return random(params);
            case CANCEL_KEYWORD:
                return cancel(params);
            case SAVE_KEYWORD:
                return save(params);
            case LOAD_KEYWORD:
                return load(params);
            default:
                return CommandResult.ko(UNKNOWN_COMMAND);
        }
    }

    /*
     *
     */

    private CommandResult newGame(List<String> params) {
        // Sanity check
        if (params.size() != 1)
            return CommandResult.invalidArgsCount(params.size(), 1);

        String param = params.get(0);

        // Parsing teams
        String[] teamNames = param.split(",");

        TeamsManager.newGame(teamNames);
        CommandResult result = CommandResult.ok("Game created with " + teamNames.length + " players");

        GUIUtils.ACTIONS_PANEL.setMessage(result.getMessage());

        return result;
    }

    //---

    private CommandResult random(List<String> params) {
        // Sanity check
        if (params.size() > 2)
            return CommandResult.invalidArgsCount(params.size(), "<= 1");
        if (params.size() == 2 && !params.get(1).equals("noGUI"))
            return CommandResult.argNotFound("noGUI");

        if (params.isEmpty())
            params.add("1");

        int rounds;
        try {
            if (params.isEmpty())
                params.add("1");

            rounds = Integer.parseInt(params.get(0));
        } catch (NumberFormatException e) {
            return CommandResult.integerNotFound(null, params.get(0));
        }

        if (params.size() == 2 && params.get(1).equals("noGUI"))
            TeamsManager.randomNoGUI(rounds);
        else
            TeamsManager.random(rounds);

        return CommandResult.ok();
    }

    private CommandResult fullTeams(List<String> params) {
        // Sanity check
        if (!params.isEmpty())
            return CommandResult.invalidArgsCount(params.size(), 0);

        TeamsManager.fillTeams();

        // Printing teams
        GUIUtils.ACTIONS_PANEL.state();

        return CommandResult.ok();
    }

    private CommandResult cancel(List<String> params) {
        // Sanity check
        if (!params.isEmpty())
            return CommandResult.invalidArgsCount(params.size(), 0);

        TeamsManager.cancel();

        return state(new ArrayList<>());
    }

    private CommandResult state(List<String> params) {
        // Sanity check
        if (!params.isEmpty())
            return CommandResult.invalidArgsCount(params.size(), 0);

        // Printing teams
        GUIUtils.ACTIONS_PANEL.state();

        return CommandResult.ok();
    }

    private CommandResult save(List<String> params) {
        // Sanity check
        if (!params.isEmpty())
            return CommandResult.invalidArgsCount(params.size(), 0);

        JFileChooser chooser = new JFileChooser(System.getProperty("user.home") + "/Desktop");
        int returnVal = chooser.showSaveDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                TeamsManager.save(chooser.getSelectedFile());
            } catch (IOException e) {
                e.printStackTrace();
                return CommandResult.ko(e.getMessage());
            }
        }

        return CommandResult.ok();
    }

    private CommandResult load(List<String> params) {
        // Sanity check
        if (!params.isEmpty())
            return CommandResult.invalidArgsCount(params.size(), 0);

        JFileChooser chooser = new JFileChooser(System.getProperty("user.home") + "/Desktop");
        int returnVal = chooser.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                TeamsManager.load(chooser.getSelectedFile());
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                return CommandResult.ko(e.getMessage());
            }
        }

        return state(new ArrayList<>());
    }
}
