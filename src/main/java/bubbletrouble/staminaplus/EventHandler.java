package bubbletrouble.staminaplus;

import bubbletrouble.staminaplus.capabilities.IStamina;
import bubbletrouble.staminaplus.capabilities.StaminaCapability;
import bubbletrouble.staminaplus.config.ModConfig;
import bubbletrouble.staminaplus.network.PlayerActionMessage;
import bubbletrouble.staminaplus.network.PlayerJumpMessage;
import bubbletrouble.staminaplus.network.StaminaValueMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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
    
    private static boolean tryagain;
    boolean test;
    @SubscribeEvent
    public void onPlayerKeyPressed(net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent evt)
    {
    	if(Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed())test = true;
		if(!Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown() && test)
		{
 			Main.modChannel.sendToServer(new PlayerJumpMessage(jumping));
 			//System.out.println("fire");
			test = false;
			tryagain= true;
		}
		else tryagain = false;
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
    
	int count = 0;
    int clientTicks;
    int jumpTicks= 0;
    boolean pressed = false;
    boolean testagain = false;
    //Client Side
    private void updateClientSide(EntityPlayer p, PlayerTickEvent evt) 
    {  	
    	clientTicks++;
		if(clientTicks >= 20)
		{
		//	heldKey= true;
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
    		if(!Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown() && p.onGround)
    		{
    			Main.modChannel.sendToServer(new PlayerActionMessage(ActionType.STANDING.name()));	
    		}
    		else Main.modChannel.sendToServer(new PlayerActionMessage(ActionType.JUMPED.name()));	
	    }    	
	 //   if(!p.onGround && Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed())
       	if(Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown())
     	{
     		if(p.onGround)
     		{
     		//	p.set
     			Main.modChannel.sendToServer(new PlayerActionMessage(ActionType.JUMPING.name()));
    		}	
     	}
      // 	if(p.isAirBorne) testagain = true;
       //	if(tryagain)
      // 	{
       //		Main.modChannel.sendToServer(new PlayerActionMessage(ActionType.JUMPED.name()));
      //	}
		
	    if(ClientStamina.getStamina() >= 50)
	    {
	     	p.removeActivePotionEffect(MobEffects.SLOWNESS);	     	
		  	p.removePotionEffect(MobEffects.SLOWNESS);
	     	p.removeActivePotionEffect(MobEffects.JUMP_BOOST);	     	
		  	p.removePotionEffect(MobEffects.JUMP_BOOST);
	    }
	}	
    
    //Server Side
    private int ticks;
    public static int standingTicks;
    public static float standingMultiplier = 0F;
    
    //Values
    private float walking = ModConfig.walking /10;
    private float sneaking = ModConfig.sneaking /10;
    private float sprinting = ModConfig.sprinting/10;
    private float standing = ModConfig.standing/10;
    private float jumping = ModConfig.jumping;
    private double increaser_per_tick = ModConfig.increase_multiplier*20;


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
    		System.out.println(stam.getStamina());
    		}
    	//	System.out.println(stam.getStaminaMultiplier());
    	
    		if(playerAction != null && !p.capabilities.isCreativeMode)
    		{
        		ActionType type = ActionType.valueOf(playerAction);
    			Main.modChannel.sendTo(new StaminaValueMessage(stam.getStamina()), (EntityPlayerMP) p);
    			
    	//		System.out.println(type);
			    	switch(type)
			    	{
			    		case WALKING : 
			    		{
			       			standingMultiplier = 0;
			    			stam.setStaminaMultiplier(1F);
			    			stam.increaseStamina(walking);
			    		}
			    		break;
			    		case SPRINTING : 
			    		{
			       			standingMultiplier = 0;
			    			stam.setStaminaMultiplier(1F);
			    			stam.decreaseStamina(sprinting); 
			    		}
			    		break;
			       		case SNEAKING : 
			       		{
			       			standingMultiplier = 0;
			    			stam.setStaminaMultiplier(1F);
			       			stam.increaseStamina(sneaking);  
			       		}
			    		break;
			       		case STANDING : 
			       		{
			       			standingTicks++;
			       			if(standingTicks >= increaser_per_tick)
			       			{
			       				standingTicks = 0;
			       				if(standingMultiplier<=11)standingMultiplier += 1F;
				    			stam.setStaminaMultiplier(standingMultiplier);
			       			}
			       			stam.increaseStamina(standing);   
			       		}
			    		break;
			       		case JUMPING : 
			       		{
			    			stam.setStaminaMultiplier(1F);
			       			standingMultiplier = 0;
			       			stam.decreaseStamina(jumping/2);  
			       		}
			    		break;
			       		case JUMPED : 
			       		{
			    			stam.setStaminaMultiplier(1F);
			       			standingMultiplier = 0;
			       			//stam.decreaseStamina(jumping/6);  
			       		}
			    		break;
	    		}	
    		}
    		if(!p.capabilities.isCreativeMode)
    		{
	    		if(stam.getStamina() <= 50F && stam.getStamina() >= 20F)
	    		{
	    			p.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 0, 2));
	    			p.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 1, -1));
	    		}
	    		else if(stam.getStamina() <= 20F)
	    		{
	    		    p.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 0, 4));
	    		    p.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 1, -2));
	    		}	
    		}
	}
    
    @SubscribeEvent
    public void onFOV(FOVUpdateEvent evt)
    {
    	EntityPlayer player = evt.getEntity();
    	//float modifier = PenaltyManager.getHealthAndExhaustionModifier(player);

            float f = 1.0F;

            if (player.capabilities.isFlying)
            {
                f *= 1.1F;
            }

            IAttributeInstance iattributeinstance = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
     //       double oldAttributeValue = iattributeinstance.getAttributeValue() / modifier;
         //   f = (float)((double)f * ((oldAttributeValue / (double)player.capabilities.getWalkSpeed() + 1.0D) / 2.0D));

            if (player.capabilities.getWalkSpeed() == 0.0F || Float.isNaN(f) || Float.isInfinite(f))
            {
                f = 1.0F;
            }

            if (player.isHandActive() && player.getActiveItemStack() != null && player.getActiveItemStack().getItem() == Items.BOW)
            {
                int i = player.getItemInUseMaxCount();
                float f1 = (float)i / 20.0F;

                if (f1 > 1.0F)
                {
                    f1 = 1.0F;
                }
                else
                {
                    f1 = f1 * f1;
                }

                f *= 1.0F - f1 * 0.15F;
            }

    	evt.setNewfov(f);
    }
    
    @SubscribeEvent
    public void livingUpdate(LivingJumpEvent evt)
    {
    	if(evt.getEntityLiving() instanceof EntityPlayer)
    	{
    		EntityPlayer p = (EntityPlayer) evt.getEntityLiving();
    		
		  //  if(ClientStamina.getStamina() <= 10)
		 //   {
		 //   	p.motionY = 0F;
		//    }
		 //   else p.motionY = 0.5F;	
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