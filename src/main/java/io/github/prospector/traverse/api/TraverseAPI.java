package io.github.prospector.traverse.api;

import io.github.prospector.traverse.api.command.FindBiomeCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.entity.EntityCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;

import java.util.*;
import java.util.stream.Collectors;

public class TraverseAPI implements ModInitializer {
	public static final String MOD_ID = "traverse";

	public static final Map<String, BiomePack> BIOME_PACKS = new HashMap<>();
	public static final Map<String, Biome.Category> CATEGORY_NAME_MAP = Arrays.stream(Biome.Category.values()).collect(Collectors.toMap(Biome.Category::getName, (category) -> category));
	public static final Map<String, Biome.Precipitation> PRECIPITATION_NAME_MAP = Arrays.stream(Biome.Precipitation.values()).collect(Collectors.toMap(Biome.Precipitation::getName, (var0) -> var0));
	public static final Map<String, EntityCategory> ENTITY_CATEGORY_NAME_MAP = Arrays.stream(EntityCategory.values()).collect(Collectors.toMap(EntityCategory::getName, (var0) -> var0));

	public static final List<Biome> FOR_WORLD = new ArrayList<>();

	public static void registerBiomePack(String modid, BiomePack biomePack) {
		BIOME_PACKS.put(modid, biomePack);
	}

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
	}
}
