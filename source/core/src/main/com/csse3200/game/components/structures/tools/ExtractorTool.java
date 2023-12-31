package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.components.resources.FissureComponent;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.factories.StructureFactory;
import com.csse3200.game.services.ServiceLocator;

import java.util.Objects;

/**
 * Tool used to place extractors.
 */
public class ExtractorTool extends ReplacementTool {
    /**
     * Creates a new tool which allows for the placement of Extractors.
     *
     * @param cost     - the cost of the entity being placed.
     * @param range
     * @param texture  - the texture of this tool.
     * @param ordering - the ordering of this tool.
     */
    public ExtractorTool(ObjectMap<String, Integer> cost, float range, String texture, int ordering) {
        super(cost, range, texture, ordering, true);
    }

    Resource produces = Resource.Durasteel;

    /**
     * Creates a new extractor producing the resource of the location it is being placed, and increments the total amount of
     * tracked extractors of this type
     * @param player The player creating the extractor
     * @return the extractor, fully built
     */
    @Override
    public PlaceableEntity createStructure(Entity player) {
        Object total = ServiceLocator.getGameStateObserverService().getStateData("extractorsTotal/" + produces.toString());
        ServiceLocator.getGameStateObserverService().trigger("extractorsTotal", this.produces.toString(),
                (total != null ? (int) total : 0) + 1);
        return StructureFactory.createExtractor(100, this.produces, 1000, 10);
    }

    /**
     * Checks if the position of the extractor is valid:
     * - There is no collision with other structures
     * - The resource type of the extractor doesnt exceed the cap
     * @param position The position of the extractor
     * @return whether the extractor can be placed
     */
    @Override
    public ToolResponse isPositionValid(GridPoint2 position) {
        var validity = super.isPositionValid(position);

        if (!validity.isValid()) {
            return validity;
        }

        var existingStructure = structurePlacementService.getStructureAt(position);
        FissureComponent productionComponent = existingStructure.getComponent(FissureComponent.class);
        if (productionComponent == null) {
            return new ToolResponse(PlacementValidity.INVALID_POSITION, "Must be placed on a fissure");
        }

        Resource resource = productionComponent.getProduces();
        this.produces = resource;

        Object max = ServiceLocator.getGameStateObserverService().getStateData("extractorsMax/" + resource);
        Object count = ServiceLocator.getGameStateObserverService().getStateData("extractorsTotal/" + resource);
        if (max == null) {
            max = 0;
        }
        if (count == null) {
            count = 0;
        }

        return (int) max > (int) count ? ToolResponse.valid() :
                new ToolResponse(PlacementValidity.INVALID_POSITION, "Extractor limit exceeded");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ExtractorTool that = (ExtractorTool) o;
        return produces == that.produces;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), produces);
    }
}

