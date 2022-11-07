package effectswap2.effectswap2.listeners;

import effectswap2.effectswap2.EffectSwap2;
import effectswap2.effectswap2.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffectType;

public class NaturalDeathListener implements Listener {

    @EventHandler
    public void onNaturalDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (p.getKiller() == null) {

            if (Utils.positiveEffectCheck(p, false)) {
                PotionEffectType effect = Utils.removePositiveEffect(p);
                p.sendMessage(ChatColor.RED + "You have lost the effect: " + ChatColor.RED + effect.getName());
            } else {
                if (EffectSwap2.getEffects(p).containsAll(EffectSwap2.negativeEffectList)) return;  // ignore if maxed

                PotionEffectType effect = Utils.giveNegativeEffect(p);
                p.sendMessage(ChatColor.RED + "You have gained the effect: " + ChatColor.RED + effect.getName());

            }
        }
    }

}
