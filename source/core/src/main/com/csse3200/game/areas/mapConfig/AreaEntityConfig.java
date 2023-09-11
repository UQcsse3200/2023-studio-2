package com.csse3200.game.areas.mapConfig;

import com.csse3200.game.entities.configs.*;

import java.util.ArrayList;
import java.util.List;

public class AreaEntityConfig {
    //TODO: Update these to specific types
    public List<EnemyConfig> enemyDAOs = new ArrayList<>();
    public List<BotanistConfig> NPCDAOs = new ArrayList<>();
    public List<WallConfig> wallDAOs = new ArrayList<>();
    public List<EnemyBulletConfig> bulletDAOs = new ArrayList<>();

    //TODO: Add all types here

    public List<BaseEntityConfig> getAllDAOs() {
        List<BaseEntityConfig> DAOs = new ArrayList<>();
        DAOs.addAll(enemyDAOs);
        DAOs.addAll(NPCDAOs);
        DAOs.addAll(wallDAOs);
        DAOs.addAll(bulletDAOs);
        //TODO: Add all types here
        return DAOs;
    }
}
