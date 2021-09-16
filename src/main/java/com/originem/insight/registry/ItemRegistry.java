package com.originem.insight.registry;

import com.originem.insight.items.TotemOfIronGolemItem;
import com.originem.insight.items.tools.CustomAxeItem;
import com.originem.insight.items.tools.CustomHoeItem;
import com.originem.insight.items.tools.CustomPickaxeItem;
import com.originem.insight.items.tools.MoltenIngotItem;
import com.originem.insight.materials.MoltenIronToolMaterial;
import com.originem.insight.utils.ItemUtils;
import com.originem.insight.utils.References;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.originem.insight.InsightMod.GROUP_GENERAL;


public class ItemRegistry {
    public static final Item INSIGHT_MEDIA = new Item(new FabricItemSettings().group(GROUP_GENERAL));
    public static final Item TOTEM_OF_IRON_GOLEM = new TotemOfIronGolemItem(new FabricItemSettings().maxCount(1).group(GROUP_GENERAL));
    public static final Item COMPRESSED_COAL = new Item(new FabricItemSettings().group(GROUP_GENERAL));
    public static final Item RAW_SIC_CRYSTAL = new Item(new FabricItemSettings().group(GROUP_GENERAL));
    public static final Item SIC_CRYSTAL = new Item(new FabricItemSettings().group(GROUP_GENERAL));
    public static final Item LAVA_MELTED_INGOT = new Item(new FabricItemSettings().group(GROUP_GENERAL));
    public static final Item RAW_MOLTEN_IRON = new Item(new FabricItemSettings().group(GROUP_GENERAL));
    public static final Item MOLTEN_IRON_INGOT = new MoltenIngotItem(new FabricItemSettings().group(GROUP_GENERAL));

    public static final ToolItem MOLTEN_IRON_PICKAXE = new CustomPickaxeItem(MoltenIronToolMaterial.INSTANCE, 1, -2.8F, (new FabricItemSettings()).fireproof().group(GROUP_GENERAL));
    public static final ToolItem MOLTEN_IRON_HOE = new CustomHoeItem(MoltenIronToolMaterial.INSTANCE, -2, -1.0F, (new FabricItemSettings()).fireproof().group(GROUP_GENERAL));
    public static final ToolItem MOLTEN_IRON_AXE = new CustomAxeItem(MoltenIronToolMaterial.INSTANCE, 6.0F, -3.1F, (new FabricItemSettings()).fireproof().group(GROUP_GENERAL));
    public static final ToolItem MOLTEN_IRON_SHOVEL = new ShovelItem(MoltenIronToolMaterial.INSTANCE, 1.5F, -3.0F, (new FabricItemSettings()).fireproof().group(GROUP_GENERAL));
    public static final ToolItem MOLTEN_IRON_SWORD = new SwordItem(MoltenIronToolMaterial.INSTANCE, 3, -2.4F, (new FabricItemSettings()).fireproof().group(GROUP_GENERAL));

    public static void register() {
        Registry.register(Registry.ITEM, new Identifier(References.MOD_ID, "insight_media"), INSIGHT_MEDIA);
        Registry.register(Registry.ITEM, new Identifier(References.MOD_ID, "totem_of_iron_golem"), TOTEM_OF_IRON_GOLEM);
        Registry.register(Registry.ITEM, new Identifier(References.MOD_ID, "compressed_coal"), COMPRESSED_COAL);
        FuelRegistry.INSTANCE.add(COMPRESSED_COAL, ItemUtils.getFuelTime(20));
        Registry.register(Registry.ITEM, new Identifier(References.MOD_ID, "raw_sic_crystal"), RAW_SIC_CRYSTAL);
        Registry.register(Registry.ITEM, new Identifier(References.MOD_ID, "sic_crystal"), SIC_CRYSTAL);
        Registry.register(Registry.ITEM, new Identifier(References.MOD_ID, "lava_melted_ingot"), LAVA_MELTED_INGOT);
        FuelRegistry.INSTANCE.add(LAVA_MELTED_INGOT, ItemUtils.getFuelTime(64));
        Registry.register(Registry.ITEM, new Identifier(References.MOD_ID, "raw_molten_iron"), RAW_MOLTEN_IRON);
        Registry.register(Registry.ITEM, new Identifier(References.MOD_ID, "molten_iron_ingot"), MOLTEN_IRON_INGOT);

        Registry.register(Registry.ITEM, new Identifier(References.MOD_ID, "molten_iron_pickaxe"), MOLTEN_IRON_PICKAXE);
        Registry.register(Registry.ITEM, new Identifier(References.MOD_ID, "molten_iron_hoe"), MOLTEN_IRON_HOE);
        Registry.register(Registry.ITEM, new Identifier(References.MOD_ID, "molten_iron_axe"), MOLTEN_IRON_AXE);
        Registry.register(Registry.ITEM, new Identifier(References.MOD_ID, "molten_iron_shovel"), MOLTEN_IRON_SHOVEL);
        Registry.register(Registry.ITEM, new Identifier(References.MOD_ID, "molten_iron_sword"), MOLTEN_IRON_SWORD);
    }
}
