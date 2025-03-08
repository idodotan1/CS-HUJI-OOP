package bricker.brick_strategies;
import danogl.GameObject;

/**
 * A composite collision strategy that can hold up to three different strategies
 * and applies them sequentially during a collision event.
 */
public class MultipleCollisionStrategy implements CollisionStrategy {
    private final CollisionStrategy[] strategies;
    private int currentSize;


    /**
     * Constructs a new CompositeCollisionStrategy.
     * Initializes an empty list of strategies.
     */
    public MultipleCollisionStrategy(int maxStrategies) {
        this.strategies = new CollisionStrategy[maxStrategies];
        this.currentSize = 0;

    }

    /**
     * Adds a collision strategy to the composite strategy.
     * If the maximum number of strategies is reached, the new strategy will not be added.
     *
     * @param strategy The strategy to add.
     * @return true if the strategy was added successfully, false otherwise.
     */
    public void addStrategy(CollisionStrategy strategy) {
        strategies[currentSize] = strategy;
        currentSize++;
    }

    /**
     * Handles a collision event by sequentially applying all added strategies.
     *
     * @param thisObj The first object involved in the collision.
     * @param otherObj The second object involved in the collision.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        for (int i = 0; i < currentSize; i++) {
            strategies[i].onCollision(thisObj, otherObj);
        }
    }
}