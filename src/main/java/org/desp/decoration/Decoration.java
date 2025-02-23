package org.desp.decoration;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void register() {
        UserDataRepository.getInstance();
        ItemDataRepository.getInstance();
        LevelDataRepository.getInstance();
    }
}
