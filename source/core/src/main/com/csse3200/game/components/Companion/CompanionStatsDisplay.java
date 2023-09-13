package com.csse3200.game.components.Companion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;


/**
 * A UI component for displaying Companion stats, e.g. health.
 */
public class CompanionStatsDisplay extends UIComponent {
    Table table;
    private boolean update = false;
    Table table2;

    public Entity playerEntity;

    public Label messageLabel;
    public Label label;



    private boolean isInvincible = true;
    private boolean invincibilityImageLoaded = false;
    private float invincibilityTimer = 0.0f;
    private boolean isInfiniteHealth = true;
    private boolean isSpecialAttack = false;

    public Texture defaultTexture;
    public Texture invincibleTexture;

    private  AssetManager assetManager;

    public CompanionStatsDisplay() {
        assetManager = new AssetManager();
    }






    public CompanionStatsDisplay(Entity playerEntity){
        this.playerEntity = playerEntity;


    }

    /**
     * Creates reusable UI styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        addActors();
        entity.getEvents().addListener("updateHealth", this::updateCompanionHealthUI);
        //entity.getEvents().addListener("updateGold", this::updateCompanionGoldUI);
        playerEntity.getEvents().addListener("updateHealth", this:: updatePlayerHealthUI);
    }

    /**
     * Creates actors and positions them on the stage using a table.
     * See {@link Table} for positioning options.
     */
    private void addActors() {
        table = new Table();
        table.top().left();
        table.setFillParent(true);
        table.padTop(85f).padLeft(5f);

        // Health text
        int Chealth = entity.getComponent(CombatStatsComponent.class).getHealth();
        //int gold = entity.getComponent(InventoryComponent.class).getGold();
        CharSequence healthText = String.format("Companion Health: %d", Chealth);
        messageLabel = new Label(healthText, skin, "large");
        table.add(messageLabel);
        stage.addActor(table);

    }















    public void toggleInfiniteHealth() {

        if (isInfiniteHealth) {
            // Save the original health value before making it infinite
            Gdx.app.debug("Infinite Health", "Original Health: "+ 50 );

            updateCompanionHealthUI(Integer.MAX_VALUE);
            Gdx.app.debug("Infinite Health", "Infinite Health is ON");
            // Print the updated health value
            Gdx.app.debug("Infinite Health", "Updated Health: " + 50);
            isInfiniteHealth = false;
        } else {
            // Restore the original health value
            updateCompanionHealthUI(50);
            Gdx.app.debug("Infinite Health", "Infinite Health is OFF");

            // Print the restored health value
            Gdx.app.debug("Infinite Health", "Restored Health: " + 50);
            isInfiniteHealth = true;
        }
    }









    private void addAlert(int health){
        PhysicsComponent companionPhysics = entity.getComponent(PhysicsComponent.class);
        //calculate the player position
        Vector2 compPos = companionPhysics.getBody().getPosition();
        //compPos = playerEntity.getPosition();
        System.out.println(compPos);
        table2 = new Table();
        table2.top().left();
        table2.setFillParent(true);
        table2.setPosition(compPos.x + 550f, compPos.y - 200F);
        //table2.padTop(comPosy).padLeft(compPosx);

        // Health text
        CharSequence healthText2 = String.format("Low Health: %d", health);
        label = new Label(healthText2, skin, "large");
        table2.add(label);
        stage.addActor(table2);
    }


    @Override
    public void draw(SpriteBatch batch) {
        // Code for drawing UI elements and updating the projection matrix.
    }


    public void updatePlayerHealthUI(int health) {
        // super.update();
        if (health <= 50 && !update) {
            addAlert(health);
            update = true;
            return;
        }

        if (update) {
            // Schedule a task to remove the label after a delay (e.g., 3 seconds)
            Timer.schedule(new Task() {
                @Override
                public void run() {
                    label.remove();
                    update = false; // Reset the update flag
                }
            }, 3.0f); // Adjust the delay as needed (3.0f is 3 seconds)
        }

    }


    /**
     * Updates the companion's health on the UI.
     *
     * @param health The updated health value to display.
     */
    public void updateCompanionHealthUI(int health) {
        table = new Table();
        table.top().left();
        table.setFillParent(true);
        table.padTop(85f).padLeft(5f);
        CharSequence healthText2 = String.format("Companion Health: %d", health);
        label = new Label(healthText2, skin, "large");
        table.add(label);
        ServiceLocator.getRenderService().getStage().addActor(table);
        Timer.schedule(new Task() {
            @Override
            public void run() {
                label.remove();
                update = false;
            }
        }, 8.0f);

    }
    /*public void updateCompanionGoldUI(int gold) {
        CharSequence text = String.format("Companion Gold: %d", gold);
        messageLabel.setText(text);
    }*/

    @Override
    public void dispose() {
        super.dispose();
        messageLabel.remove();
        label.remove();
    }
}


