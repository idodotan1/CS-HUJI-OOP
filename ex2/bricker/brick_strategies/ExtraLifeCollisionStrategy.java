package bricker.brick_strategies;

import bricker.gameobjects.Heart;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;


/**
 * The class for the extra  life strategy
 */
public class ExtraLifeCollisionStrategy implements CollisionStrategy {
    private static final int FALLING_HEART_SPEED = 100;
    private final GameObjectCollection gameObject;
    private final Renderable heartRenderer;
    private final Heart[] fallingHearts;
    private final Counter fallingHeartsCounter;
    private final Counter brickCounter;

    /**
     * The constructor for the extra life strategy
     * @param gameObject The game objects collection
     * @param heartRenderer The image of the heart
     * @param fallingHearts The array that holds the falling heart
     * @param fallingHeartsCounter The falling hearts counter
     * @param brickCounter The brick counter
     */
    public ExtraLifeCollisionStrategy(GameObjectCollection  gameObject,
                                      Renderable heartRenderer, Heart[] fallingHearts,
                                      Counter fallingHeartsCounter, Counter brickCounter) {
        this.gameObject = gameObject;
        this.heartRenderer = heartRenderer;
        this.fallingHearts = fallingHearts;
        this.fallingHeartsCounter = fallingHeartsCounter;
        this.brickCounter = brickCounter;
    }

    /**
     * The function that activates the collision strategy
     * @param thisObj The brick an object 2 collides with
     * @param otherObj The object that collides with the brick
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        Vector2 brickPosition = thisObj.getCenter();
        if(this.gameObject.removeGameObject(thisObj)) {
            brickCounter.decrement();
            fallingHearts[fallingHeartsCounter.value()] = new Heart(brickPosition, new
                    Vector2(Heart.getHeartSize(), Heart.getHeartSize()), heartRenderer);
            fallingHearts[fallingHeartsCounter.value()].setVelocity(new Vector2(0, FALLING_HEART_SPEED));
            this.gameObject.addGameObject(fallingHearts[fallingHeartsCounter.value()]);
            fallingHeartsCounter.increment();
        }


    }

}