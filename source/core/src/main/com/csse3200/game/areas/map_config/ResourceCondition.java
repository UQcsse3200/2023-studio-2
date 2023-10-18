package com.csse3200.game.areas.map_config;

import java.util.Objects;

/**
 *
 */
public class ResourceCondition {
    public String resource;
    public int threshold = 0;

    public String getResource() {
        return resource;
    }

    public int getThreshold() {
        return threshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceCondition that = (ResourceCondition) o;

        if (threshold != that.threshold) return false;
        return Objects.equals(resource, that.resource);
    }

    @Override
    public int hashCode() {
        int result = resource != null ? resource.hashCode() : 0;
        result = 31 * result + threshold;
        return result;
    }
}
