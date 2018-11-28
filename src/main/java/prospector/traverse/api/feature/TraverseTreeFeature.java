package prospector.traverse.api.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.config.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.OakTreeFeature;

import java.util.function.Function;

public class TraverseTreeFeature extends OakTreeFeature {
	public boolean isWorldGen;

	public TraverseTreeFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> function, boolean isWorldGen) {
		super(function, !isWorldGen);
		this.isWorldGen = isWorldGen;
	}

	public TraverseTreeFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> function, boolean isWorldGen, int minTreeHeight, BlockState logState, BlockState leavesState, boolean vinesGrow) {
		super(function, !isWorldGen, minTreeHeight, logState, leavesState, vinesGrow);
		this.isWorldGen = isWorldGen;
	}

	public TraverseTreeFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> function, boolean isWorldGen, int minTreeHeight, BlockState logState, BlockState leavesState) {
		super(function, !isWorldGen, minTreeHeight, logState, leavesState, false);
		this.isWorldGen = isWorldGen;
	}

	public TraverseTreeFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> function, boolean isWorldGen, BlockState logState, BlockState leavesState) {
		super(function, !isWorldGen, 4, logState, leavesState, false);
		this.isWorldGen = isWorldGen;
	}
}
