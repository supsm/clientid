package supsm.clientid;

import com.mojang.authlib.GameProfile;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.HashMap;


public class main implements ModInitializer
{
	public static HashMap<GameProfile, String> players = new HashMap<GameProfile, String>();

	@Override
	public void onInitialize()
	{
		// register command
		CommandRegistrationCallback.EVENT.register((CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registry_access, CommandManager.RegistrationEnvironment environment) ->
		{
			dispatcher.register(CommandManager.literal("players").requires((ServerCommandSource source) -> source.hasPermissionLevel(3))
				.executes((CommandContext<ServerCommandSource> context) ->
				{
					ServerCommandSource source = context.getSource();
					if (players.isEmpty())
					{
						source.sendFeedback(() -> Text.literal("No players online"), false);
						return 1;
					}

					source.sendFeedback(() ->
					{
						String msg = "";
						for (HashMap.Entry<GameProfile, String> entry : players.entrySet())
						{
							msg += entry.getKey().getName() + " - " + entry.getValue() + "\n";
						}
						return Text.literal(msg.substring(0, msg.length() - 1));
					}, false);
					return 1;
				}));
		});
	}
}
