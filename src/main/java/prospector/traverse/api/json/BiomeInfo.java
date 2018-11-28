package prospector.traverse.api.json;

import com.google.gson.annotations.SerializedName;
import net.minecraft.world.biome.Biome;

import java.security.InvalidParameterException;

public class BiomeInfo {
	String category;
	String parent;
	Float depth;
	Float scale;
	Float temperature;
	Float downfall;
	String precipitation;
	@SerializedName("grass_color")
	String grassColor;
	@SerializedName("foliage_color")
	String foliageColor;
	@SerializedName("water_color")
	String waterColor;
	@SerializedName("water_fog_color")
	String waterFogColor;
	SurfaceInfo surface;

	public Biome.Category getCategory() {
		return BiomePackLoader.CATEGORY_NAME_MAP.get(category);
	}

	public String getParent() {
		return parent;
	}

	public Float getDepth() {
		return depth;
	}

	public Float getScale() {
		return scale;
	}

	public Float getTemperature() {
		return temperature;
	}

	public Float getDownfall() {
		return downfall;
	}

	public Biome.Precipitation getPrecipitation() {
		return BiomePackLoader.PRECIPITATION_NAME_MAP.get(precipitation);
	}

	public Integer getGrassColor() {
		return parseHex(grassColor);
	}

	public Integer getFoliageColor() {
		return parseHex(foliageColor);
	}

	public int getWaterColor() {
		if (waterColor == null || waterColor.isEmpty()) {
			return 0x3F76E4;
		}
		return parseHex(waterColor);
	}

	public int getWaterFogColor() {
		if (waterColor == null || waterColor.isEmpty()) {
			return 0x050533;
		}
		return parseHex(waterFogColor);
	}

	public static Integer parseHex(String string) {
		if (string == null || string.isEmpty()) {
			return null;
		}
		if (string.contains("#")) {
			String[] splitString = string.split("#");
			if (splitString.length != 2) {
				throw new InvalidParameterException(string + " contains multiple # and is an invalid hex string");
			}
			string = splitString[1];
		}
		return Integer.parseInt(string, 16);
	}

	public SurfaceInfo getSurface() {
		return surface;
	}
}
