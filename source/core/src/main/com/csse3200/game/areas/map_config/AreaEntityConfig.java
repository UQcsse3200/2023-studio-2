package com.csse3200.game.areas.map_config;

import com.csse3200.game.entities.data_access_objects.*;

import java.util.ArrayList;
import java.util.List;

public class AreaEntityConfig {
    //TODO: Update these to specific types
    public List<EnemyDAO> enemyDAOs = new ArrayList<>();
    public List<NPCDAO> NPCDAOs = new ArrayList<>();
    public List<WallDAO> wallDAOs = new ArrayList<>();
    public List<EntityDAO> bulletDAOs = new ArrayList<>();
    //TODO: Add all types here

    public List<EntityDAO> getAllDAOs() {
        List<EntityDAO> DAOs = new ArrayList<>();
        DAOs.addAll(enemyDAOs);
        DAOs.addAll(NPCDAOs);
        DAOs.addAll(wallDAOs);
        DAOs.addAll(bulletDAOs);
        //TODO: Add all types here
        return DAOs;
    }
}
