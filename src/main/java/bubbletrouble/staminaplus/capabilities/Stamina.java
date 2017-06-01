package bubbletrouble.staminaplus.capabilities;

public class Stamina implements IStamina
{
	private float stamina;
	private float maxStamina;
	
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
		else this.stamina += value;
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

}
