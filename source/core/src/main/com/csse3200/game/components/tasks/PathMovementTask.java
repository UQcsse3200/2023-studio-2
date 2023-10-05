package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.components.npc.PathFinder;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PathMovementTask extends MovementTask {
    private static final Logger logger = LoggerFactory.getLogger(PathMovementTask.class);
    private List<GridPoint2> path;
    private int pathIndex = 0;
    public PathMovementTask(List<GridPoint2> path) {
        super(ServiceLocator.getGameArea().getTerrain().tileToWorldPosition(path.get(0)), 0.2f);
        this.path = path;
    }

    @Override
    public void update() {
        if (isAtTarget() || isAtTargetGrid()) {
            if (path.size() > ++pathIndex) {
                setTarget(ServiceLocator.getGameArea().getTerrain().tileToWorldPosition(path.get(pathIndex)));
            } else {
                movementComponent.setMoving(false);
                status = Status.FINISHED;
                logger.debug("Finished moving to {}", target);
            }
        } else {
            checkIfStuck();
        }
    }

    public void setNewPath(List<GridPoint2> path) {
        pathIndex = 0;
        this.path = path;
        if (!path.isEmpty()) {
            setTarget(ServiceLocator.getGameArea().getTerrain().tileToWorldPosition(path.get(pathIndex)));
            start();
        }
    }

}
