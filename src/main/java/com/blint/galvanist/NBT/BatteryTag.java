package com.blint.galvanist.NBT;

import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

public class BatteryTag extends CompoundTag
{

    private final IntTag maxEnergy;
    private final IntTag energy;
    private final StringTag anode;
    private final StringTag cathode;

    public final Supplier<Integer> MAXENERGY;
    public final Supplier<Integer> ENERGY;
    public final Supplier<String> ANODE;
    public final Supplier<String> CATHODE;

    public BatteryTag()
    {
        this(0,0,"none", "none");
    }

    public BatteryTag(int max, int energy, String anode, String cathode)
    {
        super();
        maxEnergy = IntTag.valueOf(max);
        this.energy = IntTag.valueOf(energy);
        this.anode = StringTag.valueOf(anode);
        this.cathode = StringTag.valueOf(cathode);
        super.put("maxEnergy", maxEnergy);
        super.put("energy", this.energy);
        super.put("anode", this.anode);
        super.put("cathode", this.cathode);
        MAXENERGY = maxEnergy::getAsInt;
        ENERGY = this.energy::getAsInt;
        ANODE = this.anode::getAsString;
        CATHODE = this.cathode::getAsString;

    }

    @Override
    public Tag put(String pKey, Tag pValue) {
        throw new IllegalCallerException("Can not call this method on BatteryTag", null);
    }
    
}
