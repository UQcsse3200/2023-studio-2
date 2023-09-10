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
import com.csse3200.game.entities.Entity;
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
     * Builds the predefined trees for the melee, ranged, and building items.
     * These trees dictate the progression of weapons that can be unlocked.
     */
    private void buildTrees() {
        // todo: possibly create config file
        // Melee Tree
        UpgradeNode meleeRoot = new UpgradeNode(WeaponType.STICK, "images/upgradetree/stick.png");
        UpgradeNode swordNode = new UpgradeNode(WeaponType.KATANA, "images/upgradetree/sword.png");
        meleeRoot.addChild(swordNode);
        trees.add(meleeRoot);

        // Ranged Tree
        UpgradeNode rangedRoot = new UpgradeNode(WeaponType.THROW_ELEC_WRENCH, "images/wrench.png");
        UpgradeNode wrenchNode = new UpgradeNode(WeaponType.TEMP1, "images/speedpowerup.png");
        UpgradeNode wrenchNode2 = new UpgradeNode(WeaponType.TEMP2, "images/meteor.png");
        rangedRoot.addChild(wrenchNode);
        rangedRoot.addChild(wrenchNode2);
        wrenchNode2.addChild((new UpgradeNode(WeaponType.SLING_SHOT, "images/meteor.png")));
        wrenchNode2.addChild((new UpgradeNode(WeaponType.TEMP3, "images/meteor.png")));
        trees.add(rangedRoot);

        // Build Tree
        UpgradeNode buildRoot = new UpgradeNode(WeaponType.WOODHAMMER, "images/upgradetree/hammer1.png");
        UpgradeNode hammer2 = new UpgradeNode(WeaponType.STONEHAMMER, "images/upgradetree/hammer2.png");
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

        for (UpgradeNode treeRoot : trees) {
            float treeX = (trees.indexOf(treeRoot) + 1) * getWidth() / (trees.size() + 1);
            float startY = getHeight() - 200;  // start near the top
            createAndPositionNodes(treeRoot, treeX, startY, group);
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
    private void createAndPositionNodes(UpgradeNode node, float x, float y, Group group) {

        // base case
        if (node == null) {
            return;
        }

        node.setX(x);
        node.setY(y);

        ImageButton button = createWeaponButtons(node, SIZE, x, y);
        group.addActor(button);

        int childCount = node.getChildren().size();
        float totalWidth = childCount * HORIZONTAL_SPACING;
        float currentX = x - totalWidth / 2 + HORIZONTAL_SPACING / 2;

        for (UpgradeNode child : node.getChildren()) {
            float childX = currentX;
            float childY = y - VERTICAL_SPACING;

            createAndPositionNodes(child, childX, childY, group);
            currentX += HORIZONTAL_SPACING;
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
     * @param batch The batch used for rendering.
     */
    private void drawLines(UpgradeNode node, Batch batch) {
        if (node == null || node.getChildren().isEmpty()) {
            return;
        }

        Vector2 parentPos = localToStageCoordinates(new Vector2(node.getX() + SIZE/2, node.getY() + SIZE/2));

        for (UpgradeNode child : node.getChildren()) {
            Vector2 childPos = localToStageCoordinates(new Vector2(child.getX() + SIZE/2, child.getY() + SIZE/2));
            shapeRenderer.rectLine(parentPos, childPos, 5);

            drawLines(child, batch);
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

    /**
     * Creates a label showing the available materials for upgrades.
     *
     * @return The created materials label.
     */
    private Label createMaterialsLabel() {
        Skin skin = new Skin(Gdx.files.internal(SKIN_PATH));
        int materials = upgradeBench.getComponent(UpgradeTree.class).getMaterials();

        String str = String.format(MATERIALS_FORMAT, materials);
        this.materialsLabel = new Label(str, skin);
        materialsLabel.setPosition(MATERIAL_LABEL_X, MATERIAL_LABEL_Y);
        return materialsLabel;
    }

    /**
     * Creates a button for a given weapon node.
     *
     * @param node The upgrade node for which the button is being created.
     * @param size The size of the button.
     * @param posX The x-coordinate for the button's position.
     * @param posY The y-coordinate for the button's position.
     * @return The created ImageButton.
     */
    private ImageButton createWeaponButtons(UpgradeNode node, float size, float posX, float posY) {
        TextureRegionDrawable drawable = createTextureRegionDrawable(node.getImagePath(), size);
        ImageButton button = new ImageButton(drawable);

        UpgradeTree stats = upgradeBench.getComponent(UpgradeTree.class);

        Image lock = null;
        if (!stats.isWeaponUnlocked(node.getWeaponType())) {
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
                if (!stats.isWeaponUnlocked(node.getWeaponType())) {
                    if (stats.getMaterials() >= requiredMaterials) {
                        stats.subtractMaterials(requiredMaterials);
                        stats.unlockWeapon(node.getWeaponType());
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

    /**
     * Override the removal process to unregister input overrides.
     *
     * @return Returns true if the actor was removed.
     */
    @Override
    public boolean remove() {
        //Stop overriding input when exiting minigame
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

    /**
     * Draws the UpgradeDisplay and its children.
     *
     * @param batch       The batch used for rendering.
     * @param parentAlpha The parent alpha value, for transparency.
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
