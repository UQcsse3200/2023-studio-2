package com.csse3200.game.entities.data_access_objects;

import com.csse3200.game.entities.configs.BotanistConfig;
import com.csse3200.game.entities.configs.BaseEntityConfig;
import com.csse3200.game.entities.configs.EnemyConfig;
import com.csse3200.game.entities.configs.BossConfig;

public class NPCDAO extends EntityDAO {
  public BaseEntityConfig ghost;
  public BotanistConfig botanist;
  
  public EnemyConfig meleeEnemyPTE;
  public EnemyConfig meleeEnemyDTE;
  public EnemyConfig rangeEnemyPTE;
  public EnemyConfig rangeEnemyDTE;
  public BossConfig meleeBossPTE;
  public BossConfig meleeBossDTE;
  public BossConfig rangeBossPTE;
  public BossConfig rangeBossDTE;


}
