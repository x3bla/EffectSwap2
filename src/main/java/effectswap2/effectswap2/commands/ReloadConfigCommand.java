package effectswap2.effectswap2.commands;

import effectswap2.effectswap2.EffectSwap2;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadConfigCommand implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("effectswap.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return true;
        }

        EffectSwap2.getInstance().reloadConfig();
        sender.sendMessage(ChatColor.GREEN + EffectSwap2.getInstance().getName() + ": Configuration reloaded.");
        return true;
    }
}
