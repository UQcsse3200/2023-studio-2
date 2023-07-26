package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Renders animations from a texture atlas on an entity.
 *
 * <p>Example usage:
 *
 * <pre>
 *   AnimationRenderComponent animator = new AnimationRenderComponent("player.atlas");
 *   entity.addComponent(animator);
 *   animator.addAnimation("attack", 0.1f); // Only need to add animation once per entity
 *   animator.startAnimation("attack");
 * </pre>
 *
 * Texture atlases can be created using: <br>
 * - libgdx texture packer (included in External Libraries/gdx-tools) <br>
 * - gdx-texture-packer-gui (recommended) https://github.com/crashinvaders/gdx-texture-packer-gui <br>
 * - other third-party tools, e.g. https://www.codeandweb.com/texturepacker <br>
 */
public class AnimationRenderComponent extends RenderComponent {
  private static final Logger logger = LoggerFactory.getLogger(AnimationRenderComponent.class);
  private final GameTime timeSource;
  private final TextureAtlas atlas;
  private final Map<String, Animation<TextureRegion>> animations;
  private Animation<TextureRegion> currentAnimation;
  private String currentAnimationName;
  private float animationPlayTime;

  /**
   * Create the component for a given texture atlas.
   * @param atlas libGDX-supported texture atlas containing desired animations
   */
  public AnimationRenderComponent(TextureAtlas atlas) {
    this.atlas = atlas;
    this.animations = new HashMap<>(4);
    timeSource = ServiceLocator.getTimeSource();
  }

  /**
   * Register an animation from the texture atlas. Will play once when called with startAnimation()
   * @param name Name of the animation. Must match the name of this animation inside the texture
   *             atlas.
   * @param frameDuration How long, in seconds, to show each frame of the animation for when playing
   * @return true if added successfully, false otherwise
   */
  public boolean addAnimation(String name, float frameDuration) {
    return addAnimation(name, frameDuration, PlayMode.NORMAL);
  }

  /**
   * Register an animation from the texture atlas.
   * @param name Name of the animation. Must match the name of this animation inside the texture
   *             atlas.
   * @param frameDuration How long, in seconds, to show each frame of the animation for when playing
   * @param playMode How the animation should be played (e.g. looping, backwards)
   * @return true if added successfully, false otherwise
   */
  public boolean addAnimation(String name, float frameDuration, PlayMode playMode) {
    Array<AtlasRegion> regions = atlas.findRegions(name);
    if (regions == null || regions.size == 0) {
      logger.warn("Animation {} not found in texture atlas", name);
      return false;
    } else if (animations.containsKey(name)) {
      logger.warn(
          "Animation {} already added in texture atlas. Animations should only be added once.",
          name);
      return false;
    }

    Animation<TextureRegion> animation = new Animation<>(frameDuration, regions, playMode);
    animations.put(name, animation);
    logger.debug("Adding animation {}", name);
    return true;
  }

  /** Scale the entity to a width of 1 and a height matching the texture's ratio */
  public void scaleEntity() {
    TextureRegion defaultTexture = this.atlas.findRegion("default");
    entity.setScale(1f, (float) defaultTexture.getRegionHeight() / defaultTexture.getRegionWidth());
  }

  /**
   * Remove an animation from this animator. This is not required before disposing.
   * @param name Name of the previously added animation.
   * @return true if removed, false if animation was not found.
   */
  public boolean removeAnimation(String name) {
    logger.debug("Removing animation {}", name);
    return animations.remove(name) != null;
  }

  /**
   * Whether the animator has added the given animation.
   * @param name Name of the added animation.
   * @return true if added, false otherwise.
   */
  public boolean hasAnimation(String name) {
    return animations.containsKey(name);
  }

  /**
   * Start playback of an animation. The animation must have been added using addAnimation().
   * @param name Name of the animation to play.
   */
  public void startAnimation(String name) {
    Animation<TextureRegion> animation = animations.getOrDefault(name, null);
    if (animation == null) {
      logger.error(
          "Attempted to play unknown animation {}. Ensure animation is added before playback.",
          name);
      return;
    }

    currentAnimation = animation;
    currentAnimationName = name;
    animationPlayTime = 0f;
    logger.debug("Starting animation {}", name);
  }

  /**
   * Stop the currently running animation. Does nothing if no animation is playing.
   * @return true if animation was stopped, false if no animation is playing.
   */
  public boolean stopAnimation() {
    if (currentAnimation == null) {
      return false;
    }

    logger.debug("Stopping animation {}", currentAnimationName);
    currentAnimation = null;
    currentAnimationName = null;
    animationPlayTime = 0f;
    return true;
  }

  /**
   * Get the name of the animation currently being played.
   * @return current animation name, or null if not playing.
   */
  public String getCurrentAnimation() {
    return currentAnimationName;
  }

  /**
   * Has the playing animation finished? This will always be false for looping animations.
   * @return true if animation was playing and has now finished, false otherwise.
   */
  public boolean isFinished() {
    return currentAnimation != null && currentAnimation.isAnimationFinished(animationPlayTime);
  }

  @Override
  protected void draw(SpriteBatch batch) {
    if (currentAnimation == null) {
      return;
    }
    TextureRegion region = currentAnimation.getKeyFrame(animationPlayTime);
    Vector2 pos = entity.getPosition();
    Vector2 scale = entity.getScale();
    batch.draw(region, pos.x, pos.y, scale.x, scale.y);
    animationPlayTime += timeSource.getDeltaTime();
  }

  @Override
  public void dispose() {
    atlas.dispose();
    super.dispose();
  }
}
