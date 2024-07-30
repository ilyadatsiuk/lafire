package com.datdevelop.lafire;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sun.jna.platform.win32.Sspi;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class LaFire implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("lafire");

	@Override
	public void onInitialize() {
		LOGGER.info("LaFire: init mod");

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CommandManager.literal("firen")
					.requires(source -> source.hasPermissionLevel(2))
					.then(CommandManager.argument("nick", EntityArgumentType.players())
							.then(CommandManager.argument("time", IntegerArgumentType.integer())
									.executes(LaFire::run)
					)
			)
		);
	})
	;}
	private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> _players = EntityArgumentType.getPlayers(context, "nick");
		int _time = IntegerArgumentType.getInteger(context, "time");
		for (ServerPlayerEntity _player : _players) {
			applyEffect(_player, _time);
		}

        return 0;
    }

	private static void applyEffect(ServerPlayerEntity _player, int _time){
		_player.setFireTicks(_time*20);
	}

}