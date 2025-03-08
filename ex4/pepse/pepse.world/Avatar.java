package pepse.pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Vector2;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import static pepse.Constants.*;

/**
 * Represents the Avatar character in the game. The avatar can move left, right, and jump,
 * while consuming energy. The avatar's energy can also be replenished by picking up fruits.
 */
public class Avatar extends GameObject {
    private final ImageReader imageReader;
    private final Map<AnimationState, AnimationRenderable> animations = new EnumMap<>(AnimationState.class);
    private float energy;
    private final List<Runnable> onJumpActions;
    private boolean isJumping;
    private boolean isMovingLeft;
    private final UserInputListener inputListener;

    /**
     * Constructs an Avatar object with the specified starting position, input listener, and image reader.
     *
     * @param topLeftCorner The top-left corner position of the avatar.
     * @param inputListener The input listener used to handle user inputs.
     * @param imageReader The image reader used to read image files for the avatar's animations.
     */
    public Avatar(Vector2 topLeftCorner, UserInputListener inputListener, ImageReader imageReader) {
        super(topLeftCorner, INITIAL_AVATAR_SIZE, imageReader.readImage(FIRST_IMAGE_PATH, true));
        this.imageReader = imageReader;
        createAnimations();
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        energy = MAX_ENERGY;
        isJumping = false;
        this.onJumpActions = new ArrayList<>();
        this.setTag(AVATAR_TAG);
    }

    /**
     * Updates the avatar state based on the delta time, including movement and energy consumption.
     *
     * @param deltaTime The time delta since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        handleMovement();
    }

    /**
     * Handles collision events when the avatar collides with another game object.
     * Specifically handles interaction with blocks, trunks, and fruits.
     *
     * @param other The other game object the avatar collided with.
     * @param collision The collision details.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(other.getTag().equals("block")){
            this.transform().setVelocityY(0);  // Stop vertical velocity when hitting a block
        }
        if (other.getTag().equals("Trunk")) {
            this.transform().setVelocityX(0);  // Stop horizontal velocity when hitting a trunk
        }
        handleFruitPicking(other);
    }

    /**
     * Returns the current energy of the avatar.
     *
     * @return The avatar's current energy.
     */
    public float getEnergy() {
        return energy;
    }

    /**
     * Adds an action to be executed when the avatar jumps.
     *
     * @param action The action to be executed on jump.
     */
    public void addActionOnJump(Runnable action) {
        onJumpActions.add(action);
    }

    private void handleMovement() {
        if (isHorizontalMovement()) {
            handleHorizontalMovement();
        } else if (isJumpRequested()) {
            handleJump();
        } else if (isJumping) {
            handleJumping();
        } else {
            handleIdleState();
        }
    }

    private boolean isHorizontalMovement() {
        return (inputListener.isKeyPressed(KeyEvent.VK_LEFT) || inputListener.isKeyPressed(KeyEvent.VK_RIGHT))
                && energy >= HORIZONTAL_MOVEMENT_ENERGY_COST;
    }

    private void handleHorizontalMovement() {
        float xVel = 0;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            xVel -= VELOCITY_X;
            renderer().setIsFlippedHorizontally(true);
            isMovingLeft = true;
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            xVel += VELOCITY_X;
            if (isMovingLeft) {
                renderer().setIsFlippedHorizontally(false);
                isMovingLeft = false;
            }
        }
        transform().setVelocityX(xVel);
        renderer().setRenderable(animations.get(AnimationState.RUN));
        if (!isJumping) {
            this.energy -= HORIZONTAL_MOVEMENT_ENERGY_COST;
        }
    }

    private boolean isJumpRequested() {
        return inputListener.isKeyPressed(KeyEvent.VK_SPACE) && !isJumping && energy >= JUMP_ENERGY_COST;
    }

    private void handleJump() {
        transform().setVelocityY(VELOCITY_Y);
        this.energy -= JUMP_ENERGY_COST;
        isJumping = true;
        renderer().setRenderable(animations.get(AnimationState.JUMP));
        executeActionsOnJump();
    }

    private void handleJumping() {
        if (getVelocity().y() == 0) {
            isJumping = false;
        }
    }

    private void handleIdleState() {
        renderer().setRenderable(animations.get(AnimationState.IDLE));
        transform().setVelocityX(0);
        if (this.getVelocity().magnitude() == 0 && energy < MAX_ENERGY) {
            this.energy += IDLE_ENERGY_REGENERATION;
        }
    }

    private void createAnimations() {
        animations.put(AnimationState.IDLE, new AnimationRenderable(IDLE_IMAGES_PATHS,
                imageReader, true, TIME_BETWEEN_ANIMATION_CLIPS));
        animations.put(AnimationState.JUMP, new AnimationRenderable(JUMP_IMAGES_PATHS,
                imageReader, true, TIME_BETWEEN_ANIMATION_CLIPS));
        animations.put(AnimationState.RUN, new AnimationRenderable(RUN_IMAGES_PATHS,
                imageReader, true, TIME_BETWEEN_ANIMATION_CLIPS));
    }

    private enum AnimationState {
        IDLE,
        JUMP,
        RUN
    }

    private void handleFruitPicking(GameObject other) {
        if (other.getTag().equals(FRUIT_TAG) &&
                other.renderer().getOpaqueness() != PICKED_FRUIT_OPACITY) {
            other.renderer().setOpaqueness(PICKED_FRUIT_OPACITY); // Mark fruit as picked
            if (this.energy + FRUIT_EXTRA_ENERGY > MAX_ENERGY) {
                this.energy = MAX_ENERGY;  // Cap energy to max energy
            } else {
                this.energy += FRUIT_EXTRA_ENERGY; // Increase energy
            }
            new ScheduledTask(other, FRUIT_REGENERATE_TIME, false, () -> {
                other.renderer().setOpaqueness(UNPICKED_FRUIT_OPACITY); // Regenerate fruit appearance
            });
        }
    }

    private void executeActionsOnJump() {
        for (Runnable action : onJumpActions) {
            action.run();
        }
    }
}
