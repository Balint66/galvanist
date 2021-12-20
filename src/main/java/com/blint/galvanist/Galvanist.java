package com.blint.galvanist;

import java.io.IOException;

import com.blint.galvanist.Registries.Items;
import com.blint.galvanist.init.ModInit;
import com.blint.galvanist.managers.SEPManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("galvanist")
public class Galvanist
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public Galvanist() {

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ModInit.Init();

    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        if(event == null) return;
        try
        {
            SEPManager.INSTANCE.get().loadStandardElectrodePotentials(event
                .getServer().getResourceManager());
        }
        catch(IOException e)
        {
            //Log here
        }
        
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event)
    {
        SEPManager.INSTANCE.get().reset();
    }

    @SubscribeEvent
    public void onDatapacksReloaded(OnDatapackSyncEvent event)
    {
        final SEPManager manager = SEPManager.INSTANCE.get();
        
        if(event.getPlayer() == null)
        {
            final MinecraftServer server = event.getPlayerList().getServer();
            try{

                manager.loadStandardElectrodePotentials(server.getResourceManager());


                for (ServerPlayer player : event.getPlayerList().getPlayers()) 
                {
                    manager.sendCustomPacket(player);
                }

            }
            catch(IOException e)
            {
                //Log here
            }
        }
        else
        {
            manager.sendCustomPacket(event.getPlayer());
        }

    }

}
