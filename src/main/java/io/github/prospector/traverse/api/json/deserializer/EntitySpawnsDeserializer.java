package io.github.prospector.traverse.api.json.deserializer;

import com.google.gson.*;
import io.github.prospector.traverse.api.json.BiomePackLoader;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

public class EntitySpawnsDeserializer implements JsonDeserializer<EnumMap<EntityCategory, List<Biome.SpawnEntry>>> {
	@Override
	public EnumMap<EntityCategory, List<Biome.SpawnEntry>> deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) {
		EnumMap<EntityCategory, List<Biome.SpawnEntry>> entitySpawns = new EnumMap<>(EntityCategory.class);
		JsonObject root = json.getAsJsonObject();
		for (EntityCategory category : EntityCategory.values()) {
			List<Biome.SpawnEntry> entriesForCategory = new ArrayList<>();
			if (root.has(category.getName())) {
				JsonArray currentCategory = root.get(category.getName()).getAsJsonArray();
				for (int i = 0; i < currentCategory.size(); i++) {
					JsonObject entry = currentCategory.get(i).getAsJsonObject();
					//noinspection unchecked
					Identifier entityName = new Identifier(entry.get("type").getAsString());
					EntityType entity = Registry.ENTITY_TYPE.get(entityName);
					BiomePackLoader.throwCatchIf(entity != null, "Entity cannot be found " + entityName);
					// Welp this is impossible now
					// BiomePackLoader.throwCatchIf(MobEntity.class.isAssignableFrom(entity.getEntityClass()), entityName + " is not a MobEntity");
					entriesForCategory.add(new Biome.SpawnEntry(entity, entry.get("weight").getAsInt(), entry.get("min_group_size").getAsInt(), entry.get("max_group_size").getAsInt()));
				}
				entitySpawns.put(category, entriesForCategory);
			} else {
				entitySpawns.put(category, Collections.emptyList());
			}
		}
		return entitySpawns;
	}
}
