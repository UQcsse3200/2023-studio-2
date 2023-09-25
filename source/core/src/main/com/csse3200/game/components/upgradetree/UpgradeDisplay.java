package com.csse3200.game.components.upgradetree;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.components.structures.StructureToolPicker;
import com.csse3200.game.components.structures.ToolConfig;
import com.csse3200.game.components.structures.ToolsConfig;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.entities.configs.WeaponConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.input.InputOverrideComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.HashMap;
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
    private static final String MATERIALS_FORMAT = "%d";
    private final InputOverrideComponent inputOverrideComponent;
    private final Entity upgradeBench;
    private Label materialsLabel;
    private final WeaponConfigs weaponConfigs;
    private final Entity player;
    private final Skin skin;
    private final ToolsConfig structureTools;
    private final StructureToolPicker structurePicker;
    private final List<UpgradeNode> trees = new ArrayList<>();
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private float nodeYSpacing;
    private float nodeXSpacing;

    /**
     * Factory method for creating an instance of UpgradeDisplay.
     *
     * @return A new instance of UpgradeDisplay.
     */
    public static UpgradeDisplay createUpgradeDisplay() {
        Texture background =
                ServiceLocator.getResourceService().getAsset("images/upgradetree/background.png", Texture.class);
        background.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        return new UpgradeDisplay(background);
    }

    /**
     * Constructor for UpgradeDisplay.
     *
     * @param background    The texture to be used for the background of the upgrade display.
     */
    public UpgradeDisplay(Texture background) {
        super("", new Window.WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(background)));

        skin = new Skin(Gdx.files.internal("kenney-rpg-expansion/kenneyrpg.json"));
        weaponConfigs = FileLoader.readClass(WeaponConfigs.class, "configs/weapons.json");
        structureTools = FileLoader.readClass(ToolsConfig.class, "configs/structure_tools.json");
        player = ServiceLocator.getEntityService().getPlayer();
        structurePicker = player.getComponent(StructureToolPicker.class);
        this.upgradeBench = player;

        // todo: remove this testing line - just gives max resources in upgrade tree
        upgradeBench.getComponent(UpgradeTree.class).subtractMaterials(-1000);

        setupWindowDimensions();


        Table titleTable = createTitleTable();
        Table materialsTable = createMaterialsLabel();
        Table exitTable = createExitButton();
        Group group = createUpgradeButtons();
        addActor(group);
        addActor(materialsTable);
        addActor(exitTable);
        addActor(titleTable);

        // Override all normal user input
        inputOverrideComponent = new InputOverrideComponent();
        ServiceLocator.getInputService().register(inputOverrideComponent);
    }

    /**
     * Builds the predefined trees for the melee, ranged, and building items.
     * These trees dictate the progression of weapons that can be unlocked.
     */
    private void buildTrees() {
        // Initialise node configs
        WeaponConfig meleeWrench = weaponConfigs.GetWeaponConfig(WeaponType.MELEE_WRENCH);
        WeaponConfig katanaConfig = weaponConfigs.GetWeaponConfig(WeaponType.MELEE_KATANA);
        WeaponConfig slingshotConfig = weaponConfigs.GetWeaponConfig(WeaponType.RANGED_SLINGSHOT);
        WeaponConfig boomerangConfig = weaponConfigs.GetWeaponConfig(WeaponType.RANGED_BOOMERANG);
        WeaponConfig woodhammerConfig = weaponConfigs.GetWeaponConfig(WeaponType.WOODHAMMER);
        WeaponConfig rocketConfig = weaponConfigs.GetWeaponConfig(WeaponType.RANGED_HOMING);
        WeaponConfig beeConfig = weaponConfigs.GetWeaponConfig(WeaponType.MELEE_BEE_STING);
        ToolConfig dirtConfig = structureTools.toolConfigs
                .get("com.csse3200.game.components.structures.tools.BasicWallTool");
        ToolConfig gateConfig = structureTools.toolConfigs
                .get("com.csse3200.game.components.structures.tools.GateTool");
        ToolConfig stoneConfig = structureTools.toolConfigs
                .get("com.csse3200.game.components.structures.tools.IntermediateWallTool");
        ToolConfig turretConfig = structureTools.toolConfigs
                .get("com.csse3200.game.components.structures.tools.TurretTool");

        // Melee Tree
        UpgradeNode swordNode = new UpgradeNode(katanaConfig);
        UpgradeNode wrenchNode = new UpgradeNode(meleeWrench);
        UpgradeNode beeNode = new UpgradeNode(beeConfig);
        swordNode.addChild(wrenchNode);
        swordNode.addChild(beeNode);
        trees.add(swordNode);

        // Ranged Tree
        UpgradeNode slingShotNode = new UpgradeNode(slingshotConfig);
        UpgradeNode rocketNode = new UpgradeNode(rocketConfig);
        UpgradeNode boomerangNode = new UpgradeNode(boomerangConfig);
        boomerangNode.addChild(slingShotNode);
        slingShotNode.addChild(rocketNode);
        trees.add(boomerangNode);

        // Build Tree
        UpgradeNode buildRoot;
        UpgradeNode dirtNode = new UpgradeNode(dirtConfig);
        UpgradeNode gateNode = new UpgradeNode(gateConfig);
        UpgradeNode stoneNode = new UpgradeNode(stoneConfig);
        UpgradeNode turretNode = new UpgradeNode(turretConfig);
        buildRoot = new UpgradeNode(woodhammerConfig);
        buildRoot.addChild(dirtNode);
        dirtNode.addChild(gateNode);
        dirtNode.addChild(stoneNode);
        buildRoot.addChild(turretNode);
        trees.add(buildRoot);
    }

    /**
     * Creates a title table containing a label
     */
    private Table createTitleTable() {
        Table titleTable = new Table();
        Label title = new Label("UPGRADE TREE", skin, "large");
        title.setColor(Color.BLACK);
        title.setFontScale(0.5F, 0.5F);
        titleTable.add(title);
        titleTable.setPosition((getWidth() * getScaleX() / 2),
                (float) (getHeight() * getScaleY() * 0.88));

        return titleTable;
    }

    /**
     * Creates and organizes the upgrade buttons based on the built upgrade trees.
     *
     * @return A group containing all the upgrade buttons.
     */
    private Group createUpgradeButtons() {

        Group group = new Group();

        buildTrees();

        nodeXSpacing = (float) ((getWidth() * getScaleX()) / (trees.size() * 2.8)); // 2
        nodeYSpacing = (getHeight() * getScaleY()) / 4;

        for (UpgradeNode treeRoot : trees) {
            float treeX = (trees.indexOf(treeRoot) + 1) * getWidth() * getScaleX()
                    / (trees.size() + 1) - nodeXSpacing / 6;
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
            child.setParent(node);
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

        // Grey node background
        float dx = 20;
        float rectSize = SIZE + dx;
        float offsetX = this.getX() - (dx / 2);
        float offsetY = this.getY() - (dx / 2);

        // Black outline box
        float blackRectSize = rectSize * 1.1f;
        float blackOffsetX = offsetX - (blackRectSize - rectSize) / 2;
        float blackOffsetY = offsetY - (blackRectSize - rectSize) / 2;

        // Equipped weapon highlight box
        float equippedRectSize = rectSize * 1.05f;
        float equippedOffsetX = offsetX - (equippedRectSize - rectSize) / 2;
        float equippedOffsetY = offsetY - (equippedRectSize - rectSize) / 2;

        // Draw black square outline
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(node.getX() + blackOffsetX, node.getY() + blackOffsetY, blackRectSize, blackRectSize);

        // Draw a yellow highlight around all equipped items.
        HashMap<Integer, WeaponType> equippedWeaponMap = player.getComponent(InventoryComponent.class)
                .getEquippedWeaponMap();
        if (equippedWeaponMap.containsValue(node.getWeaponType())) {
            shapeRenderer.setColor(Color.GOLD);
            shapeRenderer.rect(node.getX() + equippedOffsetX, node.getY() + equippedOffsetY, equippedRectSize, equippedRectSize);
        }

        // Draws grey weapon background
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
        UpgradeTree stats = upgradeBench.getComponent(UpgradeTree.class);

        for (UpgradeNode child : node.getChildren()) {
            Vector2 childPos = localToStageCoordinates(new Vector2(child.getX() + SIZE/2, child.getY() + SIZE/2));

            // draw stroke
            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.rectLine(parentPos, childPos, 9);

            // Change line colour based on mutual unlocked / locked status between node and child
            if (stats.isWeaponUnlocked(node.getName()) && stats.isWeaponUnlocked(child.getName())) {
                shapeRenderer.setColor(new Color(41f/255, 222f/255, 15f/255, 0.5f)); // light green
            } else {
                shapeRenderer.setColor(new Color(150f / 255, 18f / 255, 23f / 255, 0.5f)); // dark red
            }
            shapeRenderer.rectLine(parentPos, childPos, 5);

            drawLines(child);
        }
    }

    /**
     * Sets up the window dimensions.
     */
    private void setupWindowDimensions() {
        Stage stage = ServiceLocator.getRenderService().getStage();
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
        TextButton exitButton = new TextButton("X", skin);
        Table table = new Table();
        table.add(exitButton).height(32f).width(32f);
        table.setPosition(((float) (getWidth() * getScaleX() * 0.91)),
                (float) (getHeight() * getScaleY() * 0.88));

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
        materialsLabel = new Label(str, skin, "large");
        materialsLabel.setColor(Color.BLACK);
        materialsLabel.setFontScale(0.25f);
        Image nebuliteImage =
                new Image(ServiceLocator.getResourceService().getAsset("images/nebulite.png", Texture.class));

        Table table = new Table();
        table.add(nebuliteImage).size(64,64);
        table.add(materialsLabel);
        table.setPosition((float) (getWidth() * getScaleX() * 0.10),
                (float) (getHeight() * getScaleY() * 0.88));

        // update the materials label every 250ms
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                int updatedMaterials = upgradeBench.getComponent(UpgradeTree.class).getMaterials();
                String updatedStr = String.format(MATERIALS_FORMAT, updatedMaterials);
                materialsLabel.setText(updatedStr);
            }
        }, 0.25f, 0.25f);

        return table;
    }

    /**
     * Creates a label with the specified format and arguments, and then adds it to the given table.
     */
    private void createTooltipLabel(Table table, String attributeName, String valueFormat) {
        table.pad(10);
        Label.LabelStyle labelStyle = skin.get(Label.LabelStyle.class);
        labelStyle.fontColor = Color.BLACK;
        labelStyle.font = skin.getFont("thick_black");

        Label attributeLabel = new Label(attributeName, labelStyle);
        attributeLabel.setWrap(true);
        attributeLabel.setAlignment(Align.left);

        if (valueFormat.isEmpty()) {
            table.add(attributeLabel).width(200).colspan(2).left().padLeft(10).padBottom(20).row();
        } else {
            BitmapFont font = new BitmapFont();
            Label.LabelStyle redLabelStyle = new Label.LabelStyle(font, Color.RED);

            Label valueLabel = new Label(valueFormat, redLabelStyle);
            valueLabel.setWrap(true);
            table.add(attributeLabel).width(150).left().padLeft(20).padRight(10);
            table.add(valueLabel).width(40).center().padRight(20).row();
        }
    }

    /**
     * Creates a tooltip for a given upgrade node.
     * The tooltip displays various attributes of the upgrade node, such as damage, speed, cooldown, etc.
     * The specific attributes displayed depend on whether the node is a weapon or tool.
     *
     * @param node The upgrade node for which the tooltip is created.
     * @return A Tooltip object containing a table with labels that describe the upgrade node.
     */
    private Tooltip<Table> createTooltip(UpgradeNode node) {

        Table tooltipTable = new Table();

        // Set font style and background
        tooltipTable.defaults().left().padLeft(2).padTop(5).padRight(10);
        Drawable bg = skin.getDrawable("panelInset_brown.png");
        tooltipTable.setBackground(bg);

        if (node.getWeaponType() != null) {
            WeaponConfig config = (WeaponConfig) node.getConfig();
            createTooltipLabel(tooltipTable, config.name, "");
            createTooltipLabel(tooltipTable, config.description, "");
            createTooltipLabel(tooltipTable, "Damage", String.valueOf((int) config.damage));
            createTooltipLabel(tooltipTable, "Speed", String.valueOf((int) config.weaponSpeed));
            createTooltipLabel(tooltipTable, "Cooldown", String.valueOf(config.attackCooldown));
            createTooltipLabel(tooltipTable, "Cost", String.valueOf(node.getNodeCost()));
        } else {
            ToolConfig config = (ToolConfig) node.getConfig();
            createTooltipLabel(tooltipTable, config.name, "");
            createTooltipLabel(tooltipTable, "Extractor Cost:", "");
            for (ObjectMap.Entry<String, Integer> entry : config.cost) {
                createTooltipLabel(tooltipTable, entry.key, String.valueOf(entry.value));
            }
        }

        // Return the table as a tooltip
        Tooltip<Table> tooltip = new Tooltip<>(tooltipTable);
        tooltip.setInstant(true); // Make it appear instantly on mouseover
        return tooltip;
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

        // Create tooltips
        Tooltip<Table> tooltip = createTooltip(node);
        weaponButton.addListener(tooltip);

        // Create unlock listener for unlock button
        if (costButton != null) {
            costButton.addListener(unlockWeapon(node, stats, weaponButton, lockImage, costButton));
        }

        if (node.getWeaponType() != null) {
            weaponButton.addListener(equipItem(node));
        }

        return weaponButton;
    }

    public TextButton createCostButtons(UpgradeNode node, ImageButton weaponButton) {

        UpgradeTree stats = upgradeBench.getComponent(UpgradeTree.class);
        // Dont draw cost buttons for unlocked nodes
        if (stats.isWeaponUnlocked(node.getName())) {
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
        if (stats.isWeaponUnlocked(node.getName())) return null;

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
                if (stats.isWeaponUnlocked(node.getName())) {
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

        // Ensure locked, sufficient materials, and parent is unlocked
        if (stats.isWeaponUnlocked(node.getName())
                || stats.getMaterials() < node.getNodeCost()
                || !stats.isWeaponUnlocked(node.getParent().getName())) {
            return;
        }

        // Set the node to unlocked
        weaponButton.setColor(1f, 1f, 1f, 1f); // un-grey the image
        stats.subtractMaterials(node.getNodeCost());
        stats.unlockWeapon(node.getName());

        // Add it to the StructurePicker menu if buildable
        if (node.getWeaponType() == null) {
            structurePicker.unlockTool(node.getName());
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
