package io.github.prospector.traverse.loader.json.pojo.generation;

import io.github.prospector.traverse.loader.json.BiomeLoadingException;
import net.fabricmc.fabric.api.biomes.v1.OverworldClimate;

import java.util.Arrays;

public class ClimateGeneration {

    String climate;
    double weight = 1;

    public OverworldClimate getClimate() {
        try {
            return OverworldClimate.valueOf(this.climate.toUpperCase());
        }
        catch (IllegalArgumentException e) {
            throw new BiomeLoadingException(this.climate + " is an invalid climate type. Valid types are: " + Arrays.toString(OverworldClimate.values()), e);
        }
    }

    public double getWeight() {
        return weight;
    }

}
