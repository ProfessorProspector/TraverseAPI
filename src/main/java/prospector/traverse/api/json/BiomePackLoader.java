package prospector.traverse.api.json;

import com.google.gson.*;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationSteps;
import net.minecraft.world.gen.config.surfacebuilder.TernarySurfaceConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import prospector.traverse.api.Traverse;
import prospector.traverse.api.biome.TraverseBiome;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class BiomePackLoader {
	public static final Logger LOGGER = LogManager.getLogger();

	public static final Map<String, Biome.Category> CATEGORY_NAME_MAP = Arrays.stream(Biome.Category.values()).collect(Collectors.toMap(Biome.Category::getName, (category) -> category));
	public static final Map<String, Biome.Precipitation> PRECIPITATION_NAME_MAP = Arrays.stream(Biome.Precipitation.values()).collect(Collectors.toMap(Biome.Precipitation::getName, (var0) -> var0));
	public static final Map<String, EntityCategory> ENTITY_CATEGORY_NAME_MAP = Arrays.stream(EntityCategory.values()).collect(Collectors.toMap(EntityCategory::getName, (var0) -> var0));

	public void loadBiomePacks() {
		for (String biomePack : Traverse.BIOME_PACKS) {
			loadBiomePack(biomePack);
		}
	}

	public void loadBiomePack(String id) {
		try {
			List<String> biomes = getContentsOfType(id, "biomes");
			Gson gson = new Gson();
			for (String biome : biomes) {
				BiomeInfo biomeInfo;
				JsonParser parser = new JsonParser();
				JsonObject json;
				String jsonPath = "biomepacks/" + id + "/biomes/" + biome + ".json";
				InputStreamReader isr;
				try {
					InputStream is = getClass().getClassLoader().getResourceAsStream(jsonPath);
					isr = new InputStreamReader(is);
				} catch (Exception e) {
					throw new IOException("Could not read: " + jsonPath, e);
				}
				try {
					json = parser.parse(isr).getAsJsonObject();
					biomeInfo = gson.fromJson(json, BiomeInfo.class);
				} catch (Exception e) {
					throw new JsonParseException("Could not parse file for biome: " + id + ":" + biome, e);
				}
				TernarySurfaceConfig surfaceConfig;
				try {
					JsonObject root = json.getAsJsonObject("surface");
					Dynamic<?> dynamic = new Dynamic<>(JsonOps.INSTANCE, root.get("config"));
					surfaceConfig = TernarySurfaceConfig.deserialize(dynamic);
				} catch (Exception e) {
					throw new JsonParseException("Surface could not be deserialized for " + id + ":" + biome, e);
				}
				EnumMap<GenerationSteps.FeatureStep, List<ConfiguredFeature>> features;
				EnumMap<EntityCategory, List<Biome.SpawnListEntry>> entitySpawns;
				try {
					features = deserializeFeatures(json);
				} catch (Exception e) {
					throw new JsonParseException("Features could not be deserialized for " + id + ":" + biome, e);
				}
				try {
					entitySpawns = deserializeEntitySpawns(json);
				} catch (Exception e) {
					throw new JsonParseException("Entity Spawns could not be deserialized for " + id + ":" + biome, e);
				}

				Traverse.registerBiome(id, biome, new TraverseBiome(biomeInfo, surfaceConfig, features, entitySpawns));
			}
		} catch (Exception e) {
			throw new RuntimeException("Could not load biomepack: " + id, e);
		}
	}

	public static JsonObject serializeFeatures(Biome biome, JsonObject json) throws RuntimeException {
		if (json == null) {
			json = new JsonObject();
		}
		JsonObject features = new JsonObject();
		json.add("features", features);
		for (GenerationSteps.FeatureStep step : GenerationSteps.FeatureStep.values()) {
			JsonArray currentStep = new JsonArray();
			List<ConfiguredFeature<?>> featuresForStep = biome.getFeaturesForStep(step);
			if (!featuresForStep.isEmpty()) {
				features.add(step.getName(), currentStep);
				for (ConfiguredFeature feature : featuresForStep) {
					try {
						Dynamic dynamic = feature.serialize(JsonOps.INSTANCE);
						currentStep.add((JsonElement) dynamic.getValue());
					} catch (Exception e) {
						throw new RuntimeException("Feature " + feature.feature.getClass().getName() + " with config " + feature.config.getClass().getName() + " could not be deserialized");
					}
				}
			}
		}
		return json;
	}

	public static EnumMap<GenerationSteps.FeatureStep, List<ConfiguredFeature>> deserializeFeatures(JsonObject json) throws JsonParseException {
		EnumMap<GenerationSteps.FeatureStep, List<ConfiguredFeature>> features = new EnumMap<>(GenerationSteps.FeatureStep.class);
		for (GenerationSteps.FeatureStep step : GenerationSteps.FeatureStep.values()) {
			List<ConfiguredFeature> featuresForStep = new ArrayList<>();
			JsonObject root = json.getAsJsonObject("features");
			if (root.has(step.getName())) {
				JsonArray currentStep = root.get(step.getName()).getAsJsonArray();
				for (int i = 0; i < currentStep.size(); i++) {
					try {
						Dynamic<?> dynamic = new Dynamic<>(JsonOps.INSTANCE, currentStep.get(i).getAsJsonObject());
						featuresForStep.add(ConfiguredFeature.deserialize(dynamic));
					} catch (Exception e) {
						throw new JsonParseException("Feature " + i + " in step " + step.getName() + " could not be deserialized", e);
					}
				}
				features.put(step, featuresForStep);
			} else {
				features.put(step, Collections.emptyList());
			}
		}
		return features;
	}

	public static EnumMap<EntityCategory, List<Biome.SpawnListEntry>> deserializeEntitySpawns(JsonObject json) throws JsonParseException {
		EnumMap<EntityCategory, List<Biome.SpawnListEntry>> entitySpawns = new EnumMap<>(EntityCategory.class);
		JsonObject root = json.getAsJsonObject("entity_spawns");
		for (EntityCategory category : EntityCategory.values()) {
			List<Biome.SpawnListEntry> entriesForCategory = new ArrayList<>();
			if (root.has(category.getName())) {
				JsonArray currentCategory = root.get(category.getName()).getAsJsonArray();
				for (int i = 0; i < currentCategory.size(); i++) {
					try {
						JsonObject entry = currentCategory.get(i).getAsJsonObject();
						//noinspection unchecked
						entriesForCategory.add(new Biome.SpawnListEntry((EntityType<? extends MobEntity>) Registry.ENTITY_FACTORIES.get(new Identifier(entry.get("type").getAsString())), entry.get("weight").getAsInt(), entry.get("min_group_size").getAsInt(), entry.get("max_group_size").getAsInt()));
					} catch (Exception e) {
						throw new JsonParseException("SpawnListEntry " + i + " in category " + category.getName() + " could not be created...possibly EntityType is not a MobEntity", e);
					}
				}
				entitySpawns.put(category, entriesForCategory);
			} else {
				entitySpawns.put(category, Collections.emptyList());
			}
		}
		return entitySpawns;
	}

	public List<String> getContentsOfType(String id, String type) {
		JsonParser parser = new JsonParser();
		JsonObject json = null;
		try (InputStream is = getClass().getClassLoader().getResourceAsStream("biomepacks/" + id + "/" + type + ".json"); InputStreamReader isr = new InputStreamReader(is)) {
			json = parser.parse(isr).getAsJsonObject();
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
		}
		if (json != null) {
			List<String> list = new ArrayList<>();
			JsonArray array = json.getAsJsonArray(type);
			for (int i = 0; i < array.size(); i++) {
				list.add(array.get(i).getAsString());
			}
			return list;
		}
		return null;
	}

}
