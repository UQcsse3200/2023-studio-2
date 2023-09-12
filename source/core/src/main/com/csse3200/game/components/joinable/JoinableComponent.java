package com.csse3200.game.components.joinable;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.physics.box2d.Shape;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.rendering.AtlasRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.StructurePlacementService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This component is used to change the texture and collision bounds of an entity
 * depending on its neighbours.
 */
public class JoinableComponent extends AtlasRenderComponent {
    private final Map<JoinDirection, Boolean> JoinedInDirectionMap = new HashMap<>() {
        {
            put(JoinDirection.UP, false);
            put(JoinDirection.DOWN, false);
            put(JoinDirection.LEFT, false);
            put(JoinDirection.RIGHT, false);
        }
    };
    private final JoinLayer layer;
    private final JoinableComponentShapes shapes;
    private static final Map<JoinDirection, GridPoint2> DirectionMatrices = new HashMap<>() {
        {
            put(JoinDirection.UP, new GridPoint2(0,-2));
            put(JoinDirection.DOWN, new GridPoint2(0,2));
            put(JoinDirection.LEFT, new GridPoint2(2,0));
            put(JoinDirection.RIGHT, new GridPoint2(-2,0));
        }
    };
    @Override
    public void create() {
        super.create();

        // finds current position.
        StructurePlacementService structurePlacementService = ServiceLocator.getStructurePlacementService();
        GridPoint2 centrePosition = structurePlacementService.getStructurePosition(entity);

        if (centrePosition == null) {
            return;
        }

        // calculates tile over in each direction
        Entity up = structurePlacementService.getStructureAt(centrePosition.cpy().add(0,2));
        Entity down = structurePlacementService.getStructureAt(centrePosition.cpy().add(0,-2));
        Entity left = structurePlacementService.getStructureAt(centrePosition.cpy().add(-2,0));
        Entity right = structurePlacementService.getStructureAt(centrePosition.cpy().add(2,0));

        // adds whether there is a wall in each direction to the JoinDirection array.
        JoinedInDirectionMap.put(JoinDirection.UP, isEntityJoinable(up));
        JoinedInDirectionMap.put(JoinDirection.DOWN, isEntityJoinable(down));
        JoinedInDirectionMap.put(JoinDirection.LEFT, isEntityJoinable(left));
        JoinedInDirectionMap.put(JoinDirection.RIGHT, isEntityJoinable(right));

        updateAtlasTexture();
        updateShape();
    }

    /**
     * Helper function to check if the entity is joinable.
     *
     * @param entity - checks if this entity is joinable with the components' entity.
     * @return whether the given entity is joinable with the components' entity.
     */
    private boolean isEntityJoinable(Entity entity) {
        if (entity == null) {
            return false;
        }

        JoinableComponent component = entity.getComponent(JoinableComponent.class);

        if (component == null) {
            return false;
        }

        return this.getJoinLayer() == component.getJoinLayer();
    }

    /**
     * Creates a joinable component.
     *
     * @param textures - the textures to use for each cardinality
     * @param layer - the layer to join on
     * @param shapes - the collision shapes to use for each cardinality
     */
    public JoinableComponent(TextureAtlas textures, JoinLayer layer, JoinableComponentShapes shapes) {
        super(textures, "no-connection");

        this.layer = layer;
        this.shapes = shapes;
    }


    /**
     * Updates whether the entity should be joining in a given direction.
     *
     * @param direction - the direction the entity should / should not be joined in.
     * @param isJoined - whether the entity is joined in the given direction.
     */
    public void updateJoin(JoinDirection direction, boolean isJoined) {
        JoinedInDirectionMap.put(direction, isJoined);
        updateAtlasTexture();
        updateShape();
    }

    /**
     * Updates the entities collision shape to match the new cardinalities.
     */
    private void updateShape() {
        Shape shape = shapes.getShape(deriveCardinalityId());

        entity.getComponent(ColliderComponent.class).setShape(shape);
    }

    /**
     * Updates the entities texture to match the new cardinalities.
     */
    private void updateAtlasTexture() {
        setRegion(deriveCardinalityId(), false);
    }

    /**
     * Derives the id for the current cardinality which can be used to fetch
     * the collision shape and texture.
     *
     * @return the cardinalities' id.
     */
    private String deriveCardinalityId() {
        List<String> regions = new ArrayList<>();

        if (JoinedInDirectionMap.get(JoinDirection.LEFT)) {
            regions.add("left");
        }

        if (JoinedInDirectionMap.get(JoinDirection.UP)) {
            regions.add("up");
        }

        if (JoinedInDirectionMap.get(JoinDirection.RIGHT)) {
            regions.add("right");
        }

        if (JoinedInDirectionMap.get(JoinDirection.DOWN)) {
            regions.add("down");
        }

        if (!regions.isEmpty()) {
            return String.join("-", regions);
        }

        return "no-connection";
    }

    /**
     * Notifies all the entities neighbours of a change in join status.
     * @param isJoined - whether the neighbours should or should not be joined.
     */
    public void notifyNeighbours(boolean isJoined) {
        StructurePlacementService structurePlacementService = ServiceLocator.getStructurePlacementService();
        GridPoint2 centrePosition = structurePlacementService.getStructurePosition(entity);

        notifyNeighbour(JoinDirection.UP, centrePosition, isJoined);
        notifyNeighbour(JoinDirection.DOWN, centrePosition, isJoined);
        notifyNeighbour(JoinDirection.LEFT, centrePosition, isJoined);
        notifyNeighbour(JoinDirection.RIGHT, centrePosition, isJoined);
    }

    /**
     * Notifies the neighbour in the given direction of a change in join status.
     *
     * @param direction - the direction of the neighbour to notify.
     * @param centrePosition - the centre position of the entity.
     * @param isJoined - whether the neighbour should or should not be joined.
     */
    private void notifyNeighbour(JoinDirection direction, GridPoint2 centrePosition, boolean isJoined) {
        StructurePlacementService structurePlacementService = ServiceLocator.getStructurePlacementService();

        GridPoint2 position = centrePosition.cpy().add(DirectionMatrices.get(direction));

        Entity neighbour = structurePlacementService.getStructureAt(position);

        // checks if the neighbour exists
        if (neighbour == null) {
            return;
        }

        JoinableComponent upComponent = neighbour.getComponent(JoinableComponent.class);
        // checks if the neighbour has a JoinableComponent attached.
        if (upComponent == null) {
            return;
        }

        // checks if the neighbour has the same join layer
        if (layer != upComponent.getJoinLayer()) {
            return;
        }

        upComponent.updateJoin(direction, isJoined);
    }

    /**
     * Retrieves the layer which is being joined on.
     *
     * @return the layer being joined on.
     */
    public JoinLayer getJoinLayer() {
        return layer;
    }

    /**
     * Changes the texture atlas being used to the atlas given.
     *
     * @param atlas - new atlas.
     */
    public void updateTextureAtlas(TextureAtlas atlas) {
        updateTextureAtlas(atlas, deriveCardinalityId());
    }
}

