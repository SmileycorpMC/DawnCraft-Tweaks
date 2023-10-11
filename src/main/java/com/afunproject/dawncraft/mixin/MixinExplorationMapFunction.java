package com.afunproject.dawncraft.mixin;

import com.google.gson.JsonObject;
import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ExplorationMapFunction.Serializer.class)
public class MixinExplorationMapFunction {

    @Redirect(method = "deserialize(Lcom/google/gson/JsonObject;Lcom/google/gson/JsonDeserializationContext;[Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition;)Lnet/minecraft/world/level/storage/loot/functions/ExplorationMapFunction;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/GsonHelper;getAsInt(Lcom/google/gson/JsonObject;Ljava/lang/String;I)I"))
    private int deserialize(JsonObject p_13825_, String p_13826_, int p_13827_) {
        return p_13827_ == 50 ? 5000 : p_13827_;
    }

}
