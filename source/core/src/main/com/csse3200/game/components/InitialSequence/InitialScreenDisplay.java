package com.csse3200.game.components.InitialSequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.GdxGame;
import com.csse3200.game.GdxGame.ScreenType;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

public class InitialScreenDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(InitialScreenDisplay.class);
    private final GdxGame game;

    private static float textAnimationDuration = 18;
    private float spaceSpeed = 1;
    private float planetToTextPadding = 150;
    private Image background;
    private Image planet;
    private Table rootTable;
    private TypingLabel storyLabel;

    public InitialScreenDisplay(GdxGame game) {
        super();
        this.game = game;
    }

    @Override
    public void create() {
        super.create();
        addUIElements();
    }

    private void addUIElements() {
        // Load the background starfield image.
        background =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/InitialScreenImage.png", Texture.class));
        background.setPosition(0, 0);
        // Scale the height of the background to maintain the original aspect ratio of the image
        // This prevents distortion of the image.
        float scaledHeight = Gdx.graphics.getWidth() * (background.getHeight() / background.getWidth());
        background.setHeight(scaledHeight);

        // Load the animated planet
        planet =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/InitialScreenBG.png", Texture.class));
        planet.setSize(200, 200); // Set to a reasonable fixed size

        // The planet moves at a constant speed, so to make it appear at the right time,
        // it is to be placed at the right y coordinate above the screen.
        // The height is informed by the length of the text animation and the game's target FPS.
        float planetOffset = textAnimationDuration * UserSettings.get().fps;
        planet.setPosition((float) Gdx.graphics.getWidth() / 2, planetOffset, Align.center);

        // The {TOKENS} in the String below are used by TypingLabel to create the requisite animation effects
        String story = """
            {WAIT=0.5}
            Earth has become a desolate wasteland ravaged by a deadly virus. Civilisation as we know  has crumbled, and humanity's last hope lies among the stars.
            
            {WAIT}
            You are one of the few survivors who have managed to secure a spot on a spaceship built with the hopes of finding a cure or a new home on distant planets.
            {WAIT=0.5}
            
            The spaceship belongs to Dr Emily Carter, a brilliant scientist determined to find a cure for the virus and make the earth habitable again.
            {WAIT}
            
            But the cosmos is a vast and dangerous place, filled with unknown challenges and mysteries, from alien encounters to unexpected phenomena.
            {WAIT}
            
            {WAIT=0.1}
            
            {COLOR=red}Your journey begins now as you board the spaceship \"Aurora\" and venture into the unknown."{WAIT=1}
            """;
        storyLabel = new TypingLabel(story, skin); // Create the TypingLabel with the formatted story
        // Reduce the animation speed of all text in the story.
        String defaultTokens = "{SLOWER}";
        storyLabel.setDefaultToken(defaultTokens);
        storyLabel.setAlignment(Align.center); // Center align the text

        TextButton continueButton = new TextButton("Continue", skin);

        // The continue button lets the user proceed to the main game
        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Continue button clicked");
                game.setScreen(ScreenType.GAME_STORY);
            }
        });

        rootTable = new Table();
        rootTable.setFillParent(true); // Make the table fill the screen

        rootTable.row();

        rootTable.add(storyLabel).expandX().center().padTop(150f); // The story label is at the top of the screen
        rootTable.row().padTop(30f);
        rootTable.add(continueButton).bottom().padBottom(30f);

        // The background and planet are added directly to the stage so that they can be moved and animated freely.
        stage.addActor(background);
        stage.addActor(planet);
        stage.addActor(rootTable);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // Drawing is handled by the stage
    }

    @Override
    public void update() {
        // This movement logic is triggered on every frame until the middle of the planet hits its target position on the screen.
        if (planet.getY(Align.center) >= storyLabel.getY(Align.top) + planetToTextPadding) {
            planet.setY(planet.getY() - spaceSpeed); // Move the planet
            background.setY(background.getY() - spaceSpeed); // Move the background
        }
        stage.act(ServiceLocator.getTimeSource().getDeltaTime());
    }


    @Override
    public void dispose() {
        rootTable.clear();
        planet.clear();
        background.clear();
        storyLabel.clear();
        super.dispose();
    }
}