package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import java.awt.*;

/**
 * The class used to display and manage the user's remaining lives
 */
public class LivesDisplay {
    private static final int MARGIN_BETWEEN_HEARTS = 5;
    private static final int GREEN_TEXT_FOR_LIVES = 3;
    private static final int YELLOW_TEXT_FOR_LIVES = 2;
    private final Heart[] hearts;
    private final int maxLives;
    private TextRenderable textRenderer;
    private final Renderable heartImage;
    private final int startingLives;
    private int livesRemaining;
    private final danogl.collisions.GameObjectCollection gameObjects;
    private final Vector2 textPosition;


    /**
     * Constructor for LivesDisplay.
     *
     * @param startingLives The initial number of lives.
     * @param heartsImage   The renderable image for the hearts.
     * @param textPosition  Position for the textual lives display.
     * @param gameObjects   GameObject collection to add hearts and text to.
     */
    public LivesDisplay(int maxLives,
                        int startingLives, Renderable heartsImage,
                        Vector2 textPosition, danogl.collisions.GameObjectCollection gameObjects) {
        this.maxLives = maxLives;
        this.startingLives = startingLives;
        this.livesRemaining = startingLives;
        this.hearts = new Heart[maxLives];
        this.gameObjects = gameObjects;
        this.heartImage = heartsImage;
        this.textPosition = textPosition;
    }


    /**
     * Resets the lives display to the initial state.
     */
    public void initializeHearts() {
        // Initialize hearts display
        for (int i = 0; i < startingLives; i++) {
            hearts[i] = new Heart(new Vector2(i * (Heart.getHeartSize() + MARGIN_BETWEEN_HEARTS),
                    MARGIN_BETWEEN_HEARTS),
                    new Vector2(Heart.getHeartSize(), Heart.getHeartSize()), heartImage);
            gameObjects.addGameObject(hearts[i]);
        }
        // Initialize text display
        textRenderer = new TextRenderable(String.valueOf(startingLives));
//        updateTextDisplay();
        GameObject textDisplay = new GameObject(textPosition, new Vector2(Heart.getHeartSize(),
                Heart.getHeartSize()), textRenderer);
        gameObjects.addGameObject(textDisplay);
    }


    /**
     * Resets the lives display to the initial state.
     */
    public void resetLives() {
        livesRemaining = startingLives;
        initializeHearts();
        updateTextDisplay();
    }

    /**
     * Returns the current number of lives remaining.
     *
     * @return Remaining lives.
     */
    public int getLivesRemaining() {
        return livesRemaining;
    }

    /**
     * Decrements one life from the user
     */
    public void decrement() {
        livesRemaining--;
        updateTextDisplay();
        gameObjects.removeGameObject(hearts[livesRemaining]);
    }

    /**
     * Increments one life to the user
     */
    public void increment() {
        if (livesRemaining < maxLives) {
            hearts[livesRemaining] = new Heart(new Vector2(livesRemaining * (Heart.getHeartSize() +
                    MARGIN_BETWEEN_HEARTS), MARGIN_BETWEEN_HEARTS),
                    new Vector2(Heart.getHeartSize(), Heart.getHeartSize()), heartImage);
            gameObjects.addGameObject(hearts[livesRemaining]);
            livesRemaining++;
            updateTextDisplay();
        }
    }

    private void updateTextDisplay() {
        textRenderer.setString(String.valueOf(livesRemaining));
        if (livesRemaining >= GREEN_TEXT_FOR_LIVES) {
            textRenderer.setColor(Color.GREEN);
        } else if (livesRemaining == YELLOW_TEXT_FOR_LIVES) {
            textRenderer.setColor(Color.YELLOW);
        } else {
            textRenderer.setColor(Color.RED);
        }
    }
}
