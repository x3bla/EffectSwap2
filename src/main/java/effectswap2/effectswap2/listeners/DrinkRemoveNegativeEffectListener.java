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

public class DrinkRemoveNegativeEffectListener implements Listener {

    public void potion5050(Player p) {
        if (Utils.positiveEffectCheck(p, true)) {  // if there's no effects, or if there are positive effects, return
            p.sendMessage(ChatColor.YELLOW +
                    "You don't seem to have any negative effects, but the potion vanished into thin air anyways.");
            return;
        }

        EffectSwap2.setEffect(p, null);
//        ArrayList<PotionEffectType> playerNegativeEffectList = EffectSwap2.getEffects(p);
//
//        for (int i = 0; i < playerNegativeEffectList.size(); i++) {
//            Utils.removeNegativeEffect(p);
//        }
        p.sendMessage(ChatColor.AQUA + "You feel refreshed, All negative effects has been removed.");

    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        if (!ItemCraftListener.itemIsRemoveNegativeEffect(e.getItem())) return;

        final Player p = e.getPlayer();
        ItemStack item = e.getItem();
        item.setAmount(item.getAmount() - 1);
        p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 0));
        (new BukkitRunnable() {
            public void run() {
                potion5050(p);
            }
        }).runTaskLater(EffectSwap2.getInstance(), 100L);
    }


}
