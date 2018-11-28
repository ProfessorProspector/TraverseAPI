package prospector.traverse.api.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationSteps;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import prospector.traverse.api.Traverse;
import prospector.traverse.api.biome.TraverseBiome;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

public class BiomePackLoader {
	public static final Map<String, Biome.Category> CATEGORY_NAME_MAP = Arrays.stream(Biome.Category.values()).collect(Collectors.toMap(Biome.Category::getName, (category) -> category));
	public static final Map<String, Biome.Precipitation> PRECIPITATION_NAME_MAP = Arrays.stream(Biome.Precipitation.values()).collect(Collectors.toMap(Biome.Precipitation::getName, (var0) -> var0));
	public static final Map<String, EntityCategory> ENTITY_CATEGORY_NAME_MAP = Arrays.stream(EntityCategory.values()).collect(Collectors.toMap(EntityCategory::getName, (var0) -> var0));

	public void loadBiomePacks() {
		for (String biomePack : Traverse.BIOME_PACKS) {
			loadBiomePack(biomePack);
		}
	}

	public void loadBiomePack(String id) {
		List<String> biomes = getContentsOfType(id, "biomes");
		for (String biome : biomes) {
			JsonParser parser = new JsonParser();
			JsonObject json = null;
			try (InputStream is = getClass().getClassLoader().getResourceAsStream("biomepacks/" + id + "/biomes/" + biome + ".json"); InputStreamReader isr = new InputStreamReader(is)) {
				json = parser.parse(isr).getAsJsonObject();
			} catch (IOException | NullPointerException e) {
				e.printStackTrace();
			}
			if (json != null) {
				Biome.Category category = null;
				String parent = null;
				Float depth = null;
				Float scale = null;
				Float temperature = null;
				Float downfall = null;
				Biome.Precipitation precipitation = null;
				Integer grassColor = null;
				Integer foliageColor = null;
				Integer waterColor = null;
				Integer waterFogColor = null;
				EnumMap<GenerationSteps.FeatureStep, List<ConfiguredFeature>> features = null;
				EnumMap<EntityCategory, List<Biome.SpawnListEntry>> entitySpawns = null;
				try { category = CATEGORY_NAME_MAP.get(json.get("category").getAsString()); } catch (NullPointerException ignored) {}
				try { parent = json.get("parent").getAsString(); } catch (NullPointerException ignored) {}
				try { depth = json.get("depth").getAsFloat(); } catch (NullPointerException ignored) {}
				try { scale = json.get("scale").getAsFloat(); } catch (NullPointerException ignored) {}
				try { temperature = json.get("temperature").getAsFloat(); } catch (NullPointerException ignored) {}
				try { downfall = json.get("downfall").getAsFloat(); } catch (NullPointerException ignored) {}
				try { precipitation = PRECIPITATION_NAME_MAP.get(json.get("precipitation").getAsString()); } catch (NullPointerException ignored) {}
				try { grassColor = parseHex(json.get("grass_color").getAsString()); } catch (NullPointerException ignored) {}
				try { foliageColor = parseHex(json.get("grass_color").getAsString()); } catch (NullPointerException ignored) {}
				try { waterColor = parseHex(json.get("water_color").getAsString()); } catch (NullPointerException ignored) {}
				try { waterFogColor = parseHex(json.get("water_fog_color").getAsString()); } catch (NullPointerException ignored) {}
				try { features = deserializeFeatures(json); } catch (NullPointerException e) {e.printStackTrace();}
				try {
					entitySpawns = new EnumMap<>(EntityCategory.class);
					{
						JsonArray array = json.getAsJsonArray("entity_spawns");
						for (int i = 0; i < array.size(); i++) {
							JsonObject entry = array.get(i).getAsJsonObject();
							EntityCategory entityCategory = ENTITY_CATEGORY_NAME_MAP.get(entry.get("category").getAsString());
							entitySpawns.computeIfAbsent(entityCategory, k -> new ArrayList<>());
							try {
								//noinspection unchecked
								entitySpawns.get(entityCategory).add(new Biome.SpawnListEntry((EntityType<? extends MobEntity>) Registry.ENTITY_FACTORIES.get(new Identifier(entry.get("type").getAsString())), entry.get("weight").getAsInt(), entry.get("min_group_size").getAsInt(), entry.get("max_group_size").getAsInt()));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				} catch (NullPointerException ignored) {}
				Traverse.registerBiome(id, biome, new TraverseBiome(category, parent, depth, scale, temperature, downfall, precipitation, grassColor, foliageColor, waterColor, waterFogColor, features, entitySpawns));
			}
		}
	}

	public static int parseHex(String string) {
		if (string.contains("#")) {
			String[] splitString = string.split("#");
			if (splitString.length != 2) {
				throw new InvalidParameterException(string + " contains multiple # and is an invalid hex string");
			}
			string = splitString[1];
		}
		return Integer.parseInt(string, 16);
	}

	public static JsonObject serializeFeatures(Biome biome, JsonObject json) {
		if (json == null) {
			json = new JsonObject();
		}
		JsonObject features = new JsonObject();
		json.add("features", features);
		for (GenerationSteps.FeatureStep step : GenerationSteps.FeatureStep.values()) {
			JsonArray currentStep = new JsonArray();
			features.add(step.getName(), currentStep);
			for (ConfiguredFeature feature : biome.getFeaturesForStep(step)) {
				Dynamic dynamic = feature.serialize(JsonOps.INSTANCE);
				currentStep.add((JsonElement) dynamic.getValue());
			}
		}
		return json;
	}

	public static EnumMap<GenerationSteps.FeatureStep, List<ConfiguredFeature>> deserializeFeatures(JsonObject json) {
		EnumMap<GenerationSteps.FeatureStep, List<ConfiguredFeature>> features = new EnumMap<>(GenerationSteps.FeatureStep.class);
		for (GenerationSteps.FeatureStep step : GenerationSteps.FeatureStep.values()) {
			List<ConfiguredFeature> featuresForStep = new ArrayList<>();
			JsonArray currentStep = json.getAsJsonObject("features").get(step.getName()).getAsJsonArray();
			for (int i = 0; i < currentStep.size(); i++) {
				try {
					Dynamic<?> dynamic = new Dynamic<>(JsonOps.INSTANCE, currentStep.get(i).getAsJsonObject());
					featuresForStep.add(ConfiguredFeature.deserialize(dynamic));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			features.put(step, featuresForStep);
		}
		return features;
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
