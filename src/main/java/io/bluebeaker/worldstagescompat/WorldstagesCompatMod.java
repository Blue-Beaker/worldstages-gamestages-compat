package io.bluebeaker.worldstagescompat;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.event.GameStageEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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
    public static final String MODID = Tags.MOD_ID;
    public static final String NAME = Tags.MOD_NAME;
    public static final String VERSION = Tags.VERSION;
    
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
    /**
     * When changing registered worldstages in gamestages, change corresponding worldstages instead. 
     */
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
            // event.setCanceled(true);
        }
    }
    @EventHandler
    public void onServerStart(FMLServerStartingEvent event){
        this.server=event.getServer();
    }
    @SubscribeEvent
    public void onStageChanged(WorldStageEvent event){
        if(!event.getWorld().isRemote && this.server!=null){
            for(EntityPlayerMP player: server.getPlayerList().getPlayers()){
                MinecraftForge.EVENT_BUS.post(new GameStageEvent(player, event.laststage));
                GameStageHelper.syncPlayer(player);
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

    @SubscribeEvent
    public void onConfigChangedEvent(OnConfigChangedEvent event) {
        if (event.getModID().equals(MODID)) {
            ConfigManager.sync(MODID, Type.INSTANCE);
        }
    }
}
