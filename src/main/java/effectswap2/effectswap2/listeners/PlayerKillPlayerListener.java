package effectswap2.effectswap2.listeners;

import effectswap2.effectswap2.EffectSwap2;
import effectswap2.effectswap2.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Random;

public class PlayerKillPlayerListener implements Listener {

    private final Random random = new Random();

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (e.getEntity().getKiller() == null)
            return;

        Player victim = e.getEntity();
        Player killer = victim.getKiller();

        if (victim.equals(killer)) return;

        if (canSwap(victim, killer)) {  // swap effects
            if (random.nextInt(101) <= 15) {  // 15% chance to swap effects
                ArrayList<PotionEffectType> effects = Utils.swapEffect(victim, killer);
                victim.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have swapped effects with " + killer.getName()
                        + ". You lost &4" + effects.get(0).getName() + "&c and gained &4" + effects.get(1).getName() + "&c!"));
                killer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have swapped effects with " + victim.getName()
                        + ". You lost &4" + effects.get(1).getName() + "&c and gained &4" + effects.get(0).getName() + "&c!"));
                return;
            }
        }

        // lose and gain effects
        if (Utils.positiveEffectCheck(victim, false)) {
            PotionEffectType victimEffect = Utils.removePositiveEffect(victim);
            victim.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou have lost the effect &4" + victimEffect.getName() + "&c!"));
        } else {
            PotionEffectType victimEffect = Utils.giveNegativeEffect(victim);
            if (victimEffect == null) return;
            victim.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou have gained the effect &4" + victimEffect.getName() + "&c!"));
        }

        if (Utils.positiveEffectCheck(killer, true)) {
            PotionEffectType killerEffect = Utils.givePositiveEffect(killer);
            if (killerEffect == null) return;
            killer.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou have gained the effect &4" + killerEffect.getName() + "&c!"));
        } else {
            PotionEffectType killerEffect = Utils.removeNegativeEffect(killer);
            killer.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou have lost the effect &4" + killerEffect.getName() + "&c!"));
        }



    }

    private boolean canSwap(Player victim, Player killer) {
        ArrayList<PotionEffectType> victimEffects = EffectSwap2.getEffects(victim);
        ArrayList<PotionEffectType> killerEffects = EffectSwap2.getEffects(killer);

        // all the conditions for swapping
        if (Utils.positiveEffectCheck(victim, false) && Utils.positiveEffectCheck(killer, false)) return true;  // both players have positive effects
        if (Utils.positiveEffectCheck(victim, false) && victimEffects.size() == 1) {  // killer has negative effect and victim has positive effect
            return !Utils.positiveEffectCheck(killer, false) && killerEffects.size() == 1;
        }

        return false;
    }
}