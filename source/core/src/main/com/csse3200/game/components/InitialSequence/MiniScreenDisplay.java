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

public class MiniScreenDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MiniScreenDisplay.class);
    private final GdxGame game;

    private static float textAnimationDuration = 18;
    private float spaceSpeed = 1;
    private float planetToTextPadding = 150;
    private Image background;
    private Image planet;
    private Table rootTable;
    private TypingLabel spaceLabel;

    public MiniScreenDisplay(GdxGame game) {
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
                                .getAsset("images/earth_design.png", Texture.class));
        background.setPosition(0, 0);
        // Scale the height of the background to maintain the original aspect ratio of the image
        // This prevents distortion of the image.
        float scaledHeight = Gdx.graphics.getWidth() * (background.getHeight() / background.getWidth());
        background.setHeight(scaledHeight);

        // Load the animated planet
        planet =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/earth_design.png", Texture.class));
        planet.setSize(200, 200); // Set to a reasonable fixed size

        // The planet moves at a constant speed, so to make it appear at the right time,
        // it is to be placed at the right y coordinate above the screen.
        // The height is informed by the length of the text animation and the game's target FPS.
        float planetOffset = textAnimationDuration * UserSettings.get().fps;
        planet.setPosition((float) Gdx.graphics.getWidth() / 2, planetOffset, Align.center);

        // The {TOKENS} in the String below are used by TypingLabel to create the requisite animation effects
        String space = """
             3
             2
             1
             """;
        spaceLabel = new TypingLabel(space, skin);
        String defaultTokens = "{SLOWER}";
        spaceLabel.setDefaultToken(defaultTokens);
        spaceLabel.setAlignment(Align.center);

        TextButton LaunchMissionButton = new TextButton("Launch Mission", skin);


        LaunchMissionButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("LaunchMission button clicked");
                startGame();
            }
        });

        rootTable = new Table();
        rootTable.setFillParent(true);

        rootTable.row();

        rootTable.add(spaceLabel).expandX().center().padTop(250f);
        rootTable.row().padTop(30f);
        rootTable.add(LaunchMissionButton).bottom().padBottom(30f);


        stage.addActor(background);
        stage.addActor(planet);
        stage.addActor(rootTable);
    }

    private void startGame() {
        game.setScreen(ScreenType.SPACE_MAP);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // Drawing is handled by the stage
    }

    @Override
    public void update() {
        // This movement logic is triggered on every frame until the middle of the planet hits its target position on the screen.
        if (planet.getY(Align.center) >= spaceLabel.getY(Align.top) + planetToTextPadding) {
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
        spaceLabel.clear();
        super.dispose();
    }
}