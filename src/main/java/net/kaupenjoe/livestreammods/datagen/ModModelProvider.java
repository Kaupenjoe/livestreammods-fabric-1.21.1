package net.kaupenjoe.livestreammods.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.kaupenjoe.livestreammods.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;

import static net.minecraft.data.client.TextureMap.getSubId;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerCubeWithCustomTextures(ModBlocks.DICE_BLOCK, ModBlocks.DICE_BLOCK, this::perFaceTextures);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }

    public TextureMap perFaceTextures(Block frontTopSideBlock, Block downBlock) {
        return new TextureMap()
                .put(TextureKey.PARTICLE, getSubId(frontTopSideBlock, "_north"))
                .put(TextureKey.DOWN, getSubId(frontTopSideBlock, "_down"))
                .put(TextureKey.UP, getSubId(frontTopSideBlock, "_up"))
                .put(TextureKey.NORTH, getSubId(frontTopSideBlock, "_north"))
                .put(TextureKey.SOUTH, getSubId(frontTopSideBlock, "_south"))
                .put(TextureKey.EAST, getSubId(frontTopSideBlock, "_east"))
                .put(TextureKey.WEST, getSubId(frontTopSideBlock, "_west"));
    }
}
