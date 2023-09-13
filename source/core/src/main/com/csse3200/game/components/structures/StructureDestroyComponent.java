package com.csse3200.game.components.structures;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StructureDestroyComponent extends Component {
    Logger logger;

    public StructureDestroyComponent() {
        super();
        logger = LoggerFactory.getLogger(this.getClass());
    }

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

        var gridPosition = structureService.getStructurePosition(entity);

        if (gridPosition == null) {
            logger.error("The structure has not been placed using the StructurePlacementService!");
            return;
        }

        structureService.removeStructureAt(gridPosition);
    }
}
