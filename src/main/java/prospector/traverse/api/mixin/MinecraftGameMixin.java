package prospector.traverse.api.mixin;

import net.minecraft.client.MinecraftGame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import prospector.traverse.api.json.BiomePackLoader;

@Mixin(MinecraftGame.class)
public abstract class MinecraftGameMixin {
	@Inject(at = @At("RETURN"), method = "init()V")
	public void afterInit(CallbackInfo info) {
		new BiomePackLoader().loadBiomePacks();
	}
}
