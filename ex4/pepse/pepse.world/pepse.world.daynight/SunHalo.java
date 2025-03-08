package pepse.pepse.world.pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import static pepse.Constants.*;

/**
 * Creates a halo around the sun to simulate its glowing effect.
 */
public class SunHalo {

    /**
     * Creates the sun's halo object that follows the sun's position.
     *
     * @param sun The sun GameObject that the halo should follow
     * @return A GameObject representing the sun's halo
     */
    public static GameObject create(GameObject sun) {
        GameObject sunHalo = new GameObject(Vector2.ZERO, SUN_HALO_SIZE,
                new OvalRenderable(SUN_HALO_COLOR));
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag(SUN_HALO_TAG);
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));
        return sunHalo;
    }
}
