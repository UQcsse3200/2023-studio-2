package com.csse3200.game.components.structures.tools;

public class ToolResponse {
    private final PlacementValidity validity;
    private final String message;

    public ToolResponse(PlacementValidity validity) {
        this(validity, validity.toString());
    }


    public ToolResponse(PlacementValidity validity, String message) {
        this.validity = validity;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public PlacementValidity getValidity() {
        return validity;
    }

    static ToolResponse valid() {
        return new ToolResponse(PlacementValidity.VALID);
    }

    public boolean isValid() {
        return getValidity().equals(PlacementValidity.VALID);
    }

    public boolean isError() {
        return getValidity().equals(PlacementValidity.ERROR);
    }
}
