package io.github.prospector.traverse.api.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.OakTreeFeature;

import java.util.function.Function;

public class TraverseTreeFeature extends OakTreeFeature {

    public boolean worldGen;

    public TraverseTreeFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> function, boolean worldGen) {
        super(function, !worldGen);
        this.worldGen = worldGen;
    }

    public TraverseTreeFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> function, boolean worldGen, int minHeight, BlockState log, BlockState leaves, boolean vines) {
        super(function, !worldGen, minHeight, log, leaves, vines);
        this.worldGen = worldGen;
    }

    public TraverseTreeFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> function, boolean worldGen, int minHeight, BlockState log, BlockState leaves) {
        super(function, !worldGen, minHeight, log, leaves, false);
        this.worldGen = worldGen;
    }

    public TraverseTreeFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> function, boolean worldGen, BlockState log, BlockState leaves) {
        super(function, !worldGen, 4, log, leaves, false);
        this.worldGen = worldGen;
    }

}
