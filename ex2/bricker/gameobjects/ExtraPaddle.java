package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The class used for the extra paddle created as part of the extra paddle strategy
 */
public class ExtraPaddle extends Paddle{
    private int collisionCounter;
    /**
     * Construct a new ExtraPaddle instance.
     *
     * @param topLeftCorner    Position of top left corner the extra paddle
     * @param dimensions       Width and height of the extra paddle
     * @param renderable       The image of the extra paddle
     * @param inputListener    Listener for user input
     * @param windowDimensions Dimensions of the game window
     * @param WallWidth        Width of the walls
     */
    public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                       UserInputListener inputListener, Vector2 windowDimensions, float WallWidth) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, WallWidth);
        collisionCounter = 0;
    }

    /**
     * The function that activates the collision logic
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionCounter++;
    }

    /**
     * @return the collision counter
     */
    public int getCollisionCounter() {return collisionCounter;}
}
