package com.originem.insight.client;

import com.originem.insight.registry.BlockEntitiesRendererRegistry;
import com.originem.insight.registry.GUIRegistry;
import com.originem.insight.registry.PacketRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.util.registry.Registry;

public class InsightClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        GUIRegistry.registerClient();
        PacketRegistry.registerClient();
        BlockEntitiesRendererRegistry.registerClient();
        registerRenderLayers();
    }

    private void registerRenderLayers() {
        Registry.BLOCK.forEach(block -> {
            if (block instanceof IBlockHasRenderLayer layerBlock) {
                BlockRenderLayerMap.INSTANCE.putBlock(block, layerBlock.getRenderLayer());
            }
        });
    }
}
