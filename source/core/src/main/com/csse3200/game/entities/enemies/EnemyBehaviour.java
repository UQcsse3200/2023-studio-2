package com.csse3200.game.entities.enemies;

/**
 * Determines the enemy behaviour on what target to prioritise.
 *
 * PTE stands for Player Targeting Enemy, which means the enemy will prioritise player entities
 * DTE stands for Destructible Targeting Enemy, which means the enemy will prioritise destructibles,
 * i.e. Extractors
 *
 * The priority is set in the Enemy factory where the target is determined by its HitBoxComponent layer,
 * where if the HitBoxComponent is set as PhysicsLayer.PLAYER then the PTEs will target it,
 * and for DTEs the PhysicsLayer.STRUCTURE should be applied.
 */
public enum EnemyBehaviour {
    PTE,
    DTE
}
