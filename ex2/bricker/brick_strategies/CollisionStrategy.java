package bricker.brick_strategies;

import danogl.GameObject;


/**
 * The interface for a collision strategy a brick can get
 */
public interface CollisionStrategy {
    /**
     * A function that gets two objects and activates a collision strategy
     * @param thisObj The object otherObj collides with
     * @param otherObj The object that collides with thisObj
     */
    void onCollision(GameObject thisObj, GameObject otherObj);
}
