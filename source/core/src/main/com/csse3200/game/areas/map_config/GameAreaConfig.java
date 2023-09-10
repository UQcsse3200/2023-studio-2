package com.csse3200.game.areas.map_config;

import com.badlogic.gdx.math.GridPoint2;

import java.util.List;

public class GameAreaConfig {
    //Map Assets - all entity specific paths should be defined within the levelConfig file
    public String[] texturePaths = null;
    public String[] textureAtlasPaths = null;
    public String[] soundPaths = null;
    public String backgroundMusicPath = null;

    //Map Properties
    public String mapName = "Planet";
    public String terrainPath = "map/base.tmx";
    public List<ResourceCondition> winConditions = null;

    public AreaEntityConfig entityConfig = null;

    //TODO: EVERYTHING AFTER THIS WILL BE REFACTORED TO AreaEntityConfig
    //Powerup Properties
    public List<GridPoint2> healthPowerups = null;
    public List<GridPoint2> speedPowerups = null;

    //Extractor Properties
    public int extractorStartHealth = 0;
    public List<GridPoint2> solstitePositions = null;
    public List<GridPoint2> durasteelPositions = null;
    public List<GridPoint2> nebulitePositions = null;
    public long solstiteProduction = 0;
    public long durasteelProduction = 0;
    public long nebuliteProduction = 0;

    //Ship Properties
    public GridPoint2 shipPosition = null;

    //Player Properties
    public GridPoint2 playerPosition = null;

    //Companion Properties
    public GridPoint2 companionPosition = null;

    //Enemies Properties
    public int numMeleePTE = 0;
    public int numMeleeDTE = 0;
    public int numRangePTE = 0;

    //Boss Properties
    public GridPoint2 bossPosition = null;

    //Botanist Properties
    public GridPoint2 botanistPosition = null;
}