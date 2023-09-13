package com.csse3200.game.entities.buildables;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.buildables.Turret;

public class Interaction {
    public void interact(Entity entity) {

        if (entity instanceof Turret) {
            Turret turret = (Turret) entity;
            turret.refillAmmo();
        }
    }
}
