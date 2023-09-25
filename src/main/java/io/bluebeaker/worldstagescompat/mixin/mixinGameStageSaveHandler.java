package io.bluebeaker.worldstagescompat.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.darkhax.gamestages.data.GameStageSaveHandler;

@Mixin(GameStageSaveHandler.class)
public abstract class mixinGameStageSaveHandler {
    // @ModifyVariable(method = "onPlayerSave(Lnet/minecraftforge/event/entity/player/PlayerEvent/SaveToFile;)V",at = @At("STORE"),ordinal = 0)
    // public static void onPlayerSave(IStageData playerdata){

    // }
    
}
