package prospector.traverse.api.biome;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationSteps;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.config.ProbabilityConfig;
import net.minecraft.world.gen.config.decorator.*;
import net.minecraft.world.gen.config.feature.*;
import net.minecraft.world.gen.config.surfacebuilder.SurfaceConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FlowerFeature;
import net.minecraft.world.gen.feature.MineshaftFeature;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

public abstract class OLDTRAVERSEBIOME extends Biome {
	public boolean hasMineshafts = true;
	public boolean hasStrongholds = true;
	public boolean hasRavines = true;
	public float ravineProbability = 0.02F;
	public boolean hasCaves = true;
	public float caveProbability = 0.14285715F;
	public boolean hasDefaultFeatures = true;
	public boolean hasWaterLakes = true;
	public int waterLakeRarity = 4;
	public boolean hasLavaLakes = true;
	public int lavaLakeRarity = 80;
	public boolean hasDungeons = true;
	public boolean hasIceAndSnowFeature = true;
	public boolean hasSphereReplaces = true;
	public boolean hadDefaultMinables = true;
	public int dirtSize = 33;
	public int gravelSize = 33;
	public int graniteSize = 33;
	public int dioriteSize = 33;
	public int andesiteSize = 33;
	public int coalSize = 17;
	public int ironSize = 9;
	public int goldSize = 9;
	public int redstoneSize = 8;
	public int diamondSize = 8;
	public int lapisSize = 7;
	public boolean hasDefaultEntitySpawns = true;
	public boolean hasDefaultVegetation = true;
	public boolean hasDoubleFlowers = true;
	public int doubleFlowerFrequency = 5;
	public FlowerFeature flowerFeature = Feature.DEFAULT_FLOWER;
	public int flowerFrequency = 2;
	public Feature grassFeature = Feature.GRASS;
	public FeatureConfig grassFeatureConfig = new GrassFeatureConfig(Blocks.GRASS.getDefaultState());
	public int grassFrequency = 2;
	public int brownMushroomChance = 4;
	public int redMushroomChance = 8;
	public int sugarCaneFrequency = 10;
	public int pumpkinChance = 32;
	public int melonFrequency = 0;
	public int vineFrequency = 0;
	public SpawnListEntry sheepSpawnEntry = new SpawnListEntry(EntityType.SHEEP, 12, 4, 4);
	public SpawnListEntry pigSpawnEntry = new SpawnListEntry(EntityType.PIG, 10, 4, 4);
	public SpawnListEntry chickenSpawnEntry = new SpawnListEntry(EntityType.CHICKEN, 10, 4, 4);
	public SpawnListEntry cowSpawnEntry = new SpawnListEntry(EntityType.COW, 8, 4, 4);
	public SpawnListEntry squidSpawnEntry = new SpawnListEntry(EntityType.SQUID, 10, 1, 2);
	public SpawnListEntry batSpawnEntry = new SpawnListEntry(EntityType.BAT, 10, 8, 8);
	public SpawnListEntry spiderSpawnEntry = new SpawnListEntry(EntityType.SPIDER, 100, 4, 4);
	public SpawnListEntry zombieSpawnEntry = new SpawnListEntry(EntityType.ZOMBIE, 95, 4, 4);
	public SpawnListEntry zombieVillagerSpawnEntry = new SpawnListEntry(EntityType.ZOMBIE_VILLAGER, 5, 1, 1);
	public SpawnListEntry skeletonSpawnEntry = new SpawnListEntry(EntityType.SKELETON, 100, 4, 4);
	public SpawnListEntry creeperSpawnEntry = new SpawnListEntry(EntityType.CREEPER, 100, 4, 4);
	public SpawnListEntry slimeSpawnEntry = new SpawnListEntry(EntityType.SLIME, 100, 4, 4);
	public SpawnListEntry endermanSpawnEntry = new SpawnListEntry(EntityType.ENDERMAN, 10, 1, 4);
	public SpawnListEntry witchSpawnEntry = new SpawnListEntry(EntityType.WITCH, 5, 1, 1);

	public OLDTRAVERSEBIOME(Builder builder) {
		super(builder);
		overrideOptions();
		addFeatures();
	}

	protected abstract void overrideOptions();

	public <C extends SurfaceConfig> OLDTRAVERSEBIOME(SurfaceBuilder<C> surfaceBuilder,
	                                                  C surfaceConfig,
	                                                  Precipitation precipitation,
	                                                  Category category,
	                                                  float depth,
	                                                  float scale,
	                                                  float temperature,
	                                                  float downfall,
	                                                  int waterColor,
	                                                  int waterFogColor,
	                                                  String parent) {
		this((new Biome.Builder()).surfaceBuilder(new ConfiguredSurfaceBuilder<>(surfaceBuilder, surfaceConfig)).precipitation(precipitation).category(category).depth(depth).scale(scale).temperature(temperature).downfall(downfall).waterColor(waterColor).waterFogColor(waterFogColor).parent(parent));
	}

	public OLDTRAVERSEBIOME(SurfaceBuilder surfaceBuilder,
	                        SurfaceConfig surfaceConfig,
	                        Precipitation precipitation,
	                        Category category,
	                        float depth,
	                        float scale,
	                        float temperature,
	                        float downfall,
	                        int waterColor,
	                        int waterFogColor) {
		this(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_CONFIG, precipitation, category, depth, scale, temperature, downfall, waterColor, waterFogColor, null);
	}

	public OLDTRAVERSEBIOME(Precipitation precipitation,
	                        Category category,
	                        float depth,
	                        float scale,
	                        float temperature,
	                        float downfall,
	                        int waterColor,
	                        int waterFogColor,
	                        String parent) {
		this(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_CONFIG, precipitation, category, depth, scale, temperature, downfall, waterColor, waterFogColor, parent);
	}

	public OLDTRAVERSEBIOME(Precipitation precipitation,
	                        Category category,
	                        float depth,
	                        float scale,
	                        float temperature,
	                        float downfall,
	                        int waterColor,
	                        int waterFogColor) {
		this(precipitation, category, depth, scale, temperature, downfall, waterColor, waterFogColor, null);
	}

	public OLDTRAVERSEBIOME(Category category,
	                        float depth,
	                        float scale,
	                        float temperature,
	                        float downfall,
	                        int waterColor,
	                        int waterFogColor) {
		this(category == Category.DESERT ? Precipitation.NONE
		                                 : category == Category.ICY ? Precipitation.SNOW : Precipitation.RAIN, category, depth, scale, temperature, downfall, waterColor, waterFogColor, null);
	}

	public OLDTRAVERSEBIOME(SurfaceBuilder surfaceBuilder,
	                        SurfaceConfig surfaceConfig,
	                        Precipitation precipitation,
	                        Category category,
	                        float depth,
	                        float scale,
	                        float temperature,
	                        float downfall) {
		this(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_CONFIG, precipitation, category, depth, scale, temperature, downfall, 0x3F76E4, 0x050533, null);
	}

	public OLDTRAVERSEBIOME(Precipitation precipitation,
	                        Category category,
	                        float depth,
	                        float scale,
	                        float temperature,
	                        float downfall,
	                        String parent) {
		this(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_CONFIG, precipitation, category, depth, scale, temperature, downfall, 0x3F76E4, 0x050533, parent);
	}

	public OLDTRAVERSEBIOME(Precipitation precipitation,
	                        Category category,
	                        float depth,
	                        float scale,
	                        float temperature,
	                        float downfall) {
		this(precipitation, category, depth, scale, temperature, downfall, 0x3F76E4, 0x050533, null);
	}

	public OLDTRAVERSEBIOME(Category category,
	                        float depth,
	                        float scale,
	                        float temperature,
	                        float downfall) {
		this(category == Category.DESERT ? Precipitation.NONE
		                                 : category == Category.ICY ? Precipitation.SNOW : Precipitation.RAIN, category, depth, scale, temperature, downfall, 0x3F76E4, 0x050533, null);
	}

	public void addFeatures() {
		if (hasMineshafts) {this.addStructureFeature(Feature.MINESHAFT, new MineshaftFeatureConfig(0.004D, MineshaftFeature.Type.NORMAL));}
		if (hasStrongholds) {this.addStructureFeature(Feature.STRONGHOLD, new DefaultFeatureConfig());}
		if (hasCaves) {this.addCarver(GenerationSteps.CarverStep.AIR, configureCarver(Carver.CAVE, new ProbabilityConfig(caveProbability)));}
		if (hasRavines) {this.addCarver(GenerationSteps.CarverStep.AIR, configureCarver(Carver.RAVINE, new ProbabilityConfig(ravineProbability)));}
		if (hasDefaultFeatures) {this.addDefaultFeatures();}
		if (hasWaterLakes) {
			this.addFeature(GenerationSteps.FeatureStep.LOCAL_MODIFICATIONS, configureFeature(Feature.LAKE, new LakeFeatureConfig(Blocks.WATER.getDefaultState()), Decorator.WATER_LAKE, new LakeDecoratorConfig(waterLakeRarity)));
		}
		if (hasLavaLakes) {
			this.addFeature(GenerationSteps.FeatureStep.LOCAL_MODIFICATIONS, configureFeature(Feature.LAKE, new LakeFeatureConfig(Blocks.LAVA.getDefaultState()), Decorator.LAVA_LAKE, new LakeDecoratorConfig(lavaLakeRarity)));
		}
		if (hasDungeons) {
			this.addFeature(GenerationSteps.FeatureStep.UNDERGROUND_STRUCTURES, configureFeature(Feature.DUNGEON, FeatureConfig.DEFAULT, Decorator.DUNEGONS, new DungeonDecoratorConfig(8)));
		}
		if (hasSphereReplaces) {
			this.addFeature(GenerationSteps.FeatureStep.UNDERGROUND_ORES, configureFeature(Feature.DISK, new BlockClusterFeatureConfig(Blocks.SAND.getDefaultState(), 7, 2, Lists.newArrayList(Blocks.DIRT.getDefaultState(), Blocks.GRASS_BLOCK.getDefaultState())), Decorator.COUNT_TOP_SOLID, new CountDecoratorConfig(3)));
			this.addFeature(GenerationSteps.FeatureStep.UNDERGROUND_ORES, configureFeature(Feature.DISK, new BlockClusterFeatureConfig(Blocks.CLAY.getDefaultState(), 4, 1, Lists.newArrayList(Blocks.DIRT.getDefaultState(), Blocks.CLAY.getDefaultState())), Decorator.COUNT_TOP_SOLID, new CountDecoratorConfig(1)));
			this.addFeature(GenerationSteps.FeatureStep.UNDERGROUND_ORES, configureFeature(Feature.DISK, new BlockClusterFeatureConfig(Blocks.GRAVEL.getDefaultState(), 6, 2, Lists.newArrayList(Blocks.DIRT.getDefaultState(), Blocks.GRASS_BLOCK.getDefaultState())), Decorator.COUNT_TOP_SOLID, new CountDecoratorConfig(1)));
		}
		if (hasIceAndSnowFeature) {
			this.addFeature(GenerationSteps.FeatureStep.TOP_LAYER_MODIFICATION, configureFeature(Feature.FREEZE_TOP_LAYER, FeatureConfig.DEFAULT, Decorator.EMPTY, DecoratorConfig.DEFAULT));

		}
		addMinables();
		addEntitySpawns();
		addVegetation();
	}

	public void addMinables() {
		if (hadDefaultMinables) {
			addMinable(Blocks.DIRT, dirtSize, Decorator.COUNT_RANGE, new RangeDecoratorConfig(10, 0, 0, 256));
			addMinable(Blocks.GRAVEL, gravelSize, Decorator.COUNT_RANGE, new RangeDecoratorConfig(8, 0, 0, 256));
			addMinable(Blocks.GRANITE, graniteSize, Decorator.COUNT_RANGE, new RangeDecoratorConfig(10, 0, 0, 80));
			addMinable(Blocks.DIORITE, dioriteSize, Decorator.COUNT_RANGE, new RangeDecoratorConfig(10, 0, 0, 80));
			addMinable(Blocks.ANDESITE, andesiteSize, Decorator.COUNT_RANGE, new RangeDecoratorConfig(10, 0, 0, 80));
			addMinable(Blocks.COAL_ORE, coalSize, Decorator.COUNT_RANGE, new RangeDecoratorConfig(20, 0, 0, 128));
			addMinable(Blocks.IRON_ORE, ironSize, Decorator.COUNT_RANGE, new RangeDecoratorConfig(20, 0, 0, 64));
			addMinable(Blocks.GOLD_ORE, goldSize, Decorator.COUNT_RANGE, new RangeDecoratorConfig(2, 0, 0, 32));
			addMinable(Blocks.REDSTONE_ORE, redstoneSize, Decorator.COUNT_RANGE, new RangeDecoratorConfig(8, 0, 0, 16));
			addMinable(Blocks.DIAMOND_ORE, diamondSize, Decorator.COUNT_RANGE, new RangeDecoratorConfig(1, 0, 0, 16));
			addMinable(Blocks.LAPIS_ORE, lapisSize, Decorator.COUNT_DEPTH_AVERAGE, new CountDepthDecoratorConfig(1, 16, 16));
		}
	}

	public <DC extends DecoratorConfig> void addMinable(OreFeatureConfig.Target target, BlockState minable, int size, Decorator<DC> placement, DC placementConfig) {
		if (size > 0) {
			addFeature(GenerationSteps.FeatureStep.UNDERGROUND_ORES, configureFeature(Feature.ORE, new OreFeatureConfig(target, minable, size), placement, placementConfig));
		}
	}

	public <DC extends DecoratorConfig> void addMinable(BlockState minable, int size, Decorator<DC> placement, DC placementConfig) {
		addMinable(OreFeatureConfig.Target.NATURAL_STONE, minable, size, placement, placementConfig);
	}

	public <DC extends DecoratorConfig> void addMinable(Block minable, int size, Decorator<DC> placement, DC placementConfig) {
		addMinable(minable.getDefaultState(), size, placement, placementConfig);
	}

	public void addEntitySpawns() {
		if (hasDefaultEntitySpawns) {
			addEntitySpawnEntry(EntityCategory.CREATURE, sheepSpawnEntry);
			addEntitySpawnEntry(EntityCategory.CREATURE, pigSpawnEntry);
			addEntitySpawnEntry(EntityCategory.CREATURE, chickenSpawnEntry);
			addEntitySpawnEntry(EntityCategory.CREATURE, cowSpawnEntry);
			addEntitySpawnEntry(EntityCategory.WATER_CREATURE, squidSpawnEntry);
			addEntitySpawnEntry(EntityCategory.AMBIENT, batSpawnEntry);
			addEntitySpawnEntry(EntityCategory.MONSTER, spiderSpawnEntry);
			addEntitySpawnEntry(EntityCategory.MONSTER, zombieSpawnEntry);
			addEntitySpawnEntry(EntityCategory.MONSTER, zombieVillagerSpawnEntry);
			addEntitySpawnEntry(EntityCategory.MONSTER, skeletonSpawnEntry);
			addEntitySpawnEntry(EntityCategory.MONSTER, creeperSpawnEntry);
			addEntitySpawnEntry(EntityCategory.MONSTER, slimeSpawnEntry);
			addEntitySpawnEntry(EntityCategory.MONSTER, endermanSpawnEntry);
			addEntitySpawnEntry(EntityCategory.MONSTER, witchSpawnEntry);
		}
	}

	public void addEntitySpawnEntry(EntityCategory type, EntityType<? extends MobEntity> entry, int weight, int minGroupSize, int maxGroupSize) {
		this.addEntitySpawnEntry(type, new SpawnListEntry(entry, weight, minGroupSize, maxGroupSize));
	}

	public void addVegetation() {
		if (hasDefaultVegetation) {
			if (hasDoubleFlowers) {
				addVegetation(Feature.RANDOM_RANDOM_SELECTOR, new RandomRandomFeatureConfig(
						new Feature[] { Feature.DOUBLE_PLANT, Feature.DOUBLE_PLANT, Feature.DOUBLE_PLANT },
						new FeatureConfig[] {
							new DoublePlantFeatureConfig(Blocks.LILAC.getDefaultState()),
							new DoublePlantFeatureConfig(Blocks.ROSE_BUSH.getDefaultState()),
							new DoublePlantFeatureConfig(Blocks.PEONY.getDefaultState())
						}, 0),
					Decorator.COUNT_HEIGHTMAP_32, new CountDecoratorConfig(doubleFlowerFrequency));
			}
			if (flowerFrequency > 0)
				addFeature(GenerationSteps.FeatureStep.VEGETAL_DECORATION, configureFeature(flowerFeature, FeatureConfig.DEFAULT, Decorator.COUNT_HEIGHTMAP_32, new CountDecoratorConfig(flowerFrequency)));
			if (grassFrequency > 0)
				addVegetation(grassFeature, grassFeatureConfig, Decorator.COUNT_HEIGHTMAP_DOUBLE, new CountDecoratorConfig(grassFrequency));
			if (brownMushroomChance > 0)
				addVegetation(Feature.BUSH, new BushFeatureConfig(Blocks.BROWN_MUSHROOM.getDefaultState()), Decorator.CHANCE_HEIGHTMAP_DOUBLE, new ChanceDecoratorConfig(brownMushroomChance));
			if (redMushroomChance > 0)
				addVegetation(Feature.BUSH, new BushFeatureConfig(Blocks.RED_MUSHROOM.getDefaultState()), Decorator.CHANCE_HEIGHTMAP_DOUBLE, new ChanceDecoratorConfig(redMushroomChance));
			if (sugarCaneFrequency > 0)
				addVegetation(Feature.REED, FeatureConfig.DEFAULT, Decorator.COUNT_HEIGHTMAP_DOUBLE, new CountDecoratorConfig(sugarCaneFrequency));
			if (pumpkinChance > 0)
				addVegetation(Feature.PUMPKIN, FeatureConfig.DEFAULT, Decorator.CHANCE_HEIGHTMAP_DOUBLE, new ChanceDecoratorConfig(pumpkinChance));
			addVegetation(Feature.SPRING, new SpringFeatureConfig(Fluids.WATER.getDefaultState()), Decorator.COUNT_BIASED_RANGE, new RangeDecoratorConfig(50, 8, 8, 256));
			addVegetation(Feature.SPRING, new SpringFeatureConfig(Fluids.LAVA.getDefaultState()), Decorator.COUNT_VERY_BIASED_RANGE, new RangeDecoratorConfig(20, 8, 16, 256));
			if (melonFrequency > 0)
				addVegetation(Feature.MELON, FeatureConfig.DEFAULT, Decorator.COUNT_HEIGHTMAP_DOUBLE, new CountDecoratorConfig(melonFrequency));
			if (vineFrequency > 0)
				addVegetation(Feature.VINES, FeatureConfig.DEFAULT, Decorator.COUNT_HEIGHTMAP_64, new CountDecoratorConfig(vineFrequency));
		}
	}

	public <FC extends FeatureConfig, DC extends DecoratorConfig> void addVegetation(Feature<FC> feature, FC featureConfig, Decorator<DC> decorator, DC decoratorConfig) {
		addFeature(GenerationSteps.FeatureStep.VEGETAL_DECORATION, configureFeature(feature, featureConfig, decorator, decoratorConfig));
	}
}
