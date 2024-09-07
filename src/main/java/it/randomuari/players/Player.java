package it.randomuari.players;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Player {

    private final int id;
    private final String name;
    private final String team;
    private final Roles role;


    public Player(int id, String name, String team, String role) {
        this.id = id;
        this.name = name;
        this.team = team;
        this.role = Roles.fromKey(role);
    }


    public Player clone() {
        return new Player(id, name, team, role);
    }
}
