package prospector.traverse.api.json.object;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityCategory;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import prospector.traverse.api.Traverse;
import prospector.traverse.api.json.BiomePackLoader;
import prospector.traverse.api.json.deserializer.EntitySpawnsDeserializer;
import prospector.traverse.api.json.deserializer.FeaturesDeserializer;

import java.util.EnumMap;
import java.util.List;

public class BiomeInfo {
	String category;
	String parent;
	Float depth;
	Float scale;
	Float temperature;
	Float downfall;
	String precipitation;
	@SerializedName("grass_color")
	String grassColor;
	@SerializedName("foliage_color")
	String foliageColor;
	@SerializedName("water_color")
	String waterColor = "#3F76E4";
	@SerializedName("water_fog_color")
	String waterFogColor = "#050533";
	SurfaceInfo surface;
	@JsonAdapter(FeaturesDeserializer.class)
	EnumMap<GenerationStep.Feature, List<ConfiguredFeature>> features;
	@SerializedName("entity_spawns")
	@JsonAdapter(EntitySpawnsDeserializer.class)
	EnumMap<EntityCategory, List<Biome.SpawnEntry>> entitySpawns;

	public Biome.Category getCategory() {
		return Traverse.CATEGORY_NAME_MAP.get(category);
	}

	public String getParent() {
		return parent;
	}

	public Float getDepth() {
		return depth;
	}

	public Float getScale() {
		return scale;
	}

	public Float getTemperature() {
		return temperature;
	}

	public Float getDownfall() {
		return downfall;
	}

	public Biome.Precipitation getPrecipitation() {
		if (precipitation == null) {
			Biome.Category category = getCategory();
			if (category == Biome.Category.DESERT || category == Biome.Category.NETHER || category == Biome.Category.THE_END) {
				return Biome.Precipitation.NONE;
			} else if (category == Biome.Category.ICY) {
				return Biome.Precipitation.SNOW;
			} else {
				return Biome.Precipitation.RAIN;
			}
		}
		return Traverse.PRECIPITATION_NAME_MAP.get(precipitation);
	}

	public Integer getGrassColor() {
		return BiomePackLoader.parseHexString(grassColor);
	}

	public Integer getFoliageColor() {
		return BiomePackLoader.parseHexString(foliageColor);
	}

	public int getWaterColor() {
		return BiomePackLoader.parseHexString(waterColor);
	}

	public int getWaterFogColor() {
		return BiomePackLoader.parseHexString(waterFogColor);
	}

	public SurfaceInfo getSurface() {
		if (surface == null) {
			return new SurfaceInfo();
		}
		return surface;
	}

	public EnumMap<GenerationStep.Feature, List<ConfiguredFeature>> getFeatures() {
		return features;
	}

	public EnumMap<EntityCategory, List<Biome.SpawnEntry>> getEntitySpawns() {
		return entitySpawns;
	}
}
