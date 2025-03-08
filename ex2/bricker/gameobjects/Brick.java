package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The class used for a brick in the game
 */
public class Brick extends GameObject {
    private final CollisionStrategy collisionStrategy;

    /**
     * A constructor for the brick
     * @param topLeftCorner the top left corner of the brick
     * @param dimensions the dimensions of the brick
     * @param renderable the image of the brick
     * @param collisionStrategy the strategy that would be activated once something collides with the brick
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, CollisionStrategy
            collisionStrategy) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;

    }

    /**
     * The function that activates the brick's collision strategy
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        collisionStrategy.onCollision(this,other);
    }
}
