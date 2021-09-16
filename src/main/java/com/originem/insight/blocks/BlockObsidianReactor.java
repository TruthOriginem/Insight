package com.originem.insight.blocks;

import com.originem.insight.blockentities.BlockEntityObsidianReactor;
import com.originem.insight.blocks.base.AbstractBlockAutomationWithDirection;
import com.originem.insight.registry.BlockEntitiesRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BlockObsidianReactor extends AbstractBlockAutomationWithDirection {

    public BlockObsidianReactor(Settings settings) {
        super(settings);
    }


    public BlockObsidianReactor() {
        this(FabricBlockSettings.copyOf(Blocks.OBSIDIAN)
                     .requiresTool().breakByTool(FabricToolTags.PICKAXES, MiningLevels.DIAMOND));
        this.setDefaultState(getStateManager().getDefaultState().with(WORKING, false).with(FACING, Direction.NORTH));
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(WORKING)) {
            if (random.nextInt(200) == 0)
                world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
        }
    }


    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityObsidianReactor(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, BlockEntitiesRegistry.BLOCK_ENTITY_OBSIDIAN_REACTOR, BlockEntityObsidianReactor::serverTick);
    }
}
