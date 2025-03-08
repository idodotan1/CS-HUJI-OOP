package pepse.pepse.world.pepse.world.trees;

import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.pepse.world.Block;
import static pepse.Constants.*;

/**
 * Represents a fruit that can be placed on the tree.
 * The fruit is visually represented by an oval and has specific physical properties like mass.
 */
public class Fruit extends Block {

    /**
     * Constructs a new fruit at a specified position.
     * The fruit is represented by an oval with a set color.
     *
     * @param position The position of the fruit
     */
    public Fruit(Vector2 position) {
        super(position, new OvalRenderable(FRUIT_COLOR));
        physics().setMass(PICKED_FRUIT_MASS);
    }
}

