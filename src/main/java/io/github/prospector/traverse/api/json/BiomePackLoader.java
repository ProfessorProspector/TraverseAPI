package io.github.prospector.traverse.api.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.github.prospector.traverse.api.TraverseAPI;
import io.github.prospector.traverse.api.biome.TraverseBiome;
import io.github.prospector.traverse.api.json.object.BiomeInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BiomePackLoader {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Map<String, Boolean> BIOMEPACK_EXCEPTIONS = new HashMap<>();
	public static String currentPack = "";
	public static String currentBiome = "";

	public void loadBiomePacks() {
		for (String biomePack : TraverseAPI.BIOME_PACKS.keySet()) {
			loadBiomePack(biomePack);
		}
	}

	public void loadBiomePack(String biomepack) {
		currentPack = biomepack;
		List<String> biomes = getContentsOfType(biomepack, "biomes");
		BIOMEPACK_EXCEPTIONS.put(biomepack, false);
		Gson gson = new Gson();
		for (String biome : biomes) {
			currentBiome = biome;
			BiomeInfo biomeInfo;
			JsonParser parser = new JsonParser();
			JsonObject json;
			String jsonPath = "biomepacks/" + biomepack + "/biomes/" + biome + ".json";
			try (final InputStream is = getClass().getClassLoader().getResourceAsStream(jsonPath); final InputStreamReader isr = new InputStreamReader(is)) {
				json = parser.parse(isr).getAsJsonObject();
			} catch (final IOException e) {
				catchException("Failed to parse json file", e);
				continue;
			}
			try {
				biomeInfo = gson.fromJson(json, BiomeInfo.class);
			} catch (BiomeLoadingException e) {
				catchException("Failed to load biome info", e);
				continue;
			}
			TraverseAPI.registerBiome(biomepack, biome, new TraverseBiome(biomeInfo));
			currentBiome = null;
		}
		if (BIOMEPACK_EXCEPTIONS.get(biomepack)) {
			throw new BiomeLoadingException("Biomepack '" + biomepack + "' failed to load");
		}
		currentPack = null;
	}

	public List<String> getContentsOfType(String biomepack, String type) {
		JsonParser parser = new JsonParser();
		JsonObject json = null;
		try (InputStream is = getClass().getClassLoader().getResourceAsStream("biomepacks/" + biomepack + "/" + type + ".json"); InputStreamReader isr = new InputStreamReader(is)) {
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

	public static void catchException(String message, Exception e) {
		try {
			BIOMEPACK_EXCEPTIONS.put(currentPack, true);
			throw new BiomeLoadingException("[" + currentPack + ":" + currentBiome + "] " + message, e);
		} catch (BiomeLoadingException ble) {
			ble.printStackTrace();
		}
	}

	public static void throwCatchIf(boolean expression, String message) {
		if (!expression) {
			try {
				BIOMEPACK_EXCEPTIONS.put(currentPack, true);
				throw new BiomeLoadingException("[" + currentPack + ":" + currentBiome + "] " + message);
			} catch (BiomeLoadingException e) {
				e.printStackTrace();
			}
		}
	}

	public static Integer parseHexString(String string) {
		String originalString = string;
		Integer integer = null;
		try {
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
			integer = Integer.parseInt(string, 16);
		} catch (NumberFormatException e) {
			catchException("Cannot parse as hex: " + originalString, e);
		}
		return integer;
	}
}
