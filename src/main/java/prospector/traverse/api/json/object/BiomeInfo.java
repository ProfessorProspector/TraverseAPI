package prospector.traverse.api.json.object;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityCategory;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationSteps;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import prospector.traverse.api.Traverse;
import prospector.traverse.api.json.deserializer.EntitySpawnsDeserializer;
import prospector.traverse.api.json.deserializer.FeaturesDeserializer;
import prospector.traverse.api.util.NumberUtil;

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
	EnumMap<GenerationSteps.FeatureStep, List<ConfiguredFeature>> features;
	@SerializedName("entity_spawns")
	@JsonAdapter(EntitySpawnsDeserializer.class)
	EnumMap<EntityCategory, List<Biome.SpawnListEntry>> entitySpawns;

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
		return Traverse.PRECIPITATION_NAME_MAP.get(precipitation);
	}

	public Integer getGrassColor() {
		return NumberUtil.parseHexString(grassColor);
	}

	public Integer getFoliageColor() {
		return NumberUtil.parseHexString(foliageColor);
	}

	public int getWaterColor() {
		return NumberUtil.parseHexString(waterColor);
	}

	public int getWaterFogColor() {
		return NumberUtil.parseHexString(waterFogColor);
	}

	public SurfaceInfo getSurface() {
		return surface;
	}

	public EnumMap<GenerationSteps.FeatureStep, List<ConfiguredFeature>> getFeatures() {
		return features;
	}

	public EnumMap<EntityCategory, List<Biome.SpawnListEntry>> getEntitySpawns() {
		return entitySpawns;
	}
}
