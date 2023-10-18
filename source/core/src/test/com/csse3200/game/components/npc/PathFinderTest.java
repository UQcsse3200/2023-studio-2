package com.csse3200.game.components.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BaseEntityConfig;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.StructurePlacementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
public class PathFinderTest {
    @BeforeEach
    void setup() {
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.getResourceService().loadTextures(new String[] {"images/entity/tree.png",
                "images/structures/TurretOne.png", "images/structures/TurretTwo.png"} );
        ServiceLocator.getResourceService().loadTextureAtlases(new String[] {} );
        ServiceLocator.getResourceService().loadAll();

        // Mock rendering, physics, game time, game area
        RenderService renderService = new RenderService();
        StructurePlacementService structurePlacementService = new StructurePlacementService(new EventHandler());
        TerrainComponent testTerrain = makeComponent();
        GameArea gameArea = new GameArea() {
            @Override
            public void create() {
                terrain = testTerrain;
            }
        };
        gameArea.create();
        ServiceLocator.registerGameArea(gameArea);
        ServiceLocator.registerRenderService(renderService);
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerStructurePlacementService(structurePlacementService);

        //place obstacles on the map
        placeObjects();
    }

    @Test
    void copiesCorrectGrid() {
        GridPoint2 start = new GridPoint2(0, 0);
        GridPoint2 target = new GridPoint2(1, 0);

        PathFinder.findPath(start, target);

        boolean isSameGrid = false;
        Map<GridPoint2, Entity> redGrids = ServiceLocator.getGameArea().getAreaEntities();
        Set<PathFinder.Node> closedSet = PathFinder.getClosedSet();

        assertEquals(redGrids.size(), closedSet.size() - 2);
    }

    /**
     * [ ] [ ]
     * [s] [t]
     */
    @Test
    void findPathTest() {
        GridPoint2 start = new GridPoint2(0, 0);
        GridPoint2 target = new GridPoint2(1, 0);

        List<GridPoint2> correctPath = new ArrayList<>();
        correctPath.add(target);

        assertEquals(correctPath, PathFinder.findPath(start, target));
    }

    private static TerrainComponent makeComponent() {
        OrthographicCamera camera = mock(OrthographicCamera.class);
        String mapPath = "map/base.tmx";
        TmxMapLoader mapLoader = new TmxMapLoader();

        TiledMap tiledMap = null;
        for (String origin :
                new String[]{"source/core/assets/", "core/assets/", "./"}) {
            File file = Gdx.files.internal(origin + mapPath).file();
            if (file.exists()) {
                tiledMap = mapLoader.load(file.getAbsolutePath());
                break;
            }
        }

        if (tiledMap == null) {
            throw new RuntimeException("Error loading TileMap" + mapPath);
        }

        TiledMapRenderer renderer = mock(TiledMapRenderer.class);

        return new TerrainComponent(camera, tiledMap, renderer, TerrainComponent.TerrainOrientation.ORTHOGONAL, 0.5f);
    }

    private void placeObjects() {
        BaseEntityConfig treeConfig = FileLoader.readClass(BaseEntityConfig.class, "configs/tree.json");
        Vector2 location1 = new Vector2(5, 5);
        Vector2 location2 = new Vector2(10, 10);
        Vector2 location3 = new Vector2(11, 15);

        Entity tree1 = createTree(treeConfig);
        ServiceLocator.getStructurePlacementService().spawnEntityAtVector(tree1, location1);

        Entity tree2 = createTree(treeConfig);
        ServiceLocator.getStructurePlacementService().spawnEntityAtVector(tree2, location2);

        Entity tree3 = createTree(treeConfig);
        ServiceLocator.getStructurePlacementService().spawnEntityAtVector(tree3, location3);
    }

    public static Entity createTree(BaseEntityConfig config) {
        Entity tree =
                new Entity()
                        .addComponent(new TextureRenderComponent(config.spritePath))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

        tree.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        tree.getComponent(TextureRenderComponent.class).scaleEntity();
        tree.scaleHeight(2.5f);
        PhysicsUtils.setScaledCollider(tree, 0.5f, 0.2f);
        return tree;
    }
}
