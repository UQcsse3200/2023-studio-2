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

    public PowerupType powerUpEntity;
    private Label PowerUpLabel;

    Image SpeedUpImage = null;
    Image HealthUpImage = null;
    Image ExtraLifeImage = null;

    public PowerUpDisplayHUD(PowerupType powerUpEntity) {
        this.powerUpEntity = powerUpEntity;
    }

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

        if (powerUpEntity == PowerupType.HEALTH_BOOST) {
            HealthUpImage = new Image(ServiceLocator.getResourceService().getAsset("images/powerups/health_boost.png", Texture.class));
            return HealthUpImage;
        }

        if (powerUpEntity == PowerupType.SPEED_BOOST) {
            SpeedUpImage = new Image(ServiceLocator.getResourceService().getAsset("images/powerups/speed_boost.png", Texture.class));
            return SpeedUpImage;
        }

        if (powerUpEntity == PowerupType.EXTRA_LIFE) {
            ExtraLifeImage = new Image(ServiceLocator.getResourceService().getAsset("images/powerups/extralife.png", Texture.class));
            return ExtraLifeImage;
        }

        if (powerUpEntity == PowerupType.DOUBLE_CROSS) {
            ExtraLifeImage = new Image(ServiceLocator.getResourceService().getAsset("images/powerups/double_cross.png", Texture.class));
            return ExtraLifeImage;
        }
        if (powerUpEntity == PowerupType.TEMP_IMMUNITY) {
            ExtraLifeImage = new Image(ServiceLocator.getResourceService().getAsset("images/powerups/invincibility_potion.png", Texture.class));
            return ExtraLifeImage;
        }
        if (powerUpEntity == PowerupType.DOUBLE_DAMAGE) {
            ExtraLifeImage = new Image(ServiceLocator.getResourceService().getAsset("images/powerups/double_damage.png", Texture.class));
            return ExtraLifeImage;
        }
        if (powerUpEntity == PowerupType.SNAP) {
            ExtraLifeImage = new Image(ServiceLocator.getResourceService().getAsset("images/powerups/snap.png", Texture.class));
            return ExtraLifeImage;
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

//        String PowerUp = entity.getComponent(PowerupComponent.class).getPowerupType();
        float powerUpLength = 40f;
        String powerUp = " ";
        CharSequence powerUpText = String.format("Current PowerUp : %s",powerUp);
        PowerUpLabel = new Label(powerUpText, skin,"small");
        table.add(PowerUpLabel);

        if (selectPowerUp() == HealthUpImage) {
            table.add(HealthUpImage).size(powerUpLength).pad(5);
        }
        else if (selectPowerUp() == SpeedUpImage) {
            // timer task on applyEffect
            table.add(SpeedUpImage).size(powerUpLength).pad(5);
        }
        else if (selectPowerUp() == ExtraLifeImage) {
            table.add(ExtraLifeImage).size(powerUpLength).pad(5);
        }
        // stage.addActor(table); todo: fix
    }

    /**
     * @param batch Batch to render to.
     */

    protected void draw(SpriteBatch batch) {
    }

    /**
     * Updates the Player PowerUp display name
     * @param powerUp contains the string value of the updated powerUp Type
     */
    public void updatePowerUpDisplayUi(String powerUp) {
        CharSequence text = String.format("Current PowerUp: %s", powerUp);
        PowerUpLabel.setText(text);
        selectPowerUp();
    }

    @Override
    public void dispose() {
        super.dispose();
        PowerUpLabel.remove();
        selectPowerUp();
    }
}