package com.csse3200.game.upgradetree;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
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

// todo: make this a somewhat functional upgrade tree, lol.
/**
* A GUI component for the UpgradeTree
*/
public class UpgradeDisplay extends Window {
    private final InputOverrideComponent inputOverrideComponent;
    private final Entity upgradeBench;

    public static UpgradeDisplay MakeNewMinigame(Entity upgradeBench) {
        Texture background = ServiceLocator.getResourceService().getAsset("images/upgradetree/background.png", Texture.class);
        background.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        return new UpgradeDisplay(background, upgradeBench);
    }

    public UpgradeDisplay(Texture background, Entity upgradeBench) {
        super("", new Window.WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(background)));

        this.upgradeBench = upgradeBench;

        //Here setup window to be centered on the stage with 80% width and 65% height.
        Stage stage = ServiceLocator.getRenderService().getStage();
        setWidth((float) (stage.getWidth()*0.8));
        setHeight((float) (stage.getHeight()*0.65));
        setPosition(stage.getWidth()/2 - getWidth()/2 * getScaleX(), stage.getHeight()/2 - getHeight()/2 * getScaleY());

        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        TextureRegionDrawable image = new TextureRegionDrawable(new Texture("images/upgradetree/sword.png"));

        TextButton button = new TextButton("Slingshot", skin);
        TextButton button2 = new TextButton("Wrench", skin);

        String materials = String.format("Materials: %d", upgradeBench.getComponent(UpgradeTree.class).getMaterials());
        Label materialsLabel = new Label(materials, skin);
        materialsLabel.setPosition(1150, 600);

        // Create tree branch tables
        Table table = new Table();
        table.top().left();
        table.setFillParent(true);

        // Add stuff to the branch tables
        Image img = new Image(image);

        Label a = new Label("Left", skin);
        Label c = new Label("Middle", skin);
        Label b = new Label("Right", skin);

        Label aa = new Label("BL", skin);
        Label bb = new Label("BR", skin);

        table.add(a).pad(10);
        table.add(b).pad(10);
        table.row();
        table.add(aa).pad(10);
        table.add(bb).pad(10);
        table.row();
        table.add(c).colspan(2);


        table.padLeft(650);
        table.padTop(160);
        addActor(table);
        addActor(materialsLabel);


        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                exitUpgradeTree();
            }
        });

        button2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                exitUpgradeTree();
            }
        });




        debugAll();

        // Override all normal user input
        inputOverrideComponent = new InputOverrideComponent();
        ServiceLocator.getInputService().register(inputOverrideComponent);
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
