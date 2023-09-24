package com.csse3200.game.components.structures;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
class RotationRenderComponentTest {
    @Mock
    TextureAtlas textureAtlas;

    @Test
    void testSetAndGetRotation() {
        testSetAndGetRotation(Rotation.NORTH);
        testSetAndGetRotation(Rotation.SOUTH);
        testSetAndGetRotation(Rotation.EAST);
        testSetAndGetRotation(Rotation.WEST);
    }

    void testSetAndGetRotation(Rotation rotation) {
        var rotationRenderComponent = new RotationRenderComponent(textureAtlas, Rotation.NORTH);

        reset(textureAtlas);

        rotationRenderComponent.setRotation(rotation);

        assertEquals(rotationRenderComponent.getRotation(), rotation);

        verify(textureAtlas, times(1)).findRegion(rotation.toString());
    }
}