package com.originem.insight.utils;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PacketUtils {

    public static void sendPacketToAllTrackingClient(World world, BlockPos pos, PacketByteBuf buf, Identifier identifier) {
        for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, pos)) {
            ServerPlayNetworking.send(player, identifier, buf);
        }
    }
}
