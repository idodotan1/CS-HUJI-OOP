package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.gameobjects.ExtraPaddle;
import bricker.gameobjects.Heart;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;

import java.util.Random;

/**
 * Factory class to create collision strategies for bricks based on probabilities.
 */
public class CollisionStrategyFactory {

    // Constants
    private static final double PROBABILITY_NORMAL = 0.5;
    private static final int MAX_STRATEGIES_ALLOWED = 3;
    private static final int DOUBLE_STRATEGY = 2;
    private static final double PROBABILITY_PUCKS = 0.2;
    private static final double PROBABILITY_EXTRA_PADDLE = 0.4;
    private static final double PROBABILITY_TURBO_MODE = 0.6;
    private static final double PROBABILITY_EXTRA_LIFE = 0.8;
    private static final double PROBABILITY_TWO_STRATEGIES = 0.96;

    private static final double PROBABILITY_PUCKS_NO_MULTIPLE = 0.25;
    private static final double PROBABILITY_EXTRA_PADDLE_NO_MULTIPLE = 0.5;
    private static final double PROBABILITY_TURBO_MODE_NO_MULTIPLE = 0.75;

    private static final float PUCK_SPEED_MULTIPLIER = 0.75f;

    private final Random random;
    private final GameObjectCollection gameObjectCollection;
    private final Counter brickCounter;
    private final Counter fallingHeartCounter;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final Heart[] fallingHearts;
    private final Ball[] pucks;
    private final Ball ball;
    private final float ballSpeed;
    private final ExtraPaddle extraPaddle;

    /**
     * Constructor for CollisionStrategyFactory.
     *
     * @param gameObjectCollection   Collection of all game objects in the game.
     * @param brickCounter           Counter for the number of remaining bricks.
     * @param fallingHeartCounter    Counter for the number of falling hearts.
     * @param imageReader            Image reader for loading images.
     * @param soundReader            Sound reader for loading sounds.
     * @param fallingHearts          Array of falling hearts.
     * @param pucks                  Array of pucks.
     * @param ball                   The main ball object.
     * @param ballSpeed              The speed of the ball.
     * @param extraPaddle            The extra paddle object.
     */
    public CollisionStrategyFactory(GameObjectCollection gameObjectCollection,
                                    Counter brickCounter,
                                    Counter fallingHeartCounter,
                                    ImageReader imageReader,
                                    SoundReader soundReader,
                                    Heart[] fallingHearts,
                                    Ball[] pucks,
                                    Ball ball,
                                    float ballSpeed,
                                    ExtraPaddle extraPaddle) {
        this.gameObjectCollection = gameObjectCollection;
        this.brickCounter = brickCounter;
        this.fallingHeartCounter = fallingHeartCounter;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.fallingHearts = fallingHearts;
        this.pucks = pucks;
        this.ball = ball;
        this.ballSpeed = ballSpeed;
        this.extraPaddle = extraPaddle;
        this.random = new Random();
    }

    /**
     * Creates a collision strategy for a brick based on the specified probabilities.
     *
     * @return A collision strategy combining one or more behaviors.
     */
    public CollisionStrategy createCollisionStrategy() {
        double probability = random.nextDouble();
        if (probability < PROBABILITY_NORMAL) {
            return new BasicCollisionStrategy(gameObjectCollection, brickCounter);
        } else {
            return getRandomSpecialStrategy(true);
        }
    }

    private CollisionStrategy getRandomSpecialStrategy(boolean canBeMultiple) {
        double probability = random.nextDouble();
        if (canBeMultiple) {
            if (probability < PROBABILITY_PUCKS) {
                return createPucksStrategy();
            }
            if (probability < PROBABILITY_EXTRA_PADDLE) {
                return createExtraPaddleStrategy();
            }
            if (probability < PROBABILITY_TURBO_MODE) {
                return createTurboModeStrategy();
            }
            if (probability < PROBABILITY_EXTRA_LIFE) {
                return createExtraLifeStrategy();
            }
            if (probability < PROBABILITY_TWO_STRATEGIES) {
                return createMultipleStrategy(DOUBLE_STRATEGY);
            }
            return createMultipleStrategy(MAX_STRATEGIES_ALLOWED);
        }
        if (probability < PROBABILITY_PUCKS_NO_MULTIPLE) {
            return createPucksStrategy();
        }
        if (probability < PROBABILITY_EXTRA_PADDLE_NO_MULTIPLE) {
            return createExtraPaddleStrategy();
        }
        if (probability < PROBABILITY_TURBO_MODE_NO_MULTIPLE) {
            return createTurboModeStrategy();
        }
        return createExtraLifeStrategy();
    }

    private CollisionStrategy createExtraLifeStrategy() {
        Renderable heartImage = imageReader.readImage("assets/heart.png", true);
        return new ExtraLifeCollisionStrategy(gameObjectCollection,heartImage,fallingHearts,
                                              fallingHeartCounter,brickCounter);
    }

    private CollisionStrategy createExtraPaddleStrategy() {
        return new ExtraPaddleCollisionStrategy(gameObjectCollection, extraPaddle, brickCounter);
    }

    private CollisionStrategy createPucksStrategy() {
        Renderable puckImage = imageReader.readImage("assets/mockBall.png", true);
        Sound puckSound = soundReader.readSound("assets/blop.wav");
        float puckSpeed = ballSpeed * PUCK_SPEED_MULTIPLIER;
        return new PucksCollisionStrategy(gameObjectCollection, puckImage, puckSound, pucks,
                                          ball.getDimensions(), puckSpeed, fallingHeartCounter, brickCounter);
    }

    private CollisionStrategy createTurboModeStrategy() {
        Renderable turboBallImage = imageReader.readImage("assets/redball.png", true);
        return new TurboCollisionStrategy(gameObjectCollection, turboBallImage, brickCounter);
    }

    private CollisionStrategy createMultipleStrategy(int strategiesAmount) {
        MultipleCollisionStrategy multipleCollisionStrategy =
                new MultipleCollisionStrategy(MAX_STRATEGIES_ALLOWED);
        for (int i = 0; i < MAX_STRATEGIES_ALLOWED; i++) {
            if (i < strategiesAmount) {
                multipleCollisionStrategy.addStrategy(getRandomSpecialStrategy(false));
            } else {
                break;
            }
        }
        return multipleCollisionStrategy;
    }
}
