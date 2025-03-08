package bricker.main;

import bricker.brick_strategies.*;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;
import java.util.Random;

/**
 * Manages the Bricker game, including initialization, updates, and game over logic.
 */
public class BrickerGameManager extends GameManager {
    // Constants
    private static final float BALL_SPEED = 250f;
    private static final int WALL_WIDTH = 10;
    private static final int TOP_WALL_WIDTH = 30;
    private static final int DEF_BRICKS_IN_ROW = 8;
    private static final int DEF_BRICK_ROWS = 7;
    private static final int BRICK_HEIGHT = 15;
    private static final int MARGIN_BETWEEN_BRICKS = 5;
    private static final int MARGIN_FROM_WALLS = 15;
    private static final int NUM_LIVES = 3;
    private static final int MAX_NUM_LIVES = 4;
    private static final int NUM_OF_PUCKS_CREATED = 2;
    private static final int MARGIN_FROM_END = 20;
    private static final int MARGIN_FROM_BOTTOM = 30;
    private static final int BALL_SIZE = 20;
    private static final int DEFAULT_PADDLE_WIDTH = 100;
    private static final int DEFAULT_PADDLE_HEIGHT = 15;
    private static final int NUM_OF_COLLISIONS_FOR_TURBO_BALL = 6;
    private static final int NUM_OF_COLLISIONS_FOR_EXTRA_PADDLE = 4;
    private static final float FACTOR_TO_NEGATE_TURBO_SPEED = 5/7f;
    private static final float TURBO_BALL_SPEED_FACTOR = 1.4f;
    private static final float MID_SCREEN_FACTOR = 0.5f;
    private static final int SCREEN_WIDTH = 700;
    private static final int SCREEN_HEIGHT = 500;
    private static final int VALID_NUM_OF_ARGS = 2;
    private static final String WIN_PROMPT = "You win!";
    private static final String LOSE_PROMPT = "You lose!";

    // Fields
    private boolean isTurbo;
    private boolean isExtraPaddle;
    private int extraPaddleCounter;
    private int turboCounter;
    private final int bricksInRow;
    private final int brickRows;
    private Vector2 windowDimensions;
    private Ball ball;
    private ExtraPaddle extraPaddle;
    private WindowController windowController;
    private UserInputListener inputListener;
    private LivesDisplay livesDisplay;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private Ball[] pucks;
    private  Heart[] fallingHearts;
    private Counter puckCounter;
    private Counter fallingHeartCounter;
    private Counter brickCounter;
    private CollisionStrategyFactory collisionStrategyFactory;

    /**
     * Constructor with default brick configuration.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
        this.bricksInRow = DEF_BRICKS_IN_ROW;
        this.brickRows = DEF_BRICK_ROWS;
    }

    /**
     * Constructor with customizable brick configuration.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions,
                              int bricksInRow, int brickRows) {
        super(windowTitle, windowDimensions);
        this.bricksInRow = bricksInRow;
        this.brickRows = brickRows;
    }

    /**
     * This function initializes a new game and all the basic objects of the game
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self-explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        this.windowController = windowController;
        this.windowDimensions = windowController.getWindowDimensions();
        this.inputListener = inputListener;
        this.isTurbo = false;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.pucks = new Ball[NUM_OF_PUCKS_CREATED* brickRows * bricksInRow];
        this.fallingHearts = new Heart[brickRows * bricksInRow];
        this.fallingHeartCounter = new Counter();
        this.puckCounter = new Counter();
        this.brickCounter = new Counter();
        createBackground();
        createWalls();
        createLivesDisplay();
        createBall(soundReader);
        createPaddles();
        resetExtraPaddle();
        livesDisplay.resetLives();
        this.collisionStrategyFactory = new CollisionStrategyFactory(gameObjects(),brickCounter,
                                                                     fallingHeartCounter,imageReader,
                                                                     soundReader,fallingHearts,pucks,ball,
                                                                     BALL_SPEED,extraPaddle);
        createBricks();
    }

    private void createBackground() {
        Renderable bgImage = imageReader.readImage("assets/DARK_BG2_small.jpeg",
                false);
        GameObject bg = new GameObject(Vector2.ZERO, windowDimensions, bgImage);
        bg.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(bg, Layer.BACKGROUND);
    }

    private void createWalls() {
        Renderable wallRenderer = new RectangleRenderable(Color.BLACK);

        GameObject leftWall = new GameObject(
                Vector2.ZERO, new Vector2(WALL_WIDTH, windowDimensions.y()), wallRenderer);
        GameObject rightWall = new GameObject(
                new Vector2(windowDimensions.x() - WALL_WIDTH, 0),
                new Vector2(WALL_WIDTH, windowDimensions.y()), wallRenderer);
        GameObject topWall = new GameObject(
                Vector2.ZERO, new Vector2(windowDimensions.x(), TOP_WALL_WIDTH), wallRenderer);

        gameObjects().addGameObject(leftWall);
        gameObjects().addGameObject(rightWall);
        gameObjects().addGameObject(topWall);
    }

    private void createLivesDisplay() {
        Renderable heartImage = imageReader.readImage("assets/heart.png", true);
        Vector2 textPosition = new Vector2(windowDimensions.x() - MARGIN_FROM_END, 0);
        livesDisplay = new LivesDisplay(MAX_NUM_LIVES,NUM_LIVES, heartImage, textPosition, gameObjects());
    }

    private void createBricks() {
        Renderable brickRenderer = imageReader.readImage("assets/brick.png",
                false);
        float brickWidth = calculateBrickWidth();
        for (int row = 0; row < brickRows; row++) {
            for (int col = 0; col < bricksInRow; col++) {
                Vector2 brickPosition = calculateBrickPosition(row, col, brickWidth);
                Vector2 brickDimensions = new Vector2(brickWidth, BRICK_HEIGHT);
                CollisionStrategy collisionStrategy = collisionStrategyFactory.createCollisionStrategy();
                Brick brick = new Brick(brickPosition, brickDimensions, brickRenderer, collisionStrategy);
                gameObjects().addGameObject(brick);
            }
        }
        brickCounter.increaseBy(brickRows * bricksInRow);
    }

    private float calculateBrickWidth() {
        return (windowDimensions.x() - (MARGIN_FROM_WALLS + MARGIN_FROM_WALLS) -
                (bricksInRow - 1) * MARGIN_BETWEEN_BRICKS) / bricksInRow;
    }

    private Vector2 calculateBrickPosition(int row, int col, float brickWidth) {
        float x = MARGIN_FROM_WALLS + col * (brickWidth + MARGIN_BETWEEN_BRICKS);
        float y = TOP_WALL_WIDTH + row * (BRICK_HEIGHT + MARGIN_BETWEEN_BRICKS);
        return new Vector2(x, y);
    }

    private void createBall(SoundReader soundReader) {
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop.wav");
        ball = new Ball(Vector2.ZERO, new Vector2(BALL_SIZE, BALL_SIZE), ballImage, collisionSound);
        ball.setTag("MainBall");
        initializeBall();
        gameObjects().addGameObject(ball);
    }

    private void initializeBall() {
        ball.setCenter(windowDimensions.mult(MID_SCREEN_FACTOR));
        Random rand = new Random();
        float ballVelX = BALL_SPEED * (rand.nextBoolean() ? -1 : 1);
        float ballVelY = BALL_SPEED * (rand.nextBoolean() ? -1 : 1);
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
        if (ball.getTag().equals("TurboBall")){
            ball.setVelocity(ball.getVelocity().mult(TURBO_BALL_SPEED_FACTOR));
        }
    }

    private void createPaddles() {
        Renderable paddleImage = imageReader.readImage("assets/paddle.png",
                true);

        // Calculate the top-left corner based on the desired center
        Vector2 paddleDimensions = new Vector2(DEFAULT_PADDLE_WIDTH, DEFAULT_PADDLE_HEIGHT);
        Vector2 userPaddleCenter = new Vector2(windowDimensions.mult(MID_SCREEN_FACTOR).x(),
                                               windowDimensions.y() - MARGIN_FROM_BOTTOM);
        Vector2 extraPaddleCenter = new Vector2(windowDimensions.mult(MID_SCREEN_FACTOR).x(),
                                                windowDimensions.mult(MID_SCREEN_FACTOR).y());
        Vector2 userTopLeftCorner = userPaddleCenter.subtract(paddleDimensions.mult(MID_SCREEN_FACTOR));
        Vector2 extraTopLeftCorner = extraPaddleCenter.subtract(paddleDimensions.mult(MID_SCREEN_FACTOR));
        // Create the User paddle
        Paddle userPaddle = new Paddle(
                userTopLeftCorner, paddleDimensions, paddleImage, inputListener, windowDimensions,
                WALL_WIDTH);
        userPaddle.setTag("UserPaddle");
        gameObjects().addGameObject(userPaddle);
        //Create the extra Paddle
        extraPaddle = new ExtraPaddle(extraTopLeftCorner,paddleDimensions, paddleImage,
                inputListener, windowDimensions, WALL_WIDTH);
        extraPaddle.setTag("NotInGame");
    }

    /**
     * This function updates the status all the object in the game, and removes objects that exit the window
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkBallPosition();
        checkTurbo();
        checkPucks();
        checkExtraPaddle();
        checkFallingHearts();
        if (brickCounter.value() == 0 || inputListener.isKeyPressed('W')) {
            gameOver(WIN_PROMPT);
        }
    }

    private void checkFallingHearts() {
        for (Heart heart : fallingHearts) {
            if (heart != null){
                if(heart.getTag().equals("Collided")){
                    heart.setTag("");
                    livesDisplay.increment();
                    gameObjects().removeGameObject(heart);
                }
                if (heart.getCenter().y() > windowDimensions.y()){
                    gameObjects().removeGameObject(heart);
                }
            }
        }

    }

    private void resetExtraPaddle(){
        isExtraPaddle = false;
        extraPaddle.setTag("NotInGame");
        gameObjects().removeGameObject(extraPaddle);
    }
    private void checkExtraPaddle(){
        if (extraPaddle.getTag().equals("InGame")){
            if (!isExtraPaddle){
                isExtraPaddle = true;
                extraPaddleCounter = extraPaddle.getCollisionCounter();
            }
            else {
                if (extraPaddle.getCollisionCounter() >= extraPaddleCounter +
                        NUM_OF_COLLISIONS_FOR_EXTRA_PADDLE){
                    resetExtraPaddle();
                }
            }
        }
    }
    private void resetTurbo(){
        isTurbo = false;
        ball.setVelocity(ball.getVelocity().mult(FACTOR_TO_NEGATE_TURBO_SPEED));
        ball.renderer().setRenderable(imageReader.readImage("assets/ball.png",true));
        ball.setTag("MainBall");
    }
    private void checkTurbo()
    {
        if (ball.getTag().equals("TurboBall"))
        {
            if (!isTurbo) {
                isTurbo = true;
                turboCounter = ball.getCollisionCounter();
            }
            else {
                if (ball.getCollisionCounter() >= turboCounter + NUM_OF_COLLISIONS_FOR_TURBO_BALL){
                    resetTurbo();
                }
            }
        }
    }
    private  void checkPucks()
    {
        for (Ball puck : pucks) {
            if (puck != null){
                if (puck.getCenter().y() > windowDimensions.y()){
                    gameObjects().removeGameObject(puck);
                }
            }
        }
    }
    private void checkBallPosition() {
        if (ball.getCenter().y() > windowDimensions.y()) {
            livesDisplay.decrement();
            if (livesDisplay.getLivesRemaining() == 0) {
                gameOver(LOSE_PROMPT);
            } else {
                initializeBall();
            }
        }
    }

    private void gameOver(String prompt) {
        livesDisplay.initializeHearts();
        if (windowController.openYesNoDialog(prompt + " Play again?")) {
            windowController.resetGame();
        } else {
            windowController.closeWindow();
        }
    }

    /**
     * The main function that runs the game
     */
    public static void main(String[] args) {
        Vector2 windowDimensions = new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT);
        if (args.length == VALID_NUM_OF_ARGS) {
            try {
                int bricksInRow = Integer.parseInt(args[0]);
                int brickRows = Integer.parseInt(args[1]);
                if (bricksInRow > 0 && brickRows > 0) {
                    new BrickerGameManager("Bricker", windowDimensions, bricksInRow, brickRows).run();
                    return;
                }
            } catch (NumberFormatException ignored) {
            }
        }
        new BrickerGameManager("Bricker", windowDimensions).run();
    }
}
