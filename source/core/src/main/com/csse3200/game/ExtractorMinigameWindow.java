package com.csse3200.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.areas.ExtractorMiniGameArea;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.gamearea.PerformanceDisplay;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.components.maingame.MainGameExitDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.entities.factories.StructureFactory;
import com.csse3200.game.input.*;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.screens.ExtractorMiniGameScreen;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.terminal.Terminal;
import com.csse3200.game.ui.terminal.TerminalDisplay;

/**
 * This is a window that can be added to a stage to pop up for the extractor minigame.
 */
public class ExtractorMinigameWindow extends Window {
    private final Entity extractor;
    private final InputOverrideComponent inputOverrideComponent;
    private MouseState currentMouseState = MouseState.DEFAULT;

    /**
     * Returns a new Minigame window intialised with appropriate background.
     * @param extractor This extractor will be repaired to max health if the minigame is finished correctly.
     * @return New extractor minigame window
     */
    public static ExtractorMinigameWindow MakeNewMinigame(Entity extractor) {
        Texture background = ServiceLocator.getResourceService().getAsset("images/SpaceMiniGameBackground.png", Texture.class);
        background.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        return new ExtractorMinigameWindow(background, extractor);
    }

    public ExtractorMinigameWindow(Texture background, Entity extractor) {
        super("", new Window.WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(background)));

        this.extractor = extractor;

        //Here setup window to be centered on the stage with 80% width and 65% height.
        Stage stage = ServiceLocator.getRenderService().getStage();
        setWidth((float) (stage.getWidth()*0.8));
        setHeight((float) (stage.getHeight()*0.65));
        setPosition(stage.getWidth()/2 - getWidth()/2 * getScaleX(), stage.getHeight()/2 - getHeight()/2 * getScaleY());

        inputOverrideComponent = new InputOverrideComponent();
        ServiceLocator.getInputService().register(inputOverrideComponent);

        Table imageTable = new Table();

        float cellSize = 200; // Size of each cell in the grid

        // put extractors grid
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Image extractorImage = new Image(new Texture(Gdx.files.internal("images/elixir_collector.png")));
                float x = col * cellSize;
                float y = row * cellSize;
                extractorImage.setPosition(x, y);
                imageTable.addActor(extractorImage);
            }
        }

        // put extinguisher and spanner
        Image extinguisherImage = new Image(new Texture(Gdx.files.internal("images/extinguisher.png")));// TODO: change to extinguisher.png
        extinguisherImage.setPosition(-300, 400);
        imageTable.addActor(extinguisherImage);
        extinguisherImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentMouseState = MouseState.EXTINGUISHER;
                Pixmap extinguisherPixmap = new Pixmap(Gdx.files.internal("images/extinguisherCursor.png"));// TODO: change to extinguisherCursor.png
                Gdx.graphics.setCursor(Gdx.graphics.newCursor(extinguisherPixmap, 0, 0));
            }
        });
        Image spannerImage = new Image(new Texture(Gdx.files.internal("images/spanner.png")));// TODO: change to spanner.png
        spannerImage.setPosition(600, 400);
        imageTable.addActor(spannerImage);
        spannerImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentMouseState = MouseState.SPANNER;
                Pixmap spannerPixmap = new Pixmap(Gdx.files.internal("images/spannerCursor.png"));// TODO: change to spannerCursor.png
                Gdx.graphics.setCursor(Gdx.graphics.newCursor(spannerPixmap, 0, 0));
            }
        });

        // put fire and holes
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if ((row == 0 && (col == 0 || col == 2)) || (row == 2 && (col == 0 || col == 2))) {
                    Image fireImage = new Image(new Texture(Gdx.files.internal("images/fire.png")));
                    fireImage.setName("fire");
                    float x = col * cellSize + 50;
                    float y = row * cellSize + 50;
                    fireImage.setScale(0.5f);
                    fireImage.setPosition(x, y);
                    imageTable.addActor(fireImage);
                    // Attach a click listener to make the fire disappear when clicked
                    fireImage.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            switch (currentMouseState) {
                                case DEFAULT:
                                    break;
                                case EXTINGUISHER:
                                    fireImage.remove();
                                    break;
                                case SPANNER:
                                    failMinigame();
                            }
                        }
                    });
                } else {
                    Image holeImage = new Image(new Texture(Gdx.files.internal("images/Hole.png")));
                    holeImage.setName("hole");
                    float x = col * cellSize + 40;
                    float y = row * cellSize + 30;
                    holeImage.setPosition(x, y);
                    holeImage.setScale(0.7f);
                    imageTable.addActor(holeImage);
                    // Attach a click listener to make the fire disappear when clicked
                    holeImage.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            switch (currentMouseState) {
                                case DEFAULT:
                                    break;
                                case SPANNER:
                                    holeImage.remove();
                                    break;
                                case EXTINGUISHER:
                                    failMinigame();
                            }
                        }
                    });
                }
            }
        }
        add(imageTable).fill();

        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        TextButton button = new TextButton("Complete Minigame", skin);
        TextButton button2 = new TextButton("Exit Minigame", skin);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for (Actor stageActor : imageTable.getChildren()) {
                    if (stageActor instanceof Image && (stageActor.getName() != null)) {
                        failMinigame();
                        return;
                    }
                }
                succeedMinigame();
            }
        });
        button2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                failMinigame();
            }
        });
        Table buttonTable = new Table();
        buttonTable.add(button).pad(10);
        buttonTable.add(button2).pad(10);

        // Add the buttonTable to the window
        add(buttonTable).padTop(20).expandY().top().row();
    }

    /**
     * Call this method to exit the minigame without repairing the extractor.
     */
    private void failMinigame() {
        remove();
    }

    /**
     * Call this method to exit the minigame and repair the extractors health
     */
    private void succeedMinigame() {
        CombatStatsComponent extractorHealth = extractor.getComponent(CombatStatsComponent.class);
        extractorHealth.setHealth(extractorHealth.getMaxHealth());
        remove();
    }

    @Override
    public boolean remove() {
        //Stop overriding input when exiting minigame
        ServiceLocator.getInputService().unregister(inputOverrideComponent);
        return super.remove();
    }

    private enum MouseState {
        DEFAULT,
        EXTINGUISHER,
        SPANNER
    }
}

