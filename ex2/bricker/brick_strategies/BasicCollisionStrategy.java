package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * The class for the basic collision strategy
 */
public class BasicCollisionStrategy implements CollisionStrategy {
    private final GameObjectCollection gameObject;
    private final Counter brickCounter;

    /**
     * The constructor for the basic collision strategy
     * @param gameObject The game objects collection
     * @param brickCounter The brick counter
     */
    public BasicCollisionStrategy(GameObjectCollection  gameObject, Counter brickCounter) {
        this.gameObject = gameObject;
        this.brickCounter = brickCounter;
    }

    /**
     * The function that activates the basic collision strategy
     * @param thisObj The brick otherObj collides with
     * @param otherObj The object that collides with the brick
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        if(this.gameObject.removeGameObject(thisObj)){
            this.brickCounter.decrement();
        }
    }
}
