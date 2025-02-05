package de.miskynet.arsenic.listener;

import de.miskynet.arsenic.Main;
import de.miskynet.arsenic.utils.CustomConfig;
import de.miskynet.arsenic.utils.ItemUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Listener implements org.bukkit.event.Listener {

    @EventHandler
    public static void inventoryClick(org.bukkit.event.inventory.InventoryClickEvent event) {

        // Check if the item material is null
        if (event.getCurrentItem() == null) {
            return;
        }

        // Define the item material
        String itemMaterial = event.getCurrentItem().getType().toString();
        String title = event.getView().getTitle();

        // Check if the title is equal to the title in the config
        if (title.equals(Main.getInstance().getTitle())) {
            for (String key : CustomConfig.get("pages").getConfigurationSection("items").getKeys(false)) {
                if (itemMaterial.equals(CustomConfig.get("pages").getString("items." + key + ".material").toUpperCase())) {
                    if (event.isRightClick() && Main.getInstance().getConfig().getBoolean("enableStackBuy")) {
                        ItemStack itemStack = ItemUtil.modifyItemStack(key, true, false, true, true);

                        itemStack.setAmount(64);

                        event.getWhoClicked().getInventory().addItem(itemStack);
                    }else {
                        ItemStack itemStack = ItemUtil.modifyItemStack(key, true, false, true, true);

                        event.getWhoClicked().getInventory().addItem(itemStack);
                    }
                }
            }

            event.setCancelled(true);
        }
    }

}
