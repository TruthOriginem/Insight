package com.originem.insight.utils;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemUtils {
    public static boolean addStack(Inventory inventory, int slotID, int amount, Item item) {
        ItemStack added = inventory.getStack(slotID);
        if (added.isEmpty()) {
            inventory.setStack(slotID, new ItemStack(item, amount));
            return true;
        } else if (added.isOf(item)) {
            if (isStackAddable(added)) {
                added.increment(amount);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isStackAddable(ItemStack stack) {
        return isStackAddable(stack, 0);
    }

    public static boolean isStackAddable(ItemStack stack, int addAmount) {
        return stack.getCount() + addAmount < stack.getMaxCount();
    }

    public static void tryTransferItemBetweenInventory(Inventory fromInventory, int fromSlot, Inventory toInventory, int toSlot, int amount, boolean ignoreTotalMatch) {
        ItemStack from = fromInventory.getStack(fromSlot);
        ItemStack to = toInventory.getStack(toSlot);
        if (to.isItemEqual(from)) {
            if (ignoreTotalMatch) {
                amount = Math.min(amount, to.getMaxCount() - to.getCount());
                if (amount > 0) {
                    to.increment(amount);
                    from.decrement(amount);
                }
            } else {
                if (isStackAddable(to, amount)) {
                    to.increment(amount);
                    from.decrement(amount);
                }
            }
        } else if (to.isEmpty()) {
            toInventory.setStack(toSlot, new ItemStack(from.getItem()));
            from.decrement(amount);
        }
    }

    public static int getFuelTime(int itemAmount) {
        return References.BURN_TIME_FOR_EACH_ITEM * itemAmount;
    }
}
