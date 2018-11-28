package prospector.traverse.api.biome;

import net.minecraft.entity.EntityCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationSteps;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

import java.util.EnumMap;
import java.util.List;

public class TraverseBiome extends Biome {
	public final Biome.Category category;
	public final String parent;
	public final Float depth;
	public final Float scale;
	public final Float temperature;
	public final Float downfall;
	public final Precipitation precipitation;
	public final Integer grassColor;
	public final Integer foliageColor;
	public final Integer waterColor;
	public final Integer waterFogColor;
	public final EnumMap<GenerationSteps.FeatureStep, List<ConfiguredFeature>> features;
	public final EnumMap<EntityCategory, List<Biome.SpawnListEntry>> entitySpawns;

	public TraverseBiome(Biome.Category category,
	                     String parent,
	                     Float depth,
	                     Float scale,
	                     Float temperature,
	                     Float downfall,
	                     Precipitation precipitation,
	                     Integer grassColor,
	                     Integer foliageColor,
	                     Integer waterColor,
	                     Integer waterFogColor,
	                     EnumMap<GenerationSteps.FeatureStep, List<ConfiguredFeature>> features,
	                     EnumMap<EntityCategory, List<Biome.SpawnListEntry>> entitySpawns) {
		super(build(category, parent, depth, scale, temperature, downfall, precipitation, waterColor, waterFogColor));
		this.category = category;
		this.parent = parent;
		this.depth = depth;
		this.scale = scale;
		this.temperature = temperature;
		this.downfall = downfall;
		this.precipitation = precipitation;
		this.grassColor = grassColor;
		this.foliageColor = foliageColor;
		this.waterColor = waterColor;
		this.waterFogColor = waterFogColor;
		this.features = features;
		this.entitySpawns = entitySpawns;
		for (GenerationSteps.FeatureStep step : features.keySet()) {
			List<ConfiguredFeature> featureList = features.get(step);
			for (ConfiguredFeature feature : featureList) {
				this.addFeature(step, feature);
			}
		}
		for (EntityCategory entityCategory : entitySpawns.keySet()) {
			List<SpawnListEntry> entries = entitySpawns.get(entityCategory);
			for (SpawnListEntry entry : entries) {
				addEntitySpawnEntry(entityCategory, entry);
			}
		}
	}

	public static Builder build(Biome.Category category,
	                            String parent,
	                            Float depth,
	                            Float scale,
	                            Float temperature,
	                            Float downfall,
	                            Precipitation precipitation,
	                            Integer waterColor,
	                            Integer waterFogColor) {
		Builder builder = new Builder();
		builder.surfaceBuilder(new ConfiguredSurfaceBuilder<>(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_CONFIG));
		if (category != null)
			builder.category(category);
		if (parent != null && !parent.isEmpty())
			builder.parent(parent);
		if (depth != null)
			builder.depth(depth);
		if (scale != null)
			builder.scale(scale);
		if (temperature != null)
			builder.temperature(temperature);
		if (downfall != null)
			builder.downfall(downfall);
		if (precipitation != null)
			builder.precipation(precipitation);
		if (waterColor != null)
			builder.waterColor(waterColor);
		if (waterFogColor != null)
			builder.waterFogColor(waterFogColor);
		return builder;
	}

	@Override
	public int getGrassColorAt(BlockPos blockPos) {
		return grassColor;
	}

	@Override
	public int getFoliageColorAt(BlockPos blockPos) {
		return foliageColor;
	}
}
