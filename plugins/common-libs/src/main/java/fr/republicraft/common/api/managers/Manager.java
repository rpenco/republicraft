package fr.republicraft.common.api.managers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Manager {
    boolean started = false;

    public void start() {
    }

    public void stop() {
    }

}
