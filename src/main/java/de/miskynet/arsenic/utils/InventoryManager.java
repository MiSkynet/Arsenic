package de.miskynet.arsenic.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryManager {

    // Add items to the inventory
    public static Inventory addItems(Inventory inventory) {

        for (String key : CustomConfig.get("pages").getConfigurationSection("items").getKeys(false)) {
            ItemStack itemStack = ItemUtil.modifyItemStack(key);
            inventory.addItem(itemStack);
        }

        return inventory;
    }



}
