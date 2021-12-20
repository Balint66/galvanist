package com.blint.galvanist.items;

import com.blint.galvanist.NBT.BatteryTag;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class Battery extends Item {

    public Battery() 
    {
        super(new Properties().stacksTo(1).setNoRepair().tab(CreativeModeTab.TAB_MISC));
    }

    @Override
    public int getDamage(ItemStack stack) 
    {
        CompoundTag nbt = stack.serializeNBT().getCompound("battery_stat");

        if(nbt == null || !(nbt instanceof BatteryTag)) return 0;

        BatteryTag bat = (BatteryTag)nbt;

        return Math.round( bat.ENERGY.get()/(float)bat.MAXENERGY.get());

    }
    
}
