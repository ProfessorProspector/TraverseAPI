package io.github.prospector.traverse.loader.biome;

import io.github.prospector.traverse.loader.json.BiomeLoadingException;
import io.github.prospector.traverse.loader.json.pojo.BiomeProperties;
import io.github.prospector.traverse.loader.json.pojo.DefaultFeatures;
import io.github.prospector.traverse.loader.json.pojo.InheritedFeatures;
import net.minecraft.entity.EntityCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.EnumMap;
import java.util.List;
import java.util.OptionalInt;

public class TraverseBiome extends Biome {
	public final OptionalInt grassColor;
	public final OptionalInt foliageColor;
	public final EnumMap<GenerationStep.Feature, List<ConfiguredFeature>> features;
	public final EnumMap<EntityCategory, List<Biome.SpawnEntry>> entitySpawns;

	public TraverseBiome(String id, BiomeProperties properties) throws BiomeLoadingException {
		super(new Settings().category(properties.getCategory()).parent(properties.getParent()).depth(properties.getDepth()).scale(properties.getScale()).temperature(properties.getTemperature()).downfall(properties.getDownfall()).precipitation(properties.getPrecipitation()).waterColor(properties.getWaterColor()).waterFogColor(properties.getWaterFogColor()).configureSurfaceBuilder(properties.getSurface().getSurfaceBuilder(), properties.getSurface().getConfig()));
		this.grassColor = properties.getGrassColor() == null ? OptionalInt.empty() : OptionalInt.of(properties.getGrassColor());
		this.foliageColor = properties.getFoliageColor() == null ? OptionalInt.empty() : OptionalInt.of(properties.getFoliageColor());
		this.features = properties.getFeatures();
		this.entitySpawns = properties.getEntitySpawns();
		InheritedFeatures[] featureInheritanceArray = properties.getInheritedFeatures();
		if (featureInheritanceArray != null) {
			for (InheritedFeatures featureInheritance : featureInheritanceArray) {
				Biome biome = Registry.BIOME.get(new Identifier(featureInheritance.getBiome()));
				if (biome == null) {
					throw new BiomeLoadingException("Biome " + id);
				}
				for (GenerationStep.Feature step : GenerationStep.Feature.values()) {
					for (ConfiguredFeature feature : biome.getFeaturesForStep(step)) {
						addFeature(step, feature);
					}
				}
			}
		}
		DefaultFeatures defaultFeatures = properties.getDefaultFeatures();
		if (defaultFeatures != null) {
			if (defaultFeatures.addLandCarvers()) {
				DefaultBiomeFeatures.addLandCarvers(this);
			}
			if (defaultFeatures.addOceanCarvers()) {
				DefaultBiomeFeatures.addOceanCarvers(this);
			}
			if (defaultFeatures.addDefaultStructures()) {
				DefaultBiomeFeatures.addDefaultStructures(this);
			}
			if (defaultFeatures.addDefaultLakes()) {
				DefaultBiomeFeatures.addDefaultLakes(this);
			}
			if (defaultFeatures.addDesertLakes()) {
				DefaultBiomeFeatures.addDesertLakes(this);
			}
			if (defaultFeatures.addDungeons()) {
				DefaultBiomeFeatures.addDungeons(this);
			}
			if (defaultFeatures.addMineables()) {
				DefaultBiomeFeatures.addMineables(this);
			}
			if (defaultFeatures.addDefaultOres()) {
				DefaultBiomeFeatures.addDefaultOres(this);
			}
			if (defaultFeatures.addExtraGoldOre()) {
				DefaultBiomeFeatures.addExtraGoldOre(this);
			}
			if (defaultFeatures.addEmeraldOre()) {
				DefaultBiomeFeatures.addEmeraldOre(this);
			}
			if (defaultFeatures.addInfestedStone()) {
				DefaultBiomeFeatures.addInfestedStone(this);
			}
			if (defaultFeatures.addDefaultDisks()) {
				DefaultBiomeFeatures.addDefaultDisks(this);
			}
			if (defaultFeatures.addClay()) {
				DefaultBiomeFeatures.addClay(this);
			}
			if (defaultFeatures.addMossyRocks()) {
				DefaultBiomeFeatures.addMossyRocks(this);
			}
			if (defaultFeatures.addLargeFerns()) {
				DefaultBiomeFeatures.addLargeFerns(this);
			}
			if (defaultFeatures.addSweetBerryBushesSnowy()) {
				DefaultBiomeFeatures.addSweetBerryBushesSnowy(this);
			}
			if (defaultFeatures.addSweetBerryBushes()) {
				DefaultBiomeFeatures.addSweetBerryBushes(this);
			}
			if (defaultFeatures.addBamboo()) {
				DefaultBiomeFeatures.addBamboo(this);
			}
			if (defaultFeatures.addBambooJungleTrees()) {
				DefaultBiomeFeatures.addBambooJungleTrees(this);
			}
			if (defaultFeatures.addTaigaTrees()) {
				DefaultBiomeFeatures.addTaigaTrees(this);
			}
			if (defaultFeatures.addWaterBiomeOakTrees()) {
				DefaultBiomeFeatures.addWaterBiomeOakTrees(this);
			}
			if (defaultFeatures.addBirchTrees()) {
				DefaultBiomeFeatures.addBirchTrees(this);
			}
			if (defaultFeatures.addForestTrees()) {
				DefaultBiomeFeatures.addForestTrees(this);
			}
			if (defaultFeatures.addTallBirchTrees()) {
				DefaultBiomeFeatures.addTallBirchTrees(this);
			}
			if (defaultFeatures.addSavannaTrees()) {
				DefaultBiomeFeatures.addSavannaTrees(this);
			}
			if (defaultFeatures.addExtraSavannaTrees()) {
				DefaultBiomeFeatures.addExtraSavannaTrees(this);
			}
			if (defaultFeatures.addMountainTrees()) {
				DefaultBiomeFeatures.addMountainTrees(this);
			}
			if (defaultFeatures.addExtraMountainTrees()) {
				DefaultBiomeFeatures.addExtraMountainTrees(this);
			}
			if (defaultFeatures.addJungleEdgeTrees()) {
				DefaultBiomeFeatures.addJungleEdgeTrees(this);
			}
			if (defaultFeatures.addBadlandsPlateauTrees()) {
				DefaultBiomeFeatures.addBadlandsPlateauTrees(this);
			}
			if (defaultFeatures.addSnowySpruceTrees()) {
				DefaultBiomeFeatures.addSnowySpruceTrees(this);
			}
			if (defaultFeatures.addGiantSpruceTaigaTrees()) {
				DefaultBiomeFeatures.addGiantSpruceTaigaTrees(this);
			}
			if (defaultFeatures.addGiantTreeTaigaTrees()) {
				DefaultBiomeFeatures.addGiantTreeTaigaTrees(this);
			}
			if (defaultFeatures.addJungleGrass()) {
				DefaultBiomeFeatures.addJungleGrass(this);
			}
			if (defaultFeatures.addSavannaTallGrass()) {
				DefaultBiomeFeatures.addSavannaTallGrass(this);
			}
			if (defaultFeatures.addShatteredSavannaGrass()) {
				DefaultBiomeFeatures.addShatteredSavannaGrass(this);
			}
			if (defaultFeatures.addSavannaGrass()) {
				DefaultBiomeFeatures.addSavannaGrass(this);
			}
			if (defaultFeatures.addBadlandsGrass()) {
				DefaultBiomeFeatures.addBadlandsGrass(this);
			}
			if (defaultFeatures.addForestFlowers()) {
				DefaultBiomeFeatures.addForestFlowers(this);
			}
			if (defaultFeatures.addForestGrass()) {
				DefaultBiomeFeatures.addForestGrass(this);
			}
			if (defaultFeatures.addSwampFeatures()) {
				DefaultBiomeFeatures.addSwampFeatures(this);
			}
			if (defaultFeatures.addMushroomFieldsFeatures()) {
				DefaultBiomeFeatures.addMushroomFieldsFeatures(this);
			}
			if (defaultFeatures.addPlainsFeatures()) {
				DefaultBiomeFeatures.addPlainsFeatures(this);
			}
			if (defaultFeatures.addDesertDeadBushes()) {
				DefaultBiomeFeatures.addDesertDeadBushes(this);
			}
			if (defaultFeatures.addGiantTaigaGrass()) {
				DefaultBiomeFeatures.addGiantTaigaGrass(this);
			}
			if (defaultFeatures.addDefaultFlowers()) {
				DefaultBiomeFeatures.addDefaultFlowers(this);
			}
			if (defaultFeatures.addExtraDefaultFlowers()) {
				DefaultBiomeFeatures.addExtraDefaultFlowers(this);
			}
			if (defaultFeatures.addDefaultGrass()) {
				DefaultBiomeFeatures.addDefaultGrass(this);
			}
			if (defaultFeatures.addTaigaGrass()) {
				DefaultBiomeFeatures.addTaigaGrass(this);
			}
			if (defaultFeatures.addPlainsTallGrass()) {
				DefaultBiomeFeatures.addPlainsTallGrass(this);
			}
			if (defaultFeatures.addDefaultMushrooms()) {
				DefaultBiomeFeatures.addDefaultMushrooms(this);
			}
			if (defaultFeatures.addDefaultVegetation()) {
				DefaultBiomeFeatures.addDefaultVegetation(this);
			}
			if (defaultFeatures.addBadlandsVegetation()) {
				DefaultBiomeFeatures.addBadlandsVegetation(this);
			}
			if (defaultFeatures.addJungleVegetation()) {
				DefaultBiomeFeatures.addJungleVegetation(this);
			}
			if (defaultFeatures.addDesertVegetation()) {
				DefaultBiomeFeatures.addDesertVegetation(this);
			}
			if (defaultFeatures.addSwampVegetation()) {
				DefaultBiomeFeatures.addSwampVegetation(this);
			}
			if (defaultFeatures.addDesertVegetation()) {
				DefaultBiomeFeatures.addDesertVegetation(this);
			}
			if (defaultFeatures.addFossils()) {
				DefaultBiomeFeatures.addFossils(this);
			}
			if (defaultFeatures.addKelp()) {
				DefaultBiomeFeatures.addKelp(this);
			}
			if (defaultFeatures.addSeagrassOnStone()) {
				DefaultBiomeFeatures.addSeagrassOnStone(this);
			}
			if (defaultFeatures.addSeagrass()) {
				DefaultBiomeFeatures.addSeagrass(this);
			}
			if (defaultFeatures.addMoreSeagrass()) {
				DefaultBiomeFeatures.addMoreSeagrass(this);
			}
			if (defaultFeatures.addLessKelp()) {
				DefaultBiomeFeatures.addLessKelp(this);
			}
			if (defaultFeatures.addSprings()) {
				DefaultBiomeFeatures.addSprings(this);
			}
			if (defaultFeatures.addIcebergs()) {
				DefaultBiomeFeatures.addIcebergs(this);
			}
			if (defaultFeatures.addBlueIce()) {
				DefaultBiomeFeatures.addBlueIce(this);
			}
			if (defaultFeatures.addFrozenTopLayer()) {
				DefaultBiomeFeatures.addFrozenTopLayer(this);
			}
		}
		if (features != null) {
			for (GenerationStep.Feature step : features.keySet()) {
				List<ConfiguredFeature> featureList = features.get(step);
				for (ConfiguredFeature feature : featureList) {
					this.addFeature(step, feature);
				}
			}
		}
		if (entitySpawns != null) {
			for (EntityCategory entityCategory : entitySpawns.keySet()) {
				List<SpawnEntry> entries = entitySpawns.get(entityCategory);
				for (SpawnEntry entry : entries) {
					addSpawn(entityCategory, entry);
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
