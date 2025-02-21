package org.desp.decoration;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.desp.decoration.database.ItemDataRepository;
import org.desp.decoration.database.UserDataRepository;

public final class Decoration extends JavaPlugin {

    @Getter
    private static Decoration instance;

    @Override
    public void onEnable() {
        instance = this;

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void register() {
        UserDataRepository.getInstance();
        ItemDataRepository.getInstance();
    }
}
