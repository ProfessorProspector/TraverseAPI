package prospector.traverse.api;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.FabricLoader;
import net.minecraft.entity.EntityCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import prospector.traverse.api.json.BiomePackLoader;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Traverse implements ModInitializer {
	public static final String MOD_ID = "traverse";

	public static final Map<String, BiomePack> BIOME_PACKS = new HashMap<>();
	public static final Map<String, Biome.Category> CATEGORY_NAME_MAP = Arrays.stream(Biome.Category.values()).collect(Collectors.toMap(Biome.Category::getName, (category) -> category));
	public static final Map<String, Biome.Precipitation> PRECIPITATION_NAME_MAP = Arrays.stream(Biome.Precipitation.values()).collect(Collectors.toMap(Biome.Precipitation::getName, (var0) -> var0));
	public static final Map<String, EntityCategory> ENTITY_CATEGORY_NAME_MAP = Arrays.stream(EntityCategory.values()).collect(Collectors.toMap(EntityCategory::getName, (var0) -> var0));

	public static void registerBiomePack(String modid, BiomePack biomePack) {
		BIOME_PACKS.put(modid, biomePack);
	}

	public static void registerBiome(String modid, String name, Biome biome) {
		Registry.register(Registry.BIOMES, new Identifier(modid, name), biome);
		if (biome.hasParent()) {
			Biome.PARENT_BIOME_ID_MAP.add(biome);
		}
	}

	public static <F extends Feature> F registerFeature(String modid, String name, F feature) {
		Registry.register(Registry.FEATURES, new Identifier(modid, name), (Feature<?>) feature);
		return feature;
	}

	@Override
	public void onInitialize() {
		File biomepacksDir = new File(FabricLoader.INSTANCE.getGameDirectory(), "biomepacks");
		if (!biomepacksDir.exists()) {
			biomepacksDir.mkdir();
		}
		for (String id : Traverse.BIOME_PACKS.keySet()) {
			{
				Map<String, Feature<?>> map = new HashMap<>();
				Traverse.BIOME_PACKS.get(id).addFeatures(map);
				map.keySet().forEach(s -> Registry.register(Registry.FEATURES, new Identifier(id, s), map.get(s)));
			}
			{
				Map<String, Carver<?>> map = new HashMap<>();
				Traverse.BIOME_PACKS.get(id).addCarvers(map);
				map.keySet().forEach(s -> Registry.register(Registry.CARVERS, new Identifier(id, s), map.get(s)));
			}
			{
				Map<String, Decorator<?>> map = new HashMap<>();
				Traverse.BIOME_PACKS.get(id).addDecorators(map);
				map.keySet().forEach(s -> Registry.register(Registry.DECORATORS, new Identifier(id, s), map.get(s)));
			}
			{
				Map<String, SurfaceBuilder<?>> map = new HashMap<>();
				Traverse.BIOME_PACKS.get(id).addSurfaceBuilders(map);
				map.keySet().forEach(s -> Registry.register(Registry.SURFACE_BUILDERS, new Identifier(id, s), map.get(s)));
			}
		}
		new BiomePackLoader().loadBiomePacks();
	}
}
