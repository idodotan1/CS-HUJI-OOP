package pepse.pepse.world;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import static pepse.Constants.*;

/**
 * The energy display of the game.
 * The energy display is a bar that shows the energy of the player,represented by a green over red bar
 * that fills up as the player gains energy and empties as the player loses energy.
 */
public class EnergyDisplay {
    /**
     * Creates an energy display GameObject.
     * @param energySupplier A supplier of the energy of the player.
     * @param addGameObject A consumer that adds a GameObject to the game.
     */
    public EnergyDisplay(Supplier<Float> energySupplier,
                         BiConsumer<GameObject,Integer> addGameObject) {
        GameObject backgroundBar = new GameObject(ENERGY_DISPLAY_POSITION,
                BAR_SIZE, new RectangleRenderable(BACKGROUND_COLOR));
        GameObject energyBar = new GameObject(ENERGY_DISPLAY_POSITION,
                BAR_SIZE, new RectangleRenderable(ENERGY_COLOR));
        energyBar.addComponent(deltaTime -> {
            float energyPercentage = energySupplier.get()/ FULL_PERCENTAGE;
            energyBar.setDimensions(new Vector2(BAR_SIZE.x() * energyPercentage, BAR_SIZE.y()));
        });
        backgroundBar.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        energyBar.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        addGameObject.accept(backgroundBar, Layer.BACKGROUND);
        addGameObject.accept(energyBar, ENERGY_BAR_LAYER);
    }
}
