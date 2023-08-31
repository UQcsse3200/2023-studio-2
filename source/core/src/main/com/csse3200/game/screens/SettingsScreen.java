package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.settingsmenu.SettingsMenuDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.assets.AssetManager;

/**
 * The game screen containing the settings.
 */
public class SettingsScreen extends ScreenAdapter {

    private Table table;
    public AssetManager assetManager = new AssetManager();
    private static final Logger logger = LoggerFactory.getLogger(SettingsScreen.class);

    private final GdxGame game;
    private final Renderer renderer;
    private static final String[] Mamta = {"images/settings-image1.png"};

    private Stage stage;

    /**
     * Constructs a new SettingsScreen.
     *
     * @param game The main game instance.
     */
    public SettingsScreen(GdxGame game) {
        this.game = game;
        logger.debug("Initialising settings screen services");
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerTimeSource(new GameTime());
        renderer = RenderFactory.createRenderer();
        renderer.getCamera().getEntity().setPosition(5f, 5f);

        // Set the background color to black

        // Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // (R, G, B, A)

        loadAssets();
        create();
        createUI();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(Mamta);
        ServiceLocator.getResourceService().loadAll();
    }

    public void create() {
        stage = ServiceLocator.getRenderService().getStage();
        Image title =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/settings-image1.png", Texture.class));

        title.setWidth(Gdx.graphics.getWidth());
        title.setHeight(Gdx.graphics.getHeight());
        title.setPosition(0, 0);
        stage.addActor(title);
    }

    @Override
    public void render(float delta) {
        ServiceLocator.getEntityService().update();
        renderer.render();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void dispose() {
        renderer.dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getEntityService().dispose();

        ServiceLocator.clear();
    }

    /**
     * Creates the setting screen's UI including components for rendering UI elements to the screen
     * and capturing and handling UI input.
     */
    private void createUI() {
        logger.debug("Creating UI");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new SettingsMenuDisplay(game)).addComponent(new InputDecorator(stage, 10));
        ServiceLocator.getEntityService().register(ui);
    }
}
