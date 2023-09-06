package com.csse3200.game.upgradetree;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputOverrideComponent;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ServiceLocator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private static final float SIZE = 64f;
    private static final String SKIN_PATH = "flat-earth/skin/flat-earth-ui.json";
    private static final String MATERIALS_FORMAT = "Materials: %d";
    private final InputOverrideComponent inputOverrideComponent;
    private final Entity upgradeBench;
    private Label materialsLabel;

    // Tree stuff
    private final List<UpgradeNode> trees = new ArrayList<>();
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private static final float VERTICAL_SPACING = 200f;
    private static final float HORIZONTAL_SPACING = 150f;


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

    /**
     * Stores melee, ranged, and building trees inside a tree data structure :)
     */
    private void buildTrees() {
        // todo: reduce imagePath hardcode
        // Melee Tree
        UpgradeNode meleeRoot = new UpgradeNode(WeaponType.STICK, "images/upgradetree/stick.png");
        UpgradeNode swordNode = new UpgradeNode(WeaponType.KATANA, "images/upgradetree/sword.png");
        meleeRoot.addChild(swordNode);
        trees.add(meleeRoot);

        // Ranged Tree
        UpgradeNode rangedRoot = new UpgradeNode(WeaponType.ELEC_WRENCH, "images/wrench.png");
        UpgradeNode wrenchNode = new UpgradeNode(WeaponType.THROW_ELEC_WRENCH, "images/speedpowerup.png");
        UpgradeNode wrenchNode2 = new UpgradeNode(WeaponType.LASERGUN, "images/meteor.png");
        rangedRoot.addChild(wrenchNode);
        rangedRoot.addChild(wrenchNode2);
        wrenchNode2.addChild((new UpgradeNode(WeaponType.SLING_SHOT, "images/meteor.png")));
        wrenchNode2.addChild((new UpgradeNode(WeaponType.SLING_SHOT, "images/meteor.png")));
        trees.add(rangedRoot);

        // Build Tree
        UpgradeNode buildRoot = new UpgradeNode(WeaponType.WOODHAMMER, "images/upgradetree/hammer1.png");
        UpgradeNode hammer2 = new UpgradeNode(WeaponType.STONEHAMMER, "images/upgradetree/hammer2.png");
        UpgradeNode hammer3 = new UpgradeNode(WeaponType.LASERGUN, "images/upgradetree/hammer2.png");
        UpgradeNode x = new UpgradeNode(WeaponType.THROW_ELEC_WRENCH, "images/upgradetree/hammer2.png");
        buildRoot.addChild(hammer2);
        buildRoot.addChild(hammer3);
        hammer2.addChild(x);
        trees.add(buildRoot);
    }

    private Group createUpgradeButtons() {
        Group group = new Group();

        buildTrees();

        for (UpgradeNode treeRoot : trees) {
            float treeX = (trees.indexOf(treeRoot) + 1) * getWidth() / (trees.size() + 1);
            float startY = getHeight() - 200;  // start near the top
            createAndPositionNodes(treeRoot, treeX, startY, group);
        }

        return group;
    }

    private void createAndPositionNodes(UpgradeNode node, float x, float y, Group group) {

        // base case
        if (node == null) {
            return;
        }

        node.x = x;
        node.y = y;


        ImageButton button = createWeaponButtons(node, SIZE, x, y);
        group.addActor(button);

        int childCount = node.children.size();
        float totalWidth = childCount * HORIZONTAL_SPACING;
        float currentX = x - totalWidth / 2 + HORIZONTAL_SPACING / 2;

        for (UpgradeNode child : node.children) {
            float childX = currentX;
            float childY = y - VERTICAL_SPACING;

            createAndPositionNodes(child, childX, childY, group);
            currentX += HORIZONTAL_SPACING;
        }
    }

    private void drawBoxBackground(UpgradeNode node) {
        if (node == null) {
            return;
        }

        // draws an outline around the weapon texture
        float dx = 20;
        float rectSize = SIZE + dx;
        float offsetX = this.getX() - (dx / 2);
        float offsetY = this.getY() - (dx / 2);

        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(node.x + offsetX, node.y + offsetY, rectSize, rectSize);

        for (UpgradeNode child : node.children) {
            drawBoxBackground(child);
        }
    }

    private void drawLines(UpgradeNode node, Batch batch) {
        if (node == null || node.children.isEmpty()) {
            return;
        }

        Vector2 parentPos = localToStageCoordinates(new Vector2(node.x + SIZE/2, node.y + SIZE/2));

        for (UpgradeNode child : node.children) {
            Vector2 childPos = localToStageCoordinates(new Vector2(child.x + SIZE/2, child.y + SIZE/2));
            shapeRenderer.rectLine(parentPos, childPos, 5);

            drawLines(child, batch);
        }
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
        int materials = upgradeBench.getComponent(UpgradeTree.class).getMaterials();

        String str = String.format(MATERIALS_FORMAT, materials);
        this.materialsLabel = new Label(str, skin);
        materialsLabel.setPosition(MATERIAL_LABEL_X, MATERIAL_LABEL_Y);
        return materialsLabel;
    }

    // todo: make this less gross
    private ImageButton createWeaponButtons(UpgradeNode node, float size, float posX, float posY) {
        TextureRegionDrawable drawable = createTextureRegionDrawable(node.imagePath, size);
        ImageButton button = new ImageButton(drawable);

        UpgradeTree stats = upgradeBench.getComponent(UpgradeTree.class);

        Image lock = null;
        if (!stats.isWeaponUnlocked(node.weaponType)) {
            lock = new Image(new Texture("images/upgradetree/lock.png"));
            lock.setSize(size, size);
            button.addActor(lock);
            button.setColor(0.5f,0.5f,0.5f,0.5f);
        }
        button.setPosition(posX, posY);

        final Image finalLockImage = lock;
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int requiredMaterials = 50;  // todo: make each level cost more

                // Try to unlock a new weapon if the node is locked
                if (!stats.isWeaponUnlocked(node.weaponType)) {
                    if (stats.getMaterials() >= requiredMaterials) {
                        stats.subtractMaterials(requiredMaterials);
                        stats.unlockWeapon(node.weaponType);
                        button.setColor(1f,1f,1f,1f);

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

    @Override
    protected void drawBackground(Batch batch, float parentAlpha, float x, float y) {
        // Draw order: Window -> Lines -> ImageButtons
        super.drawBackground(batch, parentAlpha, x, y);

        batch.end();

        // Draws lines connecting parents to children :)
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        for (UpgradeNode treeRoot : trees) {
            drawLines(treeRoot, batch);
        }
        shapeRenderer.end();

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (UpgradeNode treeRoot : trees) {
            drawBoxBackground(treeRoot);
        }
        shapeRenderer.end();

        batch.begin();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
