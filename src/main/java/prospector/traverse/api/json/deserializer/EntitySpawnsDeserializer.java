package prospector.traverse.api.json.deserializer;

import com.google.gson.*;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import prospector.traverse.api.json.BiomePackLoader;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

public class EntitySpawnsDeserializer implements JsonDeserializer<EnumMap<EntityCategory, List<Biome.SpawnListEntry>>> {
	@Override
	public EnumMap<EntityCategory, List<Biome.SpawnListEntry>> deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) {
		EnumMap<EntityCategory, List<Biome.SpawnListEntry>> entitySpawns = new EnumMap<>(EntityCategory.class);
		JsonObject root = json.getAsJsonObject();
		for (EntityCategory category : EntityCategory.values()) {
			List<Biome.SpawnListEntry> entriesForCategory = new ArrayList<>();
			if (root.has(category.getName())) {
				JsonArray currentCategory = root.get(category.getName()).getAsJsonArray();
				for (int i = 0; i < currentCategory.size(); i++) {
					JsonObject entry = currentCategory.get(i).getAsJsonObject();
					//noinspection unchecked
					Identifier entityName = new Identifier(entry.get("type").getAsString());
					EntityType entity = Registry.ENTITY_TYPES.get(entityName);
					BiomePackLoader.throwCatchIf(entity != null, "Entity cannot be found " + entityName);
					BiomePackLoader.throwCatchIf(MobEntity.class.isAssignableFrom(entity.getEntityClass()), entityName + " is not a MobEntity");
					entriesForCategory.add(new Biome.SpawnListEntry(entity, entry.get("weight").getAsInt(), entry.get("min_group_size").getAsInt(), entry.get("max_group_size").getAsInt()));
				}
				entitySpawns.put(category, entriesForCategory);
			} else {
				entitySpawns.put(category, Collections.emptyList());
			}
		}
		return entitySpawns;
	}
}
