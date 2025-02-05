package de.miskynet.arsenic.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryManager {

    // Add items to the inventory
    public static Inventory addItems(Inventory inventory) {

        for (String key : CustomConfig.get("pages").getConfigurationSection("items").getKeys(false)) {

            // Get the item stack and the slot
            ItemStack itemStack = ItemUtil.modifyItemStack(key, true, true, true, true);
            int slot = CustomConfig.get("pages").getInt("items." + key + ".slot");

            try {

                inventory.setItem(slot, ItemUtil.modifyItemStack(key, true, true, true, true));
            }catch (NullPointerException e) {
                inventory.addItem(itemStack);
            }
        }
        return inventory;
    }
}
