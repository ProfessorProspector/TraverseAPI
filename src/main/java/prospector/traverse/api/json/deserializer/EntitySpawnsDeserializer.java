package prospector.traverse.api.json.deserializer;

import com.google.common.base.Preconditions;
import com.google.gson.*;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

public class EntitySpawnsDeserializer implements JsonDeserializer<EnumMap<EntityCategory, List<Biome.SpawnListEntry>>> {
	@Override
	public EnumMap<EntityCategory, List<Biome.SpawnListEntry>> deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		EnumMap<EntityCategory, List<Biome.SpawnListEntry>> entitySpawns = new EnumMap<>(EntityCategory.class);
		JsonObject root = json.getAsJsonObject();
		for (EntityCategory category : EntityCategory.values()) {
			List<Biome.SpawnListEntry> entriesForCategory = new ArrayList<>();
			if (root.has(category.getName())) {
				JsonArray currentCategory = root.get(category.getName()).getAsJsonArray();
				for (int i = 0; i < currentCategory.size(); i++) {
					try {
						JsonObject entry = currentCategory.get(i).getAsJsonObject();
						//noinspection unchecked
						EntityType entityFromJson = Registry.ENTITY_FACTORIES.get(new Identifier(entry.get("type").getAsString()));
						Preconditions.checkArgument(entityFromJson != null, "");
						if (!MobEntity.class.isAssignableFrom(entityFromJson.getEntityClass())) {

						}
						entriesForCategory.add(new Biome.SpawnListEntry(entityFromJson, entry.get("weight").getAsInt(), entry.get("min_group_size").getAsInt(), entry.get("max_group_size").getAsInt()));
					} catch (Exception e) {
						throw new JsonParseException("SpawnListEntry " + i + " in category " + category.getName() + " could not be created...possibly EntityType is not a MobEntity", e);
					}
				}
				entitySpawns.put(category, entriesForCategory);
			} else {
				entitySpawns.put(category, Collections.emptyList());
			}
		}
		return entitySpawns;
	}
}
