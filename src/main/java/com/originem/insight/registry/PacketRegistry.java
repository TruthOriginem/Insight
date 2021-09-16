package com.originem.insight.registry;

import com.originem.insight.utils.References;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;

import java.util.Random;

public class PacketRegistry {
    public static final Identifier MOLTEN_UPGRADE_PARTICLE_PACKET_ID = new Identifier(References.MOD_ID, "molten_upgrade");

    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(MOLTEN_UPGRADE_PARTICLE_PACKET_ID,
                                                    (MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) -> {
                                                        NbtCompound compound = buf.readNbt();
                                                        if (compound != null && client.world != null) {
                                                            double x = compound.getDouble("x");
                                                            double y = compound.getDouble("y");
                                                            double z = compound.getDouble("z");
                                                            client.execute(() -> {
                                                                Random random = client.world.random;
                                                                for (int j = 0; j < random.nextInt(10) + 8; j++) {
                                                                    client.world.addParticle(ParticleTypes.FLAME, x, y, z, (random.nextFloat() - 0.5f) * 0.1f, (random.nextFloat()) * 0.4f, (random.nextFloat() - 0.5f) * 0.1f);
                                                                    client.world.addParticle(ParticleTypes.SMOKE, x, y, z, (random.nextFloat() - 0.5f) * 0.1f, (random.nextFloat()) * 0.2f, (random.nextFloat() - 0.5f) * 0.1f);
                                                                }
                                                            });
                                                        }
                                                    });
    }
}
