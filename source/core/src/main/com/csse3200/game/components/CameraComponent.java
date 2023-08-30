package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class CameraComponent extends Component {
  private final Camera camera;
  private Vector2 lastPosition;
  private final float zoomSpeed = 0.1f;
  private final float minimumZoom = 5f; // Set your desired minimum zoom value
  private final float maximumZoom = 20f;

  public CameraComponent() {
    this(new OrthographicCamera());
  }

  public CameraComponent(Camera camera) {
    this.camera = camera;
    lastPosition = Vector2.Zero.cpy();
  }

  @Override
  public void update() {
    Vector2 position = entity.getPosition();
    if (Gdx.input.isKeyPressed(Input.Keys.PLUS) || Gdx.input.isKeyPressed(Input.Keys.EQUALS)) {
      // Zoom in when "+" key is pressed
      fixZoom(-zoomSpeed);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
      // Zoom out when "-" key is pressed
      fixZoom(zoomSpeed);
    }
    if (!lastPosition.epsilonEquals(entity.getPosition())) {
      camera.position.set(position.x, position.y, 0f);
      lastPosition = position;
      camera.update();
    }
  }

  public Matrix4 getProjectionMatrix() {
    return camera.combined;
  }

  public Camera getCamera() {
    return camera;
  }

  public void resize(int screenWidth, int screenHeight, float gameWidth) {
    float ratio = (float) screenHeight / screenWidth;
    camera.viewportWidth = gameWidth;
    camera.viewportHeight = gameWidth * ratio;
    camera.update();
  }

  private void fixZoom(float x) {
    float currentZoom = camera.viewportWidth + x;

    // Ensure zoom stays within a valid range
    if (currentZoom < minimumZoom) {
      currentZoom = minimumZoom;
    }
    if (currentZoom > maximumZoom) {
      currentZoom = maximumZoom;
    }

    //maintaining the screen ratio post zooming
    float screenratio = (float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
    camera.viewportWidth = currentZoom;
    camera.viewportHeight = currentZoom * screenratio;
    camera.update();
  }
}
