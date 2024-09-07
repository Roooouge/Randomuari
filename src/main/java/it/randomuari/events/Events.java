package it.randomuari.events;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public enum Events {

    NONE(),
    BLAME(100f);

    private final float min;
    private final float max;

    Events() {
        this(-1f, -1f);
    }

    Events(float max) {
        this(0f, max);
    }

    Events(float min, float max) {
        this.min = min;
        this.max = max;
    }

}
