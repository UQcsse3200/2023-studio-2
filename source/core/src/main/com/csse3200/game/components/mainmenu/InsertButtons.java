package com.csse3200.game.components.mainmenu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class InsertButtons {

    public ImageButton draw(String initButton, String hoverButton){
        Texture nomButton = new Texture(Gdx.files.internal(initButton));
        Texture hovButton = new Texture(Gdx.files.internal(hoverButton));
        Drawable drawing = new TextureRegionDrawable(new TextureRegion(nomButton));
        ImageButton buttonImage = new ImageButton(drawing);
        buttonImage.getStyle().imageOver = new TextureRegionDrawable(hovButton);
        return buttonImage;
    }


}