package net.kaupenjoe.livestreammods.mixin;

import net.minecraft.recipe.RawShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(RawShapedRecipe.Data.class)
public class RawShapedRecipeDataMixin {
    @ModifyConstant(method = "method_55096(Ljava/util/List;)Lcom/mojang/serialization/DataResult;", constant = @Constant(intValue = 3))
    private static int injected(int value) {
        return 5;
    }
}
