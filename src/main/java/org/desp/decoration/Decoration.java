package org.desp.decoration;

import java.util.Collection;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.desp.decoration.command.ChangeDecoCommand;
import org.desp.decoration.command.DecoUpgradeCommand;
import org.desp.decoration.command.PurchaseItemCommand;
import org.desp.decoration.database.ItemDataRepository;
import org.desp.decoration.database.LevelDataRepository;
import org.desp.decoration.database.UserDataRepository;
import org.desp.decoration.listener.DecorationListener;

public final class Decoration extends JavaPlugin {

    @Getter
    private static Decoration instance;

    @Override
    public void onEnable() {
        instance = this;
        register();
        Bukkit.getPluginManager().registerEvents(new DecorationListener(), this);
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        for (Player onlinePlayer : onlinePlayers) {
            UserDataRepository.getInstance().loadUserData(onlinePlayer);
            DecorationListener.applyStat(onlinePlayer);
        }

        getCommand("치장구매").setExecutor(new PurchaseItemCommand());
        getCommand("치장장착").setExecutor(new ChangeDecoCommand());
        getCommand("치장강화").setExecutor(new DecoUpgradeCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            UserDataRepository.getInstance().saveUserData(DecorationListener.getPlayerCache().get(onlinePlayer.getUniqueId().toString()));
        }
    }

    private void register() {
        UserDataRepository.getInstance();
        ItemDataRepository.getInstance();
        LevelDataRepository.getInstance();
    }
}
