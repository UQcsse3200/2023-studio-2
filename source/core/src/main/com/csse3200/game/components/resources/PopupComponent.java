package com.csse3200.game.components.resources;

import com.badlogic.gdx.Gdx;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.GameTime;
import com.badlogic.gdx.utils.Timer;

public class PopupComponent extends Component {
    GameTime timer;
    double startTime;
    double lastTime;
    double speed;
    int duration;

    /**
     * Popup Component
     * a component which can be attached to an existing entity to delete it in a fancy way!
     * The entity MUST have a RenderTextureComponent.
     * when attached (with a duration and speed) this component will fade the entity upwards and out of sight over
     * the duration, disposing of it properly afterwards.
     *
     * @param duration the amount of time (ms) before cleanup
     * @param speed the speed of the popups motion
     */
    public PopupComponent(int duration, double speed) {
        this.timer = new GameTime();
        this.startTime = this.timer.getTime();
        this.lastTime = this.timer.getTime();
        this.speed = speed;
        this.duration = duration;
    }

    @Override
    public void update() {
        super.update();

        double since = this.timer.getTimeSince((long) lastTime);
        double lifespan = this.timer.getTimeSince((long) startTime);
        lastTime = timer.getTime();

        if (lifespan > duration) {
            Gdx.app.postRunnable(entity::dispose);
        }


        this.entity.setPosition(this.entity.getPosition().x, (float) (this.entity.getPosition().y + since * speed));
        this.entity.getComponent(TextureRenderComponent.class).setAlpha(1.0F - (float) (lifespan / duration));
    }

    /**
     * Overrides the generated timer, in case you want to prolong the life, pause the popup, etc
     * (useful for testing)
     * @param gameTime the new gameTime
     */
    public void setTimer(GameTime gameTime) {
        this.timer = gameTime;
    }
}
