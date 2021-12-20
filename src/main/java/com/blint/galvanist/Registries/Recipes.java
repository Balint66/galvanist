package com.blint.galvanist.Registries;

import com.blint.galvanist.recipes.BatteryRecipe;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class Recipes {
    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS 
        = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "galvanist");
    
    public static void Init()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        //TODO: Figure out more about the recipe matching.
        //SERIALIZERS.register(bus);
    }

    public static final RegistryObject<RecipeSerializer<?>> BATTERY_RECIPE = SERIALIZERS.register("battery_recipe", BatteryRecipe.Serializer::new );

}
