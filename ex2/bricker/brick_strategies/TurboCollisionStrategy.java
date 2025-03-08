package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * The class used for the turbo ball collision strategy
 */
public class TurboCollisionStrategy implements CollisionStrategy{
    private static final float TURBO_SPEED_FACTOR = 1.4f;
    private final GameObjectCollection gameObject;
    private final Renderable turboBallImage;
    private final Counter brickCounter;

    /**
     * The constructor for the turbo collision strategy
     * @param gameObject The game objects collection
     * @param turboBallImage The image of the turbo ball
     * @param brickCounter The brick counter
     */
    public TurboCollisionStrategy(GameObjectCollection  gameObject, Renderable turboBallImage,
                                  Counter brickCounter) {
        this.gameObject = gameObject;
        this.turboBallImage = turboBallImage;
        this.brickCounter = brickCounter;
    }

    /**
     * The function that activates the collision strategy
     * @param thisObj The brick that holds that strategy
     * @param otherObj The object that collides with the brick
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        if(this.gameObject.removeGameObject(thisObj)) {
            brickCounter.decrement();
            if (otherObj.getTag().equals("MainBall")) {
                Vector2 newVel = otherObj.getVelocity().mult(TURBO_SPEED_FACTOR);
                otherObj.setVelocity(newVel);
                otherObj.renderer().setRenderable(turboBallImage);
                otherObj.setTag("TurboBall");
            }
        }
    }


}
