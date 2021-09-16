package com.originem.insight.gui.slots;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.NotNull;

public class LimitItemSlot extends Slot {
    private final Item[] items;

    public LimitItemSlot(Inventory inventory, int index, int x, int y, @NotNull Item... items) {
        super(inventory, index, x, y);
        this.items = items;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        for (Item item : items) {
            if (stack.isOf(item)) {
                return true;
            }
        }
        return false;
    }
}
