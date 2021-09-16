package com.originem.insight.render;

import com.originem.insight.blockentities.BlockEntityRawIronReactor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class BlockEntityRawIronReactorRenderer implements BlockEntityRenderer<BlockEntityRawIronReactor> {
    public static final float SCALE = 0.12f;

    public BlockEntityRawIronReactorRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(BlockEntityRawIronReactor entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        World world = entity.getWorld();
        Entity golem = entity.getClientRenderedEntity(world);
        if (world != null && golem != null) {
            matrices.push();
            matrices.translate(0.5D, 0.35D, 0.5D);
            matrices.scale(SCALE, SCALE, SCALE);
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((world.getTime() + tickDelta) * 4));
            MinecraftClient.getInstance().getEntityRenderDispatcher().render(golem, 0, 0, 0, 0, 0f, matrices, vertexConsumers, light);
            matrices.pop();
        }
    }
}
