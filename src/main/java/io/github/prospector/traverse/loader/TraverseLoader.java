package io.github.prospector.traverse.loader;

import io.github.prospector.traverse.api.command.FindBiomeCommand;
import io.github.prospector.traverse.loader.api.BiomePack;
import io.github.prospector.traverse.loader.json.BiomePackLoader;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EntityCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class TraverseLoader implements ModInitializer {

	public static final String MOD_ID = "traverse";

	public static final Set<BiomePack> BIOME_PACKS = new LinkedHashSet<>();
	public static final Map<String, Biome.Category> CATEGORY_NAME_MAP = Arrays.stream(Biome.Category.values()).collect(Collectors.toMap(Biome.Category::getName, (category) -> category));
	public static final Map<String, Biome.Precipitation> PRECIPITATION_NAME_MAP = Arrays.stream(Biome.Precipitation.values()).collect(Collectors.toMap(Biome.Precipitation::getName, (var0) -> var0));
	public static final Map<String, EntityCategory> ENTITY_CATEGORY_NAME_MAP = Arrays.stream(EntityCategory.values()).collect(Collectors.toMap(EntityCategory::getName, (var0) -> var0));

	public static final List<Biome> FOR_WORLD = new ArrayList<>();

	public static void registerBiome(String modid, String name, Biome biome) {
		Registry.register(Registry.BIOME, new Identifier(modid, name), biome);
		if (biome.hasParent()) {
			Biome.PARENT_BIOME_ID_MAP.add(biome);
		}
	}

	public static <F extends Feature> F registerFeature(String modid, String name, F feature) {
		Registry.register(Registry.FEATURE, new Identifier(modid, name), (Feature<?>) feature);
		return feature;
	}

	@Override
	public void onInitialize() {
		File biomepacksDir = new File(net.fabricmc.loader.FabricLoader.INSTANCE.getGameDirectory(), "biomepacks");
		if (!biomepacksDir.exists()) {
			biomepacksDir.mkdir();
		}
		BIOME_PACKS.addAll(FabricLoader.getInstance().getEntrypoints("biomepack", BiomePack.class));
		for (BiomePack biomePack : BIOME_PACKS) {
			String namespace = biomePack.getNamespace();
			{
				Map<String, Feature<?>> features = new HashMap<>();
				biomePack.addFeatures(features);
				features.keySet().forEach(name -> Registry.register(Registry.FEATURE, new Identifier(namespace, name), features.get(name)));
			}
			{
				Map<String, Carver<?>> carvers = new HashMap<>();
				biomePack.addCarvers(carvers);
				carvers.keySet().forEach(s -> Registry.register(Registry.CARVER, new Identifier(namespace, s), carvers.get(s)));
			}
			{
				Map<String, Decorator<?>> decorators = new HashMap<>();
				biomePack.addDecorators(decorators);
				decorators.keySet().forEach(s -> Registry.register(Registry.DECORATOR, new Identifier(namespace, s), decorators.get(s)));
			}
			{
				Map<String, SurfaceBuilder<?>> surfaceBuilders = new HashMap<>();
				biomePack.addSurfaceBuilders(surfaceBuilders);
				surfaceBuilders.keySet().forEach(s -> Registry.register(Registry.SURFACE_BUILDER, new Identifier(namespace, s), surfaceBuilders.get(s)));
			}
		}
		new BiomePackLoader().loadBiomePacks();
		CommandRegistry.INSTANCE.register(false, FindBiomeCommand::register);
	}
}
