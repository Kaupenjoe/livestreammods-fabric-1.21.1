package net.kaupenjoe.livestreammods.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class FiveByFiveCraftingRecipe implements CraftingRecipe {
    final ModRawShapedRecipe raw;
    final ItemStack result;
    final String group;
    final CraftingRecipeCategory category;
    final boolean showNotification;

    public FiveByFiveCraftingRecipe(String group, CraftingRecipeCategory category, ModRawShapedRecipe raw, ItemStack result, boolean showNotification) {
        this.group = group;
        this.category = category;
        this.raw = raw;
        this.result = result;
        this.showNotification = showNotification;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.FIVE_BY_FIVE_CRAFTING_RECIPE_SERIALIZER;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public CraftingRecipeCategory getCategory() {
        return this.category;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return this.result;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return this.raw.getIngredients();
    }

    @Override
    public boolean showNotification() {
        return this.showNotification;
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= this.raw.getWidth() && height >= this.raw.getHeight();
    }

    public boolean matches(CraftingRecipeInput craftingRecipeInput, World world) {
        return this.raw.matches(craftingRecipeInput);
    }

    public ItemStack craft(CraftingRecipeInput craftingRecipeInput, RegistryWrapper.WrapperLookup wrapperLookup) {
        return this.getResult(wrapperLookup).copy();
    }

    public int getWidth() {
        return this.raw.getWidth();
    }

    public int getHeight() {
        return this.raw.getHeight();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.FIVE_BY_FIVE_CRAFTING_RECIPE_TYPE;
    }

    @Override
    public boolean isEmpty() {
        DefaultedList<Ingredient> defaultedList = this.getIngredients();
        return defaultedList.isEmpty()
                || defaultedList.stream().filter(ingredient -> !ingredient.isEmpty()).anyMatch(ingredient -> ingredient.getMatchingStacks().length == 0);
    }

    public static class Serializer implements RecipeSerializer<FiveByFiveCraftingRecipe> {
        public static final MapCodec<FiveByFiveCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
                                CraftingRecipeCategory.CODEC.fieldOf("category").orElse(CraftingRecipeCategory.MISC).forGetter(recipe -> recipe.category),
                                ModRawShapedRecipe.CODEC.forGetter(recipe -> recipe.raw),
                                ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                                Codec.BOOL.optionalFieldOf("show_notification", true).forGetter(recipe -> recipe.showNotification)
                        )
                        .apply(instance, FiveByFiveCraftingRecipe::new)
        );
        public static final PacketCodec<RegistryByteBuf, FiveByFiveCraftingRecipe> PACKET_CODEC = PacketCodec.ofStatic(
                FiveByFiveCraftingRecipe.Serializer::write, FiveByFiveCraftingRecipe.Serializer::read
        );

        @Override
        public MapCodec<FiveByFiveCraftingRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, FiveByFiveCraftingRecipe> packetCodec() {
            return PACKET_CODEC;
        }

        private static FiveByFiveCraftingRecipe read(RegistryByteBuf buf) {
            String string = buf.readString();
            CraftingRecipeCategory craftingRecipeCategory = buf.readEnumConstant(CraftingRecipeCategory.class);
            ModRawShapedRecipe rawShapedRecipe = ModRawShapedRecipe.PACKET_CODEC.decode(buf);
            ItemStack itemStack = ItemStack.PACKET_CODEC.decode(buf);
            boolean bl = buf.readBoolean();
            return new FiveByFiveCraftingRecipe(string, craftingRecipeCategory, rawShapedRecipe, itemStack, bl);
        }

        private static void write(RegistryByteBuf buf, FiveByFiveCraftingRecipe recipe) {
            buf.writeString(recipe.group);
            buf.writeEnumConstant(recipe.category);
            ModRawShapedRecipe.PACKET_CODEC.encode(buf, recipe.raw);
            ItemStack.PACKET_CODEC.encode(buf, recipe.result);
            buf.writeBoolean(recipe.showNotification);
        }
    }
}
