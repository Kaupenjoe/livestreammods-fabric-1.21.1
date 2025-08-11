package net.kaupenjoe.livestreammods.screen;

import net.kaupenjoe.livestreammods.LivestreamMods;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static final ScreenHandlerType<FiveByFiveCraftingTableScreenHandler> FIVE_BY_FIVE_CRAFTING =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(LivestreamMods.MOD_ID, "five_by_five_crafting"),
                    new ScreenHandlerType<>(FiveByFiveCraftingTableScreenHandler::new, FeatureFlags.VANILLA_FEATURES));


    public static void registerScreenHandlers() {
        LivestreamMods.LOGGER.info("Registering Screen Hanlders " + LivestreamMods.MOD_ID);
    }
}
