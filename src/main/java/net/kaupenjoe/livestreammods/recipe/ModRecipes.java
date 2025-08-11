package net.kaupenjoe.livestreammods.recipe;

import net.kaupenjoe.livestreammods.LivestreamMods;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    public static final RecipeSerializer<FiveByFiveCraftingRecipe> FIVE_BY_FIVE_CRAFTING_RECIPE_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER,
            Identifier.of(LivestreamMods.MOD_ID, "five_by_five_crafting"), new FiveByFiveCraftingRecipe.Serializer());
    public static final RecipeType<FiveByFiveCraftingRecipe> FIVE_BY_FIVE_CRAFTING_RECIPE_TYPE = Registry.register(Registries.RECIPE_TYPE,
            Identifier.of(LivestreamMods.MOD_ID, "five_by_five_crafting"), new RecipeType<FiveByFiveCraftingRecipe>() {
                @Override
                public String toString() {
                    return "five_by_five_crafting";
                }
            });

    public static void registerRecipes() {
        LivestreamMods.LOGGER.info("Registering Recipes " + LivestreamMods.MOD_ID);
    }
}
