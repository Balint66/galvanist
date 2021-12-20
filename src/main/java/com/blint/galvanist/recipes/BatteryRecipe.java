package com.blint.galvanist.recipes;

import com.blint.galvanist.NBT.BatteryTag;
import com.blint.galvanist.Registries.Recipes;
import com.blint.galvanist.managers.SEPManager;
import com.google.gson.JsonObject;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class BatteryRecipe extends ShapedRecipe {

    public BatteryRecipe(ResourceLocation pId, String pGroup) {
        super(pId, pGroup, 3, 3,
            NonNullList.of(null,
                Ingredient.of(Tags.Items.INGOTS), Ingredient.EMPTY,
                Ingredient.of(Tags.Items.INGOTS),
                Ingredient.EMPTY, Ingredient.of(Items.BUCKET), Ingredient.EMPTY
            ),
            new ItemStack(com.blint.galvanist.Registries.Items.BATTERY.get(), 1));
    }


    @Override
    public ItemStack assemble(CraftingContainer pInv) {
        ItemStack copy = getResultItem().copy();
        SEPManager manager = SEPManager.INSTANCE.get();

        float seps[] = new float[2];
        String mapping[] = new String[2];
        int s = 0;

        for (int i = 0; i < pInv.getHeight() && s < 2; i++) {
            for (int j = 0; j < pInv.getWidth() && s < 2; j++) {
                ItemStack ingredient = pInv.getItem(i+j);
                if(ingredient.isEmpty()) continue;
                if(!manager.contains(ingredient.getItem())) continue;

                seps[s] = manager.get(ingredient.getItem());
                mapping[s] = ingredient.getItem().getRegistryName().toString();
                s++;

            }
        }

        if(s != 0)
        {
            int menergy = Math.round(100* Math.abs(seps[0]-seps[1])*0.2f);
            boolean reversed = seps[0] < seps[1];
            BatteryTag tag = new BatteryTag(menergy, menergy,
                reversed ? mapping[1] : mapping[0],
                reversed ? mapping[0] : mapping[1]);
            
            copy.addTagElement("battery_stat", tag);
            return copy;
        }


        return ItemStack.EMPTY;

    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Recipes.BATTERY_RECIPE.get();
    }


    public static final class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<BatteryRecipe>
    {

        @Override
        public BatteryRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ShapedRecipe rec =  RecipeSerializer.SHAPED_RECIPE.fromJson(pRecipeId, pSerializedRecipe);
            return new BatteryRecipe(rec.getId(), rec.getGroup());
        }

        @Override
        public BatteryRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            ShapedRecipe rec =  RecipeSerializer.SHAPED_RECIPE.fromNetwork(pRecipeId, pBuffer);
            return new BatteryRecipe(rec.getId(), rec.getGroup());
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, BatteryRecipe pRecipe) {
            RecipeSerializer.SHAPED_RECIPE.toNetwork(pBuffer, pRecipe);
            
        }

    }

    
}
