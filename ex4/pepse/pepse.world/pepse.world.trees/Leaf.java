package pepse.pepse.world.pepse.world.trees;

import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.pepse.util.ColorSupplier;
import pepse.pepse.world.Block;
import static pepse.Constants.*;

/**
 * Represents a leaf that can sway back and forth on the tree.
 * A leaf is visually represented by a block, and it can animate by changing its angle and size.
 */
public class Leaf extends Block {

    /**
     * Constructs a new leaf at a specified position with a color.
     * The leaf is represented as a block with the defined color.
     *
     * @param position The position where the leaf will be placed
     */
    public Leaf(Vector2 position) {
        super(position, new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR)));
    }

    /**
     * Makes the leaf sway back and forth with a smooth transition.
     * The swaying effect is achieved by animating the leaf's angle and size.
     *
     * @param delay The delay before starting the sway animation
     */
    public void sway(float delay) {
        Vector2 leafSize = new Vector2(BLOCK_SIZE, BLOCK_SIZE);
        new ScheduledTask(this, delay, true, () -> {
            createAngleTransition();
            createSizeTransition(leafSize);
        });
    }

    private void createAngleTransition() {
        float currentAngle = this.renderer().getRenderableAngle();
        float targetAngle = currentAngle + SWAY_ANGLE;

        new Transition<>(this,
                (Float angle) -> this.renderer().setRenderableAngle(angle),
                currentAngle, targetAngle,
                Transition.LINEAR_INTERPOLATOR_FLOAT, SWAY_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    private void createSizeTransition(Vector2 leafSize) {
        Vector2 targetSize = leafSize.subtract(new Vector2((float) BLOCK_SIZE / 2, 0));
        new Transition<>(this,
                this::setDimensions,
                leafSize, targetSize,
                Transition.LINEAR_INTERPOLATOR_VECTOR, SWAY_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }
}
