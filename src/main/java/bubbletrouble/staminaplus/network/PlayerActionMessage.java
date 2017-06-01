package bubbletrouble.staminaplus.network;

import bubbletrouble.staminaplus.PlayerAction;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PlayerActionMessage implements IMessage
{
	private String playerAction;
	
	public PlayerActionMessage()
	{
		
	}
	
	public PlayerActionMessage(String playerAction)
	{
		this.playerAction = playerAction;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		playerAction = ByteBufUtils.readUTF8String(buf); 
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		  ByteBufUtils.writeUTF8String(buf, playerAction);
	}

	public static class Handler implements IMessageHandler<PlayerActionMessage, IMessage>
	{
        @Override
        public IMessage onMessage(PlayerActionMessage message, MessageContext ctx) {
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

	static void processMessage(PlayerActionMessage message, EntityPlayerMP player)
	{
		PlayerAction.setPlayerAction(player, message.playerAction);
	}
}
