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
	
	@Config.Comment("The stamina increase multiplier when standing per sec")
	public static double increase_multiplier = 1.5;
	
	@Config.Comment("The max Stamina")
	public static float max_stamina = 200F;
	
	@Config.Comment("The walking increase value")
	public static float walking = 2.5F;
	
	@Config.Comment("The standing increase value")
	public static float standing = 1.5F;
	
	@Config.Comment("The sprinting decrease value")
	public static float sprinting = 3.0F;
	
	@Config.Comment("The sneaking increase value")
	public static float sneaking = 2.0F;
	
	@Config.Comment("The jumping decrease value")
	public static float jumping = 10.0F;
	
	
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
