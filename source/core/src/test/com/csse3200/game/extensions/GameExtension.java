package com.csse3200.game.extensions;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mockito;

/**
 * A JUnit extension which provides libGDX mocking and clears global variables between tests.
 * Use this extension when testing game-related classes.
 */
public class GameExtension implements AfterEachCallback, BeforeAllCallback {
  private Application game;

  @Override
  public void beforeAll(ExtensionContext context) {
    // 'Headless' back-end, so no rendering happens
    game = new HeadlessApplication(new ApplicationAdapter() {});

    // Mock any calls to OpenGL
    Gdx.gl20 = Mockito.mock(GL20.class);
    Gdx.gl = Gdx.gl20;
  }

  @Override
  public void afterEach(ExtensionContext context) {
    // Clear the global state from the service locator
    ServiceLocator.clear();
  }
}
