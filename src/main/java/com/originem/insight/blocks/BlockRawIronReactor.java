package com.originem.insight.blocks;

import com.originem.insight.blockentities.BlockEntityRawIronReactor;
import com.originem.insight.blocks.base.AbstractBlockAutomation;
import com.originem.insight.client.IBlockHasRenderLayer;
import com.originem.insight.registry.BlockEntitiesRegistry;
import com.originem.insight.utils.MathUtils;
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
import net.minecraft.client.render.RenderLayer;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BlockRawIronReactor extends AbstractBlockAutomation implements IBlockHasRenderLayer {
    private static final VoxelShape SHAPE = VoxelShapes.union(
            Block.createCuboidShape(0, 0, 0, 16, 5, 16),
            Block.createCuboidShape(1, 5, 1, 15, 11, 15),
            Block.createCuboidShape(0, 11, 0, 16, 16, 16));

    public BlockRawIronReactor(Settings settings) {
        super(settings);
    }


    public BlockRawIronReactor() {
        this(FabricBlockSettings.of(Material.METAL)
                     .requiresTool().breakByTool(FabricToolTags.PICKAXES, MiningLevels.IRON)
                     .strength(4f, 15f).luminance(AbstractBlockAutomation.getLuminance()));
        this.setDefaultState(getStateManager().getDefaultState().with(WORKING, false));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(WORKING)) {
            if (random.nextInt(200) == 0)
                world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
            if (random.nextInt(2) == 0)
                world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.3 + random.nextDouble() * 0.4, pos.getY() + 0.35, pos.getZ() + 0.3 + random.nextDouble() * 0.4, 0.0D, 0.0D, 0.0D);
            if (random.nextInt(2) == 0)
                world.addParticle(ParticleTypes.FLAME,
                                  pos.getX() + MathUtils.getRandomNumberInRange(random, 0.3f, 0.7f),
                                  pos.getY() + 0.35,
                                  pos.getZ() + MathUtils.getRandomNumberInRange(random, 0.3f, 0.7f),
                                  MathUtils.getRandomNumberInRange(random, -0.05f, 0.05f),
                                  MathUtils.getRandomNumberInRange(random, 0.01f, 0.05f),
                                  MathUtils.getRandomNumberInRange(random, -0.05f, 0.05f));
        }
    }


    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityRawIronReactor(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, BlockEntitiesRegistry.BLOCK_ENTITY_RAW_IRON_REACTOR, BlockEntityRawIronReactor::serverTick);
    }

    @Override
    public RenderLayer getRenderLayer() {
        return RenderLayer.getCutout();
    }
}
