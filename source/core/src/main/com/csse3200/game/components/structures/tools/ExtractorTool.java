package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.components.ParticleComponent;
import com.csse3200.game.components.resources.FissureComponent;
import com.csse3200.game.components.resources.ProductionComponent;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.factories.StructureFactory;
import com.csse3200.game.services.ServiceLocator;

public class ExtractorTool extends PlacementTool {
    public ExtractorTool(ObjectMap<String, Integer> cost) {
        super(cost);
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
        if (existingStructure == null) {
            return false;
        }
        FissureComponent productionComponent = existingStructure.getComponent(FissureComponent.class);
        if (productionComponent == null) {
            return false;
        }

        Resource resource = productionComponent.getProduces();
        this.produces = resource;

        ServiceLocator.getStructurePlacementService().removeStructureAt(position);
        Gdx.app.postRunnable(existingStructure::dispose);

        Object max = ServiceLocator.getGameStateObserverService().getStateData("extractorsMax/" + resource);
        Object count = ServiceLocator.getGameStateObserverService().getStateData("extractorsTotal/" + resource);
        if (max == null) {
            max = 0;
        }
        if (count == null) {
            count = 0;
        }

        return (int) max > (int) count;
    }
}

