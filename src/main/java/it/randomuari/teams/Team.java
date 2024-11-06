package it.randomuari.teams;

import it.randomuari.config.Config;
import it.randomuari.players.Player;
import it.randomuari.players.Roles;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class Team {

    public static final int MAX_POR = 3;
    public static final int MAX_DIF = 8;
    public static final int MAX_CEN = 8;
    public static final int MAX_ATT = 6;

    private final String name;
    private final List<Player> por;
    private final List<Player> dif;
    private final List<Player> cen;
    private final List<Player> att;

    public Team(String name) {
        this.name = name;
        por = new ArrayList<>();
        dif = new ArrayList<>();
        cen = new ArrayList<>();
        att = new ArrayList<>();
    }

    /*
     *
     */

    public void addPlayer(Player player) {
        Roles role = player.getRole();

        List<Player> list = null;

        switch (role) {
            case POR:
                list = por;
                break;
            case DIF:
                list = dif;
                break;
            case CEN:
                list = cen;
                break;
            case ATT:
                list = att;
                break;
        }

        if (isFullRole(role)) {
            log.warn("Role " + role.name() + " is full for team " + name);
            return;
        }

        list.add(player);
    }

    public Player getPlayerAt(int i) {
        int porBound = MAX_POR;
        int difBound = porBound + MAX_DIF;
        int cenBound = difBound + MAX_CEN;

        int realIndex = -1;
        List<Player> list = null;

        if (i < porBound) {
            realIndex = i;
            list = por;
        } else if (i < difBound) {
            realIndex = i - porBound;
            list = dif;
        } else if (i < cenBound) {
            realIndex = i - difBound;
            list = cen;
        } else {
            realIndex = i - cenBound;
            list = att;
        }

        if (realIndex >= list.size())
            return null;
        return list.get(realIndex);
    }

    public void setPlayerAt(int i, Player player) {
        int porBound = MAX_POR;
        int difBound = porBound + MAX_DIF;
        int cenBound = difBound + MAX_CEN;

        log.info("{},{},{}", porBound, difBound, cenBound);

        if (i < porBound) {
            setToCorrectPosition(player, i, por, MAX_POR);
        } else if (i < difBound) {
            setToCorrectPosition(player, i - porBound, dif, MAX_DIF);
        } else if (i < cenBound) {
            setToCorrectPosition(player, i - difBound, dif, MAX_CEN);
        } else {
            setToCorrectPosition(player, i - cenBound, att, MAX_ATT);
        }
    }

    private void setToCorrectPosition(Player player, int desiredIndex, List<Player> list, int max) {
        if (desiredIndex > max)
            desiredIndex = max;

        for (int i = 0; i < max; i++) {
            if (list.get(i) == null)
                desiredIndex = i;
        }

        list.set(desiredIndex, player);
    }

    public int getPlayerIndex(Player player) {
        int index = 0;

        List<Player> list = new ArrayList<>();
        list.addAll(por);
        list.addAll(dif);
        list.addAll(cen);
        list.addAll(att);

        for (Player p : list) {
            if (p.equals(player))
                return index;
            index++;
        }

        return -1;
    }

    public Player getRandomPlayer() {
        int maxPlayers = MAX_POR + MAX_DIF + MAX_CEN + MAX_ATT;
        Random random = new Random();
        Player player = null;
        do {
            player = getPlayerAt(random.nextInt(maxPlayers));
        } while (player == null);

        return player;
    }

    public Player getRandomPlayerFromRole(Roles role) {
        Player player = null;
        do {
            player = getRandomPlayer();
        } while (player == null || !player.getRole().equals(role));

        return player;
    }

    public void removePlayer(Player player) {
        por.remove(player);
        dif.remove(player);
        cen.remove(player);
        att.remove(player);
    }

    public boolean isTeamEmpty() {
        return por.isEmpty() && dif.isEmpty() && cen.isEmpty() && att.isEmpty();
    }

    public boolean isFullRole(Roles role) {
        switch (role) {
            case POR:
                return isListFull(por, MAX_POR);
            case DIF:
                return isListFull(dif, MAX_DIF);
            case CEN:
                return isListFull(cen, MAX_CEN);
            case ATT:
                return isListFull(att, MAX_ATT);
        }

        return true;
    }

    private boolean isListFull(List<Player> players, int max) {
        return players.size() >= max;
    }

    public boolean isFullTeam() {
        return por.size() >= MAX_POR
                && dif.size() >= MAX_DIF
                && cen.size() >= MAX_CEN
                && att.size() >= MAX_ATT;
    }

    /*
     *
     */

    public boolean acceptNewRedacted() {
        int maxRedacted = Integer.parseInt(Config.getConfig("//players/manager/redactedMaxPerTeam").getText());

        int count = 0;
        int maxPlayers = MAX_POR + MAX_DIF + MAX_CEN + MAX_ATT;

        for (int i = 0; i < maxPlayers; i++) {
            Player player = getPlayerAt(i);
            if (player != null && player.isRedacted()) {
                count++;

                if (count >= maxRedacted)
                    return false;
            }
        }

        return true;
    }

    public void reveal(int index) {
        Player player = getPlayerAt(index);
        if (player != null && player.isRedacted())
            player.reveal();
    }

    public void revealAll() {
        int maxPlayers = MAX_POR + MAX_DIF + MAX_CEN + MAX_ATT;

        for (int i = 0; i < maxPlayers; i++) {
            reveal(i);
        }
    }

    /*
     *
     */

    public void clearAll() {
        clear(Roles.POR);
        clear(Roles.DIF);
        clear(Roles.CEN);
        clear(Roles.ATT);
    }

    public void clear(Roles role) {
        switch (role) {
            case POR:
                por.clear();
                break;
            case DIF:
                dif.clear();
                break;
            case CEN:
                cen.clear();
                break;
            case ATT:
                att.clear();
                break;
        }
    }

    /*
     *
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(name, team.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
