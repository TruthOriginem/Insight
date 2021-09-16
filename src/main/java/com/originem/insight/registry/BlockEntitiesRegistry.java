package com.originem.insight.registry;

import com.originem.insight.blockentities.BlockEntityObsidianReactor;
import com.originem.insight.blockentities.BlockEntityRawIronReactor;
import com.originem.insight.blockentities.BlockEntityStoneReactor;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder.Factory;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

/**
 * Must be called after {@link BlockRegistry}
 */
public class BlockEntitiesRegistry {
    public static final BlockEntityType<BlockEntityStoneReactor> BLOCK_ENTITY_STONE_REACTOR =
            registerBlockEntity(BlockEntityStoneReactor::new, BlockRegistry.BLOCK_STONE_REACTOR);
    public static final BlockEntityType<BlockEntityRawIronReactor> BLOCK_ENTITY_RAW_IRON_REACTOR =
            registerBlockEntity(BlockEntityRawIronReactor::new, BlockRegistry.BLOCK_RAW_IRON_REACTOR);
    public static final BlockEntityType<BlockEntityObsidianReactor> BLOCK_ENTITY_OBSIDIAN_REACTOR =
            registerBlockEntity(BlockEntityObsidianReactor::new, BlockRegistry.BLOCK_OBSIDIAN_REACTOR);

    static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(Factory<T> factory, Block block) {
        BlockEntityType<T> build = FabricBlockEntityTypeBuilder.create(factory, block).build(null);
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, BlockRegistry.getIdentifier(block), build);
    }

    /**
     *
     */
    public static void register() {
    }
}
