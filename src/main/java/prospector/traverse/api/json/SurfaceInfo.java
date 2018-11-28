package prospector.traverse.api.json;

import com.google.gson.annotations.SerializedName;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

public class SurfaceInfo {
	@SerializedName("surface_builder")
	String surfaceBuilder;

	public SurfaceBuilder getSurfaceBuilder() {
		return Registry.SURFACE_BUILDERS.get(new Identifier(surfaceBuilder));
	}
}
