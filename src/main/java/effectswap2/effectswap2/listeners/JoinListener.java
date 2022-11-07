package effectswap2.effectswap2.listeners;

import effectswap2.effectswap2.EffectSwap2;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffectType;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!p.hasPlayedBefore()) {
            PotionEffectType newEffect = EffectSwap2.setRandomEffect(p);
            p.sendMessage(ChatColor.RED + "Your effect is: " + ChatColor.RED + newEffect.getName());
        } else {
            EffectSwap2.loadPlayerEffect(p);  // to combat a milk and log bug
        }
    }
}
