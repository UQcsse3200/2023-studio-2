package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.initialsequence.InitialScreenDisplay;
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

import java.util.ArrayList;

/**
 * A customizable screen for displaying images and text.
 */
public class InitialScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(InitialScreen.class);

    private final GdxGame game;
    private final Renderer renderer;
    private ArrayList<String> assetPaths;
    private ArrayList<String> textList;


    /**
     * Creates a new instance of the CustomizableScreen.
     *
     * @param game        The main game instance.
     */
    public InitialScreen(GdxGame game) {

        this.game = game;
        logger.debug("Initializing screen services");
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerTimeSource(new GameTime());

        renderer = RenderFactory.createRenderer();
        renderer.getCamera().getEntity().setPosition(5f, 5f);
        mainScreenText();
        loadAssets();
        createUI();
    }

    public void setinitialscreen(ArrayList<String> assetPaths, ArrayList<String> textList){
        this.assetPaths = assetPaths;
        this.textList = textList;
    }

    private void mainScreenText(){
        ArrayList<String> stories = new ArrayList<>();
        stories.add( "Amidst Earth's ruins, you stand, humanity's last hope in the cosmic expanse." );
        stories.add("Meet Dr. Emily Carter, a fellow Scientist who remains untouched by the virus.");
        stories.add("With courage intertwined, you both embrace the unknown, a symbiotic bond on this journey.");
        stories.add("Behold Astro's spacecraft, a sentinel of possibility, ready to explore the celestial unknown.");
        stories.add("Step into destiny's vessel, as you and Emily set course for the uncharted cosmic domain.");

        ArrayList<String> initialScreenImages = new ArrayList<>();
        initialScreenImages.add("images/menu/InitialScreenImage.png");
        initialScreenImages.add("images/menu/InitialScreenImage-2.png");
        initialScreenImages.add("images/menu/InitialScreenImage-3.png");
        initialScreenImages.add("images/menu/InitialScreenImage-4.png");
        initialScreenImages.add("images/menu/InitialScreenImage-5.png");

       setinitialscreen(initialScreenImages,stories);
    }

    @Override
    public void render(float delta) {
        ServiceLocator.getEntityService().update();
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void dispose() {
        unloadAssets();
        renderer.dispose();
        ServiceLocator.clear();
    }

    /**
     * Load the image textures required for this screen into memory.
     */
    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(assetPaths.toArray(new String[0]));
        ServiceLocator.getResourceService().loadAll();
    }

    /**
     * Remove the loaded image textures from the ResourceService, and thus game memory.
     */
    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(assetPaths.toArray(new String[0]));
    }

    /**
     * Creates the screen's UI, including components for rendering UI elements to the screen
     * and capturing and handling UI input.
     */
    private void createUI() {
        logger.debug("Creating UI");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new InitialScreenDisplay(game,assetPaths,textList))
                .addComponent(new InputDecorator(stage, 10));
        ServiceLocator.getEntityService().register(ui);
    }

}
