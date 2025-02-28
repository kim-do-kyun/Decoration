package org.desp.decoration.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.desp.decoration.dto.ItemDataDto;
import org.desp.decoration.dto.UserDataDto;
import org.desp.decoration.listener.DecorationListener;
import org.jetbrains.annotations.NotNull;

public class PurchaseItemGUI implements InventoryHolder {

    public Inventory inventory;

    public PurchaseItemGUI(String title, Player player) {
        this.inventory = Bukkit.createInventory(this, 54, title);
        setUpInventory(player);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    private void setUpInventory(Player player) {
        Map<String, ItemDataDto> itemData = DecorationListener.itemDataRepository.getItemData();

        Map<String, UserDataDto> playerCache = DecorationListener.getPlayerCache();
        int slot = 0;

        for (Entry<String, ItemDataDto> stringItemDataDtoEntry : itemData.entrySet()) {
            ItemDataDto itemDto = stringItemDataDtoEntry.getValue();
            String decoID = itemDto.getDecoID();
            System.out.println(playerCache.get(player.getUniqueId().toString()));
            if (playerCache.get(player.getUniqueId().toString()) == null) {
                continue;
            }
            if(playerCache.get(player.getUniqueId().toString()).getUnlockedDeco().contains(decoID)){
                continue;
            }
            List<String> lore = new ArrayList<>();

            ItemStack item = new ItemStack(Material.valueOf(itemDto.getMaterial()));
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setCustomModelData(itemDto.getCustomModelData());
            itemMeta.setDisplayName("§b  치장: §f"+itemDto.getDecoID());
            lore.add("§3§m                                                ");
            lore.add(" §3  가격: §f치장주화 "+itemDto.getCost()+"개");
            lore.add(" §3");
            lore.add(" §7  >        구매하기 (클릭)");
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);
            inventory.setItem(slot, item);
            slot++;
            lore.clear();
        }
    }

}
