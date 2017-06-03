package bubbletrouble.staminaplus.network;

import bubbletrouble.staminaplus.EventHandler;
import bubbletrouble.staminaplus.capabilities.IStamina;
import bubbletrouble.staminaplus.capabilities.StaminaCapability;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PlayerJumpMessage implements IMessage
{
	public PlayerJumpMessage()
	{
		
	}
	@Override
	public void fromBytes(ByteBuf buf)
	{
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
	}

	public static class Handler implements IMessageHandler<PlayerJumpMessage, IMessage>
	{
        @Override
        public IMessage onMessage(PlayerJumpMessage message, MessageContext ctx) {
            IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.world; // or Minecraft.getMinecraft() on the client
			final EntityPlayerMP player = ctx.getServerHandler().player;

            mainThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                	processMessage(message, player);
                }
            });
            return null; 
        }
	}

	static void processMessage(PlayerJumpMessage message, EntityPlayerMP p)
	{
		IStamina stam = p.getCapability(StaminaCapability.Stamina, null);
		stam.setStaminaMultiplier(1F);
		stam.decreaseStamina(1);
		EventHandler.standingTicks = 0;
	}
}
