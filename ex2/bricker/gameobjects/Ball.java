package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The class used for a ball in the game
 */
public class Ball extends GameObject {
    private final Sound collisionSound;
    private int collisionCounter;

    /**
     * The ball constructor
     * @param topLeftCorner top left corner of the ball
     * @param dimensions the dimensions of the ball
     * @param renderable the image of the ball
     * @param collisionSound the sound of the ball
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        collisionCounter = 0;
    }

    /**
     * This function manages the collision of the ball with other objects
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionCounter++;
        collisionSound.play();
    }

    /**
     *
     * @return the collision counter of the ball
     */
    public int getCollisionCounter() {
        return collisionCounter;
    }

}
