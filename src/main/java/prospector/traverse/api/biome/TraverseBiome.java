package prospector.traverse.api.biome;

import net.minecraft.entity.EntityCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationSteps;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import prospector.traverse.api.json.object.BiomeInfo;

import java.util.EnumMap;
import java.util.List;
import java.util.OptionalInt;

public class TraverseBiome extends Biome {
	public final OptionalInt grassColor;
	public final OptionalInt foliageColor;
	public final EnumMap<GenerationSteps.FeatureStep, List<ConfiguredFeature>> features;
	public final EnumMap<EntityCategory, List<Biome.SpawnListEntry>> entitySpawns;

	public TraverseBiome(BiomeInfo biomeInfo) {
		super(new Builder().category(biomeInfo.getCategory()).parent(biomeInfo.getParent()).depth(biomeInfo.getDepth()).scale(biomeInfo.getScale()).temperature(biomeInfo.getTemperature()).downfall(biomeInfo.getDownfall()).precipitation(biomeInfo.getPrecipitation()).waterColor(biomeInfo.getWaterColor()).waterFogColor(biomeInfo.getWaterFogColor()).configureSurfaceBuilder(biomeInfo.getSurface().getSurfaceBuilder(), biomeInfo.getSurface().getConfig()));
		this.grassColor = biomeInfo.getGrassColor() == null ? OptionalInt.empty() : OptionalInt.of(biomeInfo.getGrassColor());
		this.foliageColor = biomeInfo.getFoliageColor() == null ? OptionalInt.empty() : OptionalInt.of(biomeInfo.getFoliageColor());
		this.features = biomeInfo.getFeatures();
		this.entitySpawns = biomeInfo.getEntitySpawns();
		if (features != null) {
			for (GenerationSteps.FeatureStep step : features.keySet()) {
				List<ConfiguredFeature> featureList = features.get(step);
				for (ConfiguredFeature feature : featureList) {
					this.addFeature(step, feature);
				}
			}
		}
		if (entitySpawns != null) {
			for (EntityCategory entityCategory : entitySpawns.keySet()) {
				List<SpawnListEntry> entries = entitySpawns.get(entityCategory);
				for (SpawnListEntry entry : entries) {
					addEntitySpawnEntry(entityCategory, entry);
				}
			}
		}
	}

	@Override
	public int getGrassColorAt(BlockPos pos) {
		return grassColor.orElse(super.getGrassColorAt(pos));
	}

	@Override
	public int getFoliageColorAt(BlockPos pos) {
		return foliageColor.orElse(super.getFoliageColorAt(pos));
	}
}
