package com.csse3200.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import net.dermetfan.utils.Pair;

import java.util.List;

public class MapConfig {
    //Assets
    public String[] texturePaths = null;
    public String[] textureAtlasPaths = null;
    public String[] soundPaths = null;
    public String backgroundMusicPath = null;

    //Map Properties
    public String mapName = "Planet";
    public String terrainPath = "map/base.tmx";

    //Wall Properties
    public float wallSize = 0.1f;

    //Powerup Properties
    public List<GridPoint2> healthPowerups = null;
    public List<GridPoint2> speedPowerups = null;

    //Extractor Properties
    public List<GridPoint2> solstitePositions = null;
    public List<GridPoint2> durasteelPositions = null;
    public List<GridPoint2> nebulitePositions = null;

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
