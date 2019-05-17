package io.github.prospector.traverse.loader.json;

import com.google.gson.*;
import io.github.prospector.traverse.loader.TraverseLoader;
import io.github.prospector.traverse.loader.api.BiomePack;
import io.github.prospector.traverse.loader.biome.TraverseBiome;
import io.github.prospector.traverse.loader.json.pojo.BiomeProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.*;

public class BiomePackLoader {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Set<String> BIOMEPACK_EXCEPTIONS = new LinkedHashSet<>();
	public static String currentPack = "";
	public static String currentBiome = "";
	public static boolean somethingExcepted = false;

	public void loadBiomePacks() {
		for (BiomePack biomePack : TraverseLoader.BIOME_PACKS) {
			loadBiomePack(biomePack);
		}
	}

	public void loadBiomePack(BiomePack biomepack) {
		String namespace = biomepack.getNamespace();
		currentPack = namespace;
		List<String> biomes = getContentsOfType(namespace, "biomes");
		Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
		for (String biome : biomes) {
			currentBiome = biome;
			BiomeProperties biomeInfo;
			JsonParser parser = new JsonParser();
			JsonObject json;
			String jsonPath = "biomepacks/" + namespace + "/biomes/" + biome + ".json";
			try (final InputStream is = getClass().getClassLoader().getResourceAsStream(jsonPath); final InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(is))) {
				json = parser.parse(isr).getAsJsonObject();
			} catch (final IOException e) {
				printException("Failed to parse json file for " + namespace + ":" + biome, e);
				continue;
			}
			try {
				biomeInfo = gson.fromJson(json, BiomeProperties.class);
			} catch (Exception e) {
				printException("Failed to load biome info for " + namespace + ":" + biome, e);
				continue;
			}
			attempt(() -> TraverseLoader.registerBiome(namespace, biome, new TraverseBiome(namespace + ":" + biome, biomeInfo)), "Failed to register biome: " + namespace + ":" + biome);
			currentBiome = null;
		}
		if (BIOMEPACK_EXCEPTIONS.contains(namespace)) {
			new BiomeLoadingException("Biomepack '" + namespace + "' failed to load").printStackTrace();
		}
		if (somethingExcepted) {
			throw new BiomeLoadingException("Biome packs failed to load. Exceptions appear to be from " + BIOMEPACK_EXCEPTIONS);
		}
		currentPack = null;
	}

	public List<String> getContentsOfType(String namespace, String type) {
		JsonParser parser = new JsonParser();
		JsonObject json = null;
		try (InputStream is = getClass().getClassLoader().getResourceAsStream("biomepacks/" + namespace + "/" + type + ".json"); InputStreamReader isr = new InputStreamReader(is)) {
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

	public static void attempt(Runnable action, String errorMessage) {
		try {
			action.run();
		} catch (Exception e) {
			printException(errorMessage, e);
		}
	}

	public static void printException(String message, Exception e) {
		BIOMEPACK_EXCEPTIONS.add(currentPack);
		somethingExcepted = true;
		new BiomeLoadingException("[" + currentPack + ":" + currentBiome + "] " + message, e).printStackTrace();
	}

	public static void checkExpression(boolean expression, String message) {
		if (!expression) {
			BIOMEPACK_EXCEPTIONS.add(currentPack);
			somethingExcepted = true;
			new BiomeLoadingException("[" + currentPack + ":" + currentBiome + "] " + message).printStackTrace();
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
			printException("Cannot parse as hex: " + originalString, e);
		}
		return integer;
	}
}
