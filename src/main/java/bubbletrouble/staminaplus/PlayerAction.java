package bubbletrouble.staminaplus;

import net.minecraft.entity.player.EntityPlayer;

public class PlayerAction 
{
    private static String playerAction; 
    
    public static void setPlayerAction(EntityPlayer p, String action)
    {
    	if(p != null)
    	{
	    	if(action != null)	playerAction = action;
	    	else System.out.println("Some error is Occuring in PlayerAction");
    	}
    }
    
    public static String getPlayerAction()
    {
    	return playerAction;
    }
    
}
