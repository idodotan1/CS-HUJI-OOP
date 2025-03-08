package pepse.pepse.world.pepse.world.trees;

import danogl.util.Vector2;
import java.util.*;
import java.util.function.Function;
import static pepse.Constants.*;

/**
 * Singleton class that manages the creation of trees within a specified range.
 * It uses the ground height and seed to generate trees in a consistent manner.
 */
public class Flora {
    private final Function<Integer, Float> groundHeightAt;
    private final int seed;
    private static Flora instance;

    /**
     * Private constructor to initialize Flora with ground height function and seed.
     *
     * @param groundHeightAt The function that provides the ground height at a specific x-coordinate
     * @param seed The seed value for random generation
     */
    private Flora(Function<Integer, Float> groundHeightAt, int seed) {
        this.groundHeightAt = groundHeightAt;
        this.seed = seed;
    }

    /**
     * Creates the Flora singleton instance.
     *
     * @param groundHeightAt The function that provides the ground height at a specific x-coordinate
     * @param seed The seed value for random generation
     */
    public static void createInstance(Function<Integer, Float> groundHeightAt, int seed) {
        if (instance == null) {
            instance = new Flora(groundHeightAt, seed);
        }
    }

    /**
     * Retrieves the Flora singleton instance.
     *
     * @return The singleton instance of Flora
     * @throws IllegalStateException if the instance has not been created yet
     */
    public static Flora getInstance() throws IllegalStateException {
        if (instance == null) {
            throw new IllegalStateException("Flora instance has not been created. " +
                    "Call createInstance first.");
        }
        return instance;
    }

    /**
     * Creates a list of trees within a specified horizontal range.
     * The trees are created based on a random chance determined by the seed.
     *
     * @param minX The minimum x-coordinate of the range
     * @param maxX The maximum x-coordinate of the range
     * @return A list of trees created within the specified range
     */
    public List<Tree> createInRange(int minX, int maxX) {
        int startX = Math.min(minX, maxX) / BLOCK_SIZE * BLOCK_SIZE;
        int endX = Math.max(minX, maxX) / BLOCK_SIZE * BLOCK_SIZE;
        List<Tree> trees = new ArrayList<>();
        Random random = new Random(seed);
        for (int x = startX; x <= endX; x += TRUNK_WIDTH) {
            random.setSeed(Objects.hash(x, seed));
            if (random.nextDouble() < CHANCE_OF_TREE) {
                float groundHeight = groundHeightAt.apply(x);
                float treeHeight = random.nextFloat() * TREE_HEIGHT_FACTOR + TREE_HEIGHT_ADDING;
                trees.add(new Tree(new Vector2(x, groundHeight), (int) treeHeight, seed));
                x += TRUNK_WIDTH;
            }
        }
        return trees;
    }
}
