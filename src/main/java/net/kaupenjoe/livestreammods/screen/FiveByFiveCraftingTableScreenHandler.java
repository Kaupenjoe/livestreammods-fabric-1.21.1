package net.kaupenjoe.livestreammods.screen;

import net.kaupenjoe.livestreammods.block.ModBlocks;
import net.kaupenjoe.livestreammods.recipe.FiveByFiveCraftingRecipe;
import net.kaupenjoe.livestreammods.recipe.ModCraftingResultSlot;
import net.kaupenjoe.livestreammods.recipe.ModRecipes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.Optional;

public class FiveByFiveCraftingTableScreenHandler extends AbstractRecipeScreenHandler<CraftingRecipeInput, FiveByFiveCraftingRecipe> {
    private final RecipeInputInventory input;
    private final CraftingResultInventory result;
    private final ScreenHandlerContext context;
    private final PlayerEntity player;

    public FiveByFiveCraftingTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public FiveByFiveCraftingTableScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModScreenHandlers.FIVE_BY_FIVE_CRAFTING, syncId);
        this.input = new CraftingInventory(this, 5, 5);
        this.result = new CraftingResultInventory();
        this.context = context;
        this.player = playerInventory.player;
        this.addSlot(new ModCraftingResultSlot(playerInventory.player, this.input, this.result, 0, 141, 48));

        int i;
        int j;
        for(i = 0; i < input.getHeight(); ++i) {
            for(j = 0; j < input.getWidth(); ++j) {
                this.addSlot(new Slot(this.input, j + i * 5, 12 + j * 18, 11 + i * 18));
            }
        }

        for(i = 0; i < 3; ++i) {
            for(j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 110 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 168));
        }
    }

    protected static void updateResult(ScreenHandler handler, World world, PlayerEntity player, RecipeInputInventory craftingInventory,
                                       CraftingResultInventory resultInventory) {
        if (!world.isClient) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
            ItemStack itemStack = ItemStack.EMPTY;
            Optional<RecipeEntry<CraftingRecipe>> optionalCrafting = world.getServer().getRecipeManager()
                    .getFirstMatch(RecipeType.CRAFTING, CraftingRecipeInput.create(5, 5, craftingInventory.getHeldStacks()), world);
            Optional<RecipeEntry<FiveByFiveCraftingRecipe>> optionalFiveByFive = world.getServer().getRecipeManager()
                    .getFirstMatch(ModRecipes.FIVE_BY_FIVE_CRAFTING_RECIPE_TYPE, CraftingRecipeInput.create(5, 5, craftingInventory.getHeldStacks()), world);

            if (optionalCrafting.isPresent()) {
                // Normal Crafting
                RecipeEntry<CraftingRecipe> craftingRecipe = optionalCrafting.get();

                if (resultInventory.shouldCraftRecipe(world, serverPlayerEntity, craftingRecipe)) {
                    ItemStack itemStack2 = craftingRecipe.value().craft(CraftingRecipeInput.create(5, 5,
                            craftingInventory.getHeldStacks()), world.getRegistryManager());
                    if (itemStack2.isItemEnabled(world.getEnabledFeatures())) {
                        itemStack = itemStack2;
                    }
                }
            } else if (optionalFiveByFive.isPresent()) {
                // 5x5 Crafting
                RecipeEntry<FiveByFiveCraftingRecipe> craftingRecipe = optionalFiveByFive.get();

                if (resultInventory.shouldCraftRecipe(world, serverPlayerEntity, craftingRecipe)) {
                    ItemStack itemStack2 = craftingRecipe.value().craft(CraftingRecipeInput.create(5, 5,
                            craftingInventory.getHeldStacks()), world.getRegistryManager());
                    if (itemStack2.isItemEnabled(world.getEnabledFeatures())) {
                        itemStack = itemStack2;
                    }
                }
            }

            resultInventory.setStack(0, itemStack);
            handler.setPreviousTrackedSlot(0, itemStack);
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 0, itemStack));
        }
    }

    public void onContentChanged(Inventory inventory) {
        this.context.run((world, pos) -> {
            updateResult(this, world, this.player, this.input, this.result);
        });
    }

    public void populateRecipeFinder(RecipeMatcher finder) {
        this.input.provideRecipeInputs(finder);
    }

    public void clearCraftingSlots() {
        this.input.clear();
        this.result.clear();
    }

    public boolean matches(RecipeEntry recipe) {
        return recipe.value().matches(input.createRecipeInput(), this.player.getWorld());
    }

    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> {
            this.dropInventory(player, this.input);
        });
    }

    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, ModBlocks.FIVE_BY_FIVE_CRAFTING);
    }

    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = (Slot)this.slots.get(slot);
        if (slot2 != null && slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot == 0) {
                this.context.run((world, pos) -> {
                    itemStack2.getItem().onCraft(itemStack2, world);
                });
                if (!this.insertItem(itemStack2, 26, 62, true)) {
                    return ItemStack.EMPTY;
                }

                slot2.onQuickTransfer(itemStack2, itemStack);
            } else if (slot >= 26 && slot < 62) {
                if (!this.insertItem(itemStack2, 1, 26, false)) {
                    if (slot < 53) {
                        if (!this.insertItem(itemStack2, 53, 62, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.insertItem(itemStack2, 26, 53, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.insertItem(itemStack2, 26, 62, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot2.onTakeItem(player, itemStack2);
            if (slot == 0) {
                player.dropItem(itemStack2, false);
            }
        }

        return itemStack;
    }

    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.result && super.canInsertIntoSlot(stack, slot);
    }

    public int getCraftingResultSlotIndex() {
        return 0;
    }

    public int getCraftingWidth() {
        return this.input.getWidth();
    }

    public int getCraftingHeight() {
        return this.input.getHeight();
    }

    public int getCraftingSlotCount() {
        return 26;
    }

    public RecipeBookCategory getCategory() {
        return RecipeBookCategory.CRAFTING;
    }

    public boolean canInsertIntoSlot(int index) {
        return index != this.getCraftingResultSlotIndex();
    }
}