package it.randomuari.players;

import java.util.Random;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Roles {

    POR("P"),
    DIF("D"),
    CEN("C"),
    ATT("A"),
    NULL("");


    @Getter
    private final String key;


    public static Roles fromKey(String key) {
        for (Roles role : Roles.values()) {
            if (role.key.equals(key)) {
                return role;
            }
        }

        return null;
    }

    public static Roles randomRole() {
        Random random = new Random();
        int index = -1;

        do {
            index = random.nextInt(Roles.values().length);
        } while (NULL.equals(Roles.values()[index]));

        return Roles.values()[index];
    }
}
