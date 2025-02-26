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
import org.desp.decoration.database.ItemDataRepository;
import org.desp.decoration.database.UserDataRepository;
import org.desp.decoration.dto.ItemDataDto;
import org.desp.decoration.dto.UserDataDto;
import org.desp.decoration.listener.DecorationListener;
import org.jetbrains.annotations.NotNull;

public class PlayerDerocationItemListGUI implements InventoryHolder {

    public Inventory inventory;
    public Player player;

    public PlayerDerocationItemListGUI(Player player) {
        this.player = player;
        this.inventory = Bukkit.createInventory(this, 54, player.getName()+"님의 치장 창고");
        setUpInventory();
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    private void setUpInventory() {
        ItemDataRepository itemDataRepository = ItemDataRepository.getInstance();
        Map<String, ItemDataDto> itemData = itemDataRepository.getItemData();

        Map<String, UserDataDto> playerCache = DecorationListener.getPlayerCache();
        UserDataDto userDataDto = playerCache.get(player.getUniqueId().toString());

        System.out.println("userDataDto.getUser_id() = " + userDataDto.getUser_id());
        int slot = 0;
        List<String> unlockedDeco = userDataDto.getUnlockedDeco();
        System.out.println("unlockedDeco.size() = " + unlockedDeco.size());
        for (String id : unlockedDeco) {
            List<String> lore = new ArrayList<>();

            String materialName = itemDataRepository.getItemData().get(id).getMaterial();
            int customModelData = itemDataRepository.getItemData().get(id).getCustomModelData();
            ItemStack item = new ItemStack(Material.valueOf(materialName), 1);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setCustomModelData(customModelData);
            itemMeta.setDisplayName(id);
            lore.add("§b§m                                                ");
            lore.add(" §7 장착하기 (클릭)");
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);
            inventory.setItem(slot, item);
            System.out.println(slot + "= slot");
            slot++;
            lore.clear();


        }


//        for (Entry<String, ItemDataDto> stringItemDataDtoEntry : itemData.entrySet()) {
//            ItemDataDto itemDto = stringItemDataDtoEntry.getValue();
//            List<String> lore = new ArrayList<>();
//
//            ItemStack item = new ItemStack(Material.valueOf(itemDto.getMaterial()));
//            ItemMeta itemMeta = item.getItemMeta();
//            itemMeta.setCustomModelData(itemDto.getCustomModelData());
//            itemMeta.setDisplayName(itemDto.getDecoID());
//            lore.add("§b§m                                                ");
//            lore.add(" &7 장착하기 (클릭)");
//            itemMeta.setLore(lore);
//            item.setItemMeta(itemMeta);
//            inventory.setItem(slot, item);
//            slot++;
//            lore.clear();
//        }
    }

}
