package com.csse3200.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotNull;

class LabWindowTest {

    private Stage stage;
    private Stage potionStage;

    @Before
    public void setUp() {
        // Mock the stage and input handling for lab window
        stage = Mockito.spy(new Stage(new ScreenViewport()));
        Mockito.when(stage.getWidth()).thenReturn(800f); // Adjust width as needed
        Mockito.when(stage.getHeight()).thenReturn(600f); // Adjust height as needed

        // Create a separate stage for potion images
        potionStage = Mockito.spy(new Stage(new ScreenViewport()));
        Mockito.when(potionStage.getWidth()).thenReturn(800f); // Adjust width as needed
        Mockito.when(potionStage.getHeight()).thenReturn(600f); // Adjust height as needed
    }

    @Test
    public void labWindowNotNull() {
        LabWindow labWindow = LabWindow.MakeNewLaboratory();
        stage.addActor(labWindow);

        // Add images of potions in front of the lab window
        addPotionImagesToStage(potionStage);

        // Simulate a rendering cycle (important for LibGDX tests)
        Gdx.graphics.getGL20().glClearColor(0, 0, 0, 0);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render the lab window
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        // Render the potion images on top
        potionStage.act(Gdx.graphics.getDeltaTime());
        potionStage.draw();

        assertNotNull(labWindow);

        // Assertions for the presence of potion images using their actor names
        assertNotNull(potionStage.getRoot().findActor("DeathPotionImage"));
        assertNotNull(potionStage.getRoot().findActor("SpeedPotionImage"));
        assertNotNull(potionStage.getRoot().findActor("HealthPotionImage"));
        assertNotNull(potionStage.getRoot().findActor("invincibilitypotionImage"));
        assertNotNull(potionStage.getRoot().findActor("doubledamageImage"));
        assertNotNull(potionStage.getRoot().findActor("extralifeImage"));
        assertNotNull(potionStage.getRoot().findActor("snapImage"));
        assertNotNull(potionStage.getRoot().findActor("doublecrossImage"));


    }

    // Helper method to add potion images to the stage
    private void addPotionImagesToStage(Stage stage) {
        Texture deathPotionTexture = new Texture("images/Potion3re.png");
        Image deathPotionImage = new Image(deathPotionTexture);
        deathPotionImage.setName("DeathPotionImage");
        deathPotionImage.setPosition(100, 100); // Adjust position as needed
        stage.addActor(deathPotionImage);

        Texture speedPotionTexture = new Texture("images/Potion2re.png");
        Image speedPotionImage = new Image(speedPotionTexture);
        speedPotionImage.setName("SpeedPotionImage");
        speedPotionImage.setPosition(200, 100); // Adjust position as needed
        stage.addActor(speedPotionImage);

        Texture healthPotionTexture = new Texture("images/Potion4re.png");
        Image healthPotionImage = new Image(healthPotionTexture);
        healthPotionImage.setName("HealthPotionImage");
        healthPotionImage.setPosition(300, 100); // Adjust position as needed
        stage.addActor(healthPotionImage);

        Texture potionTexture = new Texture("images/Potion1re.png");
        Image potionImage = new Image(potionTexture);
        potionImage.setName("PotionImage");
        potionImage.setPosition(400, 100); // Adjust position as needed
        stage.addActor(potionImage);
    }
}
