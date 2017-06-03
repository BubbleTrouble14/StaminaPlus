package bubbletrouble.staminaplus.capabilities;

public interface IStamina 
{
	public void setStamina(float value);
	
	public float getStamina();
	
	public void increaseStamina(float value);
	
	public void decreaseStamina(float value);
	
	public void setMaxStamina(float value);
	
	public float getMaxStatmina();
	
	public float getStaminaMultiplier();
	
	public void setStaminaMultiplier(float value);
	
}
