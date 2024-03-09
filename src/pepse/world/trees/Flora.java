package pepse.world.trees;

import danogl.GameObject;
import danogl.util.Vector2;
import pepse.main.PepseConstants;
import pepse.main.PepseGameManager;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Manages the creation and placement of trees within the game world.
 * Flora generates trees with trunks, leaves, and potentially fruits based on
 * probabilistic placement and the provided ground height function.
 *
 * @author adir.barak, asher
 */
public class Flora {
    private final Function<Float, Float> getHeightAt;
    private final Supplier<Integer> getAvatarJumpCount;
    private final Consumer<Float> changeAvatarEnergyBy;
    private static final int NET_LEAF_BLOCK_DIM = (6 * PepseConstants.LEAF_SIZE);
    private static final int LEAF_BLOCK_DIM = PepseConstants.TRUNK_SIZE_X + NET_LEAF_BLOCK_DIM;
    private static final double CREATE_TREE_PROBABILITY = 0.2;
    private static final double CREATE_LEAF_PROBABILITY = 0.8;
    private static final double CREATE_FRUIT_PROBABILITY = 1 - CREATE_LEAF_PROBABILITY;

    /**
     * Creates a new Flora object.
     *
     * @param getGroundHeightAt    a function to get the ground height at a specific x position
     * @param countForEventTrigger a function to get the avatar's jump count
     * @param alterValueBy         a function to change the avatar's energy
     */
    public Flora(Function<Float, Float> getGroundHeightAt, Supplier<Integer> countForEventTrigger,
                 Consumer<Float> alterValueBy) {
        this.getHeightAt = getGroundHeightAt;
        this.getAvatarJumpCount = countForEventTrigger;
        this.changeAvatarEnergyBy = alterValueBy;
    }

    /**
     * Generates a list of trees within the specified x-axis range.
     * This method iterates through the x-axis range and attempts to create trees
     * based on a probability. If a tree is created, its components (trunk, leaves, fruits)
     * are positioned and added to the returned list.
     *
     * @param minX the minimum x-axis position (inclusive)
     * @param maxX the maximum x-axis position (exclusive)
     * @return a list of lists containing the GameObjects for each created tree
     */
    public ArrayList<ArrayList<GameObject>> createInRange(int minX, int maxX) {
        ArrayList<ArrayList<GameObject>> listOfTrees = new ArrayList<>();
        int roundedMinX = getRoundedMinX(minX);
        int roundedMaxX = getRoundedMaxX(maxX);
        for (float curX = roundedMinX; curX < roundedMaxX; ) {
            if (coinTossAtP(CREATE_TREE_PROBABILITY)) {
                Vector2 groundPos = new Vector2(curX, getHeightAt.apply(curX));
                listOfTrees.add(createTree(groundPos));
                curX += LEAF_BLOCK_DIM; // avoid overlap between leaves blocks
            }
            curX += PepseConstants.BLOCK_SIZE;
        }

        return listOfTrees;
    }

    /**
     * Creates a single tree with trunk, leaves, and potentially fruits.
     * This method positions a trunk at the specified ground position and then attempts
     * to create leaves and fruits within a designated block area around the trunk based on
     * probabilities.
     *
     * @param groundPos the position of the tree's base (bottom of the trunk)
     * @return a list containing the trunk, leaves, and any fruits created
     */
    private ArrayList<GameObject> createTree(Vector2 groundPos) {
        ArrayList<GameObject> listOfTreeComponents = new ArrayList<>();
        GameObject trunk = new Trunk(Vector2.ZERO, getAvatarJumpCount);
        trunk.setTopLeftCorner(new Vector2(groundPos.x(), groundPos.y() - trunk.getDimensions().y()));
        listOfTreeComponents.add(trunk);
        // one-sided offset, meaning half the size of the whole block:
        float startX = trunk.getTopLeftCorner().x() - NET_LEAF_BLOCK_DIM / 2f;
        float startY = trunk.getTopLeftCorner().y() - NET_LEAF_BLOCK_DIM / 2f;
        createLeavesBlock(startX, startY, listOfTreeComponents);
        return listOfTreeComponents;
    }

    /**
     * Attempts to create leaves and fruits within a designated block area.
     * This method iterates through positions within the block and checks the ground height
     * to avoid placing objects on the ground. It then uses probabilities to potentially create
     * Leaf and Fruit objects and add them to the provided list.
     *
     * @param startX               the x-axis starting position of the block area
     * @param startY               the y-axis starting position of the block area
     * @param listOfTreeComponents the list to add created leaves and fruits to
     */
    private void createLeavesBlock(float startX, float startY, ArrayList<GameObject> listOfTreeComponents) {
        float incAmount = PepseConstants.LEAF_SIZE;
        for (float i = startY; i < startY + LEAF_BLOCK_DIM; i += incAmount) {
            for (float j = startX; j < startX + LEAF_BLOCK_DIM; j += incAmount) {
                if (getHeightAt.apply(j) - PepseConstants.LEAF_SIZE > i) { // avoid creating on ground
                    if (coinTossAtP(CREATE_LEAF_PROBABILITY)) {
                        GameObject leaf = new Leaf(new Vector2(j, i), getAvatarJumpCount);
                        listOfTreeComponents.add(leaf);
                    }
                    if (coinTossAtP(CREATE_FRUIT_PROBABILITY)) {
                        GameObject fruit = new Fruit(new Vector2(j, i), getAvatarJumpCount, changeAvatarEnergyBy);
                        listOfTreeComponents.add(fruit);
                    }
                }
            }
        }
    }

    /**
     * Calculates the rounded minimum x-axis position based on block size.
     * This method ensures the minimum position is a multiple of the block size
     *
     * @param minX the original minimum x-axis position
     * @return the rounded minimum x-axis position
     */
    private int getRoundedMinX(int minX) {
        if (minX % PepseConstants.BLOCK_SIZE != 0) {
            int denominator = minX / PepseConstants.BLOCK_SIZE;
            if (minX < 0) {
                denominator--;
            }
            return denominator * PepseConstants.BLOCK_SIZE;
        } else {
            return minX;
        }
    }

    /**
     * Calculates the rounded maximum x-axis position based on block size.
     * This method ensures the maximum position is a multiple of the block size
     *
     * @param maxX the original maximum x-axis position
     * @return the rounded maximum x-axis position
     */
    private int getRoundedMaxX(int maxX) {
        if (maxX % PepseConstants.BLOCK_SIZE != 0) {
            int denominator = maxX / PepseConstants.BLOCK_SIZE;
            if (maxX > 0) {
                denominator++;
            }
            return denominator * PepseConstants.BLOCK_SIZE;
        } else {
            return maxX;
        }
    }

    /**
     * Simulates a coin toss with a specified probability.
     * This is a helper method using the global random number generator
     *
     * @param p the probability of heads (between 0 and 1)
     * @return true if the coin toss lands on heads, false otherwise
     */
    private static boolean coinTossAtP(double p) {
        return PepseGameManager.random.nextDouble() < p;
    }
}
