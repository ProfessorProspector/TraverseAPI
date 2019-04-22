package io.github.prospector.traverse.api.mixin;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.layer.BiomeGroupToBiomeLayer;
import net.minecraft.world.biome.layer.LayerRandomnessSource;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BiomeGroupToBiomeLayer.class)
public class BiomeGroupToBiomeLayerMixin {

	@Shadow
	@Final
	@Mutable
	private static int[] GROUP_2;

	@Shadow
	@Final
	@Mutable
	private static int[] GROUP_3;

	@Shadow
	@Final
	private static int JUNGLE_ID;
	@Shadow
	@Final
	private static int SWAMP_ID;
	private static final int AUTUMNAL_WOODS_ID = Registry.BIOME.getRawId(Registry.BIOME.get(new Identifier("wander", "autumnal_woods")));
	private static final int WOODLANDS_WOODS_ID = Registry.BIOME.getRawId(Registry.BIOME.get(new Identifier("wander", "woodlands")));
	private static final int MEADOW_ID = Registry.BIOME.getRawId(Registry.BIOME.get(new Identifier("wander", "meadow")));
	private static final int MINI_JUNGLE_ID = Registry.BIOME.getRawId(Registry.BIOME.get(new Identifier("wander", "mini_jungle")));

	@Inject(method = "sample", at = @At("RETURN"), cancellable = true)
	private void sample(LayerRandomnessSource rand, int prev, CallbackInfoReturnable info) {
		if (info.getReturnValueI() == JUNGLE_ID && rand.nextInt(5) == 0 || info.getReturnValueI() == SWAMP_ID && rand.nextInt(3) == 0) {
			info.setReturnValue(MINI_JUNGLE_ID);
		}
	}

	static {
		GROUP_2 = ArrayUtils.addAll(GROUP_2, AUTUMNAL_WOODS_ID);
		GROUP_3 = ArrayUtils.addAll(GROUP_3, WOODLANDS_WOODS_ID, MEADOW_ID);
	}

}
