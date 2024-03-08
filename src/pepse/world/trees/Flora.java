package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static pepse.main.ConstantsAsher.TRUNK_TAG;

public class Flora {


    private final Function<Float, Float> getHeightAt;
    private final Supplier<Integer> getAvatarJumpCount;
    private final Consumer<Float> changeAvatarEnergyBy;

    // TODO it shouldnt be here
    private static final int BLOCK_SIZE = 30;
    private static final Color DEFAULT_TRUNK_COLOR = new Color(100, 50, 20);
    private static final Color DEFAULT_LEAF_COLOR = new Color(50, 200, 30);
    private static final Color DEFAULT_FRUIT_COLOR = new Color(200, 50, 30);
    private static final Vector2 DEFAULT_TRUNK_SIZE = new Vector2(BLOCK_SIZE, BLOCK_SIZE * 6);
    private static final Vector2 DEFAULT_LEAF_SIZE = Vector2.ONES.mult(BLOCK_SIZE);
    private static final Vector2 DEFAULT_FRUIT_SIZE = Vector2.ONES.mult(BLOCK_SIZE).mult(0.9f);
    private static final int LEAF_BLOCK_DIM = 3;

    private static final double CREATE_TREE_PROBABILITY = 0.5;
    private static final double CREATE_LEAF_PROBABILITY = 0.8;
    private static final double CREATE_FRUIT_PROBABILITY = 1 - CREATE_LEAF_PROBABILITY;

    public Flora(Function<Float, Float> getGroundHeightAt, Supplier<Integer> getAvatarJumpCount,
                 Consumer<Float> changeAvatarEnergyBy) {
        this.getHeightAt = getGroundHeightAt;
        this.getAvatarJumpCount = getAvatarJumpCount;
        this.changeAvatarEnergyBy = changeAvatarEnergyBy;
    }

    public ArrayList<ArrayList<GameObject>> createInRange(int minX, int maxX) {
        ArrayList<ArrayList<GameObject>> listOfTrees = new ArrayList<>();
        int roundedMinX = getRoundedMinX(minX);
        int roundedMaxX = getRoundedMaxX(maxX);
        for (float curX = roundedMinX; curX < roundedMaxX; curX += BLOCK_SIZE * 8) {
            if (coinTossAtP(CREATE_TREE_PROBABILITY)) {
                Vector2 trunkTopLeftPos = new Vector2(curX, getHeightAt.apply(curX) - DEFAULT_TRUNK_SIZE.y());
                listOfTrees.add(createTree(trunkTopLeftPos));
            }
        }

        return listOfTrees;
    }

    private ArrayList<GameObject> createTree(Vector2 trunkTopLeftPos) {
        ArrayList<GameObject> listOfTreeComponents = new ArrayList<>();
        GameObject trunk = new Trunk(trunkTopLeftPos, DEFAULT_TRUNK_SIZE, getAvatarJumpCount);
        listOfTreeComponents.add(trunk);
        float posX = trunkTopLeftPos.x();
        float posY = trunkTopLeftPos.y();
        for (float i = posY - LEAF_BLOCK_DIM * BLOCK_SIZE; i < posY + LEAF_BLOCK_DIM * BLOCK_SIZE; i += BLOCK_SIZE) {
            for (float j = posX - LEAF_BLOCK_DIM * BLOCK_SIZE; j < posX + LEAF_BLOCK_DIM * BLOCK_SIZE; j += BLOCK_SIZE) {
                if (coinTossAtP(CREATE_LEAF_PROBABILITY)) {
                    GameObject leaf = new Leaf(new Vector2(j,i), getAvatarJumpCount);
                    listOfTreeComponents.add(leaf);
                }
                if (coinTossAtP(CREATE_FRUIT_PROBABILITY)) {
                    GameObject fruit = new Fruit(new Vector2(j,i) ,getAvatarJumpCount, changeAvatarEnergyBy);
                    listOfTreeComponents.add(fruit);
                }
            }
        }
        return listOfTreeComponents;
    }

    private int getRoundedMinX(int minX) {
        if (minX % BLOCK_SIZE != 0) {
            int denominator = minX / BLOCK_SIZE;
            if (minX < 0) {
                denominator--;
            }
            return denominator * BLOCK_SIZE;
        } else {
            return minX;
        }
    }

    private int getRoundedMaxX(int maxX) {
        if (maxX % BLOCK_SIZE != 0) {
            int denominator = maxX / BLOCK_SIZE;
            if (maxX > 0) {
                denominator++;
            }
            return denominator * BLOCK_SIZE;
        } else {
            return maxX;
        }
    }

    // TODO move to utils?
    private boolean coinTossAtP(double p) {
        return new Random().nextDouble() < p;
    }
}
