package com.csse3200.game.areas.map_config;

import java.util.Objects;

/**
 * Represents a condition based on a particular resource and its threshold.
 * This class can be used to encapsulate conditions for game mechanics that rely on resource quantities or values.
 */
public class ResourceCondition {
    public String resource;
    public int threshold = 0;

    /**
     * Retrieves the name or type of the resource.
     *
     * @return The resource name or type.
     */
    public String getResource() {
        return resource;
    }


    /**
     * Retrieves the threshold value of the resource.
     *
     * @return The resource threshold value.
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
