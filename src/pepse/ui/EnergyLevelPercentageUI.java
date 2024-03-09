package pepse.ui;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.function.Supplier;

public class EnergyLevelPercentageUI extends GameObject {
    private static final String DISPLAY_FORMAT = "%d%%";
    private static final int MAX_RED_VALUE = 255;
    private final TextRenderable textRenderable;
    private final Supplier<Float> energyLevelSupplier;

    private static final Vector2 DEFAULT_POS = Vector2.ONES.mult(25f);
    private static final Vector2 DEFAULT_DIMENSIONS = Vector2.ONES.mult(50f);

    public EnergyLevelPercentageUI(Supplier<Float> energyLevelSupplier) {
        super(DEFAULT_POS, DEFAULT_DIMENSIONS, null);
        this.textRenderable = new TextRenderable("");
        renderer().setRenderable(textRenderable);
        this.energyLevelSupplier = energyLevelSupplier;
    }

    private void updateString(int energyLevel) {
        textRenderable.setString(String.format(DISPLAY_FORMAT, energyLevel));
    }

    private void updateColor(int energyLevel) {
        int redColorValueInPercentage = (MAX_RED_VALUE * (100 - energyLevel) / 100); // TODO magic?
        textRenderable.setColor(new Color(redColorValueInPercentage, 0, 0));
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        int IntegerEnergyLevel = energyLevelSupplier.get().intValue();
        updateString(IntegerEnergyLevel);
        updateColor(IntegerEnergyLevel);
    }
}
