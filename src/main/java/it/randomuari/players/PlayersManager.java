package it.randomuari.players;

import it.randomuari.config.Config;
import it.randomuari.gui.GUIUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@UtilityClass
public class PlayersManager {

    private List<Player> players;

    public void init() {
        if (players != null)
            players.clear();
        else
            players = new ArrayList<>();

        try (
            XSSFWorkbook workbook = new XSSFWorkbook(PlayersManager.class.getClassLoader().getResourceAsStream("listone.xlsx"));
        ) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            double weights = Double.parseDouble(Config.getConfig("//players/manager/weights").getText());

            for (Row row : sheet) {
                if (row.getRowNum() < 2)
                    continue;

                int id = Integer.parseInt(String.valueOf(row.getCell(0).getNumericCellValue()).replace(".0",""));
                String role = row.getCell(1).getStringCellValue();
                String name = row.getCell(3).getStringCellValue();
                String team = row.getCell(4).getStringCellValue();
                int value = Integer.parseInt(String.valueOf(row.getCell(5).getNumericCellValue()).replace(".0",""));

                players.add(new Player(id, name, team, role, value));
                if (weights > 0) {
                    int extraTimes = ((int) (value * weights)) - 1;
                    for (int i = 0; i < extraTimes; i++) {
                        players.add(new Player(id, name, team, role, value));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

            GUIUtils.terminate();
        }
    }

    /*
     *
     */

    public Player random(Roles role) {
        Random random = new Random();
        Player player = null;
        Roles playerRole = null;

        do {
            player = players.get(random.nextInt(players.size()));
            playerRole = player.getRole();
        } while (playerRole != role);

        int size = players.size();
        for (int i = 0; i < size; i++) {
            if (players.get(i).getId() == player.getId()) {
                players.remove(i);
                i--;
                size--;
            }
        }

        return player;
    }

    public void removeAll(Player player) {
        int size = players.size();
        for (int i = 0; i < size; i++) {
            if (players.get(i).getId() == player.getId()) {
                players.remove(i);
                i--;
                size--;
            }
        }
    }
}
