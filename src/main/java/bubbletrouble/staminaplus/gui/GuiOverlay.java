package bubbletrouble.staminaplus.gui;

import bubbletrouble.staminaplus.ClientStamina;
import bubbletrouble.staminaplus.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiOverlay extends Gui
{
	private static final Minecraft mc = Minecraft.getMinecraft();
	private static final ResourceLocation OVERLAY = new ResourceLocation(Main.MODID, "textures/gui/stamina.png");
	
	@SubscribeEvent
	public void renderGUIOverlay(RenderGameOverlayEvent e)
	{
		if (!e.isCancelable() && e.getType().equals(ElementType.EXPERIENCE))
		{
			Overlay(e);
		}

	}
	
	public static void Overlay(RenderGameOverlayEvent evt)
	{
		int posX = evt.getResolution().getScaledWidth() / 2 + 10;
		int posY = evt.getResolution().getScaledHeight() -49;
		float stamValue = ClientStamina.getStamina();
		
        mc.renderEngine.bindTexture(OVERLAY);
        int BlocksToDraw = (int)stamValue/10;

		for(int i = 0; i < 10; i++)
		{
	        mc.ingameGUI.drawTexturedModalRect(posX, posY, 0, 0, 9, 9);
	        posX = posX + 8;
		}
		posX = evt.getResolution().getScaledWidth() / 2 + 11;
		for(int j = 0; j < BlocksToDraw; j++)
		{
			if(j % 2 == 0)
			{
				mc.ingameGUI.drawTexturedModalRect(posX, posY + 1, 9, 0, 5, 9);    
			}
			else
			{
				 mc.ingameGUI.drawTexturedModalRect(posX, posY + 1, 14, 0, 4, 9);
			}
			posX = posX + 4;
		}
	}
}
