package com.originem.insight.registry;

import com.originem.insight.render.BlockEntityRawIronReactorRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;

public class BlockEntitiesRendererRegistry {
    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        BlockEntityRendererRegistry.INSTANCE.register(BlockEntitiesRegistry.BLOCK_ENTITY_RAW_IRON_REACTOR,
                                                      BlockEntityRawIronReactorRenderer::new);
    }
}
