package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.factories.StructureFactory;
import com.csse3200.game.services.ServiceLocator;

public class ExtractorTool extends PlacementTool {
    public ExtractorTool(ObjectMap<String, Integer> cost) {
        super(cost);
    }

    /**
     * Returns the resource at the location or null, that the extractor will be built to produce
     * TODO right now this just returns durasteel for testing
     * @param position the position to check for a floor resource
     * @return resource at the location or null
     */
    public Resource getResource(GridPoint2 position) {
        return Resource.Durasteel;
    }

    Resource produces = Resource.Durasteel; // TODO make this depend on the position

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
        return StructureFactory.createExtractor(100, this.produces, 100, 10);
    }

    /**
     * Checks if the position of the extractor is valid:
     * - There is no collision with other structures (TODO or the map)
     * - The resource type of the extractor doesnt exceed the cap
     * @param position The position of the extractor
     * @return whether the extractor can be placed
     */
    @Override
    public boolean isPositionValid(GridPoint2 position) {
        PlaceableEntity existingStructure = ServiceLocator.getStructurePlacementService().getStructureAt(position);

        Object max = ServiceLocator.getGameStateObserverService().getStateData("extractorsMax/" + this.getResource(position));
        Object count = ServiceLocator.getGameStateObserverService().getStateData("extractorsTotal/" + this.getResource(position));
        boolean underCapacity =  ((max != null) ? (int) max : 0) > ((count != null) ? (int) count : 0);

        return existingStructure == null && underCapacity;
    }
}

