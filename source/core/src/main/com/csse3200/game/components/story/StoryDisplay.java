/**
 * StoryDisplay is a UI component that displays a sequence of images along with navigation buttons for a story presentation.
 * It allows the user to navigate through different scenes and skip the story.
 */
package com.csse3200.game.components.story;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.mainmenu.InsertButtons;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;

/**
 * A UI component that displays a sequence of images along with navigation buttons for a story presentation.
 */
public class StoryDisplay extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(StoryDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private Label storyTextLabel;
    private ArrayList<String> storyImages;
    private int start;
    private int end;
    private BitmapFont font;

    private static final String[] buttonImages = {"images/next_cut.png", "images/prev-cut.png"};

    @Override
    public void create() {
        super.create();
        addActors();
        entity.getEvents().addListener("next", this::nextScene);
        entity.getEvents().addListener("previous", this::prevScene);
        font = new BitmapFont();
    }

    /**
     * Adds UI components such as images and navigation buttons to the stage.
     */
    private void addActors() {
        table = new Table();
        table.setFillParent(true);
        table.bottom().right();

        storyImages = new ArrayList<>();
        storyImages.add("images/P1_B.png");
        storyImages.add("images/P2_B.png");
        storyImages.add("images/P3_B.png");
        storyImages.add("images/P4_B.png");
        storyImages.add("images/P5_B.png");
        storyImages.add("images/P6_B.png");
        start = 0;
        end = 6;


        // Load the first image
        Drawable storyBackground = loadStoryImage(storyImages.get(start));
        table.setBackground(storyBackground);


        start += 1;
        stage.addActor(table);

        // Define your custom label style
        Label.LabelStyle customLabelStyle = new Label.LabelStyle();
        customLabelStyle.font = font; // 'font' is your custom font, replace it with your font instance


        // Create and configure the Label for displaying text
        storyTextLabel = new Label("Your story text goes here", skin); // Adjust the skin file path as needed
        storyTextLabel.setWrap(true); // Enable text wrapping
        storyTextLabel.setColor(Color.WHITE); // Set text color to white

        storyTextLabel.setWidth(Gdx.graphics.getWidth());
        // Center align the text horizontally
        storyTextLabel.setAlignment(Align.center);

        // Position the text at the bottom of the screen
        float textY = 0; // Adjust this value to fine-tune the vertical position
        textY += storyTextLabel.getHeight(); // Adjust for text height
        storyTextLabel.setPosition(
                (Gdx.graphics.getWidth() - storyTextLabel.getWidth()) / 2,
                textY
        );

        // Add the Label to the stage
        stage.addActor(storyTextLabel);


//        Texture storyLine = new Texture(Gdx.files.internal(storyImages.get(start)));
//        TextureRegionDrawable storyBackground = new TextureRegionDrawable(storyLine);
//        table.setBackground(storyBackground);
//
//        start += 1;
//        stage.addActor(table);
        InsertButtons bothButtons = new InsertButtons();

        // Create next button
        String nextTexture = "images/next_cut.png";
        String nextTextureHover = "images/next_cut_hover.png";
        ImageButton nextBtn = bothButtons.draw(nextTexture, nextTextureHover);

        // Create previous button
        String prevTexture = "images/prev_cut.png";
        String prevTextureHover = "images/prev_cut_hover.png";
        ImageButton prevBtn = bothButtons.draw(prevTexture, prevTextureHover);

        // Create skip button
        String skipTexture = "images/skip_btn.png";
        String skipTextureHover = "images/skip_btn_hover.png";
        ImageButton skipBtn = bothButtons.draw(skipTexture, skipTextureHover);

        // Attach listeners to navigation buttons
        nextBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Next button clicked");
                        entity.getEvents().trigger("next");
                    }
                });

        skipBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Skip button clicked");
                        entity.getEvents().trigger("skip");
                    }
                });

        prevBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Previous button clicked");
                        entity.getEvents().trigger("previous");
                    }
                });

        table.add(skipBtn).expand().top().right().width(200f);
        table.row();
        table.add(prevBtn).left().width(70f).padBottom(300f);
        table.add(nextBtn).right().width(70f).padBottom(300f);
        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Drawing is handled by the stage
    }

    private Drawable loadStoryImage(String imagePath) {
        Texture storyLine = new Texture(Gdx.files.internal(imagePath));
        return new TextureRegionDrawable(new TextureRegion(storyLine));
    }


    /**
     * Advances to the next scene of the story.
     */
    private void nextScene() {
        if (start < end) {
            Drawable next = new TextureRegionDrawable(new Texture(Gdx.files.internal(storyImages.get(start))));
            table.setBackground(next);
            start += 1;
        } else {
            entity.getEvents().trigger("skip");
        }
    }

    /**
     * Moves to the previous scene of the story.
     */
    private void prevScene() {
        if (end - start > 0 && start > 0) {
            Drawable prev = new TextureRegionDrawable(new Texture(Gdx.files.internal(storyImages.get(start - 1))));
            table.setBackground(prev);
            start -= 1;
        }
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        font.dispose();
        table.clear();
        stage.clear();
        super.dispose();
    }
}
