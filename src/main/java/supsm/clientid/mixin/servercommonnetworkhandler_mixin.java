package supsm.clientid.mixin;

import static supsm.clientid.main.players;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.DisconnectionInfo;
import net.minecraft.network.packet.BrandCustomPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ServerCommonNetworkHandler.class)
public abstract class servercommonnetworkhandler_mixin
{
	@Shadow
	protected abstract GameProfile getProfile();

	@Inject(method = "onCustomPayload(Lnet/minecraft/network/packet/c2s/common/CustomPayloadC2SPacket;)V", at = @At("RETURN"))
	private void on_custom_payload(CustomPayloadC2SPacket p, CallbackInfo callback)
	{
		if (p.payload() instanceof BrandCustomPayload payload)
		{
			GameProfile profile = getProfile();
			if (!players.containsKey(profile))
			{
				String clientid = payload.brand();
				System.out.println(profile.getName() + " connected with " + clientid);
				players.put(profile, clientid);
			}
		}
	}

	@Inject(method = "onDisconnected(Lnet/minecraft/network/DisconnectionInfo;)V", at = @At("RETURN"))
	private void on_disconnect(DisconnectionInfo info, CallbackInfo callback)
	{
		players.remove(getProfile());
	}
}
