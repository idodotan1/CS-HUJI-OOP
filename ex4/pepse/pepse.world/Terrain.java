package pepse.pepse.world;
import java.util.ArrayList;
import java.util.List;
import pepse.pepse.util.ColorSupplier;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.pepse.util.NoiseGenerator;
import static pepse.Constants.*;

/**
 * Class representing the terrain of the game.
 * The terrain is generated using noise generator.
 */
public class Terrain {
    private static Terrain instance; // Singleton instance
    private final float groundHeightAtX0;
    private final int seed;

    /**
     * Private constructor to prevent instantiation from outside.
     */
    private Terrain(Vector2 windowDimensions, int seed)
    {
        this.seed = seed;
        this.groundHeightAtX0 = windowDimensions.y() * GROUND_HEIGHT_AT_X0_FACTOR;
    }

    /**
     * Static method to initialize the singleton instance of the Terrain class.
     * If the instance already exists, it won't recreate it.
     *
     * @param windowDimensions Dimensions of the game window.
     * @param seed Seed for noise generation.
     */
    public static void createInstance(Vector2 windowDimensions, int seed) {
        if (instance == null) {
            instance = new Terrain(windowDimensions, seed);
        }
    }

    /**
     * Static method to get the singleton instance of the Terrain class.
     * Ensure that `createInstance` has been called beforehand.
     *
     * @return The singleton instance of the Terrain class.
     * @throws IllegalStateException if `createInstance` has not been called.
     */
    public static Terrain getInstance() throws IllegalStateException {
        if (instance == null) {
            throw new IllegalStateException("Terrain instance has not been created. " +
                                            "Call createInstance first.");
        }
        return instance;
    }

    /**
     * Get the ground height at x=0.
     *
     * @return The ground height at x=0.
     */
    public float getGroundHeightAtX0() {
        return groundHeightAtX0;
    }

    /**
     * Get ground height at certain x value. using noise generation for randomization.
     *
     * @return The ground height value at given x.
     */
    public float groundHeightAt(float x) {
        NoiseGenerator noiseGenerator = new NoiseGenerator(this.seed, (int)groundHeightAtX0);
        float noise = (float) noiseGenerator.noise(x, BLOCK_SIZE * NOISE_FACTOR);
        return groundHeightAtX0 + noise;
    }

    /**
     * Create terrain blocks in a specified horizontal range.
     * For each column, a stack of blocks is generated starting at the terrain height, extending downward.
     *
     * @param minX The minimum x-coordinate of the range.
     * @param maxX The maximum x-coordinate of the range.
     * @return A list of Block objects representing the terrain in the specified range.
     */
    public List<Block> createInRange(int minX, int maxX) {
        List<Block> blocks = new ArrayList<>();
        int startX = Math.min(minX, maxX) / BLOCK_SIZE * BLOCK_SIZE;
        int endX = Math.max(minX, maxX) / BLOCK_SIZE * BLOCK_SIZE;
        for (int x = startX; x <= endX; x += BLOCK_SIZE) {
            float baseHeight = (float) Math.floor(groundHeightAt(x) / BLOCK_SIZE) * BLOCK_SIZE;
            for (int depth = 0; depth < TERRAIN_DEPTH; depth++) {
                float y = baseHeight + depth * BLOCK_SIZE;
                Block block = new Block(new Vector2(x, y),
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
                block.setTag(GROUND_TAG);
                blocks.add(block);
            }
        }
        return blocks;
    }
}
