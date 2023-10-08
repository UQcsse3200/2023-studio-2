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
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.mainmenu.InsertButtons;
import com.csse3200.game.services.PlanetTravel;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.AlertBox;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

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
    private ArrayList<String> InitialScreenImages;
    private Image backgroundImage;
    private ArrayList<String> stories;
    private int currentStoryIndex = 0;

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
        entity.getEvents().addListener("next", this::nextScene);
        entity.getEvents().addListener("previous", this::prevScene);
        entity.getEvents().addListener("skip", this::onSkip);
    }

    private void addUIElements() {
        // Load the background starfield image.
        background =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/menu/InitialScreenImage.png", Texture.class));
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background.setPosition(0, 0);

        // Load the animated planet
        planet =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/menu/InitialScreenBG.png", Texture.class));
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
        stories = new ArrayList<>();
        stories.add("Earth has become a desolate wasteland ravaged by a deadly virus.\n" +
                "Last hope lies among the stars. You are one of the few survivors.");
        stories.add("This is the second text you want to display when 'Next' is pressed.");

        String story = stories.get(currentStoryIndex);

        // Configure the Label
        Label.LabelStyle labelStyle = skin.get(Label.LabelStyle.class);
        labelStyle.font.getData().setScale(0.4f); // Set the font size (adjust the scale as needed)
        storyLabel = new Label(story, labelStyle);
        storyLabel.setAlignment(Align.center);
        storyLabel.setWrap(false); // Allow text wrapping
        storyLabel.setWidth(Gdx.graphics.getWidth());

        // Add the storyLabel to the rootTable and make it expand
        rootTable.add(storyLabel).expandX().center().padTop(900f);
        rootTable.row().padTop(30f);

        // Add actors to the stage
        stage.addActor(background);
        stage.addActor(planet);
        stage.addActor(rootTable);
        InitialScreenImages = new ArrayList<>();
        InitialScreenImages.add("images/menu/InitialScreenImage.png");
        InitialScreenImages.add("images/menu/InitialScreenImage-2.png");

        RepeatAction repeatAction = Actions.forever(
                Actions.sequence(
                        Actions.moveBy(0, planetToTextPadding, 4.0f), // Move up
                        Actions.moveBy(0, -planetToTextPadding, 4.0f) // Move down
                )
        );
        backgroundImage = new Image(new Texture(Gdx.files.internal(InitialScreenImages.get(0))));
        stage.addActor(backgroundImage);

        // Apply the RepeatAction to the planet image
        planet.addAction(repeatAction);

        // Start printing text letter by letter
        printTextLetterByLetter(story, storyLabel, 0.035f, 0.5f);

        InsertButtons bothButtons = new InsertButtons();
        // Create next button
        String nextTexture = "images/interface/next_cut.png";
        String nextTextureHover = "images/interface/next_cut_hover.png";
        ImageButton nextBtn = bothButtons.draw(nextTexture, nextTextureHover);

        // Create previous button
        String prevTexture = "images/interface/prev_cut.png";
        String prevTextureHover = "images/interface/prev_cut_hover.png";
        ImageButton prevBtn = bothButtons.draw(prevTexture, prevTextureHover);

        // Create skip button
        String skipTexture = "images/interface/skip_btn.png";
        String skipTextureHover = "images/interface/skip_btn_hover.png";
        ImageButton skipBtn = bothButtons.draw(skipTexture, skipTextureHover);

        // Attach listeners to navigation buttons
        nextBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeListener.ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Next button clicked");
                        entity.getEvents().trigger("next");
                    }
                });

        prevBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeListener.ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Previous button clicked");
                        entity.getEvents().trigger("previous");
                    }
                });
        skipBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Skip button clicked");
                        entity.getEvents().trigger("skip");
                    }
                });


// Create a new table for navigation buttons
        Table buttonTable = new Table();
        buttonTable.center().setFillParent(true); // Set table to fill the parent and center-align vertically and horizontally
        buttonTable.pad(0, 20f, 0, 20f); // Adjust the padding to control button placement

        buttonTable.add(prevBtn).left().width(70f).center().padBottom(20f);
        buttonTable.add().expand().center(); // Empty cell for center alignment
        buttonTable.add(skipBtn).expand().top().right().width(200f); // Skip button

// Add the "Next" button to the right-most cell
        buttonTable.add(nextBtn).right().width(70f).center().padBottom(20f).padLeft(20f); // Next button
        buttonTable.row(); // Move to the next row


        // Create a Stack to overlay the text label and buttons
        Stack stack = new Stack();
        stack.setFillParent(true);
        stack.add(rootTable);
        stack.add(buttonTable);

        // Add the stack to the stage
        stage.addActor(stack);
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

    int start = 0;
    int end = 1; // Change this to the number of images you have - 1

    private void nextScene() {
        if (start < end) {
            start += 1;
            currentStoryIndex += 1;
            if (currentStoryIndex < stories.size()) {
                String newStory = stories.get(currentStoryIndex);
                storyLabel.clearActions();
                storyLabel.setText("");
                printTextLetterByLetter(newStory, storyLabel, 0.035f, 0.5f);
            }
            Texture newImageTexture = new Texture(Gdx.files.internal(InitialScreenImages.get(start)));
            backgroundImage.setDrawable(new TextureRegionDrawable(newImageTexture));
        } else {
            new PlanetTravel(game).returnToCurrent();
        }
    }

    private void prevScene() {
        if (start > 0) {
            start -= 1;
            currentStoryIndex -= 1;
            if (currentStoryIndex >= 0) {
                String prevStory = stories.get(currentStoryIndex);
                storyLabel.clearActions();
                storyLabel.setText("");
                printTextLetterByLetter(prevStory, storyLabel, 0.035f, 0.5f);
            }
            Texture prevImageTexture = new Texture(Gdx.files.internal(InitialScreenImages.get(start)));
            backgroundImage.setDrawable(new TextureRegionDrawable(prevImageTexture));
        }
    }

    private void onSkip() {
        logger.info("Skipping to game");
        new PlanetTravel(game).returnToCurrent();
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
