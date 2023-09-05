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
    private static final String SKIN_PATH = "flat-earth/skin/flat-earth-ui.json";
    private static final String MATERIALS_FORMAT = "Materials: %d";
    private final InputOverrideComponent inputOverrideComponent;
    private final Entity upgradeBench;

    private Label materialsLabel;

    public static UpgradeDisplay createUpgradeDisplay(Entity upgradeBench) {
        Texture background =
                ServiceLocator.getResourceService().getAsset("images/upgradetree/background.png", Texture.class);
        background.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        return new UpgradeDisplay(background, upgradeBench);
    }

    public UpgradeDisplay(Texture background, Entity upgradeBench) {
        super("", new Window.WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(background)));

        this.upgradeBench = upgradeBench;

        setupWindowDimensions();

        Label materialsLabel = createMaterialsLabel();
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

    private TextureRegionDrawable createTextureRegionDrawable(String path, float size) {
        TextureRegionDrawable drawable = new TextureRegionDrawable(new Texture(path));
        drawable.setMinSize(size, size);
        return drawable;
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

    private Label createMaterialsLabel() {
        Skin skin = new Skin(Gdx.files.internal(SKIN_PATH));
        String materials = String.format(MATERIALS_FORMAT, upgradeBench.getComponent(UpgradeTree.class).getMaterials());
        this.materialsLabel = new Label(materials, skin);
        materialsLabel.setPosition(MATERIAL_LABEL_X, MATERIAL_LABEL_Y);
        return materialsLabel;
    }

    private Group createUpgradeButtons() {
        Group group = new Group();

        // sorry
        ImageButton meleeOne = createImageButton("images/upgradetree/stick.png", 64f, 534, 512, WeaponType.STICK);
        ImageButton rangedOne = createImageButton("images/wrench.png", 64f, 534 + 204, 512, WeaponType.ELEC_WRENCH);
        ImageButton meleeTwo = createImageButton("images/upgradetree/sword.png", 64f, 534, 512 - 154, WeaponType.KATANA);
        ImageButton rangedTwo = createImageButton("images/wrench.png", 64f, 534 + 204, 512 - 154, WeaponType.THROW_ELEC_WRENCH);
        ImageButton tierThree = createImageButton("images/upgradetree/sword.png", 64f, 534 + 102, 512 - 385, WeaponType.LASERGUN);
        ImageButton hammerOne = createImageButton("images/upgradetree/hammer1.png", 64f, 534 + 2 * 204, 512, WeaponType.WOODHAMMER);
        ImageButton hammerTwo = createImageButton("images/upgradetree/hammer2.png", 64f, 534 + 2 * 204, 512 - 154, WeaponType.STONEHAMMER);

        group.addActor(meleeOne);
        group.addActor(meleeTwo);
        group.addActor(rangedOne);
        group.addActor(rangedTwo);
        group.addActor(tierThree);
        group.addActor(hammerOne);
        group.addActor(hammerTwo);

        return group;
    }

    private ImageButton createImageButton(String imagePath, float size, float posX, float posY, WeaponType weaponType) {
        TextureRegionDrawable drawable = createTextureRegionDrawable(imagePath, size);
        ImageButton button = new ImageButton(drawable);

        UpgradeTree stats = upgradeBench.getComponent(UpgradeTree.class);
        Image lock = null;

        if (!stats.isWeaponUnlocked(weaponType)) {
            lock = new Image(new Texture("images/upgradetree/lock.png"));
            lock.setSize(size, size);
            button.addActor(lock);
        }
        button.setPosition(posX, posY);

        final Image finalLockImage = lock;
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int requiredMaterials = 50;

                // Try to unlock a new weapon
                if (!stats.isWeaponUnlocked(weaponType)) {
                    if (stats.getMaterials() >= requiredMaterials) {
                        stats.subtractMaterials(requiredMaterials);
                        stats.unlockWeapon(weaponType);

                        materialsLabel.setText(String.format("Materials: %d", stats.getMaterials()));

                        if (finalLockImage != null) {
                            finalLockImage.remove();
                        }
                    }
                }
            }
        });

        return button;
    }

    /**
     * Exit the upgrade tree menu.
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
