package pepse.pepse.world.pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import java.awt.*;
import static pepse.Constants.*;

/**
 * Simulates the night effect by gradually increasing and decreasing a dark overlay's opacity.
 */
public class Night {

    /**
     * Creates a GameObject to simulate night by overlaying a dark semi-transparent rectangle.
     *
     * @param windowDimensions The dimensions of the game window
     * @param cycleLength      The duration of a full day-night cycle
     * @return A GameObject representing the night effect
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions,
                new RectangleRenderable(Color.BLACK));
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(NIGHT_TAG);
        new Transition<>(night,
                night.renderer()::setOpaqueness,
                INITIAL_NIGHT_OPACITY,
                FINAL_NIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                cycleLength / HALF_DAY_FACTOR,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
        return night;
    }
}
