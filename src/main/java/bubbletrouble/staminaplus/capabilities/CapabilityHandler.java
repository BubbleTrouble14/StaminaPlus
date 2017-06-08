package bubbletrouble.staminaplus.capabilities;

import bubbletrouble.staminaplus.Main;
import bubbletrouble.staminaplus.config.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CapabilityHandler 
{
    public static final ResourceLocation STAMINA = new ResourceLocation(Main.MODID, "stamina");

    @SubscribeEvent
    public void attachPlayerCapability(AttachCapabilitiesEvent<Entity> event)
    {
        if (!(event.getObject() instanceof EntityPlayer)) return;
        	event.addCapability(STAMINA, new StaminaCapability(ModConfig.max_stamina));   	
    }      
}	
