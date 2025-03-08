package pepse;

import danogl.collisions.Layer;
import danogl.util.Vector2;
import java.awt.*;
import java.util.List;

public class Constants {
    // PepseGameManager
    public static final int INITIAL_WORLD_ZONE = 0;
    public static final float CAMERA_FACTOR = 0.5f; // a factor used to set the camera position in the middle

    // Sky
    public static final int SKY_LAYER = Layer.BACKGROUND;
    public static final Vector2 SKY_TOP_LEFT_POSITION = Vector2.ZERO;
    public static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");
    public static final String SKY_TAG = "sky";

    // BLOCK
    public static final int BLOCK_SIZE = 25;

    // Terrain
    public static final int GROUND_LAYER = Layer.STATIC_OBJECTS;
    public static final float GROUND_HEIGHT_AT_X0_FACTOR = (float) 7 / 8; //start the ground at 7/8 of the window height
    public static final int TERRAIN_DEPTH = 15;
    public static final int NOISE_FACTOR = 8; // Factor influencing terrain noise generation.
    public static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    public static final String GROUND_TAG = "ground";

    // Night
    public static final int DAY_AND_NIGHT_CYCLE_LENGTH = 30;
    public static final int NIGHT_LAYER = Layer.BACKGROUND;
    public static final float INITIAL_NIGHT_OPACITY = 0f; // initial opacity of the night(0 is transparent)
    public static final float FINAL_NIGHT_OPACITY = 0.5f; // final opacity of the night(1 is opaque)
    public static final String NIGHT_TAG = "night";
    public static final int HALF_DAY_FACTOR = 2;

    // Sun
    public static final int SUN_LAYER = Layer.BACKGROUND;
    public static final Vector2 INITIAL_SUN_POSITION_ADDING = new Vector2(0, -500); //We add the sun size
    // to the cycle center
    public static final Vector2 SUN_SIZE = new Vector2(180, 150);
    public static final float INITIAL_SUN_CYCLE_ANGLE = 0f;
    public static final float FINAL_SUN_CYCLE_ANGLE = 360f;
    public static final String SUN_TAG = "sun";
    public static final int MID_SCREEN_FACTOR = 2;

    // Sun Halo
    public static final int SUN_HALO_LAYER = Layer.BACKGROUND;
    public static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 20);
    public static final Vector2 SUN_HALO_SIZE = SUN_SIZE.mult(1.2f);
    public static final String SUN_HALO_TAG = "sunHalo";

    // Avatar
    public static final int AVATAR_LAYER = Layer.FOREGROUND;
    public static final String FIRST_IMAGE_PATH = "assets/idle_0.png";
    public static final String [] IDLE_IMAGES_PATHS =
            { "assets/idle_0.png","assets/idle_1.png","assets/idle_2.png","assets/idle_3.png"};
    public static final String [] JUMP_IMAGES_PATHS =
            { "assets/jump_0.png","assets/jump_1.png","assets/jump_2.png","assets/jump_3.png"};
    public static final String [] RUN_IMAGES_PATHS =
            { "assets/run_0.png","assets/run_1.png","assets/run_2.png","assets/run_3.png"};
    public static final int TIME_BETWEEN_ANIMATION_CLIPS = 3;
    public static final Vector2 INITIAL_AVATAR_SIZE = new Vector2(50, 50);
    public static final int INITIAL_AVATAR_POSITION_ADDING = 100;
    public static final float VELOCITY_X = 500;
    public static final float VELOCITY_Y = -350;
    public static final float GRAVITY = 250;
    public static final float MAX_ENERGY = 100;
    public static final float FRUIT_EXTRA_ENERGY = 10;
    public static final float HORIZONTAL_MOVEMENT_ENERGY_COST = 0.5f;
    public static final float JUMP_ENERGY_COST = 10f;
    public static final float IDLE_ENERGY_REGENERATION = 1f;
    public static final String AVATAR_TAG = "avatar";

    // Energy Display
    public static final Vector2 BAR_SIZE = new Vector2(200, 20);
    public static final Color BACKGROUND_COLOR = Color.RED;
    public static final Color ENERGY_COLOR = Color.GREEN;
    public static final float FULL_PERCENTAGE = 100f;
    public static final int ENERGY_BAR_LAYER = Layer.BACKGROUND + 1;
    public static final Vector2 ENERGY_DISPLAY_POSITION = new Vector2(10, 10);

    // Tree
    public static final int TRUNK_WIDTH = 25;
    public static final double CHANCE_OF_TREE = 0.05;
    public static final int TREE_HEIGHT_FACTOR = 10;
    public static final int TREE_HEIGHT_ADDING = 2; //The minimum height of the tree
    public static final int TREE_LAYER = Layer.STATIC_OBJECTS;
    public static final Color TRUNK_COLOR = new Color(100, 50, 20);
    public static final String TRUNK_TAG = "trunk";

    // Leaf
    public static final int LEAF_LAYER = Layer.STATIC_OBJECTS + 1;
    public static final int LEAF_SQUARE_SIZE = 6; // The size of the square that contains the leafs
    public static final Color LEAF_COLOR = new Color(74, 193, 60);
    public static final float SWAY_ANGLE = 30;
    public static final float SWAY_TIME = 6;
    public static final float LEAF_SWAY_DELAY = 0.1f;
    public static final double CHANCE_OF_LEAF = 0.7;
    public static final String LEAF_TAG = "leaf";

    // Fruit
    public static final int FRUIT_LAYER = Layer.FOREGROUND - 1;
    public static final int PICKED_FRUIT_MASS = 0;
    public static final Color FRUIT_COLOR = Color.ORANGE;
    public static final int FRUIT_REGENERATE_TIME = 30;
    public static final double CHANCE_OF_FRUIT = 0.07;
    public static final float PICKED_FRUIT_OPACITY = 0f;
    public static final float UNPICKED_FRUIT_OPACITY = 1f;
    public static final String FRUIT_TAG = "fruit";

    // Cloud
    public static final int CLOUD_LAYER = Layer.BACKGROUND + 1;
    public static final int CLOUD_HEIGHT_FACTOR = 4;
    public static final float RAIN_GRAVITY = 500;
    public static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);
    public static final Color RAIN_COLOR = Color.BLUE;
    public static final java.util.List<java.util.List<Integer>> CLOUD_MATRIX = java.util.List.of(
            java.util.List.of(0, 1, 1, 0, 0, 0),
            java.util.List.of(1, 1, 1, 0, 1, 0),
            java.util.List.of(1, 1, 1, 1, 1, 1),
            java.util.List.of(1, 1, 1, 1, 1, 1),
            java.util.List.of(0, 1, 1, 1, 0, 0),
            List.of(0, 0, 0, 0, 0, 0));
    public static final int CLOUD_MATRIX_SIZE = 6;
    public static final int CLOUD_TRANSITION_TIME = 10000;
    public static final float INITIAL_CLOUD_TRANSITION_VALUE = 0f;
    public static final double CHANCE_FOR_RAIN_BLOCK = 0.2;
    public static final float INITIAL_RAIN_OPAQUENESS = 0.5f;
    public static final float FINAL_RAIN_OPAQUENESS = 0f;
    public static final float RAIN_TRANSITION_TIME = 1.5f;
    public static final String CLOUD_TAG = "cloud";
}
