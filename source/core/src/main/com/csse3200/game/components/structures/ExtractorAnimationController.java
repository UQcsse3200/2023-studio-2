package com.csse3200.game.components.structures;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;


/**
     * This class listens to events relating to the state of the extractor and plays
     *  the extracting animation when the broken extractor is fixed, otherwise the broken
     *  extractor remains 'un-animated'.
     */
    public class ExtractorAnimationController extends Component {
        AnimationRenderComponent animator;

        /**
         * Adds action listener to the extractor
         */

        @Override
        public void create() {
            super.create();
            animator = this.entity.getComponent(AnimationRenderComponent.class);
            updatingHealth(0); // Placeholder integer
            entity.getEvents().addListener("updateHealth", this::updatingHealth);
        }

        void updatingHealth(int health) {
            CombatStatsComponent healthStats = entity.getComponent(CombatStatsComponent.class);
            if (healthStats.isDead()) {
                animator.startAnimation("animateBroken");
                entity.getEvents().trigger("stopEffect", "rubble");
            } else {
                animator.startAnimation("animateExtracting");
                entity.getEvents().trigger("startEffect", "rubble");
            }
        }
    }


