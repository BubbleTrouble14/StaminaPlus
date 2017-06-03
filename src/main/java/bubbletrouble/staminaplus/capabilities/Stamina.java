package bubbletrouble.staminaplus.capabilities;

public class Stamina implements IStamina
{
	private float stamina;
	private float maxStamina;
	private float staminaMultiplier = 1F;
	
	@Override
	public void setStamina(float valse) 
	{
		this.stamina = valse;
	}

	@Override
	public float getStamina() 
	{
		return stamina;
	}

	@Override
	public void increaseStamina(float value)
	{
		if(stamina + value > maxStamina)
		{
			stamina = maxStamina;
		}
	//	if(stamina >= maxStamina)stamina = getMaxStatmina();
		else this.stamina += value * getStaminaMultiplier();
	}

	@Override
	public void decreaseStamina(float value)
	{
		if(stamina - value < 0) 
		{
			stamina = 0;
		}
		//if(stamina <= 0)stamina = 0;
		else stamina -= value;
	}

	@Override
	public void setMaxStamina(float value) 
	{
		maxStamina = value;
	}

	@Override
	public float getMaxStatmina() 
	{
		return maxStamina;
	}

	@Override
	public float getStaminaMultiplier() 
	{
		if(staminaMultiplier == 0F) return 1F;
		return staminaMultiplier;
	}

	@Override
	public void setStaminaMultiplier(float value)
	{
		staminaMultiplier = value;
	}

}
