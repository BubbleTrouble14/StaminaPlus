package bubbletrouble.staminaplus.network;

import bubbletrouble.staminaplus.ClientStamina;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StaminaValueMessage implements IMessage
{
	private float value;
	
	public StaminaValueMessage()
	{
		
	}
	
	public StaminaValueMessage(float value)
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

	public static class Handler implements IMessageHandler<StaminaValueMessage, IMessage>
	{
        @Override
        public IMessage onMessage(StaminaValueMessage message, MessageContext ctx) {
            IThreadListener mainThread = Minecraft.getMinecraft(); // or Minecraft.getMinecraft() on the client
			final EntityPlayerSP player = Minecraft.getMinecraft().player;

            mainThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                	processMessage(message, player);
                }
            });
            return null; 
        }
	}

	static void processMessage(StaminaValueMessage message, EntityPlayerSP p)
	{
	//	p.sendMessage(new TextComponentString(String.valueOf(message.value)));
	//	setClientStamina(message.value);
		ClientStamina.setStamina(message.value);
	}
}
