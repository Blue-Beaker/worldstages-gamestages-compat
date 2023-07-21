package io.bluebeaker.worldstagescompat;
import net.darkhax.gamestages.event.GameStageEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

import org.apache.logging.log4j.Logger;

import io.bluebeaker.worldstages.StageChecker;
import io.bluebeaker.worldstages.WorldStagesConfig;
import io.bluebeaker.worldstages.WorldStagesWorldSavedData;
import io.bluebeaker.worldstages.ConfigStorage;
// The value here should match an entry in the META-INF/mods.toml file

@Mod(modid = WorldstagesCompatMod.MODID, name = WorldstagesCompatMod.NAME, version = WorldstagesCompatMod.VERSION)
public class WorldstagesCompatMod
{
    public static final String MODID = "worldstagescompat";
    public static final String NAME = "Worldstages Compat";
    public static final String VERSION = "1.0";

    private static Logger logger;

    public WorldstagesCompatMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onCheckStage(GameStageEvent.Check event){
        if(WorldStagesWorldSavedData.get(event.getEntityPlayer().world).stages.contains(event.getStageName()))
        event.setHasStage(true);
    }
    @SubscribeEvent
    public void onAddStage(GameStageEvent.Add event){
        if(ConfigStorage.instance.RegisteredStages.contains(event.getStageName()))
        WorldStagesWorldSavedData.get(event.getEntityPlayer().world).addStage(event.getStageName());
        event.setCanceled(true);
    }
    @SubscribeEvent
    public void onRemoveStage(GameStageEvent.Remove event){
        if(ConfigStorage.instance.RegisteredStages.contains(event.getStageName()))
        WorldStagesWorldSavedData.get(event.getEntityPlayer().world).removeStage(event.getStageName());
        event.setCanceled(true);
    }
}
