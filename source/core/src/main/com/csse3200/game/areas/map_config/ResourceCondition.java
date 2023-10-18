package com.csse3200.game.areas.map_config;

import java.util.Objects;

/**
 * Represents a condition for a particular resource, where the condition is
 * defined by a threshold value. This can be used, for instance, to check
 * if a certain amount of a resource has been reached or surpassed.
 */
public class ResourceCondition {
    public String resource;
    public int threshold = 0;

    public String getResource() {
        return resource;
    }

    /**
     * Retrieves the threshold value of the condition.
     *
     * @return The threshold value.
     */
    public int getThreshold() {
        return threshold;
    }

    /**
     * Checks if the provided object is equal to this ResourceCondition instance.
     *
     * @param o Object to be compared.
     * @return true if objects are equal, otherwise false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceCondition that = (ResourceCondition) o;

        if (threshold != that.threshold) return false;
        return Objects.equals(resource, that.resource);
    }

    /**
     * Generates a hash code for the ResourceCondition instance.
     *
     * @return Hash code of the object.
     */
    @Override
    public int hashCode() {
        int result = resource != null ? resource.hashCode() : 0;
        result = 31 * result + threshold;
        return result;
    }
}
