package net.kaupenjoe.livestreammods;

import net.fabricmc.api.ClientModInitializer;
import net.kaupenjoe.livestreammods.screen.FiveByFiveCraftingTableScreen;
import net.kaupenjoe.livestreammods.screen.ModScreenHandlers;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class LivestreamModsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandlers.FIVE_BY_FIVE_CRAFTING, FiveByFiveCraftingTableScreen::new);
    }
}
