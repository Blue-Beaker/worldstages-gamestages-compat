package io.bluebeaker.worldstagescompat.mixin;

import java.util.Collection;
import java.util.HashSet;

import javax.annotation.Nonnull;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;

import io.bluebeaker.worldstages.ConfigStorage;
import io.bluebeaker.worldstages.StageChecker;
import io.bluebeaker.worldstagescompat.WorldstagesCompatConfig;
import net.darkhax.bookshelf.util.NBTUtils;
import net.darkhax.gamestages.data.StageData;
import net.minecraft.nbt.NBTTagCompound;

@Mixin(StageData.class)
public abstract class mixinStageData {
    @Inject(method = "getStages", remap = false, cancellable = true, at = { @At("RETURN") })
    public void getStages(CallbackInfoReturnable<Collection<String>> cir) {
        if (!WorldstagesCompatConfig.compat_gamestages)
            return;
        Collection<String> list1 = new HashSet<String>();
        list1.addAll(cir.getReturnValue());
        list1.addAll(StageChecker.instance.stages);
        cir.setReturnValue(Collections.unmodifiableCollection(list1));
    }

    @Inject(method = "hasStage", remap = false, cancellable = true, at = @At("RETURN"))
    public void hasStage(@Nonnull final String stage, CallbackInfoReturnable<Boolean> cir) {
        if (!WorldstagesCompatConfig.compat_gamestages)
            return;
        cir.setReturnValue(cir.getReturnValue() || StageChecker.instance.stages.contains(stage));
    }

    /**
     * @author Blue_Beaker
     * @reason Dont save stages managed by worldstages to gamestages data
     */
    // @Overwrite(remap = false)
    @Inject(method = "writeToNBT", remap = false, cancellable = true, at = @At("HEAD"))
    public void writeToNBT(CallbackInfoReturnable<NBTTagCompound> cir) {
        if (!WorldstagesCompatConfig.compat_gamestages)
            return;
        final NBTTagCompound tag = new NBTTagCompound();
        Collection<String> stages = new HashSet<String>();
        stages.addAll(((StageData) (Object) this).getStages());
        stages.removeAll(StageChecker.instance.stages); // Exclude active worldstages
        stages.removeAll(ConfigStorage.instance.RegisteredStages); // Exclude registered worldstages
        tag.setTag(StageData.TAG_STAGES, NBTUtils.writeCollection(stages, stage -> stage));
        cir.setReturnValue(tag);
    }
}
