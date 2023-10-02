package com.csse3200.game.components;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BaseEntityConfig;

import java.util.function.Function;

public class SaveableComponent<T extends BaseEntityConfig> extends Component {
    Function<Entity, T> saveFunction;
    Class<T> configType;

    public SaveableComponent(Function<Entity, T> saveFunction, Class<T> configType) {
        this.saveFunction = saveFunction;
        this.configType = configType;
    }

    public T save() {
        BaseEntityConfig config = saveFunction.apply(entity);
        if (config.position != null) config.position = new GridPoint2((int) (config.position.x / 0.5), (int) (config.position.y /0.5));
        return (T) config;
    }

    public Class<T> getType() {
        return configType;
    }
}
