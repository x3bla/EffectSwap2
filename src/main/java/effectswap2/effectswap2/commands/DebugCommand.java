package effectswap2.effectswap2.commands;

import effectswap2.effectswap2.EffectSwap2;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DebugCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("effectswap.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return true;
        }

        sender.sendMessage(ChatColor.GREEN + "Variables has been printed to console.");
        System.out.println("EffectMap: " + EffectSwap2.getEffectMap());
        return true;
    }
}
