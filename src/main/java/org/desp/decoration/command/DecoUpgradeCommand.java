package org.desp.decoration.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.desp.decoration.database.LevelDataRepository;
import org.desp.decoration.gui.DecoUpgradeGUI;
import org.desp.decoration.gui.PurchaseItemGUI;
import org.jetbrains.annotations.NotNull;

public class DecoUpgradeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] strings) {

        if (!(commandSender instanceof Player player)) {
            return false;
        }
        DecoUpgradeGUI gui = new DecoUpgradeGUI(player);
        player.openInventory(gui.getInventory());
        return false;
    }
}
