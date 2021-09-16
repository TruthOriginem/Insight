package com.originem.insight.blockentities;

import com.originem.insight.blockentities.base.AbstractBlockEntityAutomation;
import com.originem.insight.gui.StoneReactorScreenHandler;
import com.originem.insight.registry.BlockEntitiesRegistry;
import com.originem.insight.utils.ItemUtils;
import com.originem.insight.utils.MathUtils;
import com.originem.insight.utils.structure.SlotPluginStructure;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BlockEntityStoneReactor extends AbstractBlockEntityAutomation {
    public static final SlotPluginStructure SLOT_STRUCTURE = new SlotPluginStructure(3, 0);
    public static final short COBBLE_STONE_GENERATE_TIME = 60;
    public static final short VERTICAL_TRANSFER_TIME = 5;
    private final PropertyDelegate propertyDelegate;
    private short generateTime = COBBLE_STONE_GENERATE_TIME;
    private short verticalTransferCoolDown = VERTICAL_TRANSFER_TIME;


    public BlockEntityStoneReactor(BlockPos pos, BlockState state) {
        super(BlockEntitiesRegistry.BLOCK_ENTITY_STONE_REACTOR, pos, state);
        this.inventory = DefaultedList.ofSize(SLOT_STRUCTURE.getInventorySize(), ItemStack.EMPTY);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                if (index == 0) return generateTime;
                return 0;
            }

            @Override
            public void set(int index, int value) {
                if (index == 0) generateTime = (short) value;
            }

            @Override
            public int size() {
                return 1;
            }
        };
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new StoneReactorScreenHandler(syncId, inv, this, propertyDelegate);
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, BlockEntityStoneReactor stoneReactor) {
        boolean working = stoneReactor.isWorking();
        if (working) {
            stoneReactor.generateTime--;
            if (stoneReactor.generateTime <= 0) {
                stoneReactor.generateTime = COBBLE_STONE_GENERATE_TIME;
                if (ItemUtils.addStack(stoneReactor, 1, 1, Items.COBBLESTONE)) {
                    playGenerateSound(world, pos);
                    markDirty(world, pos, state);
                }
            }
        } else {
            stoneReactor.generateTime = COBBLE_STONE_GENERATE_TIME;
        }
        // 向下传输
        ItemStack itemStack = stoneReactor.getStack(1);
        if (!itemStack.isEmpty()) {
            stoneReactor.verticalTransferCoolDown--;
            if (stoneReactor.verticalTransferCoolDown <= 0) {
                stoneReactor.verticalTransferCoolDown = VERTICAL_TRANSFER_TIME;
                BlockEntity blockEntity = world.getBlockEntity(pos.down());
                if (blockEntity instanceof BlockEntityStoneReactor otherReactor) {
                    ItemUtils.tryTransferItemBetweenInventory(stoneReactor, 1, otherReactor, 1, 1, true);
                }
            }
        }

        syncWorkingState(world, pos, state, working);
    }

    private static void playGenerateSound(World world, BlockPos pos) {
        world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_LAVA_EXTINGUISH,
                        SoundCategory.BLOCKS, 0.2f, 2.6F + MathUtils.getRandomNumberInRange(-0.8f, 0.8f));
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        if (slot != 1) {
            if (SLOT_STRUCTURE.isPluginSlot(slot)) {
                return false;
            }
            return stack.isOf(Items.LAVA_BUCKET) || stack.isOf(Items.WATER_BUCKET);
        }
        return false;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.generateTime = nbt.getShort("GenerateTime");
        this.verticalTransferCoolDown = nbt.getShort(NBT_KEY_VERTICAL_TRANSFER_CD);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putShort("GenerateTime", generateTime);
        nbt.putShort(NBT_KEY_VERTICAL_TRANSFER_CD, verticalTransferCoolDown);
        return nbt;
    }

    @Override
    public boolean isWorking() {
        if (!ItemUtils.isStackAddable(getStack(1))) return false;
        boolean hasLavaBucket = false;
        boolean hasWaterBucket = false;
        ItemStack slot = inventory.get(0);
        if (slot.isOf(Items.LAVA_BUCKET)) {
            hasLavaBucket = true;
        } else if (slot.isOf(Items.WATER_BUCKET)) {
            hasWaterBucket = true;
        }
        slot = inventory.get(2);
        if (slot.isOf(Items.LAVA_BUCKET)) {
            hasLavaBucket = true;
        } else if (slot.isOf(Items.WATER_BUCKET)) {
            hasWaterBucket = true;
        }
        return hasWaterBucket && hasLavaBucket;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return slot != 1 && !SLOT_STRUCTURE.isPluginSlot(slot);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot == 1;
    }
}
