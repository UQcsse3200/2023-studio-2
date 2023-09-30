package com.csse3200.game.components.structures;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This component can be placed on PlaceableEntities and will destroy the structure
 * if it runs out of health.
 */
public class StructureDestroyComponent extends Component {
    private final Logger logger;

    /**
     * Creates a StructureDestroyComponent.
     */
    public StructureDestroyComponent() {
        super();
        logger = LoggerFactory.getLogger(this.getClass());
    }

    /**
     * Checks whether the structure should be destroyed. The structure should be destroyed
     * if it has run out of health. If the structure has run out of health, the dispose method
     * is scheduled to be called.
     */
    @Override
    public void update() {
        var combatStatsComponent = entity.getComponent(CombatStatsComponent.class);

        if (combatStatsComponent == null) {
            logger.warn("There is a StructureDestroyComponent on entity {} " +
                    "without a corresponding combat stats component.", entity);
            return;
        }

        if (!combatStatsComponent.isDead()) {
            return;
        }

        var structureService = ServiceLocator.getStructurePlacementService();

        if (structureService == null) {
            logger.error("The structure service has not been registered. This component requires this service!");
            return;
        }

        var gridPosition = structureService.getStructurePosition((PlaceableEntity) entity);

        if (gridPosition == null) {
            logger.error("The structure has not been placed using the StructurePlacementService!");
            return;
        }

        structureService.removeStructureAt(gridPosition);
    }
}
