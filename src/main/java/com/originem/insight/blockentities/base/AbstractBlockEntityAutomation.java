package com.originem.insight.blockentities.base;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import static com.originem.insight.blocks.base.AbstractBlockAutomation.WORKING;

public abstract class AbstractBlockEntityAutomation extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory, SidedInventory {
    protected static final String NBT_KEY_VERTICAL_TRANSFER_CD = "TransferCoolDown";
    protected DefaultedList<ItemStack> inventory;

    public AbstractBlockEntityAutomation(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        int[] result = new int[getItems().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
        return result;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
        return nbt;
    }

    public abstract boolean isWorking();

    protected static void syncWorkingState(World world, BlockPos pos, BlockState state, boolean working) {
        if (state.get(WORKING) != working) {
            state = state.with(WORKING, working);
            world.setBlockState(pos, state);
            markDirty(world, pos, state);
        }
    }
}
