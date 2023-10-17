package com.csse3200.game.components;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

/**
 * This UI Component for displaying PowerUp.
 * It will display the current Power Up which is selected .
 */
public class PowerUpDisplayHUD extends UIComponent {
    Table table;

    private PowerupType powerupentity;
    private Label poweruplabel;

    Image speedupimage = null;
    Image healthupimage = null;
    Image extralifeimage = null;
    Image doublecrossimage = null;
    Image tempimmunityimage = null;
    Image doubledamageimage = null;
    Image snapimage = null;

    public PowerUpDisplayHUD(PowerupType powerUpEntity) {
        this.powerupentity = powerUpEntity;
    }

    @Override
    public void create() {
        super.create();
        addActors();
        entity.getEvents().addListener("Current PowerUp", this::updatePowerUpDisplayUi);
    }

    /**
     * the select PowerUp function helps to select the current powerUp
     * @return PowerUpEntity
     */
    //
    public Image selectPowerUp() {

        if (powerupentity == PowerupType.HEALTH_BOOST) {
            healthupimage = new Image(ServiceLocator.getResourceService().getAsset("images/powerups/health_boost.png", Texture.class));
            return healthupimage;
        }

        if (powerupentity == PowerupType.SPEED_BOOST) {
            speedupimage = new Image(ServiceLocator.getResourceService().getAsset("images/powerups/speed_boost.png", Texture.class));
            return speedupimage;
        }

        if (powerupentity == PowerupType.EXTRA_LIFE) {
            extralifeimage = new Image(ServiceLocator.getResourceService().getAsset("images/powerups/extra_life.png", Texture.class));
            return extralifeimage;
        }

        if (powerupentity == PowerupType.DOUBLE_CROSS) {
            doublecrossimage = new Image(ServiceLocator.getResourceService().getAsset("images/powerups/double_cross.png", Texture.class));
            return doublecrossimage;
        }
        if (powerupentity == PowerupType.TEMP_IMMUNITY) {
            tempimmunityimage = new Image(ServiceLocator.getResourceService().getAsset("images/powerups/temp_immunity.png", Texture.class));
            return tempimmunityimage;
        }
        if (powerupentity == PowerupType.DOUBLE_DAMAGE) {
            doubledamageimage = new Image(ServiceLocator.getResourceService().getAsset("images/powerups/double_damage.png", Texture.class));
            return doubledamageimage;
        }
        if (powerupentity == PowerupType.SNAP) {
            snapimage = new Image(ServiceLocator.getResourceService().getAsset("images/powerups/snap.png", Texture.class));
            return snapimage;
        }
        else return null;
    }

    /**
     * Creates actors and position them on the stage using a table
     * @see Table for positining options
     */
    private void addActors() {
        table = new Table();
        table.top().left();
        table.setFillParent(true);
        table.padTop(115f).padLeft(5f);
        float powerUpLength = 40f;
        String powerUp = " ";
        CharSequence powerUpText = String.format("Current PowerUp : %s",powerUp);
        poweruplabel = new Label(powerUpText, skin,"small");
        table.add(poweruplabel);

        if (selectPowerUp() == healthupimage) {
            table.add(healthupimage).size(powerUpLength).pad(5);
        }
        else if (selectPowerUp() == speedupimage) {
            table.add(speedupimage).size(powerUpLength).pad(5);
        }
        else if (selectPowerUp() == extralifeimage) {
            table.add(extralifeimage).size(powerUpLength).pad(5);
        }
        // stage.addActor(table); todo: fix
    }

    /**
     * @param batch Batch to render to.
     */

    protected void draw(SpriteBatch batch) {
        // Drawing is handled by the stage
    }

    /**
     * Updates the Player PowerUp display name
     * @param powerUp contains the string value of the updated powerUp Type
     */
    public void updatePowerUpDisplayUi(String powerUp) {
        CharSequence text = String.format("Current PowerUp: %s", powerUp);
        poweruplabel.setText(text);
        selectPowerUp();
    }

    @Override
    public void dispose() {
        super.dispose();
        poweruplabel.remove();
        selectPowerUp();
    }
}