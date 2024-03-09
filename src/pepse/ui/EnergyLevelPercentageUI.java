package pepse.ui;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.function.Supplier;

/**
 * The EnergyLevelPercentageUI class represents a UI element displaying the energy level percentage.
 *
 * @author adir.barak, asher
 */
public class EnergyLevelPercentageUI extends GameObject {

    /**
     * The display format for the energy level percentage.
     */
    private static final String DISPLAY_FORMAT = "%d%%";

    /**
     * The maximum red value for setting the text color.
     */
    private static final int MAX_RED_VALUE = 255;

    /**
     * The text renderable representing the energy level percentage.
     */
    private final TextRenderable textRenderable;

    /**
     * The supplier for getting the current energy level percentage.
     */
    private final Supplier<Float> energyLevelSupplier;

    /**
     * The default position of the energy level UI element.
     */
    private static final Vector2 DEFAULT_POS = Vector2.ONES.mult(25f);

    /**
     * The default dimensions of the energy level UI element.
     */
    private static final Vector2 DEFAULT_DIMENSIONS = Vector2.ONES.mult(50f);

    /**
     * Constructs a new EnergyLevelPercentageUI instance with the given energy level supplier.
     *
     * @param energyLevelSupplier The supplier for getting the current energy level percentage.
     */
    public EnergyLevelPercentageUI(Supplier<Float> energyLevelSupplier) {
        super(DEFAULT_POS, DEFAULT_DIMENSIONS, null);
        this.textRenderable = new TextRenderable("");
        renderer().setRenderable(textRenderable);
        this.energyLevelSupplier = energyLevelSupplier;
    }

    /**
     * Updates the displayed energy level percentage and color based on the current energy level.
     *
     * @param energyLevel The current energy level percentage.
     */
    private void updateString(int energyLevel) {
        textRenderable.setString(String.format(DISPLAY_FORMAT, energyLevel));
    }

    /**
     * Updates the text color based on the current energy level.
     *
     * @param energyLevel The current energy level percentage.
     */
    private void updateColor(int energyLevel) {
        // calculate the value of the red color, accounting it gets linearly big inversely to energy level
        int redColorValueInPercentage = (MAX_RED_VALUE * (100 - energyLevel) / 100);
        textRenderable.setColor(new Color(redColorValueInPercentage, 0, 0));
    }

    /**
     * Updates the energy level UI element.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        int IntegerEnergyLevel = energyLevelSupplier.get().intValue();
        updateString(IntegerEnergyLevel);
        updateColor(IntegerEnergyLevel);
    }
}