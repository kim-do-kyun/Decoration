package org.desp.decoration.gui;

import static org.desp.decoration.listener.DecorationListener.userDataRepository;

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
import org.desp.decoration.database.ItemDataRepository;
import org.desp.decoration.database.UserDataRepository;
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
        System.out.println("PurchaseItemGUI.setUpInventory");
        //playerplayerItemDataRepository repository = ItemDataRepository.getInstance();
        Map<String, ItemDataDto> itemData = DecorationListener.itemDataRepository.getItemData();

//        userDataRepository = UserDataRepository.getInstance();
//        Map<String, UserDataDto> userData = userDataRepository.getUserData();
//        System.out.println("userData.get(player.getUniqueId().toString()).getUser_id() = " + userData.get(player.getUniqueId().toString()).getUser_id());

        Map<String, UserDataDto> playerCache = DecorationListener.getPlayerCache();
        System.out.println("PurchaseItemGUI.setUpInventory");
//        System.out.println("playerCache.size() = " + playerCache.size());
//        System.out.println("player.getName() = " + player.getName());
//        System.out.println("playerCache = " + playerCache.get(player.getUniqueId().toString()));
        System.out.println("playerCache.get(player.getUniqueId().toString()) = " + playerCache.get(player.getUniqueId().toString()));
        for (Entry<String, UserDataDto> stringUserDataDtoEntry : playerCache.entrySet()) {
            System.out.println("stringUserDataDtoEntry.getKey() = " + stringUserDataDtoEntry.getKey());
            System.out.println("stringUserDataDtoEntry.getValue().getUuid() = " + stringUserDataDtoEntry.getValue().getUuid());
        }

        int slot = 0;

        for (Entry<String, ItemDataDto> stringItemDataDtoEntry : itemData.entrySet()) {
            ItemDataDto itemDto = stringItemDataDtoEntry.getValue();
            String decoID = itemDto.getDecoID();
            System.out.println(playerCache.get(player.getUniqueId().toString()));
            if (playerCache.get(player.getUniqueId().toString()) == null) {
                System.out.println("jklasdfhjklasdjfklasdjlfkjasdlf");
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
