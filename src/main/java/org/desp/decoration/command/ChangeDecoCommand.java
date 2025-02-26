package org.desp.decoration.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.desp.decoration.gui.PlayerDerocationItemListGUI;
import org.desp.decoration.gui.PurchaseItemGUI;
import org.jetbrains.annotations.NotNull;

public class ChangeDecoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] strings) {

        if (!(commandSender instanceof Player player)) {
            return false;
        }
        PlayerDerocationItemListGUI gui = new PlayerDerocationItemListGUI(player);
        player.openInventory(gui.getInventory());
        return false;
    }
}
