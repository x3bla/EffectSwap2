package effectswap2.effectswap2.listeners;


import effectswap2.effectswap2.EffectSwap2;
import effectswap2.effectswap2.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class DrinkLuckyPotionListener implements Listener {

    Random random = new Random();

    public void luckyPotion(Player p) {
        if (!(Utils.positiveEffectCheck(p, true))) {  // if there's negative effects, return
            p.sendMessage(ChatColor.YELLOW + "It seems that you are still affected by negative effects, nothing seems to happen.");
            return;
        }

        if (random.nextInt(100) < 20) {
            PotionEffectType positiveEffect = Utils.givePositiveEffect(p);
            p.sendMessage(ChatColor.AQUA + "You have gained " + ChatColor.GREEN + positiveEffect.getName() + ChatColor.AQUA + "!");

        } else {
            p.sendMessage(ChatColor.YELLOW + "Nothing seemed to happen");
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        if (!ItemCraftListener.itemIsLuckyPotion(e.getItem())) return;

        final Player p = e.getPlayer();
        ItemStack item = e.getItem();
        item.setAmount(item.getAmount() - 1);
        p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 0));
        (new BukkitRunnable() {
            public void run() {
                luckyPotion(p);
            }
        }).runTaskLater(EffectSwap2.getInstance(), 100L);
    }


}
