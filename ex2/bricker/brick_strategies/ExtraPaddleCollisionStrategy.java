package bricker.brick_strategies;

import bricker.gameobjects.ExtraPaddle;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;


/**
 * The class for the extra paddle strategy
 */
public class ExtraPaddleCollisionStrategy implements CollisionStrategy{
    private final GameObjectCollection gameObject;
    private final ExtraPaddle extraPaddle;
    private final Counter brickCounter;

    /**
     * The constructor for the extra paddle strategy
     * @param gameObject The game objects collection
     * @param extraPaddle The extra paddle
     * @param brickCounter The brick counter
     */
    public ExtraPaddleCollisionStrategy(GameObjectCollection  gameObject, ExtraPaddle extraPaddle,
                                        Counter brickCounter) {
        this.gameObject = gameObject;
        this.extraPaddle = extraPaddle;
        this.brickCounter = brickCounter;
    }

    /**
     * The function that activates the collision strategy
     * @param thisObj The brick it collides with
     * @param otherObj the object that collides with the brick
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        if(this.gameObject.removeGameObject(thisObj)) {
            brickCounter.decrement();
            if (extraPaddle.getTag().equals("NotInGame")) {
                extraPaddle.setTag("InGame");
                gameObject.addGameObject(extraPaddle);
            }
        }
    }

}