package net.kaupenjoe.livestreammods.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.kaupenjoe.livestreammods.LivestreamMods;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block DICE_BLOCK = registerBlock("dice_block",
            new Block(AbstractBlock.Settings.create().strength(4f)));

    public static final Block FIVE_BY_FIVE_CRAFTING = registerBlock("five_by_five_crafting_block",
            new FiveByFiveCraftingTableBlock(AbstractBlock.Settings.create().strength(4f)));

    private static Block registerBlockWithoutBlockItem(String name, Block block) {
        return Registry.register(Registries.BLOCK, Identifier.of(LivestreamMods.MOD_ID, name), block);
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(LivestreamMods.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(LivestreamMods.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        LivestreamMods.LOGGER.info("Registering Mod Blocks for " + LivestreamMods.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(ModBlocks.DICE_BLOCK);
        });
    }
}