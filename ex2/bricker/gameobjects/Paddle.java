package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * The class used for the user paddle
 */
public class Paddle extends GameObject {
    private static final float MOVEMENT_SPEED = 400;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private final float wallWidth;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner   Position of the user paddle top left corner
     * @param dimensions      Width and height in window coordinates.
     * @param renderable      The image representing the user paddle
     * @param inputListener   Listener for user input.
     * @param windowDimensions Dimensions of the game window.
     * @param WallWidth    Width of the walls.
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions,
                      Renderable renderable, UserInputListener inputListener,
                      Vector2 windowDimensions, float WallWidth) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.wallWidth = WallWidth;
    }

    /**
     * Updates the status of the user paddle
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        setVelocity(movementDir.mult(MOVEMENT_SPEED));
        Vector2 newPosition = getTopLeftCorner().add(getVelocity().mult(deltaTime));
        float leftBoundary = wallWidth;
        float rightBoundary = windowDimensions.x() - wallWidth - getDimensions().x();
        float clampedX = Math.max(leftBoundary, Math.min(newPosition.x(), rightBoundary));
        setTopLeftCorner(new Vector2(clampedX, getTopLeftCorner().y()));
    }
}
