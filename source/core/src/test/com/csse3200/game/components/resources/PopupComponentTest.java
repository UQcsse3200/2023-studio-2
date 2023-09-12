package com.csse3200.game.components.resources;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.PopupFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.GameStateInteraction;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PopupComponentTest {

        int duration = 500;
        double speed = 0.001;

        @Mock
        GameTime gameTime;

        @Mock
        TextureRenderComponent renderComponent;

        @Test
        void validMultiplierWithoutCombatStats() throws InterruptedException {
            Entity entity = new Entity();
            entity.addComponent(new PopupComponent(duration, speed));
            entity.addComponent(renderComponent);

            entity.getComponent(PopupComponent.class).setTimer(gameTime);
            entity.create();
            entity.setPosition(0, 0);

            // Check the entities position hasn't changed if time hasn't:
            when(gameTime.getTime()).thenReturn(0L);
            when(gameTime.getTimeSince(0)).thenReturn(0L);
            entity.update();
            assertEquals(new Vector2(0, 0), entity.getPosition());

            // Advance time half the duration exactly and assert we have moved duration/2*speed up
            when(gameTime.getTime()).thenReturn((long) (duration / 2));
            when(gameTime.getTimeSince(0)).thenReturn((long) (duration / 2));
            entity.update();
            assertEquals(new Vector2((float) 0, (float) ((duration / 2) * speed)), entity.getPosition());

            // Advance full time!
            when(gameTime.getTime()).thenReturn((long) duration);
            when(gameTime.getTimeSince(duration/2)).thenReturn((long) (duration/2));
            when(gameTime.getTimeSince(0)).thenReturn((long) (duration ));
            entity.update();
            assertEquals(new Vector2((float) 0, (float) ((duration) * speed)), entity.getPosition());
        }
}
