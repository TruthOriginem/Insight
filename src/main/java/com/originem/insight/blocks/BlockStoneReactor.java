package com.originem.insight.blocks;

import com.originem.insight.blockentities.BlockEntityStoneReactor;
import com.originem.insight.blocks.base.AbstractBlockAutomation;
import com.originem.insight.registry.BlockEntitiesRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BlockStoneReactor extends AbstractBlockAutomation {
    private static final VoxelShape TOP_SHAPE = Block.createCuboidShape(1, 0, 1, 15, 16, 15);
    private static final VoxelShape SHAPE = VoxelShapes.union(
            Block.createCuboidShape(0, 0, 0, 16, 5, 16),
            Block.createCuboidShape(1, 5, 1, 15, 16, 15));
    public static final BooleanProperty TOP = BooleanProperty.of("top");

    public BlockStoneReactor(Settings settings) {
        super(settings);
    }

    public BlockStoneReactor() {
        this(FabricBlockSettings.of(Material.STONE)
                     .requiresTool().breakByTool(FabricToolTags.PICKAXES, MiningLevels.STONE)
                     .strength(2f, 6f)
                     .luminance(getLuminance()));
        this.setDefaultState(getStateManager().getDefaultState().with(WORKING, false).with(TOP, false));
    }


    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(TOP)) return TOP_SHAPE;
        return SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(TOP);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (world.isClient())
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        BlockState downState = world.getBlockState(pos.down());
        return state.with(TOP, downState.isOf(state.getBlock()));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        BlockState downState = world.getBlockState(pos.down());
        world.setBlockState(pos, state.with(TOP, downState.isOf(state.getBlock())));
        super.onPlaced(world, pos, state, placer, itemStack);
    }


    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(WORKING)) {
            if (random.nextInt(24) == 0)
                world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
            if (random.nextInt(4) == 0)
                world.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + random.nextDouble(), pos.getY() + 0.5, pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityStoneReactor(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, BlockEntitiesRegistry.BLOCK_ENTITY_STONE_REACTOR, BlockEntityStoneReactor::serverTick);
    }
}
