package com.csse3200.game.components.gamearea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.components.upgradetree.UpgradeDisplay;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

/**
 * Displays the name of the current game area and an image of the planet.
 */
public class PlanetHudDisplay extends UIComponent {
    private String gameAreaName = "";

    private Image planetImage;
    private Image planetImageFrame;
    private Table planetTable;
    private Label planetLabel;

    public PlanetHudDisplay(String gameAreaName, String planetImagePath) {
        this.gameAreaName = gameAreaName;
        this.planetImage = new Image(ServiceLocator.getResourceService().getAsset(planetImagePath, Texture.class));
        planetImageFrame = new Image(ServiceLocator.getResourceService().getAsset("images/player/planet-frame.png", Texture.class));
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        planetTable = new Table();
        planetTable.top().left();
        planetTable.setFillParent(true);
        planetTable.padTop(5f).padLeft(5f);

        planetLabel = new Label(gameAreaName, skin, "small");
        planetLabel.setFontScale(0.22f);

        Table overlayTable = new Table();
        overlayTable.add(planetImage).size(130f).padLeft(5f).padBottom(15f);
        overlayTable.row();
        overlayTable.add(planetLabel);

        Stack planetStack = new Stack();
        planetStack.add(planetImageFrame);
        planetStack.add(overlayTable);
        planetTable.add(planetStack).size(180f, 225f);
        planetTable.row();
        createUpgradeTreeButton(planetTable);

        stage.addActor(planetTable);
    }

    /**
     * @param table - Used to add Column/Rows and define the actors
     * createUpgradeTreeButton() - creating button and defining it on the top left
     *                              also playing the sound when on tapping it
     *
     */
    public void createUpgradeTreeButton(Table table) {
        TextButton button = new TextButton("Upgrade Tree", skin);

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                KeyboardPlayerInputComponent keys =
                        ServiceLocator.getEntityService().getPlayer().getComponent(KeyboardPlayerInputComponent.class);
                keys.clearWalking();
                UpgradeDisplay display = UpgradeDisplay.createUpgradeDisplay();
                ServiceLocator.getRenderService().getStage().addActor(display);

                entity.getEvents().trigger("playSound", "upgradeTreeTap");
            }
        });

        table.add(button);
    }

    @Override
    public void draw(SpriteBatch batch)  {

    }

    @Override
    public void dispose() {
        super.dispose();
        planetImage.remove();
        planetImageFrame.remove();
        planetTable.remove();
        planetLabel.remove();
    }
}
