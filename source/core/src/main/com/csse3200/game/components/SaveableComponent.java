package com.csse3200.game.components;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BaseEntityConfig;

import java.util.function.Function;

public class SaveableComponent<T extends BaseEntityConfig> extends Component {
    Function<Entity, T> saveFunction;

    public SaveableComponent(Function<Entity, T> saveFunction) {
        this.saveFunction = saveFunction;
    }

    public T save() {
        return saveFunction.apply(entity);
    }
}
