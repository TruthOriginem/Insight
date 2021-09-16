package com.originem.insight.items;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class TotemOfIronGolemItem extends Item {
    public TotemOfIronGolemItem(Settings settings) {
        super(settings);
    }

    //    @Override
//    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//        ItemStack itemStack = user.getStackInHand(hand);
//        user.incrementStat(Stats.USED.getOrCreateStat(this));
//        user.getItemCooldownManager().set(this, 200);
//        if (!world.isClient) {
//            LivingEntity golem = EntityType.IRON_GOLEM.create(world,null,null,user,)
//        }
//        return TypedActionResult.success(itemStack);
//    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (world instanceof ServerWorld) {
            ItemStack itemStack = context.getStack();
            BlockPos blockPos = context.getBlockPos();
            Direction direction = context.getSide();
            BlockState blockState = world.getBlockState(blockPos);

            BlockPos blockPos3;
            if (blockState.getCollisionShape(world, blockPos).isEmpty()) {
                blockPos3 = blockPos;
            } else {
                blockPos3 = blockPos.offset(direction);
            }
            MobEntity entity = EntityType.IRON_GOLEM.spawn((ServerWorld) world, null, null, context.getPlayer(), blockPos3, SpawnReason.MOB_SUMMONED, true, blockPos != blockPos3 && direction == Direction.UP);
            if (entity != null) {
                modifyGolem(entity);
                itemStack.decrement(1);
                world.emitGameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);
            }

        }
        return ActionResult.CONSUME;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        BlockHitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
        if (hitResult.getType() != HitResult.Type.BLOCK) {
            return TypedActionResult.pass(itemStack);
        } else if (!(world instanceof ServerWorld)) {
            return TypedActionResult.success(itemStack);
        } else {
            BlockPos blockPos = hitResult.getBlockPos();
            if (!(world.getBlockState(blockPos).getBlock() instanceof FluidBlock)) {
                return TypedActionResult.pass(itemStack);
            } else if (world.canPlayerModifyAt(user, blockPos) && user.canPlaceOn(blockPos, hitResult.getSide(), itemStack)) {
                MobEntity entity = EntityType.IRON_GOLEM.spawn((ServerWorld) world, null, null, user, blockPos, SpawnReason.MOB_SUMMONED, false, false);
                if (entity == null) {
                    return TypedActionResult.pass(itemStack);
                } else {
                    modifyGolem(entity);
                    if (!user.isCreative())
                        itemStack.decrement(1);
                    user.incrementStat(Stats.USED.getOrCreateStat(this));
                    world.emitGameEvent(GameEvent.ENTITY_PLACE, user);
                    return TypedActionResult.consume(itemStack);
                }
            } else {
                return TypedActionResult.fail(itemStack);
            }
        }
    }

    private void modifyGolem(MobEntity entity) {
        EntityAttributeInstance maxHealthAttribute = entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (maxHealthAttribute != null) {
            maxHealthAttribute.setBaseValue(maxHealthAttribute.getBaseValue() * 0.6);
            entity.setHealth(entity.getMaxHealth());
        }
    }
}
