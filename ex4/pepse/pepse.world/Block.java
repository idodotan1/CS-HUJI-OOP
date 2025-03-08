package pepse.pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import static pepse.Constants.*;

/**
 * A block GameObject.
 * Blocks are basic GameObjects that other GameObjects can be constructed from.
 */
public class Block extends GameObject {
    /**
     * Creates a block GameObject.
     * @param topLeftCorner The top left corner of the block.
     * @param renderable The renderable of the block.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner,Vector2.ONES.mult(BLOCK_SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }

}
