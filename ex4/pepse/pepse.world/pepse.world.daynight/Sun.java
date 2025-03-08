package pepse.pepse.world.pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.pepse.world.Terrain;
import java.awt.*;
import static pepse.Constants.*;

/**
 * Handles the creation and movement of the sun in the game world.
 */
public class Sun {

    /**
     * Creates a sun GameObject that moves in a circular path to simulate day and night.
     *
     * @param windowDimensions The dimensions of the game window
     * @param cycleLength      The duration of a full sun cycle (day-night cycle)
     * @return A GameObject representing the sun
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        Vector2 cycleCenter = new Vector2(windowDimensions.x() / MID_SCREEN_FACTOR,
                Terrain.getInstance().getGroundHeightAtX0());
        Vector2 initialSunPosition = cycleCenter.add(INITIAL_SUN_POSITION_ADDING);
        GameObject sun = new GameObject(initialSunPosition, SUN_SIZE, new OvalRenderable(Color.YELLOW));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SUN_TAG);
        new Transition<>(sun,
                angle -> sun.setCenter(initialSunPosition.subtract(cycleCenter)
                        .rotated(angle).add(cycleCenter)),
                INITIAL_SUN_CYCLE_ANGLE,
                FINAL_SUN_CYCLE_ANGLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null);
        return sun;
    }
}
