package io.github.prospector.traverse.loader.api;

import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

import java.util.Map;

public interface BiomePack {
	public String getNamespace();

	public default void addSurfaceBuilders(Map<String, SurfaceBuilder<?>> surfaceBuilder) {}

	public default void addCarvers(Map<String, Carver<?>> carvers) {}

	public default void addFeatures(Map<String, Feature<?>> features) {}

	public default void addDecorators(Map<String, Decorator<?>> decorators) {}
}
