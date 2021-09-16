package com.originem.insight;

import com.originem.insight.registry.BlockEntitiesRegistry;
import com.originem.insight.registry.BlockRegistry;
import com.originem.insight.registry.GUIRegistry;
import com.originem.insight.registry.ItemRegistry;
import com.originem.insight.utils.References;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class InsightMod implements ModInitializer {
    public static final ItemGroup GROUP_GENERAL = FabricItemGroupBuilder.build(
            new Identifier(References.MOD_ID, "general"),
            () -> new ItemStack(ItemRegistry.INSIGHT_MEDIA));

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

//        System.out.println("Hello Fabric world!");
        ItemRegistry.register();
        BlockRegistry.register();
        BlockEntitiesRegistry.register();
        GUIRegistry.register();
    }
}
