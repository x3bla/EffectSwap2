package effectswap2.effectswap2.commands;

import effectswap2.effectswap2.EffectSwap2;
import effectswap2.effectswap2.PositiveEffectPotionManager;
import effectswap2.effectswap2.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class WithdrawCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println("This is a player only command!");
            return false;
        }
        if (args.length != 1) return false;
        if (PotionEffectType.getByName(args[0]) == null) return false;

        Player p = (Player) sender;
        HashMap<UUID, ArrayList<PotionEffectType>> effectMap = EffectSwap2.getEffectMap();

        if (!(Utils.positiveEffectCheck(p, false))) {  // positive effect check
            p.sendMessage(ChatColor.RED + "You do not have any positive " + ChatColor.AQUA + "EffectSwap.effects");
            return false;
        }

        PotionEffectType potionEffectType = PotionEffectType.getByName(args[0]);

        if (effectMap.values().contains(potionEffectType)) {
            Utils.removePositiveEffect(p, potionEffectType);
            ItemStack potion = PositiveEffectPotionManager.getPotion(potionEffectType);
            p.getWorld().dropItem(p.getLocation(), potion);
            return true;
        }


        return false;
    }
}
