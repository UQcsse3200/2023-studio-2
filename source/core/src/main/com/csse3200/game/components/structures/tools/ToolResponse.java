package com.csse3200.game.components.structures.tools;

/**
 * Used to pass information surrounding the validity of a tools usage and
 * an optional message to go with it.
 */
public class ToolResponse {
    private final PlacementValidity validity;
    private final String message;

    /**
     * Creates a new tool response containing the given validity. The message will
     * be the validity's string representation.
     *
     * @param validity - whether the usage is valid.
     */
    public ToolResponse(PlacementValidity validity) {
        this(validity, validity.toString());
    }

    /**
     * Creates a new tool response with the given validity and message.
     *
     * @param validity - whether the tools usage is valid.
     * @param message - a message explaining why the tools usage is invalid/valid.
     */
    public ToolResponse(PlacementValidity validity, String message) {
        this.validity = validity;
        this.message = message;
    }

    /**
     * Gets the message.
     * @return the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the validity.
     * @return the validity.
     */
    public PlacementValidity getValidity() {
        return validity;
    }

    /**
     * Shorthand way to create a new valid ToolResponse.
     * @return valid ToolResponse.
     */
    static ToolResponse valid() {
        return new ToolResponse(PlacementValidity.VALID);
    }

    /**
     * Gets whether the ToolResponse is valid.
     * @return whether the ToolResponse is valid.
     */
    public boolean isValid() {
        return getValidity().equals(PlacementValidity.VALID);
    }

    /**
     * Gets whether the ToolResponse is an error.
     * @return whether the ToolResponse is an error.
     */
    public boolean isError() {
        return getValidity().equals(PlacementValidity.ERROR);
    }
}
