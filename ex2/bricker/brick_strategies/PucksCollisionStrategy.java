package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;

/**
 * The class used for the puck collision strategy
 */
public class PucksCollisionStrategy implements CollisionStrategy {
    private static final int NUM_OF_PUCKS = 2;
    private static final float PUCK_SIZE_FACTOR = 0.75f;
    private final Vector2 puckSize;
    private final GameObjectCollection gameObject;
    private final Renderable puckRenderer;
    private final Sound sound;
    private final Ball[] pucks;
    private final float ballSpeed;
    private final Counter pucksCounter;
    private final Counter brickCounter;
    /**
     * The constructor of the puck collision strategy
     * @param gameObject the collection of the game objects
     * @param puckRenderer the image of the puck
     * @param sound the sound of the puck
     * @param pucks the array that holds all the puck
     * @param ballSize the size of the puck
     * @param speed the speed of the puck
     * @param pucksCounter the counter of all the pucks
     * @param brickCounter the counter of all the bricks
     */
    public PucksCollisionStrategy(GameObjectCollection  gameObject, Renderable puckRenderer,
                                  Sound sound, Ball[] pucks, Vector2 ballSize, float speed,
                                  Counter pucksCounter, Counter brickCounter) {
        this.gameObject = gameObject;
        this.puckRenderer = puckRenderer;
        this.sound = sound;
        this.pucks = pucks;
        this.puckSize = ballSize.mult(PUCK_SIZE_FACTOR);
        this.ballSpeed = speed;
        this.pucksCounter = pucksCounter;
        this.brickCounter = brickCounter;
    }

    /**
     * The function that activates the puck strategy
     * @param thisObj The brick it collides with
     * @param otherObj The object that collides with the brick
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        Vector2 brickPosition = thisObj.getCenter();
        if(this.gameObject.removeGameObject(thisObj)) {
            this.brickCounter.decrement();
            for (int i = pucksCounter.value(); i < NUM_OF_PUCKS + pucksCounter.value(); i++) {
                Random random = new Random();
                double angle = random.nextDouble() * Math.PI;
                float velocityX = (float) Math.cos(angle) * ballSpeed;
                float velocityY = (float) -Math.abs(Math.sin(angle)) * ballSpeed;
                Vector2 newVel = new Vector2(velocityX, velocityY);
                pucks[i] = new Ball(brickPosition, puckSize, puckRenderer, sound);
                pucks[i].setTag("Puck");
                pucks[i].setVelocity(newVel);
                this.gameObject.addGameObject(pucks[i]);
            }
            pucksCounter.increaseBy(NUM_OF_PUCKS);
        }
    }
}
