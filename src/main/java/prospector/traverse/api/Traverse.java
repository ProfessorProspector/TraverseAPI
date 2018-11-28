package prospector.traverse.api;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.FabricLoader;
import net.minecraft.entity.EntityCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Traverse implements ModInitializer {
	public static final String MOD_ID = "traverse";

	public static final List<String> BIOME_PACKS = new ArrayList<>();
	public static final Map<String, Biome.Category> CATEGORY_NAME_MAP = Arrays.stream(Biome.Category.values()).collect(Collectors.toMap(Biome.Category::getName, (category) -> category));
	public static final Map<String, Biome.Precipitation> PRECIPITATION_NAME_MAP = Arrays.stream(Biome.Precipitation.values()).collect(Collectors.toMap(Biome.Precipitation::getName, (var0) -> var0));
	public static final Map<String, EntityCategory> ENTITY_CATEGORY_NAME_MAP = Arrays.stream(EntityCategory.values()).collect(Collectors.toMap(EntityCategory::getName, (var0) -> var0));

	public static void registerBiomePack(String modid) {
		BIOME_PACKS.add(modid);
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
	}
}
