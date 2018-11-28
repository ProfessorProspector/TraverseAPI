package prospector.traverse.api.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import prospector.traverse.api.Traverse;
import prospector.traverse.api.biome.TraverseBiome;
import prospector.traverse.api.json.object.BiomeInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BiomePackLoader {
	public static final Logger LOGGER = LogManager.getLogger();

	public void loadBiomePacks() {
		for (String biomePack : Traverse.BIOME_PACKS) {
			loadBiomePack(biomePack);
		}
	}

	public void loadBiomePack(String id) {
		List<String> biomes = getContentsOfType(id, "biomes");
		Gson gson = new Gson();
		for (String biome : biomes) {
			BiomeInfo biomeInfo;
			JsonParser parser = new JsonParser();
			JsonObject json;
			String jsonPath = "biomepacks/" + id + "/biomes/" + biome + ".json";
			InputStreamReader isr;
			InputStream is = getClass().getClassLoader().getResourceAsStream(jsonPath);
			isr = new InputStreamReader(is);
			json = parser.parse(isr).getAsJsonObject();
			biomeInfo = gson.fromJson(json, BiomeInfo.class);
			Traverse.registerBiome(id, biome, new TraverseBiome(biomeInfo));
		}
	}

	public List<String> getContentsOfType(String id, String type) {
		JsonParser parser = new JsonParser();
		JsonObject json = null;
		try (InputStream is = getClass().getClassLoader().getResourceAsStream("biomepacks/" + id + "/" + type + ".json"); InputStreamReader isr = new InputStreamReader(is)) {
			json = parser.parse(isr).getAsJsonObject();
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
		}
		if (json != null) {
			List<String> list = new ArrayList<>();
			JsonArray array = json.getAsJsonArray(type);
			for (int i = 0; i < array.size(); i++) {
				list.add(array.get(i).getAsString());
			}
			return list;
		}
		return null;
	}
}
