package bubbletrouble.staminaplus.gui;

import bubbletrouble.staminaplus.capabilities.IStamina;
import bubbletrouble.staminaplus.capabilities.StaminaCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiOverlay extends Gui
{
	private static final Minecraft mc = Minecraft.getMinecraft();
	
	@SubscribeEvent
	public void renderGUIOverlay(RenderGameOverlayEvent.Post e)
	{
		EntityPlayer p = mc.player;
		IStamina stam = p.getCapability(StaminaCapability.Stamina, null);
	
		//Dispaly Stamina
		if (e.getType().equals(ElementType.HELMET))
		{
			String text = "";
			//System.out.println(stam.getStamina());
			text = String.valueOf(stam.getStamina());
			int x = e.getResolution().getScaledWidth() - 4 - mc.fontRenderer.getStringWidth(text);
			int y = 20;
			drawString(mc.fontRenderer, text, x, y - 16, 0xFFFFFFFF);
		}

	}
}
