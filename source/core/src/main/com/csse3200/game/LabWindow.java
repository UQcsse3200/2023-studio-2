package com.csse3200.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.components.PowerupType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputOverrideComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * This is a window that can be added to a stage to pop up for the extractor Laboratory.
 */
public class LabWindow extends Window {
    private InputOverrideComponent inputOverrideComponent;
    private int points = 100; // Initialize points with your starting value
    private Label pointsLabel; // Label to display points

    private TextButton[] potionButton=new TextButton[8];

    private Texture potionImage;
    private String potionName;

    public static LabWindow MakeNewLaboratory() {
        Texture background = new Texture("images/companion/lab.png");
        background.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        return new LabWindow(background);
    }

    public LabWindow(Texture background) {
        super("", new WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(background)));
        create();
    }

    void create(){
        // Here set up the window to be centered on the stage with 80% width and 65% height.
        Stage stage = ServiceLocator.getRenderService().getStage();
        setWidth((float) (stage.getWidth() * 0.70));
        setHeight((float) (stage.getHeight() * 0.50));
        setPosition(stage.getWidth() / 2 - getWidth() / 2 * getScaleX(), stage.getHeight() / 2 - getHeight() / 2 * getScaleY());
        Skin skin = new Skin(Gdx.files.internal("kenney-rpg-expansion/kenneyrpg.json"));
        // Create a Table to hold the buttons and center them within the window

        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        // Fill the entire LabWindow

        Texture[] potionImages = {
                new Texture("images/powerups/death_potion.png"),
                new Texture("images/powerups/speed_potion.png"),
                new Texture("images/powerups/health_potion.png"),
                new Texture("images/powerups/temp_immunity.png"),
                new Texture("images/powerups/double_damage.png"),
                new Texture("images/powerups/extra_life.png"),
                new Texture("images/powerups/snap.png"),
                new Texture("images/powerups/double_cross.png")
        };

        String[] potionNames = {
                "Death", "Speed", "Health", "Invincibility", "2x Damage", "Extra Life", "Snap", "Double Cross"
        };

        int[] potionCosts = {
                50, 60, 30, 40, 70, 80, 90, 100
        };
        // Create points label
        pointsLabel = new Label("Points: " + points, skin);
        pointsLabel.setFontScale(0.5f); // Adjust font scale as needed
        pointsLabel.setPosition(40, 550); // Adjust position as needed
        addActor(pointsLabel);
        // Create a separate table for the "EXIT" button and position it at the bottom-right
        Table exitButtonTable = new Table();
        exitButtonTable.setFillParent(true);
        exitButtonTable.bottom().right(); // Align the table to the bottom-right corner of the screen

        TextButton returnToGameButton = new TextButton("EXIT", skin);
        exitButtonTable.add(returnToGameButton).padBottom(20).padRight(20);
        addActor(exitButtonTable);
        //addActor(exit);
        returnToGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                failLaboratory();
            }
        });

        for (int i = 0; i < potionImages.length; i++) {
            potionImage = potionImages[i];
            potionName = potionNames[i];


            Image potionImageWidget = new Image(potionImage);
            potionButton[i] = new TextButton(potionName + " - " + potionCosts[i] + " points", skin);
            buttonTable.add(potionImageWidget).width(100).height(64);;
            buttonTable.add(potionButton[i]).pad(10);
            int finalI = i;
            potionButton[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Deduct points when purchasing a potion and update the pointsLabel
                    if (points >=potionCosts[finalI]) {
                        update(potionCosts[finalI]);
                        pointsLabel.setText("Points: " + points); // Update pointsLabel text
                        PowerupType powerupType = getPowerupTypeForPotionName(potionNames[finalI]);
                        ServiceLocator.getEntityService().getCompanion().getEvents().trigger("SpawnPowerup", powerupType);
                    }
                }
            });

            if(i==3) {
                buttonTable.row();
            }
        }
        addActor(buttonTable);




        // Override all normal user input
        inputOverrideComponent = new InputOverrideComponent();
        ServiceLocator.getInputService().register(inputOverrideComponent);

    }

    void update (int cost){
        this.points-=cost;
    }

    private PowerupType getPowerupTypeForPotionName(String potionName) {
        switch (potionName) {
            case "Death":
                return PowerupType.DEATH_POTION;
            case "Speed":
                return PowerupType.SPEED_BOOST;
            case "Health":
                return PowerupType.HEALTH_BOOST;
            case "Invincibility":
                return PowerupType.TEMP_IMMUNITY;
            case "2x Damage":
                return PowerupType.DOUBLE_DAMAGE;
            case "Extra Life":
                return PowerupType.EXTRA_LIFE;
            case "Snap":
                return PowerupType.SNAP;
            case "Double Cross":
                return PowerupType.DOUBLE_CROSS;
            default:
                return null;
        }
    }

    /**
     * Call this method to exit the Laboratory
     */
    private void failLaboratory() {
        remove();
    }

    /**
     * Call this method to exit the Laboratory and repair the extractor's health.
     */

    @Override
    public boolean remove() {
        // Stop overriding input when exiting the Laboratory
        ServiceLocator.getInputService().unregister(inputOverrideComponent);
        return super.remove();
    }
}
// updated with points