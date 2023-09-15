package com.csse3200.game.components.upgradetree;

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
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.components.structures.StructureToolPicker;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.entities.configs.WeaponConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.input.InputOverrideComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

/**
 * The UpgradeDisplay class represents a GUI component for displaying upgrades.
 * The display visualizes upgrade trees where each item can be upgraded based on available materials.
 * <p>
 * The class extends the Window class from libGDX to represent a pop-up or overlay menu in the game.
 */
public class UpgradeDisplay extends Window {
    private static final float WINDOW_WIDTH_SCALE = 0.65f;

    private static final float WINDOW_HEIGHT_SCALE = 0.65f;

    private static final float SIZE = 64f;
    private static final String SKIN_PATH = "kenney-rpg-expansion/kenneyrpg.json";
    private static final String MATERIALS_FORMAT = "Materials: %d";
    private final InputOverrideComponent inputOverrideComponent;
    private final Entity upgradeBench;
    private Label materialsLabel;
    private final WeaponConfigs weaponConfigs;
    private final Entity player;
    private UpgradeNode buildRoot;
    private final Skin skin;
    private Stage stage;

    // Tree stuff
    private final List<UpgradeNode> trees = new ArrayList<>();
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private float nodeYSpacing; // 175
    private float nodeXSpacing; // 150

    /**
     * Factory method for creating an instance of UpgradeDisplay.
     *
     * @param upgradeBench The entity representing the upgrade bench in the game.
     * @return A new instance of UpgradeDisplay.
     */
    public static UpgradeDisplay createUpgradeDisplay(Entity upgradeBench) {
        Texture background =
                ServiceLocator.getResourceService().getAsset("images/upgradetree/background.png", Texture.class);
        background.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        return new UpgradeDisplay(background, upgradeBench);
    }

    /**
     * Constructor for UpgradeDisplay.
     *
     * @param background    The texture to be used for the background of the upgrade display.
     * @param upgradeBench  The entity representing the upgrade bench in the game.
     */
    public UpgradeDisplay(Texture background, Entity upgradeBench) {
        super("", new Window.WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(background)));

        this.upgradeBench = upgradeBench;

        skin = new Skin(Gdx.files.internal(SKIN_PATH));
        weaponConfigs = FileLoader.readClass(WeaponConfigs.class, "configs/weapons.json");
        player = ServiceLocator.getEntityService().getPlayer();

        setupWindowDimensions();

        Table materialsTable = createMaterialsLabel();
        Table exitTable = createExitButton();
        Group group = createUpgradeButtons();

        addActor(group);
        addActor(materialsTable);
        addActor(exitTable);

        // Override all normal user input
        inputOverrideComponent = new InputOverrideComponent();
        ServiceLocator.getInputService().register(inputOverrideComponent);

    }

    /**
     * Builds the predefined trees for the melee, ranged, and building items.
     * These trees dictate the progression of weapons that can be unlocked.
     */
    private void buildTrees() {
        // todo: make this less bad
        WeaponConfig meleeWrench = weaponConfigs.GetWeaponConfig(WeaponType.MELEE_WRENCH);
        WeaponConfig katanaConfig = weaponConfigs.GetWeaponConfig(WeaponType.MELEE_KATANA);
        WeaponConfig slingshotConfig = weaponConfigs.GetWeaponConfig(WeaponType.RANGED_SLINGSHOT);
        WeaponConfig boomerangConfig = weaponConfigs.GetWeaponConfig(WeaponType.RANGED_BOOMERANG);
        WeaponConfig woodhammerConfig = weaponConfigs.GetWeaponConfig(WeaponType.WOODHAMMER);
        WeaponConfig stonehammerConfig = weaponConfigs.GetWeaponConfig(WeaponType.STONEHAMMER);
        WeaponConfig rocketConfig = weaponConfigs.GetWeaponConfig(WeaponType.RANGED_HOMING);
        WeaponConfig beeConfig = weaponConfigs.GetWeaponConfig(WeaponType.MELEE_BEE_STING);

        // Melee Tree
        UpgradeNode swordNode = new UpgradeNode(katanaConfig, WeaponType.MELEE_KATANA);
        UpgradeNode wrenchNode = new UpgradeNode(meleeWrench, WeaponType.MELEE_WRENCH);
        UpgradeNode beeNode = new UpgradeNode(beeConfig, WeaponType.MELEE_BEE_STING);
        swordNode.addChild(wrenchNode);
        swordNode.addChild(beeNode);
        trees.add(swordNode);

        // Ranged Tree
        UpgradeNode slingShot = new UpgradeNode(slingshotConfig, WeaponType.RANGED_SLINGSHOT);
        UpgradeNode rocket = new UpgradeNode(rocketConfig, WeaponType.RANGED_HOMING);
        UpgradeNode boomerang = new UpgradeNode(boomerangConfig, WeaponType.RANGED_BOOMERANG);
        boomerang.addChild(slingShot);
        slingShot.addChild(rocket);
        trees.add(boomerang);

        // Build Tree
        buildRoot = new UpgradeNode(woodhammerConfig, WeaponType.WOODHAMMER);
        UpgradeNode hammer2 = new UpgradeNode(stonehammerConfig, WeaponType.STONEHAMMER);
        buildRoot.addChild(hammer2);
        trees.add(buildRoot);
    }

    /**
     * Creates and organizes the upgrade buttons based on the built upgrade trees.
     *
     * @return A group containing all the upgrade buttons.
     */
    private Group createUpgradeButtons() {

        Group group = new Group();

        buildTrees();

        nodeXSpacing = (getWidth() * getScaleX()) / (trees.size() * 2);
        nodeYSpacing = (getHeight() * getScaleY()) / 4;

        for (UpgradeNode treeRoot : trees) {
            float treeX = (trees.indexOf(treeRoot) + 1) * getWidth() * getScaleX() / (trees.size() + 1);
            float startY = getHeight() - (getHeight() / 3);
            createAndPositionNodes(treeRoot, treeX, startY, group, 0);
        }

        return group;
    }

    /**
     *  Positions nodes and creates buttons for them.
     *
     * @param node The current node to position.
     * @param x    The x-coordinate of the node.
     * @param y    The y-coordinate of the node.
     * @param group The group where the buttons will be added.
     */
    private void createAndPositionNodes(UpgradeNode node, float x, float y, Group group, int depth) {

        // base case
        if (node == null) {
            return;
        }

        node.setX(x);
        node.setY(y);
        node.setDepth(depth);

        ImageButton button = createWeaponButtons(node, x, y);
        group.addActor(button);

        int childCount = node.getChildren().size();
        float totalWidth = childCount * nodeXSpacing;
        float currentX = x - totalWidth / 2 + nodeXSpacing / 2;

        for (UpgradeNode child : node.getChildren()) {
            float childX = currentX;
            float childY = y - nodeYSpacing;

            createAndPositionNodes(child, childX, childY, group, depth + 1);
            currentX += nodeXSpacing;
        }
    }


    /**
     * Draws a background box around each upgrade node.
     *
     * @param node The node around which the background will be drawn.
     */
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
        shapeRenderer.rect(node.getX() + offsetX, node.getY() + offsetY, rectSize, rectSize);

        for (UpgradeNode child : node.getChildren()) {
            drawBoxBackground(child);
        }
    }

    /**
     * Draws lines connecting parent nodes to their child nodes.
     *
     * @param node The current node from which lines will be drawn to its children.
     */
    private void drawLines(UpgradeNode node) {
        if (node == null || node.getChildren().isEmpty()) {
            return;
        }

        Vector2 parentPos = localToStageCoordinates(new Vector2(node.getX() + SIZE/2, node.getY() + SIZE/2));

        for (UpgradeNode child : node.getChildren()) {
            Vector2 childPos = localToStageCoordinates(new Vector2(child.getX() + SIZE/2, child.getY() + SIZE/2));
            shapeRenderer.rectLine(parentPos, childPos, 5);

            drawLines(child);
        }
    }

    /**
     * Sets up the window dimensions.
     */
    private void setupWindowDimensions() {
        stage = ServiceLocator.getRenderService().getStage();
        setWidth(stage.getWidth() * WINDOW_WIDTH_SCALE);
        setHeight(stage.getHeight() * WINDOW_HEIGHT_SCALE);
        setPosition(
                stage.getWidth() / 2 - getWidth() / 2 * getScaleX(),
                stage.getHeight() / 2 - getHeight() / 2 * getScaleY());
    }

    /**
     * Creates a TextureRegionDrawable
     *
     * @param path The path of the texture.
     * @param size The size of the resulting drawable.
     * @return The TextureRegionDrawable instance.
     */
    private TextureRegionDrawable createTextureRegionDrawable(String path, float size) {
        TextureRegionDrawable drawable = new TextureRegionDrawable(new Texture(path));
        drawable.setMinSize(size, size);
        return drawable;
    }

    /**
     * Creates an exit button for the upgrade tree display.
     *
     * @return The created exit button.
     */
    private Table createExitButton() {
        TextureRegionDrawable exitTexture = createTextureRegionDrawable("images/upgradetree/exit.png", 100f);
        Button exitButton = new Button(exitTexture);

        Table table = new Table();
        table.add(exitButton);
        table.setPosition(((float) (getWidth() * getScaleX() * 0.98)),
                (float) (getHeight() * getScaleY() * 0.95));

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                exitUpgradeTree();
            }
        });
        return table;
    }

    /**
     * Creates a label showing the available materials for upgrades.
     *
     * @return The created materials label.
     */
    private Table createMaterialsLabel() {
        int materials = upgradeBench.getComponent(UpgradeTree.class).getMaterials();
        String str = String.format(MATERIALS_FORMAT, materials);
        this.materialsLabel = new Label(str, skin);

        Table table = new Table();
        table.add(materialsLabel);
        table.setPosition(((getWidth() * getScaleX() / 2)),
                (float) (getHeight() * getScaleY() * 0.95));

        return table;
    }

    /**
     * Creates a button for a given weapon node.
     *
     * @param node The upgrade node for which the button is being created.
     * @param posX The x-coordinate for the button's position.
     * @param posY The y-coordinate for the button's position.
     * @return The created ImageButton.
     */
    private ImageButton createWeaponButtons(UpgradeNode node, float posX, float posY) {
        TextureRegionDrawable buttonDrawable = createTextureRegionDrawable(node.getImagePath(), UpgradeDisplay.SIZE);
        ImageButton weaponButton = new ImageButton(buttonDrawable);
        weaponButton.setPosition(posX, posY);

        UpgradeTree stats = upgradeBench.getComponent(UpgradeTree.class);

        Image lockImage = lockItem(node, stats, weaponButton);
        TextButton costButton = createCostButtons(node, weaponButton);

        // Create unlock listener for unlock button
        if (costButton != null) {
            costButton.addListener(unlockWeapon(node, stats, weaponButton, lockImage, costButton));
        }

        weaponButton.addListener(equipItem(node));

        return weaponButton;
    }

    public TextButton createCostButtons(UpgradeNode node, ImageButton weaponButton) {

        UpgradeTree stats = upgradeBench.getComponent(UpgradeTree.class);
        // Dont draw cost buttons for unlocked nodes
        if (stats.isWeaponUnlocked(node.getWeaponType())) {
            return null;
        }

        TextButton costButton = new TextButton(String.valueOf(node.getNodeCost()), skin);
        costButton.setSize(SIZE, SIZE / 2);
        costButton.setColor(Color.WHITE);
        costButton.setPosition(
                weaponButton.getX() + weaponButton.getWidth(),
                weaponButton.getY() + weaponButton.getWidth());
        addActor(costButton);

        return costButton;
    }

    /**
     * Draws a lock image on a current node
     *
     * @param node         the current node to lock
     * @param stats        the upgrade tree node statistics
     * @param weaponButton the weapons image button
     * @return the lock image
     */
    private Image lockItem(UpgradeNode node, UpgradeTree stats, ImageButton weaponButton) {
        if (stats.isWeaponUnlocked(node.getWeaponType())) return null;

        Image lock = new Image(new Texture("images/upgradetree/lock.png"));
        lock.setSize(UpgradeDisplay.SIZE, UpgradeDisplay.SIZE);
        weaponButton.addActor(lock);
        weaponButton.setColor(0.5f, 0.5f, 0.5f, 0.5f); // grey out the image

        return lock;
    }

    /**
     * Equips the node item into the players inventory if it is unlocked
     *
     * @param node the node selected
     * @return a change listener
     */
    private ChangeListener equipItem(UpgradeNode node) {
        return new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UpgradeTree stats = upgradeBench.getComponent(UpgradeTree.class);
                if (stats.isWeaponUnlocked(node.getWeaponType())) {
                    InventoryComponent playerInventory = player.getComponent(InventoryComponent.class);
                    playerInventory.placeInSlot(node.getWeaponType());
                }
            }
        };
    }

    /**
     * Unlocks a weapon at a given node
     * @param node the weapon node to unlock
     * @param stats the weapons upgradetree stats
     * @param weaponButton the weapons image button
     * @param lockImage the lock image
     * @return a change listener
     */
    private ChangeListener unlockWeapon(UpgradeNode node, UpgradeTree stats, ImageButton weaponButton, Image lockImage, TextButton costButton) {
        return new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handleWeaponUnlocking(node, stats, weaponButton, lockImage, costButton);
            }
        };
    }

    /**
     * Sets the node item to unlocked
     *
     * @param node the current weapon node to unlock
     * @param stats the weapons upgrade tree stats
     * @param weaponButton the weapons image button
     * @param lockImage the lock image
     */
    private void handleWeaponUnlocking(UpgradeNode node, UpgradeTree stats, ImageButton weaponButton, Image lockImage, TextButton costButton) {

        if (stats.isWeaponUnlocked(node.getWeaponType()) || stats.getMaterials() < node.getNodeCost()) {
            return;
        }

        // Set the node to unlocked
        stats.subtractMaterials(node.getNodeCost());
        stats.unlockWeapon(node.getWeaponType());
        weaponButton.setColor(1f, 1f, 1f, 1f); // un-grey the image
        materialsLabel.setText(String.format(MATERIALS_FORMAT, stats.getMaterials()));

        StructureToolPicker structurePicker = player.getComponent(StructureToolPicker.class);

        // Update the StructurePickers level
        if (buildRoot.getChildren().contains(node) && node.getDepth() == structurePicker.getLevel() + 1) {
            structurePicker.setLevel(node.getDepth());
        }

        // Remove lock and cost
        if (costButton != null && lockImage != null) {
            costButton.remove();
            lockImage.remove();
        }
    }

    /**
     * Exit the upgrade tree menu.
     */
    private void exitUpgradeTree() {
        remove();
    }

    /**
     * Override the removal process to unregister input overrides.
     *
     * @return Returns true if the actor was removed.
     */
    @Override
    public boolean remove() {
        //Stop overriding input when exiting
        ServiceLocator.getInputService().unregister(inputOverrideComponent);
        return super.remove();
    }

    /**
     * Renders the background, the connection lines, and the ImageButtons for the upgrade tree.
     *
     * @param batch       The batch used for rendering.
     * @param parentAlpha The parent alpha value, for transparency.
     * @param x           The x-coordinate of the bottom left corner of the background.
     * @param y           The y-coordinate of the bottom left corner of the background.
     */
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
            drawLines(treeRoot);
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
}
