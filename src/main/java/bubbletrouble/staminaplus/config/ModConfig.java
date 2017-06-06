package bubbletrouble.staminaplus.config;

import bubbletrouble.staminaplus.Main;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Main.MODID)
@Config.LangKey("staminaplus.config.title")
public class ModConfig
{
	@Config.Comment("Show Stamina bar")
	public static boolean show_bar = true;

	@Config.Comment("The x coordinate offset of the bar")
	public static int x_offset = 0;

	@Config.Comment("The y coordinate offset of the bar")
	public static int y_offset = 0;
	//	}
	
	@Mod.EventBusSubscriber
	private static class EventHandler 
	{
		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Main.MODID)) {
				ConfigManager.sync(Main.MODID, Config.Type.INSTANCE);
			}
		}
	}
}
