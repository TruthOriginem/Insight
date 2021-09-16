package com.originem.insight.utils.structure;

public class SlotPluginStructure {
    private int normalInventorySize = 0;
    private int pluginInventorySize = 3;

    public SlotPluginStructure(int normalInventorySize, int pluginInventorySize) {
        this.normalInventorySize = normalInventorySize;
        this.pluginInventorySize = pluginInventorySize;
    }

    public int getNormalSlotIndex(int i) {
        return i;
    }

    public int getPluginSlotIndex(int i) {
        return i + normalInventorySize;
    }

    public int getInventorySize() {
        return normalInventorySize + pluginInventorySize;
    }

    public boolean isPluginSlot(int slot) {
        return slot >= normalInventorySize;
    }
}