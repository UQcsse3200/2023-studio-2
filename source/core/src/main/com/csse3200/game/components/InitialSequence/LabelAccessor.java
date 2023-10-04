package com.csse3200.game.components.InitialSequence;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * An accessor class for tweening the alpha (transparency) property of a Label using the Universal Tween Engine.
 */
public class LabelAccessor implements TweenAccessor<Label> {
    /**
     * Tween type for changing the alpha property.
     */
    public static final int ALPHA = 1;

    @Override
    public int getValues(Label target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case ALPHA:
                returnValues[0] = target.getColor().a;
                return 1;

            default:
                throw new UnsupportedOperationException("Unsupported tween type: " + tweenType);
        }
    }

    @Override
    public void setValues(Label target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case ALPHA:
                target.setColor(target.getColor().r, target.getColor().g, target.getColor().b, newValues[0]);
                break;

            default:
                throw new UnsupportedOperationException("Unsupported tween type: " + tweenType);
        }
    }
}
