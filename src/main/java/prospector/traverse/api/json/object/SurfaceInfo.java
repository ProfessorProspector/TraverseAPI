package prospector.traverse.api.json.object;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.config.surfacebuilder.TernarySurfaceConfig;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import prospector.traverse.api.json.deserializer.SurfaceConfigDeserializer;

public class SurfaceInfo {
	@SerializedName("surface_builder")
	String surfaceBuilder;
	@JsonAdapter(SurfaceConfigDeserializer.class)
	TernarySurfaceConfig config;

	public SurfaceBuilder getSurfaceBuilder() {
		if (surfaceBuilder == null) {
			return SurfaceBuilder.DEFAULT;
		}
		return Registry.SURFACE_BUILDERS.get(new Identifier(this.surfaceBuilder));
	}

	public TernarySurfaceConfig getConfig() {
		if (config == null) {
			return SurfaceBuilder.GRASS_CONFIG;
		}
		return config;
	}
}
