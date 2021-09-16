package com.originem.insight.blockentities;

import com.originem.insight.blockentities.base.AbstractBlockEntityAutomation;
import com.originem.insight.gui.ObsidianReactorScreenHandler;
import com.originem.insight.registry.BlockEntitiesRegistry;
import com.originem.insight.utils.ItemUtils;
import com.originem.insight.utils.MathUtils;
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

import static com.originem.insight.utils.ItemUtils.isStackAddable;

public class BlockEntityObsidianReactor extends AbstractBlockEntityAutomation {
    public static final short OBSIDIAN_GENERATE_TIME = 1200;
    public static final short VERTICAL_TRANSFER_TIME = 5;
    private final PropertyDelegate propertyDelegate;
    private short generateTime = OBSIDIAN_GENERATE_TIME;
    private short verticalTransferCoolDown = VERTICAL_TRANSFER_TIME;

    public BlockEntityObsidianReactor(BlockPos pos, BlockState state) {
        super(BlockEntitiesRegistry.BLOCK_ENTITY_OBSIDIAN_REACTOR, pos, state);
        this.inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
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
        return new ObsidianReactorScreenHandler(syncId, inv, this, propertyDelegate);
    }


    public static void serverTick(World world, BlockPos pos, BlockState state, BlockEntityObsidianReactor reactor) {
        boolean working = reactor.isWorking();
        if (working) {
            reactor.generateTime--;
            if (reactor.generateTime <= 0) {
                reactor.generateTime = OBSIDIAN_GENERATE_TIME;
                if (ItemUtils.addStack(reactor, 1, 1, Items.OBSIDIAN)) {
                    playGenerateSound(world, pos);
                    markDirty(world, pos, state);
                }
            }
        } else {
            reactor.generateTime = OBSIDIAN_GENERATE_TIME;
        }
        ItemStack itemStack = reactor.getStack(1);
        // 向下传输
        if (!itemStack.isEmpty()) {
            reactor.verticalTransferCoolDown--;
            if (reactor.verticalTransferCoolDown <= 0) {
                reactor.verticalTransferCoolDown = VERTICAL_TRANSFER_TIME;
                BlockEntity blockEntity = world.getBlockEntity(pos.down());
                if (blockEntity instanceof BlockEntityObsidianReactor otherReactor) {
                    ItemUtils.tryTransferItemBetweenInventory(reactor, 1, otherReactor, 1, 1, true);
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
            return stack.isOf(Items.WATER_BUCKET);
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
        if (!isStackAddable(getStack(1))) return false;
        return inventory.get(0).isOf(Items.WATER_BUCKET);
    }


    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return slot != 1;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot == 1;
    }
}
