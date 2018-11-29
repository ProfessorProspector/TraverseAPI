package prospector.traverse.api.json.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;
import net.minecraft.world.gen.config.surfacebuilder.TernarySurfaceConfig;

import java.lang.reflect.Type;

public class SurfaceConfigDeserializer implements JsonDeserializer<TernarySurfaceConfig> {
	@Override
	public TernarySurfaceConfig deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) {
		return TernarySurfaceConfig.deserialize(new Dynamic<>(JsonOps.INSTANCE, json));
	}
}
