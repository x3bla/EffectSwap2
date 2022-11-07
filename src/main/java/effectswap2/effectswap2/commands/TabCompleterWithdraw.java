package effectswap2.effectswap2.commands;

import effectswap2.effectswap2.EffectSwap2;
import effectswap2.effectswap2.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TabCompleterWithdraw implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        HashMap<UUID, ArrayList<PotionEffectType>> effectMap = EffectSwap2.getEffectMap();
        Player p = (Player) sender;
        ArrayList<String> effectTypes = new ArrayList<>();

        if (!(Utils.positiveEffectCheck(p, false))) {  // positive effect check
            return null;
        }

        if (args.length == 1) {
            for (PotionEffectType effect : effectMap.get(p)) {
                effectTypes.add(effect.toString());
            }

            return effectTypes;
        }

        return null;
    }
}
