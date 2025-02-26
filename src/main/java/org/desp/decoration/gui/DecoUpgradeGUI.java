package org.desp.decoration.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.desp.decoration.database.ItemDataRepository;
import org.desp.decoration.database.LevelDataRepository;
import org.desp.decoration.dto.ItemDataDto;
import org.desp.decoration.dto.LevelDataDto;
import org.desp.decoration.dto.StatsDto;
import org.desp.decoration.dto.UserDataDto;
import org.desp.decoration.listener.DecorationListener;
import org.jetbrains.annotations.NotNull;

public class DecoUpgradeGUI implements InventoryHolder {

    public Inventory inventory;
    public Player player;

    public DecoUpgradeGUI(Player player) {
        this.player = player;
        int level = DecorationListener.getPlayerCache().get(player.getUniqueId().toString()).getLevel();
        this.inventory = Bukkit.createInventory(this, 27, "치장 레벨 강화 -> " + (level + 1));
        setUpInventory(level + 1);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    private void setUpInventory(Integer level) {
        ItemStack itemStack = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(10267);
        itemMeta.setDisplayName("  §b강화하기");
        LevelDataDto levelDataDto = LevelDataRepository.getInstance().getLevelData().get(level);
        if (levelDataDto == null) {
            player.sendMessage(" §c최고 레벨에 도달하여 더 이상 강화할 수 없습니다!");
            ItemStack itemStack2 = new ItemStack(Material.PAPER);
            ItemMeta itemMeta2 = itemStack2.getItemMeta();
            itemMeta2.setCustomModelData(10254);
            itemMeta2.setDisplayName("  §7 내 정보");
            LevelDataDto levelDataDto2 = LevelDataRepository.getInstance().getLevelData().get(level - 1);
            StatsDto stats2 = levelDataDto2.getStats();
            List<String> upgradeLore2 = Arrays.asList(
                    "§f    강화 정보",
                    "§9§m                                                                        ",
                    "§e     내 치장 레벨: §f" + (level-1) + "레벨",
                    "§9§m                      §7내 치장 스텟 정보§9§m                          ",
                    "§c     체력: §f+" + stats2.getHp(),
                    "§7     공격력: §f" + stats2.getSkillDMG() + "%",
                    "§b     이동속도: §f+" + stats2.getSpeed() + "%",
                    "§3     최대 마나: §f+" + stats2.getMana() + "%",
                    "§e     크리티컬 확률: §f+" + stats2.getCriticalPercentage() + "%",
                    "§6     크리티컬 공격력: §f+" + stats2.getCriticalDamage() + "%"
            );
            itemMeta2.setLore(upgradeLore2);
            itemStack2.setItemMeta(itemMeta2);
            inventory.setItem(13, itemStack2);
            return;
        }
        StatsDto stats = levelDataDto.getStats();
        List<String> upgradeLore = Arrays.asList(
                "§f    강화 정보",
                "§9§m                                                                           ",
                "§e     다음 강화 레벨: §f" + level + "레벨",
                "§e     강화 필요 비용: §f치장 코인 " + levelDataDto.getCost() + "개",
                "§9§m                      §7다음 레벨 스텟 정보§9§m                          ",
                "§c     체력: §f+" + stats.getHp(),
                "§7     공격력: §f" + stats.getSkillDMG() + "%",
                "§b     이동속도: §f+" + stats.getSpeed() + "%",
                "§3     최대 마나: §f+" + stats.getMana() + "%",
                "§e     크리티컬 확률: §f+" + stats.getCriticalPercentage() + "%",
                "§6     크리티컬 공격력: §f+" + stats.getCriticalDamage() + "%"
        );
        itemMeta.setLore(upgradeLore);
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(14, itemStack);

        // 내정보 칸
        ItemStack itemStack2 = new ItemStack(Material.PAPER);
        ItemMeta itemMeta2 = itemStack2.getItemMeta();
        itemMeta2.setCustomModelData(10254);
        itemMeta2.setDisplayName("  §7 내 정보");
        LevelDataDto levelDataDto2 = LevelDataRepository.getInstance().getLevelData().get(level - 1);
        StatsDto stats2 = levelDataDto2.getStats();
        List<String> upgradeLore2 = Arrays.asList(
                "§f    강화 정보",
                "§9§m                                                                        ",
                "§e     다음 강화 레벨: §f" + level + "레벨",
                "§e     강화 필요 비용: §f치장 코인 " + levelDataDto.getCost() + "개",
                "§9§m                      §7내 치장 스텟 정보§9§m                          ",
                "§c     체력: §f+" + stats2.getHp(),
                "§7     공격력: §f" + stats2.getSkillDMG() + "%",
                "§b     이동속도: §f+" + stats2.getSpeed() + "%",
                "§3     최대 마나: §f+" + stats2.getMana() + "%",
                "§e     크리티컬 확률: §f+" + stats2.getCriticalPercentage() + "%",
                "§6     크리티컬 공격력: §f+" + stats2.getCriticalDamage() + "%"
        );
        itemMeta2.setLore(upgradeLore2);
        itemStack2.setItemMeta(itemMeta2);
        inventory.setItem(12, itemStack2);
    }

}
