package org.desp.decoration.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.desp.decoration.gui.PurchaseItemGUI;
import org.jetbrains.annotations.NotNull;

public class PurchaseItemCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] strings) {

        if (!(commandSender instanceof Player player)) {
            return false;
        }
        PurchaseItemGUI gui = new PurchaseItemGUI("치장 아이템 구매", player);
        player.openInventory(gui.getInventory());
        return false;
    }
}
