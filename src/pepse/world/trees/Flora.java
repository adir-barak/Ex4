package pepse.world.trees;

import danogl.GameObject;
import danogl.util.Vector2;
import pepse.main.PepseConstants;
import pepse.main.PepseGameManager;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Flora {


    private final Function<Float, Float> getHeightAt;
    private final Supplier<Integer> getAvatarJumpCount;
    private final Consumer<Float> changeAvatarEnergyBy;
    private static final int NET_LEAF_BLOCK_DIM = (6 * PepseConstants.LEAF_SIZE);
    private static final int LEAF_BLOCK_DIM = PepseConstants.TRUNK_SIZE_X + NET_LEAF_BLOCK_DIM;
    private static final double CREATE_TREE_PROBABILITY = 0.2;
    private static final double CREATE_LEAF_PROBABILITY = 0.8;
    private static final double CREATE_FRUIT_PROBABILITY = 1 - CREATE_LEAF_PROBABILITY;

    public Flora(Function<Float, Float> getGroundHeightAt, Supplier<Integer> countForEventTrigger,
                 Consumer<Float> alterValueBy) {
        this.getHeightAt = getGroundHeightAt;
        this.getAvatarJumpCount = countForEventTrigger;
        this.changeAvatarEnergyBy = alterValueBy;
    }

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

    // TODO move to utils?
    private boolean coinTossAtP(double p) {
        return PepseGameManager.random.nextDouble() < p;
    }
}
