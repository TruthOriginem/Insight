package com.originem.insight.registry;

import com.originem.insight.InsightMod;
import com.originem.insight.blocks.BlockObsidianReactor;
import com.originem.insight.blocks.BlockRawIronReactor;
import com.originem.insight.blocks.BlockStoneReactor;
import com.originem.insight.utils.ItemUtils;
import com.originem.insight.utils.References;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.IdentityHashMap;
import java.util.Map;

public class BlockRegistry {
    public static final Block BLOCK_LAVA_MELTED = new Block(FabricBlockSettings.of(Material.STONE).requiresTool().ticksRandomly().strength(1F).breakByTool(FabricToolTags.PICKAXES, MiningLevels.STONE).luminance(15));
    public static final Block BLOCK_RAW_INSIGHT = new Block(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, MiningLevels.STONE).strength(3.0F, 6.0F).sounds(BlockSoundGroup.COPPER));
    public static final Block BLOCK_OBSIDIAN_INSIGHT = new Block(FabricBlockSettings.copyOf(Blocks.OBSIDIAN).requiresTool().breakByTool(FabricToolTags.PICKAXES, MiningLevels.DIAMOND));
    public static final Block BLOCK_STONE_REACTOR = new BlockStoneReactor();
    public static final Block BLOCK_RAW_IRON_REACTOR = new BlockRawIronReactor();
    public static final Block BLOCK_OBSIDIAN_REACTOR = new BlockObsidianReactor();

    private static final Map<Block, Identifier> BLOCK_IDENTIFIER_MAP = new IdentityHashMap<>();

    static {
        // Normal Block
        registerBlock(new Identifier(References.MOD_ID, "lava_melted_block"), BLOCK_LAVA_MELTED, InsightMod.GROUP_GENERAL);
        FuelRegistry.INSTANCE.add(BLOCK_LAVA_MELTED, ItemUtils.getFuelTime(64 * 5));
        registerBlock(new Identifier(References.MOD_ID, "raw_insight_block"), BLOCK_RAW_INSIGHT, InsightMod.GROUP_GENERAL);
        registerBlock(new Identifier(References.MOD_ID, "obsidian_insight_block"), BLOCK_OBSIDIAN_INSIGHT, InsightMod.GROUP_GENERAL);
        // Machine
        registerBlock(new Identifier(References.MOD_ID, "stone_reactor"), BLOCK_STONE_REACTOR, InsightMod.GROUP_GENERAL);
        registerBlock(new Identifier(References.MOD_ID, "raw_iron_reactor"), BLOCK_RAW_IRON_REACTOR, InsightMod.GROUP_GENERAL);
        registerBlock(new Identifier(References.MOD_ID, "obsidian_reactor"), BLOCK_OBSIDIAN_REACTOR, InsightMod.GROUP_GENERAL);
    }

    public static Identifier getIdentifier(Block block) {
        Identifier identifier = BLOCK_IDENTIFIER_MAP.get(block);
        if (identifier == null) {
            identifier = Registry.BLOCK.getId(block);
        }
        return identifier;
    }


    public static void register() {
        // load
    }

    public static void registerBlock(Identifier identifier, Block block, ItemGroup group) {
        BLOCK_IDENTIFIER_MAP.put(block, identifier);
        Registry.register(Registry.BLOCK, identifier, block);
        Registry.register(Registry.ITEM, identifier, new BlockItem(block, new Item.Settings().group(group)));
    }
}
