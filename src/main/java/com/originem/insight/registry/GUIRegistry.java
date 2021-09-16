package com.originem.insight.registry;

import com.originem.insight.gui.*;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public class GUIRegistry {
    public static final ScreenHandlerType<StoneReactorScreenHandler> STONE_REACTOR_SCREEN_HANDLER;
    public static final ScreenHandlerType<RawIronReactorScreenHandler> RAW_IRON_REACTOR_SCREEN_HANDLER;
    public static final ScreenHandlerType<ObsidianReactorScreenHandler> OBSIDIAN_REACTOR_SCREEN_HANDLER;

    static {
        STONE_REACTOR_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(BlockRegistry.getIdentifier(BlockRegistry.BLOCK_STONE_REACTOR), StoneReactorScreenHandler::new);
        RAW_IRON_REACTOR_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(BlockRegistry.getIdentifier(BlockRegistry.BLOCK_RAW_IRON_REACTOR), RawIronReactorScreenHandler::new);
        OBSIDIAN_REACTOR_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(BlockRegistry.getIdentifier(BlockRegistry.BLOCK_OBSIDIAN_REACTOR), ObsidianReactorScreenHandler::new);
    }

    public static void register() {
    }

    public static void registerClient() {
        ScreenRegistry.register(GUIRegistry.STONE_REACTOR_SCREEN_HANDLER, StoneReactorScreen::new);
        ScreenRegistry.register(GUIRegistry.RAW_IRON_REACTOR_SCREEN_HANDLER, BucketReactorScreen::new);
        ScreenRegistry.register(GUIRegistry.OBSIDIAN_REACTOR_SCREEN_HANDLER, BucketReactorScreen::new);
    }
}
