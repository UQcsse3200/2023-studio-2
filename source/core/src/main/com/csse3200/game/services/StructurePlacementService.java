package com.csse3200.game.services;

import com.csse3200.game.events.EventHandler;

public class StructurePlacementService {
    EventHandler handler;

    public StructurePlacementService(EventHandler handler) {
        this.handler = handler;
    }
}
