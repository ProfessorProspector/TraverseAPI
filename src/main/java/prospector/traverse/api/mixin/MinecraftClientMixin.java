package prospector.traverse.api.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import prospector.traverse.api.Traverse;
import prospector.traverse.api.json.BiomePackLoader;

import java.util.HashMap;
import java.util.Map;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
	@Inject(at = @At("RETURN"), method = "init()V")
	public void afterInit(CallbackInfo info) {
	}
}
