package com.csse3200.game.utils;

import java.io.File;
import java.util.StringJoiner;

public class LoadUtils {
    public static final String ROOT_PATH = "levels";
    public static final String SAVE_PATH = "save";
    public static final String DEFAULT_AREA = "main_area";
    public static final String JSON_EXT = ".json";
    public static final String GAMESTATE_FILE = "gamestate.json";
    public static final String ASSETS_FILE = "global_assets.json";
    public static final String GAME_FILE = "game.json";
    public static final String LEVEL_FILE = "level.json";
    public static final String MAIN_FILE = "main.json";
    public static final String ENTITIES_PATH = "entities";
    public static final String FAIL_MESSAGE = "Failed to load config of type ";
    public static final String FAIL_ENTITY = "Failed to load entities config";
    public static final String NO_LEVELS_ERROR = "No levels found in " + GAME_FILE;


    /**
     * Joins an arbitrary list of paths using the system's path separator
     * @param paths List of paths to join together
     * @return String of all the path strings joined by a path seperator
     */
    public static String joinPath(String... paths) {
        StringJoiner stringJoiner = new StringJoiner(File.separator);
        for (String part: paths) {
            stringJoiner.add(part);
        }
        return stringJoiner.toString();
    }

    /**
     * Returns that expected formatting ofr a planet name. With spaces replaced with "_" and lowercase.
     * @param name Name of planet to format
     * @return Formatted planet name.
     */
    public static String formatName(String name) {
        return name.toLowerCase().replace(" ", "_");
    }
}
