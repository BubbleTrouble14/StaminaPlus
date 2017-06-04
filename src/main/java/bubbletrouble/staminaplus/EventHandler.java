package bubbletrouble.staminaplus;

import bubbletrouble.staminaplus.capabilities.IStamina;
import bubbletrouble.staminaplus.capabilities.StaminaCapability;
import bubbletrouble.staminaplus.network.PlayerActionMessage;
import bubbletrouble.staminaplus.network.StaminaValueMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class EventHandler  
{
    @SubscribeEvent   
    public void onPlayerJoin(PlayerLoggedInEvent evt)
    {
    	EntityPlayer p = evt.player;
		IStamina stam = p.getCapability(StaminaCapability.Stamina, null);
		stam.setStaminaMultiplier(1F);;
    }
    
    @SubscribeEvent   
    public void onPlayerSleep(PlayerWakeUpEvent evt)
    {
    	EntityPlayer p = evt.getEntityPlayer();
    	if (p.world.isRemote) return;
    	System.out.println(p.isPlayerFullyAsleep());
    ///	if(p)
   // 	{
			IStamina stam = p.getCapability(StaminaCapability.Stamina, null);
			stam.setStamina(stam.getMaxStatmina());
    //	}
    }
    
    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event)
    {
        EntityPlayer p = event.getEntityPlayer();
        IStamina stam = p.getCapability(StaminaCapability.Stamina, null);
        stam.setStamina(stam.getMaxStatmina());
    }
    
    public boolean isPlayerMoving(PlayerTickEvent evt, EntityPlayer p)
    {
    	if(p.moveForward != 0)
    	{
    		return true;
    	}    		
	    return false;   
    }
    
    @SubscribeEvent   
    public void onPlayerUpdate(PlayerTickEvent evt)
    {
    	EntityPlayer p = evt.player;
		if(p != null)
		{
	    	if(evt.phase == Phase.END) //&& p.world.isRemote)
	    	{
	    		if(!p.world.isRemote)
	    		{
	    			updateServerSide(p, evt);
	    		}
	    		else updateClientSide(p, evt);
	    	}    	 
		}
    }
    
    int clientTicks;
    //Client Side
    private void updateClientSide(EntityPlayer p, PlayerTickEvent evt) 
    {  	
    	clientTicks++;
		if(clientTicks >= 20)
		{
			//p.sendMessage(new TextComponentString(String.valueOf(ClientStamina.getStamina())));
			clientTicks = 0;
		}
    	if(isPlayerMoving(evt, p))
	    {
	    	if(p.isSprinting())
	    	{
	    		Main.modChannel.sendToServer(new PlayerActionMessage(ActionType.SPRINTING.name()));
	   		}
	   		else if(p.isSneaking())
	   		{
	   			Main.modChannel.sendToServer(new PlayerActionMessage(ActionType.SNEAKING.name()));
    		}
	   		else
	    	{
	   			Main.modChannel.sendToServer(new PlayerActionMessage(ActionType.WALKING.name()));
	    	} 
	    }
    	else
	    {
    		Main.modChannel.sendToServer(new PlayerActionMessage(ActionType.STANDING.name()));	
	    }    	
	    if(!p.onGround && Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed())
	    {    	
   			Main.modChannel.sendToServer(new PlayerActionMessage(ActionType.JUMPING.name()));
	    }  
	    if(ClientStamina.getStamina() <= 50F && ClientStamina.getStamina() >= 20F)
	    {
	    	p.capabilities.setPlayerWalkSpeed(0.2F);
	    }
	    else if(ClientStamina.getStamina() <= 20F)
	    {
	    	p.capabilities.setPlayerWalkSpeed(0.99F);
	    }
	    else if(ClientStamina.getStamina() <= 0F) p.setSprinting(false);
	    else 
	    {
	    	p.capabilities.setPlayerWalkSpeed(0.1F);
	    	p.setSprinting(true);
	    }		
	}	
    
    //Server Side
    private int ticks;
    public static int standingTicks;
    private float standingMultiplier = 0F;

    private void updateServerSide(EntityPlayer p, PlayerTickEvent evt) 
    {
    		String playerAction = PlayerAction.getPlayerAction(); 
    		IStamina stam = p.getCapability(StaminaCapability.Stamina, null);
    		
    		ticks++;
    		if(ticks >= 20)
    		{
    			ticks = 0;
    		//	p.sendMessage(new TextComponentString(String.valueOf(stam.getStamina())));
    		//TODO
    	//	System.out.println(stam.getStamina());
    		}
    	
    		if(playerAction != null && !p.capabilities.isCreativeMode)
    		{
        		ActionType type = ActionType.valueOf(playerAction);
    			Main.modChannel.sendTo(new StaminaValueMessage(stam.getStamina()), (EntityPlayerMP) p);
    			
			    	switch(type)
			    	{
			    		case WALKING : 
			    		{
			       			standingMultiplier = 0;
			    			stam.setStaminaMultiplier(1F);
			    			stam.increaseStamina(0.25F);
			    		}
			    		break;
			    		case SPRINTING : 
			    		{
			       			standingMultiplier = 0;
			    			stam.setStaminaMultiplier(1F);
			    			stam.decreaseStamina(0.3F); 
			    		}
			    		break;
			       		case SNEAKING : 
			       		{
			       			standingMultiplier = 0;
			    			stam.setStaminaMultiplier(1F);
			       			stam.increaseStamina(0.2F);  
			       		}
			    		break;
			       		case STANDING : 
			       		{
			       			standingTicks++;
			       			if(standingTicks >= 5)
			       			{
			       				standingTicks = 0;
			       				if(standingMultiplier<=21)standingMultiplier += 1F;
				    			stam.setStaminaMultiplier(standingMultiplier);
			       			}
			       			stam.increaseStamina(0.04F);   
			       		}
			    		break;
			       		case JUMPING : 
			       		{
			       			standingMultiplier = 0;
			       			stam.decreaseStamina(6F);  
			       		}
	    		}	
    		}
	}
    
    @SubscribeEvent
    public void onFOV(FOVUpdateEvent evt)
    {
    //	evt.setNewfov(100);
    }
    
    @SubscribeEvent
    public void livingUpdate(LivingJumpEvent evt)
    {
    	if(evt.getEntityLiving() instanceof EntityPlayer)
    	{
    		EntityPlayer p = (EntityPlayer) evt.getEntityLiving();
    		
		    if(ClientStamina.getStamina() <= 10)
		    {
		    	p.motionY = 0F;
		    }
		    else p.motionY = 0.5F;	
//		    if(ClientStamina.getStamina() <= 10F)
//		    {
//		    	p.capabilities.setPlayerWalkSpeed(0.01F);
//		    }
//		    else p.capabilities.setPlayerWalkSpeed(0.1F);
    	}
    }
    
    
    public boolean jumped;

	public boolean DelayInHalfaSecond(PlayerTickEvent evt)
    {
        ticks++;
        if(ticks >= 10)
        {
        	ticks = 0;
        	return true;
        }
		return false;     	
    }  	
}