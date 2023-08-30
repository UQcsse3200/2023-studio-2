package com.csse3200.game.components.companion;


import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.csse3200.game.components.Companion.CompanionStatsDisplay;
import com.csse3200.game.entities.Entity;
import org.junit.Before;
import org.junit.Test;


import static org.mockito.Mockito.*;

public class CompanionStatsDisplayTest
{


        private CompanionStatsDisplay companionStatsDisplay;
        private Stage mockStage;
        private Label mockLabel;

        @Before
        public void setUp() {
            mockStage = mock(Stage.class);
            mockLabel = mock(Label.class);

            companionStatsDisplay = new CompanionStatsDisplay(true, 10f, 100);
            companionStatsDisplay.stage = mockStage;
        }

        @Test
        public void testDisplayMessage() {
            companionStatsDisplay.messageLabel = mockLabel;

            companionStatsDisplay.displayMessage();

            verify(mockLabel).setText("Low Health");
        }

        @Test
        public void testUpdate() {
            companionStatsDisplay.messageDisplayTimeLeft = 2.0f;
            companionStatsDisplay.messageLabel = mockLabel;

            companionStatsDisplay.update(1.0f);

            verify(mockLabel, never()).setText(any(String.class));

            companionStatsDisplay.update(1.0f);

            verify(mockLabel).setText("");
        }
    }

