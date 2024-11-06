package it.randomuari.events;

import it.randomuari.teams.Team;
import it.randomuari.teams.TeamsManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Getter
public enum Events {

    NONE(),
//    BLAME(100f),
    SHAKE_UP(10f, () -> {
        List<Team> teams = TeamsManager.TEAMS;

        int founded = 0;
        for (Team team : teams) {
            if (!team.isTeamEmpty()) {
                founded++;
                if (founded > 1)
                    return true;
            }
        }

        return false;
    });

    private final float min;
    private final float max;
    private final Callable<Boolean> condition;

    Events() {
        this(-1f, -1f, null);
    }

    Events(float max, Callable<Boolean> condition) {
        this(0f, max, condition);
    }

    Events(float min, float max, Callable<Boolean> condition) {
        this.min = min;
        this.max = max;
        this.condition = condition;
    }

    /*
     *
     */

    public boolean validateCondition() {
        try {
            return condition.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
