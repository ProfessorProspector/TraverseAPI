package io.github.prospector.traverse.api.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.text.event.ClickEvent;
import net.minecraft.text.event.HoverEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class FindBiomeCommand {
	private static final SimpleCommandExceptionType FAILED_EXCEPTION = new SimpleCommandExceptionType(new TranslatableTextComponent("commands.findbiome.failed"));

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal("findbiome").requires((serverCommandSource_1) -> serverCommandSource_1.hasPermissionLevel(2));
		Registry.BIOME.stream().forEach(biome -> builder.then(CommandManager.literal(Registry.BIOME.getId(biome).toString()).executes(context -> execute(context.getSource(), biome))));
		dispatcher.register(builder);
	}

	private static int execute(ServerCommandSource source, Biome biome) {
		long start = System.currentTimeMillis(); new Thread(() -> {
			BlockPos biomePos;
			try {
				ServerPlayerEntity player = source.getPlayer();
				biomePos = spiralOutwardsLookingForBiome(player, source.getWorld(), biome, source.getPlayer().getPos().getX(), source.getPlayer().getPos().getZ(), 60_000);

				if (biomePos == null) {
					source.getMinecraftServer().execute(() -> player.sendChatMessage(new StringTextComponent(TextFormat.RED + "Error! Biome '" + Registry.BIOME.getId(biome) + "' could not be found after " + TextFormat.GRAY + 60_000 + "ms" + TextFormat.RED + "."), ChatMessageType.GAME_INFO));
					return;
				}
				source.getMinecraftServer().execute(() -> {
					BlockPos blockPos_1 = new BlockPos(source.getPosition());
					if (biomePos == null) {
						try {
							throw FAILED_EXCEPTION.create();
						} catch (CommandSyntaxException e) {
							e.printStackTrace();
						}
					} else {
						int distance = MathHelper.floor(getDistance(blockPos_1.getX(), blockPos_1.getZ(), biomePos.getX(), biomePos.getZ()));
						TextComponent textComponent_1 = TextFormatter.bracketed(new TranslatableTextComponent("chat.coordinates", biomePos.getX(), "~", biomePos.getZ())).modifyStyle((style_1) -> style_1.setColor(TextFormat.GREEN).setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s " + biomePos.getX() + " ~ " + biomePos.getZ())).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableTextComponent("chat.coordinates.tooltip"))));
						source.sendFeedback(new TranslatableTextComponent("commands.locate.success", biome, textComponent_1, distance), false);
					}
					player.sendChatMessage(new StringTextComponent(TextFormat.WHITE + "Found '" + Registry.BIOME.getId(biome) + "' Biome! " + TextFormat.GRAY + "(" + (System.currentTimeMillis() - start) + "ms)"), ChatMessageType.GAME_INFO);
				});
				source.getMinecraftServer().execute(() -> player.sendChatMessage(new StringTextComponent(TextFormat.RED + "Error! An unknown error occurred."), ChatMessageType.GAME_INFO));
			} catch (CommandSyntaxException e) {
				e.printStackTrace();
			}
		}, "Biome Finder - Traverse").start();

		return 1;
	}

	//Based off https://github.com/Glitchfiend/BiomesOPlenty/blob/4977b0100ca55f96de50337f46ed673512cf503a/src/main/java/biomesoplenty/common/util/biome/BiomeUtils.java
	public static BlockPos spiralOutwardsLookingForBiome(PlayerEntity player, World world, Biome biomeToFind, double startX, double startZ, int timeout) {
		double a = 16 / Math.sqrt(Math.PI);
		double b = 2 * Math.sqrt(Math.PI);
		double x;
		double z;
		double dist = 0;
		int n;
		long start = System.currentTimeMillis();
		BlockPos.PooledMutable pos = BlockPos.PooledMutable.get();
		int previous = 0;
		int i = 0;
		for (n = 0; dist < Integer.MAX_VALUE; ++n) {
			if ((System.currentTimeMillis() - start) > timeout) {
				return null;
			}
			double rootN = Math.sqrt(n);
			dist = a * rootN;
			x = startX + (dist * Math.sin(b * rootN));
			z = startZ + (dist * Math.cos(b * rootN));
			pos.set(x, 0, z);
			if (previous == 3)
				previous = 0;
			String s = (previous == 0 ? "." : previous == 1 ? ".." : "...");
			player.addChatMessage(new StringTextComponent("Scanning" + s), true);
			if (i == 1501) {
				previous++;
				i = 0;
			}
			i++;
			if (world.getBiome(pos).equals(biomeToFind)) {
				pos.close();
				player.addChatMessage(new StringTextComponent("Found Biome"), true);
				return new BlockPos((int) x, 0, (int) z);
			}
		}
		return null;
	}

	private static float getDistance(int int_1, int int_2, int int_3, int int_4) {
		int int_5 = int_3 - int_1;
		int int_6 = int_4 - int_2;
		return MathHelper.sqrt((float) (int_5 * int_5 + int_6 * int_6));
	}
}
