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
	private float value;
	
	public PlayerJumpMessage()
	{
		
	}
	
	public PlayerJumpMessage(float value)
	{
		this.value = value;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		value = buf.readFloat(); 
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeFloat(value);
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
	//	stam.setStaminaMultiplier(1F);
		System.out.println(message.value);
		stam.decreaseStamina(message.value);
	//	EventHandler.standingMultiplier = 0;
	}
}
