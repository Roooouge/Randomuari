package it.randomuari.players;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class Player {

    private final int id;
    private final String name;
    private final String team;
    private final Roles role;
    private final int value;
    @Setter
    private boolean redacted;


    public Player(int id, String name, String team, String role, int value) {
        this(id, name, team, role, value, false);
    }

    public Player(int id, String name, String team, String role, int value, boolean redacted) {
        this.id = id;
        this.name = name;
        this.team = team;
        this.role = Roles.fromKey(role);
        this.value = value;
        this.redacted = redacted;
    }


    public Player clone() {
        return new Player(id, name, team, role, value);
    }


    public void reveal() {
        setRedacted(false);
    }

}
