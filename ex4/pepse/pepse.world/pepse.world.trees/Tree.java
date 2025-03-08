package pepse.pepse.world.pepse.world.trees;

import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.pepse.world.Block;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import static pepse.Constants.*;

/**
 * Represents a tree consisting of a trunk, leaves, and fruits.
 * The tree can have a specified height and position, and supports actions like swaying leaves.
 */
public class Tree {
    private final Block[] trunk;
    private final List<Leaf> leaves;
    private final List<Fruit> fruits;
    private final Vector2 trunkBottomLeftCorner;
    private final int trunkHeight;
    private final int seed;

    /**
     * Constructs a new tree at a specified position with a given trunk height and random seed.
     * The tree's trunk, leaves, and fruits are initialized here.
     *
     * @param trunkBottomLeftCorner The bottom-left corner position of the tree trunk
     * @param trunkHeight The height of the tree trunk
     * @param seed The random seed used to generate the tree
     */
    public Tree(Vector2 trunkBottomLeftCorner, int trunkHeight, int seed) {
        Renderable trunkRenderable = new RectangleRenderable(TRUNK_COLOR);
        this.trunkBottomLeftCorner = trunkBottomLeftCorner;
        this.trunkHeight = trunkHeight;
        this.seed = seed;
        trunk = new Block[trunkHeight];
        for (int i = 0; i < trunkHeight; i++) {
            Vector2 currentBlockTopLeftCorner =
                    trunkBottomLeftCorner.add(new Vector2(0, -(i + 1) * BLOCK_SIZE));
            Block trunkBlock = new Block(currentBlockTopLeftCorner, trunkRenderable);
            trunkBlock.setTag(TRUNK_TAG);
            trunk[i] = trunkBlock;
        }
        this.leaves = new ArrayList<>();
        this.fruits = new ArrayList<>();
        createTop();
    }

    /**
     * Returns the trunk of the tree as an array of blocks.
     *
     * @return The trunk blocks
     */
    public Block[] getTrunk() {
        return trunk;
    }

    /**
     * Returns the list of leaves attached to the tree.
     *
     * @return The list of leaves
     */
    public List<Leaf> getLeaves() {
        return leaves;
    }

    /**
     * Returns the list of fruits growing on the tree.
     *
     * @return The list of fruits
     */
    public List<Fruit> getFruits() {
        return fruits;
    }

    /**
     * Makes the leaves of the tree sway. The swaying happens with a delay for each leaf
     * to create a staggered effect.
     */
    public void swayLeaves() {
        float delay = 0;
        for (Leaf leaf : leaves) {
            leaf.sway(delay);
            delay += LEAF_SWAY_DELAY; // Staggered start for each leaf
        }
    }

    private void createTop() {
        Vector2 leafSquareTopLeft = trunkBottomLeftCorner.subtract(
                new Vector2((float) (BLOCK_SIZE * LEAF_SQUARE_SIZE) / 2,
                        (trunkHeight + (float) LEAF_SQUARE_SIZE / 2) * BLOCK_SIZE));
        Random random = new Random(Objects.hash(trunkBottomLeftCorner.x(), seed));
        for (int i = 0; i < LEAF_SQUARE_SIZE; i++) {
            for (int j = 0; j < LEAF_SQUARE_SIZE; j++) {
                if (random.nextDouble() < CHANCE_OF_LEAF) {
                    Leaf leaf = new Leaf(leafSquareTopLeft.add(new Vector2(i * BLOCK_SIZE, j * BLOCK_SIZE)));
                    leaf.setTag(LEAF_TAG);
                    leaves.add(leaf);
                }
                if (random.nextDouble() < CHANCE_OF_FRUIT) {
                    Fruit fruit = new Fruit(leafSquareTopLeft.add(new Vector2(i * BLOCK_SIZE, j * BLOCK_SIZE)));
                    fruit.setTag(FRUIT_TAG);
                    fruits.add(fruit);
                }
            }
        }
    }
}
