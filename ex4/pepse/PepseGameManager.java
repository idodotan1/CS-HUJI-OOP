package pepse;

import danogl.*;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.pepse.world.Avatar;
import pepse.pepse.world.Block;
import pepse.pepse.world.Cloud;
import pepse.pepse.world.Sky;
import pepse.pepse.world.Terrain;
import pepse.pepse.world.EnergyDisplay;
import pepse.pepse.world.pepse.world.daynight.Night;
import pepse.pepse.world.pepse.world.daynight.Sun;
import pepse.pepse.world.pepse.world.daynight.SunHalo;
import pepse.pepse.world.pepse.world.trees.Flora;
import pepse.pepse.world.pepse.world.trees.Fruit;
import pepse.pepse.world.pepse.world.trees.Leaf;
import pepse.pepse.world.pepse.world.trees.Tree;

import java.util.*;

import static pepse.Constants.*;

/**
 * The main game manager for the Pepse game.
 * Manages the initialization and updating of the game world.
 */
public class PepseGameManager extends GameManager {
    private ImageReader imageReader;
    private WindowController windowController;
    private UserInputListener inputListener;
    private Avatar avatar;
    private float worldZoneRange;
    private int currentWorldZone;
    private TreeMap<Integer, Tree> trees = new TreeMap<>();
    private int seed;

    /**
     * Initializes the game, setting up layers, terrain, avatar, and the initial world zones.
     *
     * @param imageReader     Provides functionality for reading images.
     * @param soundReader     Provides functionality for playing sounds.
     * @param inputListener   Listens for user input.
     * @param windowController Controls the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener,
                               WindowController windowController) {
        Random random = new Random();
        seed = random.nextInt();
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.imageReader = imageReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        worldZoneRange = windowController.getWindowDimensions().x();
        currentWorldZone = INITIAL_WORLD_ZONE;
        initializeSky();
        initializeTerrain();
        initializeDayNightCycle();
        initializeAvatar();
        initializeEnergyDisplay();
        initializeFlora();
        initializeCloud();
        initializeInitialWorldZones();
        gameObjects().layers().shouldLayersCollide(AVATAR_LAYER, TREE_LAYER, true);
        gameObjects().layers().shouldLayersCollide(AVATAR_LAYER, GROUND_LAYER, true);
        gameObjects().layers().shouldLayersCollide(AVATAR_LAYER,FRUIT_LAYER, true);
    }

    /**
     * Updates the game state. Handles world zone transitions based on the avatar's position.
     *
     * @param deltaTime Time passed since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float avatarX = avatar.getCenter().x();
        float worldZoneCenter = worldZoneRange * currentWorldZone + worldZoneRange / 2;

        // Check if avatar moved out of the current zone
        if (Math.abs(avatarX - worldZoneCenter) > worldZoneRange / 2) {
            if (avatarX > worldZoneCenter) { // Move to the next zone
                shiftWorldZone(worldZoneRange * (currentWorldZone - 1),
                        worldZoneRange * currentWorldZone,
                        worldZoneRange * (currentWorldZone + 2),
                        worldZoneRange * (currentWorldZone + 3));
                currentWorldZone++;
            } else { // Move to the previous zone
                shiftWorldZone(worldZoneRange * (currentWorldZone + 1),
                        worldZoneRange * (currentWorldZone + 2),
                        worldZoneRange * (currentWorldZone - 1),
                        worldZoneRange * (currentWorldZone - 2));
                currentWorldZone--;
            }
        }

    }

    /**
     * The main entry point for the Pepse game.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    private void initializeSky() {
        GameObject sky = Sky.create(windowController.getWindowDimensions());
        gameObjects().addGameObject(sky, SKY_LAYER);
    }

    private void initializeTerrain() {
        Terrain.createInstance(windowController.getWindowDimensions(), seed);
    }

    private void initializeDayNightCycle() {
        GameObject night = Night.create(windowController.getWindowDimensions(), DAY_AND_NIGHT_CYCLE_LENGTH);
        gameObjects().addGameObject(night, NIGHT_LAYER);
        GameObject sun = Sun.create(windowController.getWindowDimensions(), DAY_AND_NIGHT_CYCLE_LENGTH);
        gameObjects().addGameObject(sun, SUN_LAYER);
        gameObjects().addGameObject(SunHalo.create(sun), SUN_HALO_LAYER);
    }

    private void initializeAvatar() {
        Vector2 initialAvatarPosition = new Vector2(windowController.getWindowDimensions().x() / 2,
                Terrain.getInstance().groundHeightAt(windowController.getWindowDimensions().x() / 2)
                        - INITIAL_AVATAR_POSITION_ADDING);
        avatar = new Avatar(new Vector2(initialAvatarPosition), inputListener, imageReader);
        gameObjects().addGameObject(avatar, AVATAR_LAYER);
        setCamera(new Camera(avatar,
                windowController.getWindowDimensions().mult(CAMERA_FACTOR).subtract(initialAvatarPosition),
                windowController.getWindowDimensions(), windowController.getWindowDimensions()));
    }

    private void initializeEnergyDisplay() {
        EnergyDisplay energyDisplay = new EnergyDisplay(avatar::getEnergy, gameObjects()::addGameObject);
    }

    private void initializeFlora() {
        Flora.createInstance(Terrain.getInstance()::groundHeightAt, seed);
    }

    private void initializeInitialWorldZones() {
        createWorldZone((int) (-worldZoneRange), 0);                // Zone -1
        createWorldZone(0, (int) worldZoneRange);                  // Zone 0 (center)
        createWorldZone((int) worldZoneRange, (int) (2 * worldZoneRange)); // Zone 1
    }

    private void initializeCloud() {
        Cloud cloud = new Cloud(windowController.getWindowDimensions(),
                gameObjects()::addGameObject, gameObjects()::removeGameObject);
        gameObjects().addGameObject(cloud, CLOUD_LAYER);
        avatar.addActionOnJump(cloud::startRaining);
    }

    private void shiftWorldZone(float removeStart, float removeEnd, float createStart, float createEnd) {
        removeWorldZone((int) removeStart, (int) removeEnd);
        createWorldZone((int) createStart, (int) createEnd);
    }

    private void createWorldZone(int start, int end) {
        List<Block> blockList = Terrain.getInstance().createInRange(start, end);
        for (Block block : blockList) {
            gameObjects().addGameObject(block, GROUND_LAYER);
        }
        List<Tree> treeList = Flora.getInstance().createInRange(start, end);
        for (Tree tree : treeList) {
            if (!trees.containsKey((int)tree.getTrunk()[0].getTopLeftCorner().x())) {
                trees.put((int) tree.getTrunk()[0].getTopLeftCorner().x(), tree);
                for (Block block : tree.getTrunk()) {
                    gameObjects().addGameObject(block, TREE_LAYER);
                }
                for (Leaf leaf : tree.getLeaves()) {
                    gameObjects().addGameObject(leaf, LEAF_LAYER);
                }
                for (Fruit fruit : tree.getFruits()) {
                    gameObjects().addGameObject(fruit, FRUIT_LAYER);
                }
                tree.swayLeaves();
            }
        }
    }

    private void removeWorldZone(int start, int end) {
        for (GameObject obj : gameObjects()) {
            if (obj.getTopLeftCorner().x() > start && obj.getTopLeftCorner().x() < end) {
                {
                    if (obj.getTag().equals(GROUND_TAG) || obj.getTag().equals(LEAF_TAG) ||
                            obj.getTag().equals(FRUIT_TAG)) {
                        gameObjects().removeGameObject(obj);
                    }
                }
            }
        }
    }
}
