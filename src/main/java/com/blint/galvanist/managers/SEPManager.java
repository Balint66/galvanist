package com.blint.galvanist.managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Supplier;

import com.blint.galvanist.serialization.SEPMetadataSectionSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

public class SEPManager {
    

    static
    {
        itemDef = new LinkedHashMap<>();
        tagDef = new LinkedHashMap<>();
    }

    private static final SEPManager instance = new SEPManager();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final LinkedHashMap<Item, Float> itemDef;
    private static final LinkedHashMap<Tag.Named<Item>, Float> tagDef;

    public static final Supplier<SEPManager> INSTANCE = ()-> instance;
    public static final LinkedHashMap<Tag.Named<Item>, Float> TAGDEFAULTS(){

        return tagDef;

    };
    public static final LinkedHashMap<Item, Float> ITEMDEFAULTS(){

        
        itemDef.putIfAbsent(Items.IRON_INGOT, 1.1f);
        itemDef.putIfAbsent(Items.GOLD_INGOT, 2.3f);

        return itemDef;

    };

    private LinkedHashMap<Tag.Named<Item>, Float> tagStandardDictionary = null;
    private LinkedHashMap<Item, Float> itemStandardDictionary = null;

    private SEPManager(){
        tagStandardDictionary = TAGDEFAULTS();
        itemStandardDictionary = ITEMDEFAULTS();
    }

    public void add(Item key, float value)
    {
        itemStandardDictionary.put(key, value);
    }

    public void add(Tag.Named<Item> key, float value)
    {
        tagStandardDictionary.put(key, value);
    }

    public float get(Item key)
    {
        if(itemStandardDictionary.containsKey(key))
            return itemStandardDictionary.get(key);
        for (ResourceLocation name : key.getTags())
        {
            Tag.Named<Item> k = tagStandardDictionary.keySet().parallelStream()
                .dropWhile((i)->!i.getName().equals(name))
                .findAny().orElseGet(null);
            if(k != null)
                return tagStandardDictionary.get(k);
        }
        return Float.NaN;
    }

    public float get(Tag.Named<Item> key)
    {
        if(tagStandardDictionary.containsKey(key))
            return tagStandardDictionary.get(key);
        return Float.NaN;
    }

    public boolean contains(Item key)
    {
        return itemStandardDictionary.containsKey(key)
                || key.getTags().parallelStream()
                    .anyMatch((i)->tagStandardDictionary.keySet()
                        .stream().map((I)->I.getName()).anyMatch(i::equals));

    }

    public boolean contains(Tag.Named<Item> key)
    {
        return tagStandardDictionary.containsKey(key);
    }

    public void remove(Item key)
    {
        itemStandardDictionary.remove(key);
    }

    public void remove(Tag.Named<Item> key)
    {
        tagStandardDictionary.remove(key);
    }

    public void reset()
    {

        itemStandardDictionary = ITEMDEFAULTS();
        tagStandardDictionary = TAGDEFAULTS();

    }

    public void empty()
    {
        itemStandardDictionary = new LinkedHashMap<>();
        tagStandardDictionary = new LinkedHashMap<>();
    }

    public void loadStandardElectrodePotentials(ResourceManager manager) throws IOException
    {

        ResourceLocation res = new ResourceLocation("galvanist", "sep.json");

        if(!manager.hasResource(res)) return;

        List<Resource> ls = manager.getResources(res);

        if(ls == null || ls.size() <= 0) return;

        reset();

        SEPMetadataSectionSerializer seri = new SEPMetadataSectionSerializer();

        for (Resource resource : ls) {

            Reader reader = new BufferedReader(new InputStreamReader(
                resource.getInputStream(),
                StandardCharsets.UTF_8
            ));

            JsonElement el = GsonHelper.fromJson(GSON, reader, JsonElement.class);

            Dictionary<String, Float> dic = seri.fromJson(GsonHelper.convertToJsonObject(el, "sep.json"));

            if(dic == null) continue;

            for (Enumeration<String> e = dic.keys(); e.hasMoreElements();)
            {
                String k = e.nextElement();
                ResourceLocation key = new ResourceLocation(k);
                if(ItemTags.getAllTags().hasTag(key))
                {
                    tagStandardDictionary.put((Tag.Named<Item>)ItemTags.getAllTags().getTag(key),
                                                dic.get(k));
                }
                else if(ForgeRegistries.ITEMS.containsKey(key))
                {
                    itemStandardDictionary.put(ForgeRegistries.ITEMS.getValue(key), dic.get(k));
                }
            }

        }

    }

    public void sendCustomPacket(final ServerPlayer player)
    {
        //TODO: implement this
    }


}
