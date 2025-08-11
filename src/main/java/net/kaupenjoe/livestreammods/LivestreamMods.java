package net.kaupenjoe.livestreammods;

import net.fabricmc.api.ModInitializer;

import net.kaupenjoe.livestreammods.block.ModBlocks;
import net.kaupenjoe.livestreammods.recipe.ModRecipes;
import net.kaupenjoe.livestreammods.screen.ModScreenHandlers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LivestreamMods implements ModInitializer {
	public static final String MOD_ID = "livestreammods";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModBlocks.registerModBlocks();

		ModScreenHandlers.registerScreenHandlers();
		ModRecipes.registerRecipes();

	}
}
