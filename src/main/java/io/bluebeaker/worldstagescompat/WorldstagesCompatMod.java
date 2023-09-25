package io.bluebeaker.worldstagescompat;
import net.darkhax.gamestages.event.GameStageEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

import org.apache.logging.log4j.Logger;

import io.bluebeaker.worldstages.WorldStagesSavedData;
import io.bluebeaker.worldstages.ConfigStorage;
import io.bluebeaker.worldstages.WorldStageEvent;
// The value here should match an entry in the META-INF/mods.toml file

@Mod(modid = WorldstagesCompatMod.MODID, name = WorldstagesCompatMod.NAME, version = WorldstagesCompatMod.VERSION)
public class WorldstagesCompatMod
{
    public static final String MODID = "worldstagescompat";
    public static final String NAME = "Worldstages Compat";
    public static final String VERSION = "1.0";
    
    public MinecraftServer server;

    private static Logger logger;
    
    public WorldstagesCompatMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onCheckStage(GameStageEvent.Check event){
        if(WorldStagesSavedData.get(event.getEntityPlayer().world).getStages().contains(event.getStageName()))
        event.setHasStage(true);
    }
    @SubscribeEvent
    public void onAddStage(GameStageEvent.Add event){
        if(ConfigStorage.instance.RegisteredStages.contains(event.getStageName())){
            WorldStagesSavedData.get(event.getEntityPlayer().world).addStage(event.getStageName());
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void onRemoveStage(GameStageEvent.Remove event){
        if(ConfigStorage.instance.RegisteredStages.contains(event.getStageName())){
            WorldStagesSavedData.get(event.getEntityPlayer().world).removeStage(event.getStageName());
            event.setCanceled(true);
        }
    }
    @EventHandler
    public void onServerStart(FMLServerStartingEvent event){
        this.server=event.getServer();
    }
    @SubscribeEvent
    public void onStageChanged(WorldStageEvent event){
        if(!event.getWorld().isRemote){
            for(EntityPlayerMP player: server.getPlayerList().getPlayers()){
                MinecraftForge.EVENT_BUS.post(new GameStageEvent(player, event.laststage));
            }
        }
    }
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onClientStageChanged(WorldStageEvent event){
        if(event.getWorld().isRemote){
            MinecraftForge.EVENT_BUS.post(new GameStageEvent(Minecraft.getMinecraft().player, event.laststage));
        }
    }
}
