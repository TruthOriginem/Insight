package com.originem.insight.materials;

import com.originem.insight.registry.ItemRegistry;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class MoltenIronToolMaterial implements ToolMaterial {
    public static final MoltenIronToolMaterial INSTANCE = new MoltenIronToolMaterial();

    @Override
    public int getDurability() {
        return 512;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 7;
    }

    @Override
    public float getAttackDamage() {
        return 2.5f;
    }

    @Override
    public int getMiningLevel() {
        return 2;
    }

    @Override
    public int getEnchantability() {
        return 18;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(ItemRegistry.MOLTEN_IRON_INGOT);
    }
}
