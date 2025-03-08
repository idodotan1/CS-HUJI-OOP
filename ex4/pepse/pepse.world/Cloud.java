package pepse.pepse.world;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.pepse.util.ColorSupplier;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import static pepse.Constants.*;

/**
 * Represents a cloud in the game. A cloud consists of multiple blocks that can move across the screen,
 * and it can also start raining by turning some of the cloud blocks into rain blocks.
 */
public class Cloud extends GameObject {
    private final Block[][] cloudBlocks;
    private final BiConsumer<GameObject, Integer> addGameObject;
    private final Consumer<GameObject> removeGameObject;
    private final Vector2 cloudInitialPosition;
    private final Vector2 cloudEndPosition;

    /**
     * Constructs a new cloud object.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param addGameObject A consumer that adds a GameObject to the game at a specific layer.
     * @param removeGameObject A consumer that removes a GameObject from the game.
     */
    public Cloud(Vector2 windowDimensions, BiConsumer<GameObject, Integer> addGameObject,
                 Consumer<GameObject> removeGameObject) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.addGameObject = addGameObject;
        this.removeGameObject = removeGameObject;
        cloudBlocks = new Block[CLOUD_MATRIX_SIZE][CLOUD_MATRIX_SIZE];
        Vector2 cloudSize = new Vector2((BLOCK_SIZE * CLOUD_MATRIX_SIZE), (BLOCK_SIZE * CLOUD_MATRIX_SIZE));
        cloudInitialPosition = new Vector2(-cloudSize.x(), windowDimensions.y() / CLOUD_HEIGHT_FACTOR);
        cloudEndPosition = new Vector2(windowDimensions.x() + cloudSize.x(),
                windowDimensions.y() / CLOUD_HEIGHT_FACTOR);
        createCloud(new RectangleRenderable(ColorSupplier.approximateMonoColor(BASE_CLOUD_COLOR)));
        this.setTag(CLOUD_TAG);
        this.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        createCloudTransition();
    }

    /**
     * Starts the rain effect by turning some of the cloud blocks into rain blocks.
     * The rain blocks fall with gravity and fade out as they fall.
     */
    public void startRaining() {
        Block[][] rainBlocks = new Block[CLOUD_MATRIX_SIZE][CLOUD_MATRIX_SIZE];
        for (int i = 0; i < CLOUD_MATRIX_SIZE; i++) {
            for (int j = 0; j < CLOUD_MATRIX_SIZE; j++) {
                if (CLOUD_MATRIX.get(i).get(j) == 1) {
                    if (Math.random() < CHANCE_FOR_RAIN_BLOCK) {
                        Vector2 blockPosition = cloudBlocks[i][j].getCenter();
                        rainBlocks[i][j] = new Block(blockPosition,
                                new RectangleRenderable(ColorSupplier.approximateColor(RAIN_COLOR)));
                        addGameObject.accept(rainBlocks[i][j], Layer.BACKGROUND);
                        rainBlocks[i][j].transform().setAccelerationY(RAIN_GRAVITY);
                        createRainTransition(rainBlocks[i][j]);
                    }
                }
            }
        }
    }

    private void createCloud(Renderable cloudRenderable) {
        for (int i = 0; i < CLOUD_MATRIX_SIZE; i++) {
            for (int j = 0; j < CLOUD_MATRIX_SIZE; j++) {
                if (CLOUD_MATRIX.get(i).get(j) == 1) {
                    Vector2 blockPosition = cloudInitialPosition.add(new Vector2(j * BLOCK_SIZE, i * BLOCK_SIZE));
                    cloudBlocks[i][j] = new Block(blockPosition, cloudRenderable);
                    addGameObject.accept(cloudBlocks[i][j], CLOUD_LAYER);
                }
            }
        }
    }

    private void changeCloudBlockLocation(float deltaX) {
        for (int i = 0; i < CLOUD_MATRIX_SIZE; i++) {
            for (int j = 0; j < CLOUD_MATRIX_SIZE; j++) {
                if (cloudBlocks[i][j] != null) {
                    cloudBlocks[i][j].setCenter(cloudBlocks[i][j].getCenter().add(new Vector2(deltaX, 0)));
                }
            }
        }
    }

    private void createCloudTransition() {
        Transition<Float> cloudTransition = new Transition<Float>(this,
                this::changeCloudBlockLocation,
                INITIAL_CLOUD_TRANSITION_VALUE,
                cloudEndPosition.x(),
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                CLOUD_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_LOOP,
                null);
    }

    private void createRainTransition(Block rainBlock) {
        Transition<Float> RainTransition = new Transition<>(rainBlock,
                rainBlock.renderer()::setOpaqueness,
                INITIAL_RAIN_OPAQUENESS,
                FINAL_RAIN_OPAQUENESS,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                RAIN_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_ONCE,
                () -> removeGameObject.accept(rainBlock));
    }
}
