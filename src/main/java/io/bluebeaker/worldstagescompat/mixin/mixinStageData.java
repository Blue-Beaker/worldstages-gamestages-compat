package io.bluebeaker.worldstagescompat.mixin;

import java.util.Collection;
import java.util.HashSet;

import javax.annotation.Nonnull;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.bluebeaker.worldstages.StageChecker;
import net.darkhax.gamestages.data.StageData;

@Mixin(StageData.class)
public abstract class mixinStageData {
    @Inject(method = "getStages",remap = false,cancellable = true, at = { @At("RETURN") })
    public void getStages(CallbackInfoReturnable<Collection<String>> cir){
        Collection<String> list1 = new HashSet<String>();
        list1.addAll(cir.getReturnValue());
        list1.addAll(StageChecker.instance.stages);
        cir.setReturnValue(list1);
    }
    @Inject(method = "hasStage",remap = false,cancellable = true,at = @At("RETURN"))
    public void hasStage(@Nonnull final String stage,CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(cir.getReturnValue()||StageChecker.instance.stages.contains(stage));
    }
}
