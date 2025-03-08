package pepse.pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import static pepse.Constants.*;

/**
 * The sky of the game.
 */
public class Sky {
    /**
     * Creates a sky GameObject.
     * @param windowDimensions The dimensions of the window.
     * @return The sky GameObject.
     */
    public static GameObject create(Vector2 windowDimensions) {
        GameObject sky = new GameObject(SKY_TOP_LEFT_POSITION,windowDimensions,
                                        new RectangleRenderable(BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag(SKY_TAG);
        return sky;
    }
}
