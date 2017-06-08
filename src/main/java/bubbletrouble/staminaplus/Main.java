package bubbletrouble.staminaplus;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = Main.MODID, version = Main.VERSION, name = Main.NAME) //guiFactory = "bubbletrouble.staminaplus.config.GuiFactory"
public class Main
{
    public static final String MODID = "staminaplus";
    public static final String VERSION = "1.2";
	public static final String NAME = "StaminaPlus";
	public static Logger modLog;
   // public static ConfigHandler ConfigHandler;

	@Instance(Main.MODID)
	private static Main instance;
    
    @SidedProxy(clientSide = "bubbletrouble.staminaplus.ClientProxy", serverSide = "bubbletrouble.staminaplus.CommonProxy")
    public static CommonProxy proxy;
    
	public static SimpleNetworkWrapper modChannel;
	
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init();
    }
    
    @EventHandler	
	public void preInit(FMLPreInitializationEvent event)
    {
      //  ConfigHandler = new ConfigHandler(event.getSuggestedConfigurationFile());
      //  MinecraftForge.EVENT_BUS.register(ConfigHandler);

		proxy.preInit(event);
    }
    
	public static Main instance()
	{
		return instance;
	}
}