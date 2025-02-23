package org.desp.decoration.listener;

import static org.desp.decoration.utils.DecorationUtils.insertDefaultUserData;

import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.modifier.StatModifier;
import io.lumine.mythic.lib.player.modifier.ModifierType;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.desp.decoration.database.ItemDataRepository;
import org.desp.decoration.database.LevelDataRepository;
import org.desp.decoration.database.UserDataRepository;
import org.desp.decoration.dto.ItemDataDto;
import org.desp.decoration.dto.LevelDataDto;
import org.desp.decoration.dto.StatsDto;
import org.desp.decoration.dto.UserDataDto;

public class DecorationListener implements Listener {

    private static UserDataRepository userDataRepository;
    private static ItemDataRepository itemDataRepository;
    private static LevelDataRepository levelDataRepository;


    public DecorationListener() {
        userDataRepository = UserDataRepository.getInstance();
        itemDataRepository = ItemDataRepository.getInstance();
        levelDataRepository = LevelDataRepository.getInstance();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        System.out.println("helmet.getItemMeta().getDisplayName() = " + helmet.getItemMeta());

        player.getInventory().setHelmet(helmet);

        UUID playerUUID = player.getUniqueId();

        MMOPlayerData playerData = MMOPlayerData.get(player);

        Map<String, UserDataDto> userData = userDataRepository.getUserData();

        if (userData.get(playerUUID.toString()) == null) {
            insertDefaultUserData(player, playerUUID);
            System.out.println("Inserted default user data");
            return;
        }

        System.out.println("userData.get(playerUUID.toString()).getUser_id() = " + userData.get(playerUUID.toString())
                .getUser_id());


        Map<String, ItemDataDto> itemData = itemDataRepository.getItemData();

        UserDataDto userDataDto = userData.get(playerUUID.toString());
        int statLevel = userDataDto.getLevel();
        System.out.println("userDataDto.getUser_id() = " + userDataDto.getUser_id());
        System.out.println("userDataDto.getUuid() = " + userDataDto.getUuid());
        System.out.println("userDataDto.getLevel() = " + userDataDto.getLevel());


        Map<Integer, LevelDataDto> levelData = levelDataRepository.getLevelData();

//        if (levelData.get(statLevel) == null) {
//            return;
//        }

        LevelDataDto levelDataDto = levelData.get(statLevel);
        StatsDto status = levelDataDto.getStats();

        StatModifier statModifier = new StatModifier("key", "skill-damage", 10.3, ModifierType.FLAT);

        UUID uniqueId = statModifier.getUniqueId();
        System.out.println("uniqueId = " + uniqueId);
        String key = statModifier.getKey();
        System.out.println("key = " + key);

        statModifier.unregister(playerData);
        statModifier.register(playerData);

        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(10024);

        player.getInventory().setHelmet(helmet);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();

            ItemStack currentHelmet = player.getInventory().getHelmet();

            ItemStack customHelmet = new ItemStack(Material.PAPER);
            ItemMeta meta = customHelmet.getItemMeta();
            if (meta != null) {
                meta.setCustomModelData(10024);
                meta.setDisplayName("tset");
                customHelmet.setItemMeta(meta);
            }

            player.getInventory().setHelmet(customHelmet);
            //player.getInventory().setItemInMainHand(currentHelmet);

            System.out.println("장착 성공");
            event.setCancelled(true);
        }
    }
}
