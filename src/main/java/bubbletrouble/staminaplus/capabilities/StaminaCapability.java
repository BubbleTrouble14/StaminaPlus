package bubbletrouble.staminaplus.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class StaminaCapability implements ICapabilitySerializable<NBTBase>
{
    @CapabilityInject(IStamina.class)
    public static final Capability<IStamina> Stamina = null;
    
    private IStamina instance = Stamina.getDefaultInstance();
    
    public StaminaCapability(float setMaxStamina)
    {
    	instance.setMaxStamina(setMaxStamina);
    }
    
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == Stamina;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        return capability == Stamina ? Stamina.<T> cast(this.instance) : null;
    }

    @Override
    public NBTBase serializeNBT()
    {
        return Stamina.getStorage().writeNBT(Stamina, this.instance, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
    	Stamina.getStorage().readNBT(Stamina, this.instance, null, nbt);
    }
}
