package it.randomuari.teams;

import it.randomuari.players.Player;
import it.randomuari.players.Roles;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
