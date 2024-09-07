package it.randomuari.players;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@UtilityClass
public class PlayersManager {

    private List<Player> players;

    public void init() throws IOException {
        players = new ArrayList<>();

        XSSFWorkbook workbook = new XSSFWorkbook(PlayersManager.class.getClassLoader().getResourceAsStream ("listone.xlsx"));
        XSSFSheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() < 2)
                continue;

            int id = Integer.parseInt(String.valueOf(row.getCell(0).getNumericCellValue()).replace(".0",""));
            String role = row.getCell(1).getStringCellValue();
            String name = row.getCell(3).getStringCellValue();
            String team = row.getCell(4).getStringCellValue();

            players.add(new Player(id, name, team, role));
        }

        workbook.close();
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

        players.remove(player);
        return player;
    }
}
