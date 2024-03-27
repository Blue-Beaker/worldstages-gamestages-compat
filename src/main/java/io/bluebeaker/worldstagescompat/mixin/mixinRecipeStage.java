package io.bluebeaker.worldstagescompat.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.blamejared.recipestages.recipes.RecipeStage;

import io.bluebeaker.worldstages.StageChecker;
import io.bluebeaker.worldstagescompat.WorldstagesCompatConfig;
import net.minecraft.inventory.InventoryCrafting;

@Mixin(RecipeStage.class)
public abstract class mixinRecipeStage {
    @Inject(method = "isGoodForCrafting", remap = false, cancellable = true, at = { @At("HEAD") })
    public void isGoodForCrafting(InventoryCrafting inv, CallbackInfoReturnable<Boolean> cir) {
        if (!WorldstagesCompatConfig.compat_recipestages)
            return;
        String tier = ((RecipeStage) (Object) this).getTier();
        if (StageChecker.instance.stages.contains(tier)) {
            cir.setReturnValue(true);
        }

    }

}
