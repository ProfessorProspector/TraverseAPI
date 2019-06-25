package io.github.prospector.traverse.loader.json.pojo;

import com.google.gson.annotations.JsonAdapter;
import io.github.prospector.traverse.loader.TraverseLoader;
import io.github.prospector.traverse.loader.json.BiomePackLoader;
import io.github.prospector.traverse.loader.json.deserializer.EntitySpawnsDeserializer;
import io.github.prospector.traverse.loader.json.deserializer.FeaturesDeserializer;
import io.github.prospector.traverse.loader.json.pojo.generation.GenerationProperties;
import net.minecraft.entity.EntityCategory;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.EnumMap;
import java.util.List;

public class BiomeProperties {

    GenerationProperties generation;
    String category;
    String parent;
    Float depth;
    Float scale;
    Float temperature;
    Float downfall;
    String precipitation;
    String grassColor;
    String foliageColor;
    String waterColor = "#3F76E4";
    String waterFogColor = "#050533";
    SurfaceProperties surface;
    InheritedFeatures[] inheritedFeatures;
    DefaultFeatures defaultFeatures;
    @JsonAdapter(FeaturesDeserializer.class)
    EnumMap<GenerationStep.Feature, List<ConfiguredFeature>> features;
    @JsonAdapter(EntitySpawnsDeserializer.class)
    EnumMap<EntityCategory, List<Biome.SpawnEntry>> entitySpawns;

    public Biome.Category getCategory() {
        return TraverseLoader.CATEGORY_NAME_MAP.get(category);
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
        if (precipitation == null) {
            Biome.Category category = getCategory();
            if (category == Biome.Category.DESERT || category == Biome.Category.NETHER || category == Biome.Category.THEEND) {
                return Biome.Precipitation.NONE;
            }
            else if (category == Biome.Category.ICY) {
                return Biome.Precipitation.SNOW;
            }
            else {
                return Biome.Precipitation.RAIN;
            }
        }
        return TraverseLoader.PRECIPITATION_NAME_MAP.get(precipitation);
    }

    public Integer getGrassColor() {
        return BiomePackLoader.parseHexString(grassColor);
    }

    public Integer getFoliageColor() {
        return BiomePackLoader.parseHexString(foliageColor);
    }

    public int getWaterColor() {
        return BiomePackLoader.parseHexString(waterColor);
    }

    public int getWaterFogColor() {
        return BiomePackLoader.parseHexString(waterFogColor);
    }

    public SurfaceProperties getSurface() {
        if (surface == null) {
            return new SurfaceProperties();
        }
        return surface;
    }

    public InheritedFeatures[] getInheritedFeatures() {
        return inheritedFeatures;
    }

    public DefaultFeatures getDefaultFeatures() {
        return defaultFeatures;
    }

    public EnumMap<GenerationStep.Feature, List<ConfiguredFeature>> getFeatures() {
        return features;
    }

    public EnumMap<EntityCategory, List<Biome.SpawnEntry>> getEntitySpawns() {
        return entitySpawns;
    }

    public GenerationProperties getGenerationProperties() {
        return generation;
    }

}
