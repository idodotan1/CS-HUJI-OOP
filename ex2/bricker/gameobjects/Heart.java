package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A class used for a heart(representing a life) in the game
 */
public class Heart extends GameObject {
    private static final int HEART_SIZE = 20;

    /**
     * A constructor for the heart
     * @param topLeftCorner the location of the top left corner of the heart
     * @param dimensions the dimensions of the heart
     * @param heartsImage the image of the heart
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable heartsImage) {
        super(topLeftCorner, dimensions, heartsImage);
    }

    /**
     *
     * @return the heart size
     */
    public static int getHeartSize() {
        return HEART_SIZE;
    }

    /**
     * Makes the heart collide only with the user paddle
     * @param other The other GameObject.
     * @return True if it should collide with the object, false otherwise
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return super.shouldCollideWith(other) && other.getTag().equals("UserPaddle");
    }

    /**
     * Activates the heart collision's logic
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.setTag("Collided");
    }
}
