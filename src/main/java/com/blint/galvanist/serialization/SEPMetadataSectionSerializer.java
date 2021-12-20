package com.blint.galvanist.serialization;

import java.util.Dictionary;
import java.util.Hashtable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.util.GsonHelper;

public class SEPMetadataSectionSerializer implements MetadataSectionSerializer<Dictionary<String, Float>> {

    @Override
    public String getMetadataSectionName() {
        return "sep";
    }

    @Override
    public Dictionary<String, Float> fromJson(JsonObject pJson) 
    {

        Hashtable<String, Float> table = new Hashtable<>();

        for (java.util.Map.Entry<String, JsonElement> entry : pJson.entrySet()) {
            
            float f = 0;
            try
            {
                f = GsonHelper.convertToFloat(entry.getValue(), entry.getKey());
            }
            catch(JsonSyntaxException e)
            {
                continue;
            }

            table.put(entry.getKey(), f);            
        }

        return table;
        
    }
    
}
