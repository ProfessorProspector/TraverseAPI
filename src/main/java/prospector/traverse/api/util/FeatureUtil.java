package prospector.traverse.api.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.List;

public class FeatureUtil {
	public static JsonObject serializeFeatures(Biome biome, JsonObject json) throws RuntimeException {
		if (json == null) {
			json = new JsonObject();
		}
		JsonObject features = new JsonObject();
		json.add("features", features);
		for (GenerationStep.Feature step : GenerationStep.Feature.values()) {
			JsonArray currentStep = new JsonArray();
			List<ConfiguredFeature<?>> featuresForStep = biome.getFeaturesForStep(step);
			if (!featuresForStep.isEmpty()) {
				features.add(step.getName(), currentStep);
				for (ConfiguredFeature feature : featuresForStep) {
					try {
						Dynamic dynamic = feature.serialize(JsonOps.INSTANCE);
						currentStep.add((JsonElement) dynamic.getValue());
					} catch (Exception e) {
						throw new RuntimeException("Feature " + feature.feature.getClass().getName() + " with config " + feature.config.getClass().getName() + " could not be serialized");
					}
				}
			}
		}
		return json;
	}
}
