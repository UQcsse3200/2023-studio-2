package com.csse3200.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.graphics.Color;

public class TitleBox extends Dialog {

    public TitleBox(String title, Skin skin) {
        super(title, skin);
        setMovable(false);
        setResizable(false);

        // Create a label with your story text
        String storyText = "Once upon a time...\n\nThis is the story of your adventure.";
        text(storyText);

        // Create a "Start" button to close the dialog
        TextButton startButton = new TextButton("Start", skin);
        button(startButton, true); // The 'true' argument makes this button close the dialog when clicked.

        // Lay out the dialog's components
        getContentTable().row();
        getContentTable().add(startButton).pad(20f).center();

        // Size and positioning of the dialog
        setSize(400f, 300f); // Adjust the size as needed
        setPosition((Gdx.graphics.getWidth() - getWidth()) / 2, (Gdx.graphics.getHeight() - getHeight()) / 2);
    }

    public void showDialog(Stage stage) {
        stage.addActor(this);
    }
}
