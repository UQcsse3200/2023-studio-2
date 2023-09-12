package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.spacenavigation.NavigationBackground;
import com.csse3200.game.services.PlanetTravel;

/**
 * Represents the navigation screen for the game, allowing the user to navigate
 * between various planets and options.
 */
public class SpaceNavigationScreen implements Screen {
    /**
     * Reference to the main game instance
     */
    private final GdxGame game;
    /**
     * Textures for the planets shown on the screen
     */
    private final Texture[] planetTextures = new Texture[9];
    /**
     * Textures for the arrows on the screen
     */
    private final Texture[] arrowTextures = new Texture[4];
    /**
     * Names of the planets
     */
    private final String[] planetNames = {"Verdant Haven", "Chronos", "Rusterra", "Pyroterra", "Crimsona", "Novara", "Pyralis", "Luminae", "Aquelar"};
    private final PlanetTravel planetTravel;
    /**
     * Stage where all actors will be drawn
     */
    private Stage stage;
    /**
     * Skin for the UI elements
     */
    private Skin skin;
    /**
     * Texture for the title of this screen
     */
    private Texture navigationTitle;

    /**
     * Constructs a new SpaceNavigationScreen with a reference to the main game.
     *
     * @param game The main game instance.
     */
    public SpaceNavigationScreen(GdxGame game) {
        this.game = game;
        this.planetTravel = new PlanetTravel(game);
    }

    /**
     * Invoked when this screen becomes the current screen.
     */
    @Override
    public void show() {
        // First load textures
        navigationTitle = new Texture(Gdx.files.internal("images/space_navigation_title.png"));

        // Planet icons from:
        // https://www.freepik.com/premium-vector/pixel-planets-set-pixel-art-solar-system_36179935.htm
        for (var i = 0; i < planetTextures.length; i++) {
            planetTextures[i] = new Texture(Gdx.files.internal("images/space_navigation_planet_" + i + ".png"));
        }

        arrowTextures[0] = new Texture(Gdx.files.internal("images/space_navigation_arrow_left.png"));
        arrowTextures[1] = new Texture(Gdx.files.internal("images/space_navigation_arrow_right.png"));
        arrowTextures[2] = new Texture(Gdx.files.internal("images/space_navigation_arrow_up.png"));
        arrowTextures[3] = new Texture(Gdx.files.internal("images/space_navigation_arrow_down.png"));


        // Initialise a stage for the scene
        stage = new Stage(new ScreenViewport());


        // Animated background
        NavigationBackground animatedBackground = new NavigationBackground();
        stage.addActor(animatedBackground);

        // Create Back button
        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        TextButton button = new TextButton("Main Menu", skin);
        button.setPosition(Gdx.graphics.getWidth() - (button.getWidth() + 20),
                Gdx.graphics.getHeight() - (button.getHeight() + 20));

        button.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(GdxGame.ScreenType.MAIN_MENU);
            }
        });

        stage.addActor(button);

        // Add title
        Image navigationTitleImage = new Image(navigationTitle);
        navigationTitleImage.setOrigin(navigationTitleImage.getImageWidth() / 2, navigationTitleImage.getImageHeight() / 2);
        navigationTitleImage.setScaling(Scaling.fit);

        Table titleTable = new Table();
        titleTable.top().padTop(10);
        titleTable.setFillParent(true);
        titleTable.add(navigationTitleImage).width((float) Gdx.graphics.getWidth() / 4).align(Align.top).padTop(30);

        stage.addActor(titleTable);

        // Add planets
        spawnPlanets(stage);

        // register input processor
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Spawns planets on the stage, arranging them in a predefined grid pattern.
     *
     * @param stage The stage on which to add planets.
     */
    private void spawnPlanets(Stage stage) {
        int numCols = 5;
        int planetSize = (Gdx.graphics.getHeight() / 2) / numCols;
        int arrowSize = (Gdx.graphics.getHeight() / 3) / numCols;
        int arrowPadding = 20;

        // Create the table of planets
        Table table = new Table();
        table.setFillParent(true); // Make it fill the screen
        table.bottom(); // Align the table to the bottom and center it
        table.padBottom(planetSize); // Add padding

        // LibGDX's Scene2D ui framework is imperative, so we have to explicitly define the planet grid:
        // Row 1
        table.add(createPlanetTable(0, planetSize));
        table.add(createArrow("right")).pad(arrowPadding).width(arrowSize).height(arrowSize);
        table.add(createPlanetTable(1, planetSize));
        table.add(createArrow("right")).pad(arrowPadding).width(arrowSize).height(arrowSize);
        table.add(createPlanetTable(2, planetSize));

        // Row 2: down arrow row
        table.row();
        table.add().width(planetSize * 4).height(planetSize).colspan(4);
        Image rowTwoArrow = createArrow("down");
        table.add(rowTwoArrow).width(arrowSize).height(arrowSize);

        // Row 3
        table.row();
        table.add(createPlanetTable(3, planetSize));
        table.add(createArrow("left")).pad(arrowPadding).width(arrowSize).height(arrowSize);
        table.add(createPlanetTable(4, planetSize));
        table.add(createArrow("left")).pad(arrowPadding).width(arrowSize).height(arrowSize);
        table.add(createPlanetTable(5, planetSize));

        // Row 4: down arrow row
        table.row();
        Image rowFourArrow = createArrow("down");
        table.add(rowFourArrow).width(arrowSize).height(arrowSize);
        table.add().width(planetSize * 4).height(planetSize).colspan(4);

        // Row 5
        table.row();
        table.add(createPlanetTable(6, planetSize));
        table.add(createArrow("right")).pad(arrowPadding).width(arrowSize).height(arrowSize);
        table.add(createPlanetTable(7, planetSize));
        table.add(createArrow("right")).pad(arrowPadding).width(arrowSize).height(arrowSize);
        table.add(createPlanetTable(8, planetSize));

        // Add the populated table to the Scene2D stage
        stage.addActor(table);
    }

    /**
     * Creates an arrow image pointing in the specified direction.
     *
     * @param direction The direction in which the arrow should point.
     * @return An Image instance of the arrow.
     */
    private Image createArrow(String direction) {
        Texture arrowTexture = switch (direction) {
            case "left" -> arrowTextures[0];
            case "right" -> arrowTextures[1];
            case "up" -> arrowTextures[2];
            case "down" -> arrowTextures[3];
            default -> arrowTextures[1]; // Default to right arrow
        };

        return new Image(arrowTexture);
    }

    /**
     * Creates a label with the given text.
     *
     * @param text Text to display on the label.
     * @return A new Label instance.
     */
    private Label createLabel(String text) {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("thick_white");
        return new Label(text, labelStyle);
    }

    /**
     * Creates a table for a specific planet which includes its image and name.
     *
     * @param planetIndex Index of the planet in the planetTextures array.
     * @param planetSize  Size for displaying the planet image.
     * @return A new Table instance containing the planet image and its name.
     */
    private Table createPlanetTable(int planetIndex, int planetSize) {
        Table planetTable = new Table();
        planetTable.top();

        Image planetImage = createPlanet(planetIndex);
        planetTable.add(planetImage).width(planetSize).height(planetSize).expand().fill().row();

        planetTable.add(createLabel(planetNames[planetIndex])).padTop(5);

        return planetTable;
    }

    /**
     * Creates an image of a planet.
     *
     * @param planetIndex Index of the planet in the planetTextures array.
     * @return A new Image instance for the planet.
     */
    private Image createPlanet(int planetIndex) {
        Texture planetTexture = planetTextures[planetIndex];
        Image planet = new Image(planetTexture);

        // Add planet event listeners
        planet.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Currently there is only one planet/map.
                // For now, clicking any planet will take users to the one available map.
                // When new maps are made, they should b integrated here.
                planetTravel.beginPlanetTransition(planetNames[planetIndex]);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                planet.setScale(1.1f); // scale up by 10% on hover
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                planet.setScale(1f); // scale back to original size
            }
        });

        return planet;
    }

    /**
     * Renders the screen.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * Resizes the viewport dimensions based on the given width and height.
     *
     * @param width  The new width.
     * @param height The new height.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Method called when the game is paused. Currently empty to prevent pausing on the space map.
     */
    @Override
    public void pause() {
        // Blank to prevent pausing on the space map
    }

    /**
     * Method called when the game is resumed after pausing. Currently empty as there is no pausing on the space map.
     */
    @Override
    public void resume() {
        // Left blank as there is no pausing on the space map
    }

    /**
     * Method called when this screen is no longer the current screen for the game.
     * Currently left blank to avoid any unwanted behavior.
     */
    @Override
    public void hide() {
        // Left blank as unwanted behaviour
    }

    /**
     * Frees up resources used by this screen.
     */
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        navigationTitle.dispose();
        for (Texture texture : planetTextures) {
            texture.dispose();
        }
        for (Texture texture : arrowTextures) {
            texture.dispose();
        }
    }
}