package com.csse3200.game.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputOverrideComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;

/**
 * This is a window that can be added to a stage to pop up for the extractor minigame.
 */
public class ExtractorMinigameWindow extends Window {
    private final Entity extractor;
    private final InputOverrideComponent inputOverrideComponent;
    private MouseState currentMouseState = MouseState.DEFAULT;

    private final ArrayList<float[]> firePositions;
    private final ArrayList<float[]> holePositions;
    private int fireLeave = 2;
    private int holeLeave = 2;

    /**
     * Returns a new Minigame window intialised with appropriate background.
     * @param extractor This extractor will be repaired to max health if the minigame is finished correctly.
     * @return New extractor minigame window
     */
    public static ExtractorMinigameWindow makeNewMinigame(Entity extractor) {
        Texture background = ServiceLocator.getResourceService().getAsset("images/minigame/SpaceMiniGameBackground.png", Texture.class);
        background.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        return new ExtractorMinigameWindow(background, extractor);
    }

    public ExtractorMinigameWindow(Texture background, Entity extractor) {
        super("", new Window.WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(background)));

        this.extractor = extractor;
        this.firePositions = new ArrayList<>();
        firePositions.add(new float[]{50, 50});
        firePositions.add(new float[]{50, 450});
        firePositions.add(new float[]{450, 50});
        firePositions.add(new float[]{450, 450});
        this.holePositions = new ArrayList<>();
        holePositions.add(new float[]{240, 230});
        holePositions.add(new float[]{240, 30});
        holePositions.add(new float[]{240, 430});
        holePositions.add(new float[]{40, 230});
        holePositions.add(new float[]{440, 230});

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
                Image extractorImage = new Image(new Texture(Gdx.files.internal("images/minigame/extractor.png")));
                float x = col * cellSize;
                float y = row * cellSize;
                extractorImage.setPosition(x, y);
                imageTable.addActor(extractorImage);
            }
        }

        // put extinguisher and spanner
        Image extinguisherImage = new Image(new Texture(Gdx.files.internal("images/minigame/extinguisher.png")));// TODO: change to extinguisher.png
        extinguisherImage.setPosition(-300, 400);
        imageTable.addActor(extinguisherImage);
        extinguisherImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentMouseState = MouseState.EXTINGUISHER;
                Pixmap extinguisherPixmap = new Pixmap(Gdx.files.internal("images/minigame/extinguisherCursor.png"));// TODO: change to extinguisherCursor.png
                Gdx.graphics.setCursor(Gdx.graphics.newCursor(extinguisherPixmap, 0, 0));
            }
        });
        Image spannerImage = new Image(new Texture(Gdx.files.internal("images/minigame/spanner.png")));// TODO: change to spanner.png
        spannerImage.setPosition(600, 400);
        imageTable.addActor(spannerImage);
        spannerImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentMouseState = MouseState.SPANNER;
                Pixmap spannerPixmap = new Pixmap(Gdx.files.internal("images/minigame/spannerCursor.png"));// TODO: change to spannerCursor.png
                Gdx.graphics.setCursor(Gdx.graphics.newCursor(spannerPixmap, 0, 0));
            }
        });

        // put fire and holes
        for (int i = 0; i < fireLeave; i++) {
            int fireIndex = MathUtils.random(0, firePositions.size() - 1);
            float[] randomPosition = firePositions.get(fireIndex);
            createFire(randomPosition[0], randomPosition[1], imageTable);
            firePositions.remove(fireIndex);
        }

        for (int i = 0; i < holeLeave; i++) {
            int holeIndex = MathUtils.random(0, holePositions.size() - 1);
            float[] randomPosition = holePositions.get(holeIndex);
            createHole(randomPosition[0], randomPosition[1], imageTable);
            holePositions.remove(holeIndex);
        }
        add(imageTable).fill();

        Skin skin = new Skin(Gdx.files.internal("kenney-rpg-expansion/kenneyrpg.json"));
        TextButton button2 = new TextButton("Exit", skin);
        button2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                failMinigame();
            }
        });
        Table buttonTable = new Table();
        buttonTable.add(button2).pad(10);

        // Add the buttonTable to the window
        add(buttonTable).padTop(20).expandY().top().row();
    }

    private void createFire(float x, float y, Table imageTable) {
        Image fireImage = new Image(new Texture(Gdx.files.internal("images/minigame/fire.png")));
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
                        fireLeave -= 1;
                        if (fireLeave == 0 && holeLeave == 0) {
                            succeedMinigame();
                        }
                        break;
                    case SPANNER:
                        Image bangImage = new Image(new Texture(Gdx.files.internal("images/minigame/bang.png")));
                        bangImage.setPosition(fireImage.getX(), fireImage.getY());
                        bangImage.setScale(0.7f);
                        imageTable.addActor(bangImage);
                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                failMinigame();
                            }
                        }, 1);
                }
            }
        });
    }

    private void createHole(float x, float y, Table imageTable) {
        Image holeImage = new Image(new Texture(Gdx.files.internal("images/minigame/Hole.png")));
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
                        holeLeave -= 1;
                        if (holeLeave == 0 && fireLeave == 0) {
                            succeedMinigame();
                        }
                        break;
                    case EXTINGUISHER:
                        Image bangImage = new Image(new Texture(Gdx.files.internal("images/minigame/bang.png")));
                        bangImage.setPosition(holeImage.getX(), holeImage.getY());
                        bangImage.setScale(0.7f);
                        imageTable.addActor(bangImage);
                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                failMinigame();
                            }
                        }, 1);
                }
            }
        });
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
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        return super.remove();
    }

    private enum MouseState {
        DEFAULT,
        EXTINGUISHER,
        SPANNER
    }
}

