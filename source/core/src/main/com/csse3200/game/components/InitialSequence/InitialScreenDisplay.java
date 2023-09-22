package com.csse3200.game.components.InitialSequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.GdxGame;
import com.csse3200.game.GdxGame.ScreenType;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.screens.PlanetScreen;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.AlertBox;
import com.csse3200.game.ui.TitleBox;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The user interface component responsible for displaying the initial story sequence.
 */
public class InitialScreenDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(InitialScreenDisplay.class);
    private final GdxGame game;
    private BitmapFont font;
    private float spaceSpeed = 5;
    private float planetToTextPadding = 150;
    private Image background;
    private Image planet;
    private Table rootTable;
    private Label storyLabel;

    /**
     * Creates a new instance of the InitialScreenDisplay.
     *
     * @param game The main game instance.
     */
    public InitialScreenDisplay(GdxGame game) {
        super();
        this.game = game;
    }

    @Override
    public void create() {
        super.create();
        // Load the BitmapFont
        font = new BitmapFont();
        addUIElements();
    }

    private void addUIElements() {
        // Load the background starfield image.
        background =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/InitialScreenImage.png", Texture.class));
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background.setPosition(0, 0);

        // Load the animated planet
        planet =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/InitialScreenBG.png", Texture.class));
        planet.setSize(2500, 400); // Set to a reasonable fixed size

        // The planet moves at a constant speed, so to make it appear at the right time,
        // it is to be placed at the right y coordinate above the screen.
        // The height is informed by the length of the text animation and the game's target FPS.
        // Set the initial position to the top of the screen
        float planetInitialY = Gdx.graphics.getHeight() - planet.getHeight() + 370;
        planet.setPosition((float) Gdx.graphics.getWidth() / 2, planetInitialY, Align.center);

        // Create a Table for layout
        rootTable = new Table();
        rootTable.setFillParent(true); // Make the table fill the screen
        rootTable.center(); // Center-align content vertically

        // The initial text you want to display
        String story = "Earth has become a desolate wasteland ravaged by a deadly virus.\n" +
                "last hope lies among the stars. You are one of the few survivors.";

        // Configure the Label
        Label.LabelStyle labelStyle = skin.get(Label.LabelStyle.class);
        labelStyle.font.getData().setScale(0.4f); // Set the font size (adjust the scale as needed)
        storyLabel = new Label("", labelStyle);
        storyLabel.setAlignment(Align.center);
        storyLabel.setWrap(false); // Allow text wrapping
        storyLabel.setWidth(Gdx.graphics.getWidth());

        // Add the storyLabel to the rootTable and make it expand
        rootTable.add(storyLabel).expandX().center().padTop(850f);
        rootTable.row().padTop(30f);

        TextButton continueButton = new TextButton("Continue", skin);
        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Continue button clicked");
                String startPlanetName = "Earth";
                logger.info(String.format("Start game, go to %s", startPlanetName));
                PlanetScreen planetScreen = new PlanetScreen(game, startPlanetName);
                ServiceLocator.getGameStateObserverService().trigger("updatePlanet", "currentPlanet", planetScreen);
                game.setScreen(planetScreen);

                AlertBox alertBox = new AlertBox(game, "Alert Box", skin);
                alertBox.showDialog(stage);

                logger.info("Loading Story");
                TitleBox titleBox = new TitleBox(game, "Story Introduction", skin);
                titleBox.showDialog(stage);
                game.setScreen(ScreenType.GAME_STORY);
            }
        });

        // Add the "Continue" button to the rootTable and center it at the bottom
        rootTable.add(continueButton).expandX().bottom().padBottom(30f);

        // Add actors to the stage
        stage.addActor(background);
        stage.addActor(planet);
        stage.addActor(rootTable);

        RepeatAction repeatAction = Actions.forever(
                Actions.sequence(
                        Actions.moveBy(0, planetToTextPadding, 4.0f), // Move up
                        Actions.moveBy(0, -planetToTextPadding, 4.0f) // Move down
                )
        );

        // Apply the RepeatAction to the planet image
        planet.addAction(repeatAction);

        // Start printing text letter by letter
        printTextLetterByLetter(story, storyLabel, 0.035f, 0.5f);
    }

    private void printTextLetterByLetter(final String text, final Label label, final float speed, final float initialDelay) {
        label.setText(""); // Clear the label text initially
        Timer.schedule(new Timer.Task() {
            int charIndex = 0;

            @Override
            public void run() {
                if (charIndex < text.length()) {
                    char currentChar = text.charAt(charIndex);
                    label.setText(label.getText().toString() + currentChar);
                    charIndex++;
                } else {
                    // Stop the Timer when all characters are printed
                    this.cancel();
                }
            }
        }, initialDelay, speed, text.length() - 1);
    }



    @Override
    protected void draw(SpriteBatch batch) {
        // Drawing is handled by the stage
    }

    @Override
    public void update() {
        // This movement logic is triggered on every frame until the middle of the planet hits its target position on the screen.
//        if (planet.getY(Align.center) >= rootTable.getY() + planetToTextPadding) {
//            planet.setY(planet.getY() - spaceSpeed); // Move the planet
//            background.setY(background.getY() - spaceSpeed); // Move the background
//        }
        stage.act(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        font.dispose();
        rootTable.clear();
        planet.clear();
        background.clear();
        super.dispose();
    }
}
