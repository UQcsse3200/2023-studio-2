package com.csse3200.game.areas.mapConfig;

import com.csse3200.game.files.FileLoader;

import static com.csse3200.game.areas.mapConfig.LoadUtils.*;

public class LevelConfigLoader {
    public static LevelConfig loadLevel(String levelName) throws InvalidConfigException {
        String path = joinPath(ROOT_PATH, levelName, LEVEL_FILE);
        LevelConfig levelConfig = FileLoader.readClass(LevelConfig.class, path, FileLoader.Location.INTERNAL);
        if (levelConfig == null) throw new InvalidConfigException(FAIL_MESSAGE + LevelConfig.class.getSimpleName() + " - " + levelName);
        return levelConfig;
    }
}
