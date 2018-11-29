package prospector.traverse.api.json.deserializer;

import com.google.gson.*;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import prospector.traverse.api.json.BiomePackLoader;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

public class FeaturesDeserializer implements JsonDeserializer<EnumMap<GenerationStep.Feature, List<ConfiguredFeature>>> {
	@Override
	public EnumMap<GenerationStep.Feature, List<ConfiguredFeature>> deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) {
		EnumMap<GenerationStep.Feature, List<ConfiguredFeature>> features = new EnumMap<>(GenerationStep.Feature.class);
		for (GenerationStep.Feature step : GenerationStep.Feature.values()) {
			List<ConfiguredFeature> featuresForStep = new ArrayList<>();
			JsonObject root = json.getAsJsonObject();
			if (root.has(step.getName())) {
				JsonArray currentStep = root.get(step.getName()).getAsJsonArray();
				for (int i = 0; i < currentStep.size(); i++) {
					try {
						Dynamic<?> dynamic = new Dynamic<>(JsonOps.INSTANCE, currentStep.get(i).getAsJsonObject());
						featuresForStep.add(ConfiguredFeature.deserialize(dynamic));
					} catch (Exception e) {
						BiomePackLoader.catchException("Feature of index [" + i + "] in step " + step.getName() + " could not be deserialized. (remember, 0 is included in index)", e);
					}
				}
				features.put(step, featuresForStep);
			} else {
				features.put(step, Collections.emptyList());
			}
		}
		return features;
	}
}