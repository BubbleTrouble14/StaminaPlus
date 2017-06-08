package bubbletrouble.staminaplus;

import bubbletrouble.staminaplus.capabilities.CapabilityHandler;
import bubbletrouble.staminaplus.capabilities.IStamina;
import bubbletrouble.staminaplus.capabilities.Stamina;
import bubbletrouble.staminaplus.capabilities.StaminaStorage;
import bubbletrouble.staminaplus.network.PlayerActionMessage;
import bubbletrouble.staminaplus.network.PlayerJumpMessage;
import bubbletrouble.staminaplus.network.StaminaValueMessage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Common proxy class 
 */
public class CommonProxy
{
    public void init()	
    {	
        CapabilityManager.INSTANCE.register(IStamina.class, new StaminaStorage(), Stamina.class);
        
        MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }
    
    public void preInit(FMLPreInitializationEvent evt)
    {
	//	ModConfig.init(evt.getModConfigurationDirectory());
	//	MinecraftForge.EVENT_BUS.register(new ModConfig());
    	
		setupNetwork(evt);
    }
    
    private final void setupNetwork(FMLPreInitializationEvent event)
	{
		SimpleNetworkWrapper modChannel = NetworkRegistry.INSTANCE.newSimpleChannel(Main.MODID);
		Main.modChannel = modChannel;
		
		int id = 0;
		
		modChannel.registerMessage(PlayerActionMessage.Handler.class, PlayerActionMessage.class, id++, Side.SERVER);
		modChannel.registerMessage(StaminaValueMessage.Handler.class, StaminaValueMessage.class, id++, Side.CLIENT);
		modChannel.registerMessage(PlayerJumpMessage.Handler.class, PlayerJumpMessage.class, id++, Side.SERVER);
	}
}
