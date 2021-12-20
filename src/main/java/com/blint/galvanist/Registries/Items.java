package com.blint.galvanist.Registries;

import com.blint.galvanist.items.Battery;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class Items {

    static final DeferredRegister<Item> itemReg = DeferredRegister.create(ForgeRegistries.ITEMS, "galvanist");

    public static final void Init(){
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        itemReg.register(bus);
    }

    public static final RegistryObject<Item> BATTERY = itemReg.register("battery", Battery::new);

    
}
