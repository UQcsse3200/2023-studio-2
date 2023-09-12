package com.csse3200.game.entities.factories;

import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.ServiceLocator;

public class RenderFactory {

  public static Entity createCamera() {
    return new Entity().addComponent(new CameraComponent());
  }

  public static Renderer createRenderer() {
    Entity camera = createCamera();
    ServiceLocator.getEntityService().register(camera);
    CameraComponent camComponent = camera.getComponent(CameraComponent.class);

    return new Renderer(camComponent);
  }

  private RenderFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
