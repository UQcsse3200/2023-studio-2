package com.csse3200.game.upgradetree;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.csse3200.game.ExtractorMinigameWindow;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputOverrideComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

import java.util.HashMap;

// todo: refactor and reduce hardcode
/**
* A GUI component for the UpgradeTree
*/
public class UpgradeDisplay extends Window {
    private static final float WINDOW_WIDTH_SCALE = 0.8f;
    private static final float WINDOW_HEIGHT_SCALE = 0.65f;
    private static final int MATERIAL_LABEL_X = 50;
    private static final int MATERIAL_LABEL_Y = 650;
    private static final int EXIT_BUTTON_X = 1450;
    private static final int EXIT_BUTTON_Y = 620;
    private final InputOverrideComponent inputOverrideComponent;
    private final Entity upgradeBench;

    public static UpgradeDisplay MakeNewMinigame(Entity upgradeBench) {
        Texture background =
                ServiceLocator.getResourceService().getAsset("images/upgradetree/background.png", Texture.class);
        background.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        return new UpgradeDisplay(background, upgradeBench);
    }

    public UpgradeDisplay(Texture background, Entity upgradeBench) {
        super("", new Window.WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(background)));

        this.upgradeBench = upgradeBench;

        setupWindowDimensions();
        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        Label materialsLabel = createMaterialsLabel(skin);
        Button exitButton = createExitButton();
        Group group = createUpgradeButtons();

        addActor(group);
        addActor(materialsLabel);
        addActor(exitButton);

        // Override all normal user input
        inputOverrideComponent = new InputOverrideComponent();
        ServiceLocator.getInputService().register(inputOverrideComponent);
    }

    private void setupWindowDimensions() {
        Stage stage = ServiceLocator.getRenderService().getStage();
        setWidth(stage.getWidth() * WINDOW_WIDTH_SCALE);
        setHeight(stage.getHeight() * WINDOW_HEIGHT_SCALE);
        setPosition(
                stage.getWidth() / 2 - getWidth() / 2 * getScaleX(),
                stage.getHeight() / 2 - getHeight() / 2 * getScaleY());
    }

    private Button createExitButton() {
        TextureRegionDrawable exitTexture = createTextureRegionDrawable("images/upgradetree/exit.png", 100f);
        Button exitButton = new Button(exitTexture);
        exitButton.setPosition(EXIT_BUTTON_X, EXIT_BUTTON_Y);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                exitUpgradeTree();
            }
        });
        return exitButton;
    }

    private Label createMaterialsLabel(Skin skin) {
        String materials = String.format("Materials: %d", upgradeBench.getComponent(UpgradeTree.class).getMaterials());
        Label materialsLabel = new Label(materials, skin);
        materialsLabel.setPosition(MATERIAL_LABEL_X, MATERIAL_LABEL_Y);
        return materialsLabel;
    }

    private Group createUpgradeButtons() {
        Group group = new Group();

        ImageButton meleeOne = createImageButton("images/upgradetree/stick.png", 64f, 534, 512);
        ImageButton rangedOne = createImageButton("images/wrench.png", 64f, 534 + 204, 512);
        ImageButton meleeTwo = createImageButton("images/upgradetree/sword.png", 64f, 534, 512 - 154);
        ImageButton rangedTwo = createImageButton("images/wrench.png", 64f, 534 + 204, 512 - 154);
        ImageButton tierThree = createImageButton("images/upgradetree/sword.png", 64f, 534 + 102, 512 - 385);
        ImageButton hammerOne = createImageButton("images/upgradetree/hammer1.png", 64f, 534 + 2 * 204, 512);
        ImageButton hammerTwo = createImageButton("images/upgradetree/hammer2.png", 64f, 534 + 2 * 204, 512 - 154);

        group.addActor(meleeOne);
        group.addActor(meleeTwo);
        group.addActor(rangedOne);
        group.addActor(rangedTwo);
        group.addActor(tierThree);
        group.addActor(hammerOne);
        group.addActor(hammerTwo);

        return group;
    }

    private ImageButton createImageButton(String imagePath, float size, float posX, float posY) {
        TextureRegionDrawable drawable = createTextureRegionDrawable(imagePath, size);
        ImageButton button = new ImageButton(drawable);
        button.setPosition(posX, posY);
        return button;
    }

    private TextureRegionDrawable createTextureRegionDrawable(String path, float size) {
        TextureRegionDrawable drawable = new TextureRegionDrawable(new Texture(path));
        drawable.setMinSize(size, size);
        return drawable;
    }

    /**
     * Call this method to exit the upgrade tree menu.
     */
    private void exitUpgradeTree() {
        remove();
    }

    @Override
    public boolean remove() {
        //Stop overriding input when exiting minigame
        ServiceLocator.getInputService().unregister(inputOverrideComponent);
        return super.remove();
    }
}
