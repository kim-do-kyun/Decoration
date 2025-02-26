package org.desp.decoration.listener;

import static org.desp.decoration.database.UserDataRepository.insertDefaultUserData;

import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.StatInstance;
import io.lumine.mythic.lib.api.stat.modifier.StatModifier;
import io.lumine.mythic.lib.player.modifier.ModifierType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.desp.decoration.Decoration;
import org.desp.decoration.database.ItemDataRepository;
import org.desp.decoration.database.LevelDataRepository;
import org.desp.decoration.database.UserDataRepository;
import org.desp.decoration.dto.ItemDataDto;
import org.desp.decoration.dto.LevelDataDto;
import org.desp.decoration.dto.StatsDto;
import org.desp.decoration.dto.UserDataDto;
import org.desp.decoration.gui.DecoUpgradeGUI;
import org.desp.decoration.gui.PlayerDerocationItemListGUI;
import org.desp.decoration.gui.PurchaseItemGUI;
import org.jetbrains.annotations.Nullable;

public class DecorationListener implements Listener {

    public static UserDataRepository userDataRepository;
    public static ItemDataRepository itemDataRepository;
    public static LevelDataRepository levelDataRepository;
    @Getter
    public static Map<String, UserDataDto> playerCache = new HashMap<>();


    public DecorationListener() {
        userDataRepository = UserDataRepository.getInstance();
        itemDataRepository = ItemDataRepository.getInstance();
        levelDataRepository = LevelDataRepository.getInstance();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (!userDataRepository.isPlayerDataExist(player)) {
            List<String> base = new ArrayList<>();
            UserDataDto newUser = UserDataDto.builder()
                    .user_id(player.getName())
                    .uuid(playerUUID.toString())
                    .equippedDeco("")
                    .unlockedDeco(base)
                    .level(0)
                    .build();

            userDataRepository.insertUserData(newUser);
            userDataRepository = UserDataRepository.getInstance();

            playerCache.put(playerUUID.toString(), newUser);
        } else {
            userDataRepository.loadUserData(player);
        }

        applyStat(player);
        Map<String, ItemDataDto> itemData = itemDataRepository.getItemData();
        String equippedDeco = playerCache.get(playerUUID.toString()).getEquippedDeco();

        if (itemData.containsKey(equippedDeco)) {
            String material = itemData.get(equippedDeco).getMaterial();
            Integer modelCode = itemData.get(equippedDeco).getCustomModelData();

            Bukkit.getScheduler().runTaskLaterAsynchronously(Decoration.getInstance(), new Runnable() {
                @Override
                public void run() {
                    ItemStack item = new ItemStack(Material.valueOf(material));
                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.setCustomModelData(modelCode);
                    itemMeta.setDisplayName(equippedDeco);
                    item.setItemMeta(itemMeta);
                    player.getInventory().setHelmet(item);
                }
            }, 20L);
            System.out.println("아이템 장착 성공");
        }
    }

    public boolean isPlayerHasEnoughCoin(Player player, Integer cost) {
        int playerCoinAmount = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (MMOItems.getID(item).equals("기타_치장코인")) {
                playerCoinAmount += item.getAmount();
            }
        }
        return playerCoinAmount > cost;
    }

    public void consumeDecoCoin(Player player, Integer cost) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && MMOItems.getID(item).equals("기타_치장코인")) {
                int amount = item.getAmount();
                if (amount > cost) {
                    item.setAmount(amount - cost);
                } else {
                    cost -= amount;
                    item.setAmount(0);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof PurchaseItemGUI) {
            // 구매 아이템 클릭 로직
            Player player = (Player) e.getWhoClicked();
            String uuid = player.getUniqueId().toString();
            PlayerInventory inventory = player.getInventory();
            ItemStack[] contents = inventory.getContents();
            e.setCancelled(true);

            ItemStack currentItem = e.getCurrentItem();
            if (currentItem == null) {
                return;
            }
            String displayName = currentItem.getItemMeta().getDisplayName();
            String id = displayName.replace("§b  치장: §f", "");
            int cost = ItemDataRepository.getInstance().getItemData().get(id).getCost();
            if (!isPlayerHasEnoughCoin(player, cost)) {
                player.sendMessage("  §c코인이 부족합니다!");
                player.closeInventory();
            } else {
                consumeDecoCoin(player, cost);
                player.sendMessage("§a  " + id + " §f치장을 구매하셨습니다!");
                UserDataDto userDataDto = playerCache.get(uuid);
                List<String> unlockedDeco = userDataDto.getUnlockedDeco();
                unlockedDeco.add(id);
                playerCache.replace(uuid, userDataDto);
                player.closeInventory();
            }
        }
        // 치장 장착 리스너
        if (e.getInventory().getHolder() instanceof PlayerDerocationItemListGUI) {
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            ItemStack currentItem = e.getCurrentItem();
            if (currentItem == null) {
                return;
            }
            String displayName = currentItem.getItemMeta().getDisplayName();
            String id = displayName.replace("§b  치장: §f", "");
            ItemDataDto itemDataDto = itemDataRepository.getItemData().get(id);
            String materialName = itemDataDto.getMaterial();
            int customModelData = itemDataDto.getCustomModelData();
            Material material = Material.valueOf(materialName);
            ItemStack item = new ItemStack(material, 1);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName("§f" + id);
            itemMeta.setCustomModelData(customModelData);
            item.setItemMeta(itemMeta);
            player.getInventory().setHelmet(item);
            UserDataDto userDataDto = playerCache.get(player.getUniqueId().toString());
            userDataDto.setEquippedDeco(id);
            player.sendMessage("§a 성공적으로 §f" + id + " §a치장을 장착했습니다!");
        }
        //치장 강화 리스너
        if (e.getInventory().getHolder() instanceof DecoUpgradeGUI) {
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            ItemStack currentItem = e.getCurrentItem();
            UserDataDto userDataDto = playerCache.get(player.getUniqueId().toString());
            LevelDataDto levelDataDto = levelDataRepository.getLevelData()
                    .get(userDataDto.getLevel() + 1);
            if(levelDataDto == null){
                return;
            }
            int cost = levelDataDto.getCost();
            if (currentItem == null) {
                return;
            }

            if (e.getSlot() == 14) {
                if (!isPlayerHasEnoughCoin(player, cost)) {
                    player.sendMessage("  §c코인이 부족합니다!");
                } else {
                    consumeDecoCoin(player, cost);
                    userDataDto.setLevel(userDataDto.getLevel() + 1);
                    player.sendMessage("§a 성공적으로 §f" + userDataDto.getLevel() + " §f레벨§a로 강화되었습니다!");
                }
                player.closeInventory();
            }
            applyStat(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        userDataRepository.saveUserData(playerCache.get(e.getPlayer().getUniqueId().toString()));
    }

    public static void applyStat(Player player) {
        UUID playerUUID = player.getUniqueId();
        UserDataDto userDataDto = playerCache.get(playerUUID.toString());
        int playerDecoLevel = userDataDto.getLevel();
        Bukkit.getScheduler().runTaskLaterAsynchronously(Decoration.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (playerDecoLevel != 0) {
                    Map<Integer, LevelDataDto> levelData = levelDataRepository.getLevelData();
                    LevelDataDto levelDataDto = levelData.get(playerDecoLevel);
                    StatsDto stats = levelDataDto.getStats();
                    double hp = stats.getHp();
                    double mana = stats.getMana();
                    double speed = stats.getSpeed();
                    double criticalDamage = stats.getCriticalDamage();
                    double skillDMG = stats.getSkillDMG();
                    double criticalPercentage = stats.getCriticalPercentage();
                    MMOPlayerData mmoPlayerData = MMOPlayerData.get(playerUUID);

//                    statInstance.removeIf(key -> key.startsWith("mmoitems"));
                    if (hp != 0) {
                        StatModifier statModifier = new StatModifier("Decoration", "MAX_HEALTH", hp, ModifierType.FLAT);
                        StatInstance healthInstance = mmoPlayerData.getStatMap().getInstance("MAX_HEALTH");
                        healthInstance.removeIf(key -> key.startsWith("Decoration"));
                        statModifier.register(mmoPlayerData);
                    }
                    if (mana != 0) {
                        StatModifier statModifier = new StatModifier("Decoration", "MAX_MANA", mana, ModifierType.FLAT);
                        StatInstance manaInstance = mmoPlayerData.getStatMap().getInstance("MAX_MANA");
                        manaInstance.removeIf(key -> key.startsWith("Decoration"));
                        statModifier.register(mmoPlayerData);
                    }
                    if (speed != 0) {
                        StatModifier statModifier = new StatModifier("Decoration", "MOVEMENT_SPEED", speed,
                                ModifierType.FLAT);
                        StatInstance speedInstance = mmoPlayerData.getStatMap().getInstance("MOVEMENT_SPEED");
                        speedInstance.removeIf(key -> key.startsWith("Decoration"));
                        statModifier.register(mmoPlayerData);
                    }
                    if (criticalDamage != 0) {
                        StatModifier statModifier = new StatModifier("Decoration", "SKILL_CRITICAL_STRIKE_POWER",
                                criticalDamage, ModifierType.FLAT);
                        StatInstance criticalPowerInstance = mmoPlayerData.getStatMap()
                                .getInstance("SKILL_CRITICAL_STRIKE_POWER");
                        criticalPowerInstance.removeIf(key -> key.startsWith("Decoration"));
                        statModifier.register(mmoPlayerData);
                    }
                    if (skillDMG != 0) {
                        StatModifier statModifier = new StatModifier("Decoration", "SKILL_DAMAGE", skillDMG,
                                ModifierType.FLAT);
                        StatInstance skillDamageInstance = mmoPlayerData.getStatMap().getInstance("SKILL_DAMAGE");
                        skillDamageInstance.removeIf(key -> key.startsWith("Decoration"));
                        statModifier.register(mmoPlayerData);
                    }
                    if (criticalPercentage != 0) {
                        StatModifier statModifier = new StatModifier("Decoration", "SKILL_CRITICAL_STRIKE_CHANCE",
                                criticalPercentage, ModifierType.FLAT);
                        StatInstance criticalChanceInstance = mmoPlayerData.getStatMap()
                                .getInstance("SKILL_CRITICAL_STRIKE_CHANCE");
                        criticalChanceInstance.removeIf(key -> key.startsWith("Decoration"));
                        statModifier.register(mmoPlayerData);
                    }

                }
            }
        }, 20L);

    }
}
