package com.originem.insight.mixin;

import com.originem.insight.materials.MoltenIronToolMaterial;
import com.originem.insight.registry.PacketRegistry;
import com.originem.insight.utils.PacketUtils;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    ItemEntity itemEntity = (ItemEntity) (Object) this;

    @Shadow
    private int itemAge;


    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        final World world = itemEntity.world;
        if (!world.isClient && itemEntity.age % 20 == 0) {
            ItemStack stack = itemEntity.getStack();
            if (stack.getItem() instanceof ToolItem toolItem) {
                // 如果是熔铁材料
                if (toolItem.getMaterial() instanceof MoltenIronToolMaterial) {
                    Random random = world.random;

                    if (itemEntity.isInLava()) {
                        //每0.5秒修复1点耐久
                        stack.setDamage(stack.getDamage() - 1);
                        itemAge = 0;
                    }
                    BlockPos pos = itemEntity.getBlockPos();
                    BlockState blockState = world.getBlockState(pos);
                    if (blockState.isAir() && world.getRandom().nextFloat() < 0.25f) {
                        world.setBlockState(pos, Blocks.FIRE.getDefaultState());
                    }
                    // 升级耐久3至4
                    if (stack.hasEnchantments()) {
                        Identifier identifier = EnchantmentHelper.getEnchantmentId(Enchantments.UNBREAKING);
                        NbtList nbtList = stack.getEnchantments();

                        for (int i = 0; i < nbtList.size(); ++i) {
                            NbtCompound nbtCompound = nbtList.getCompound(i);
                            Identifier identifier2 = EnchantmentHelper.getIdFromNbt(nbtCompound);
                            if (identifier2 != null && identifier2.equals(identifier)) {
                                int level = EnchantmentHelper.getLevelFromNbt(nbtCompound);
                                if (level == 3) {
                                    EnchantmentHelper.writeLevelToNbt(nbtCompound, 4);

                                    NbtCompound compound = new NbtCompound();
                                    final double x = itemEntity.getX();
                                    final double y = itemEntity.getY();
                                    final double z = itemEntity.getZ();
                                    compound.putDouble("x", x);
                                    compound.putDouble("y", y);
                                    compound.putDouble("z", z);
                                    PacketByteBuf buf = PacketByteBufs.create();
                                    buf.writeNbt(compound);
                                    PacketUtils.sendPacketToAllTrackingClient(world, pos, buf, PacketRegistry.MOLTEN_UPGRADE_PARTICLE_PACKET_ID);
                                    world.playSound(null, x, y, z, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS,
                                                    1f, world.random.nextFloat() * 0.1F + 0.9F);

                                }
                                break;
                            }
                        }
                    }

                }
            }
        }

    }
}
