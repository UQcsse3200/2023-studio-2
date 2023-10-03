package com.csse3200.game.areas.mapConfig;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import net.dermetfan.gdx.physics.box2d.PositionController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class MapConfigLoaderTest {
    private static GameAreaConfig fullGameAreaConfig;
    private static GameAreaConfig gameAreaConfig;
    private static PlayerConfig playerConfig;
    private static AreaEntityConfig areaEntityConfig;
    private static GameAreaConfig expectedGameArea;

    private GameAreaConfig setupGameArea() {
        GameAreaConfig gameArea = new GameAreaConfig();
        gameArea.texturePaths = new String[] {"Texture1.png"};
        gameArea.textureAtlasPaths = new String[] {"Texture1.atlas"};
        gameArea.soundPaths = new String[] {"sound.wav"};
        gameArea.particleEffectPaths = new String[] {"particle.effect"};
        gameArea.backgroundMusicPath = "background.wav";
        gameArea.winConditions = List.of(new ResourceCondition());
        return gameArea;
    }

    private PlayerConfig setupPlayer() {
        PlayerConfig playerConfig = new PlayerConfig();
        playerConfig.spritePath = "playerSprite.png";
        playerConfig.position = new GridPoint2(0, 0);
        return playerConfig;
    }

    private AreaEntityConfig setupEntities() {
        return new AreaEntityConfig();
    }

    @BeforeEach
    void setup() {
        gameAreaConfig = setupGameArea();
        playerConfig = setupPlayer();
        areaEntityConfig = setupEntities();

        fullGameAreaConfig = setupGameArea();
        fullGameAreaConfig.areaEntityConfig = setupEntities();

        expectedGameArea = setupGameArea();
        expectedGameArea.areaEntityConfig = setupEntities();
    }

    @Test
    void loadValidMapFile() throws InvalidConfigException {
        try (MockedStatic<FileLoader> mockFileLoader = mockStatic(FileLoader.class)) {
            mockFileLoader.when(() -> FileLoader.readClass(eq(GameAreaConfig.class), any(), any()))
                    .thenReturn(fullGameAreaConfig);
            assertEquals(expectedGameArea, MapConfigLoader.loadMapFile("path.json"));
        }
    }

    @Test
    void loadInvalidMapFile() {
        try (MockedStatic<FileLoader> mockFileLoader = mockStatic(FileLoader.class)) {
            mockFileLoader.when(() -> FileLoader.readClass(eq(GameAreaConfig.class), any(), any()))
                    .thenReturn(null);
            assertThrows(InvalidConfigException.class, () -> MapConfigLoader.loadMapFile("path.json"));
        }
    }

    @Test
    void loadValidMapDirectory() throws InvalidConfigException {
        try (MockedStatic<FileLoader> mockFileLoader = mockStatic(FileLoader.class)) {
            mockFileLoader.when(() -> FileLoader.readClass(eq(GameAreaConfig.class), any(), any()))
                    .thenReturn(gameAreaConfig);
            mockFileLoader.when(() -> FileLoader.readClass(eq(AreaEntityConfig.class), any(), any()))
                    .thenReturn(areaEntityConfig);

            assertEquals(MapConfigLoader.loadMapDirectory("path/"), expectedGameArea);
        }
    }

    @Test
    void loadInvalidMapDirectoryGameArea() {
        try (MockedStatic<FileLoader> mockFileLoader = mockStatic(FileLoader.class)) {
            mockFileLoader.when(() -> FileLoader.readClass(eq(GameAreaConfig.class), any(), any()))
                    .thenReturn(null);
            mockFileLoader.when(() -> FileLoader.readClass(eq(AreaEntityConfig.class), any(), any()))
                    .thenReturn(areaEntityConfig);

            assertThrows(InvalidConfigException.class, () -> MapConfigLoader.loadMapDirectory("path/"));
        }
    }

    //TODO: Write tests to cover entity folder structure
}
