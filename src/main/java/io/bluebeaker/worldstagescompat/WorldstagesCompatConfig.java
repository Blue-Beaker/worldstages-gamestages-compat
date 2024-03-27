package io.bluebeaker.worldstagescompat;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Type;

@Config(modid = WorldstagesCompatMod.MODID,type = Type.INSTANCE,category = "general")
public class WorldstagesCompatConfig {
    @Comment("Enable gamestages compat. Active worldstages count as active gamestages for everyone.\nRegistered worldstages can also be managed by GameStages API.")
    @LangKey("config.worldstagecompat.compatGamestages.name")
    public static boolean compat_gamestages = true;
    @Comment("Enable recipestages compat. Recipes locked behind an active worldstage can be crafted by anyone or any autocrafter.")
    @LangKey("config.worldstagecompat.compatRecipestages.name")
    public static boolean compat_recipestages = true;
}