package com.csse3200.game.utils;

import com.badlogic.gdx.Gdx;
import com.csse3200.game.areas.mapConfig.InvalidConfigException;

import java.io.File;
import java.util.List;
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
    public static final String NO_FILE_FOUND = "No config file found ";
    public static final List<String> PATH_OPTIONS = List.of(SAVE_PATH, ROOT_PATH);

    /**
     * Joins an arbitrary list of paths using the system's path separator
     * @param paths List of paths to join together
     * @return String of all the path strings joined by a path seperator
     */
    public static String joinPath(List<String> paths) {
        StringJoiner stringJoiner = new StringJoiner(File.separator);
        for (String part: paths) {
            stringJoiner.add(part);
        }
        return stringJoiner.toString();
    }

    public static boolean pathExists(String path) {
        return Gdx.files.local(path).exists();
    }

    /**
     * Returns the first file path that exists, using the different roots in order.
     * @param roots List of roots to try in order
     * @param path File path to search at.
     * @return String file path of first valid file
     * @throws InvalidConfigException If no valid file is found.
     */
    public static String getOptionalSavePath(List<String> roots, String path) throws InvalidConfigException {
        for (String root : roots) {
            String filePath = joinPath(List.of(root, path));
            if (pathExists(filePath)) {
                return filePath;
            }
        }

        throw new InvalidConfigException(NO_FILE_FOUND + path);
    }

    /**
     * Returns the first file path that exists, using the different roots in order.
     * @param roots List of roots to try in order
     * @param paths List of paths to join together as file path
     * @return String file path of first valid file
     * @throws InvalidConfigException If no valid file is found.
     */
    public static String getOptionalSavePath(List<String> roots, List<String> paths) throws InvalidConfigException {
        return getOptionalSavePath(roots, joinPath(paths));
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
