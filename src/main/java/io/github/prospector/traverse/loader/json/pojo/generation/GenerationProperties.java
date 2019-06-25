package io.github.prospector.traverse.loader.json.pojo.generation;

public class GenerationProperties {

    ClimateGeneration[] climate;
    VariantGeneration[] variant;
    SubGeneration[] hills;
    SubGeneration[] shore;
    SubGeneration[] edge;
    String river = "null";

    public ClimateGeneration[] getClimateGeneration() {
        return climate;
    }

    public VariantGeneration[] getVariantGeneration() {
        return variant;
    }

    public SubGeneration[] getHillsGeneration() {
        return hills;
    }

    public SubGeneration[] getShoreGeneration() {
        return shore;
    }

    public SubGeneration[] getEdgeGeneration() {
        return edge;
    }

    public String getRiverBiome() {
        return river;
    }

}
