//package com.csse3200.game.components.npc;
//
//import com.badlogic.gdx.math.GridPoint2;
//import com.csse3200.game.areas.GameArea;
//import com.csse3200.game.areas.terrain.TerrainComponent;
//import com.csse3200.game.areas.terrain.TerrainFactory;
//import com.csse3200.game.entities.Entity;
//import com.csse3200.game.entities.EntityService;
//import com.csse3200.game.extensions.GameExtension;
//import com.csse3200.game.services.ServiceLocator;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.mock;
//
//@ExtendWith(GameExtension.class)
//public class PathFinderTest {
//    @Mock
//    TerrainFactory factory = mock(TerrainFactory.class);
//    TerrainComponent testTerrain = factory.createTerrain("map/base.tmx");
//
//    @BeforeEach
//    void loadGameArea() {
//        GameArea gameArea =
//                new GameArea() {
//                    @Override
//                    public void create() {
//                        terrain = testTerrain;
//                    }
//                };
//        ServiceLocator.registerGameArea(gameArea);
//    }
//
//    /**
//     * [s] [ ]
//     * [x] [t]
//     */
//    @Test
//    void findPathTest() {
//
//
//        ServiceLocator.registerEntityService(new EntityService());
//        Entity entity = mock(Entity.class);
//        entity.setPosition(0,0);
//
//        GridPoint2 start = new GridPoint2(0, 1);
//        GridPoint2 pathStep = new GridPoint2(1, 1);
//        GridPoint2 target = new GridPoint2(1, 0);
//
//        List<GridPoint2> correctPath = new ArrayList<>();
//        correctPath.add(start);
//        correctPath.add(pathStep);
//        correctPath.add(target);
//
//
//        assertEquals(PathFinder.findPath(start, target), correctPath);
//    }
//}
