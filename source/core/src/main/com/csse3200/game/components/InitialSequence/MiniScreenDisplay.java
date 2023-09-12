package com.csse3200.game.components.InitialSequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.GdxGame;
import com.csse3200.game.GdxGame.ScreenType;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MiniScreenDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MiniScreenDisplay.class);
    private static final float textAnimationDuration = 18;
    private final GdxGame game;
    private final float spaceSpeed = 1;
    private final float planetToTextPadding = 150;
    private Image background;
    private Image picture;
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
                                .getAsset("images/start.png", Texture.class));
        background.setPosition(0, 0);
        // Scale the height of the background to maintain the original aspect ratio of the image
        // This prevents distortion of the image.
        float scaledHeight = Gdx.graphics.getWidth() * (background.getHeight() / background.getWidth());
        background.setHeight(scaledHeight);

        // Load the animated planet
        picture =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/start.png", Texture.class));
        picture.setSize(200, 200); // Set to a reasonable fixed size


        float planetOffset = textAnimationDuration * UserSettings.get().fps;
        picture.setPosition((float) Gdx.graphics.getWidth() / 2, planetOffset, Align.center);

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
        stage.addActor(picture);
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
        if (picture.getY(Align.center) >= spaceLabel.getY(Align.top) + planetToTextPadding) {
            picture.setY(picture.getY() - spaceSpeed); // Move the planet
            background.setY(background.getY() - spaceSpeed); // Move the background
        }
        stage.act(ServiceLocator.getTimeSource().getDeltaTime());
    }


    @Override
    public void dispose() {
        rootTable.clear();
        picture.clear();
        background.clear();
        spaceLabel.clear();
        super.dispose();
    }
}
