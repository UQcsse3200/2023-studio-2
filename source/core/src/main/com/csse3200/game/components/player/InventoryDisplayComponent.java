package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.entities.configs.WeaponConfigs;
import com.csse3200.game.ui.UIComponent;

import java.util.HashMap;

public class InventoryDisplayComponent extends UIComponent {

    Table table = new Table();
    InventoryComponent inventory;

    public InventoryDisplayComponent(Entity player) {
        inventory = player.getComponent(InventoryComponent.class);
        makeTable();
    }

    /**
     * Creates actors and positions them on the stage using a table.
     *
     * @return
     * @see Table for positioning options
     */
    private Table makeTable() {
        table.setColor(Color.WHITE); // todo: add table background
        HashMap<Integer, WeaponType> map = inventory.getEquippedWeaponMap();

        for (WeaponType weapon : map.values()) {
            String weaponString = weapon.name();
            TextButton button = new TextButton(weaponString, skin);
            table.add(button).row();

            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    inventory.changeEquipped(weapon);
                    entity.getEvents().trigger("changeWeapon", weapon);
                }
            });
        }

        return table;
    }

    /**
     * Creates actors and positions them on the stage using a table.
     * @see Table for positioning options
     */
    private void addActors() {
        table.center().right();
        table.setFillParent(true);
        table.padBottom(45f).padRight(45f);
        stage.addActor(table);
    }

    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        addActors();
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // Handled by stage
    }
}
