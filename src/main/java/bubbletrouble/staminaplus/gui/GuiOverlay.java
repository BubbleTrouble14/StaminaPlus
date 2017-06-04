package bubbletrouble.staminaplus.gui;

import bubbletrouble.staminaplus.ClientStamina;
import bubbletrouble.staminaplus.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
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
		EntityPlayer p = Minecraft.getMinecraft().player;
		int posY = 0;
		if(p.isInWater() && p.getAir() < 300)
		{
			posY = -10;
		}else posY = 0;
		if (!e.isCancelable() && e.getType().equals(ElementType.EXPERIENCE))
		{	
			Overlay(e, + posY);
		}
	}
	
	public static void Overlay(RenderGameOverlayEvent evt, int posY)
	{
		int posX = evt.getResolution().getScaledWidth() / 2 + 10;
		posY = posY + evt.getResolution().getScaledHeight() -49;
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
