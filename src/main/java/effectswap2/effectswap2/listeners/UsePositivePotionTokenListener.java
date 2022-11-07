package effectswap2.effectswap2.listeners;

import effectswap2.effectswap2.PositiveEffectPotionManager;
import effectswap2.effectswap2.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

public class UsePositivePotionTokenListener implements Listener {

    @EventHandler
    public void onUsePositivePotionToken(PlayerInteractEvent e) {
        if (!ItemCraftListener.itemIsPositivePotionToken(e.getItem())) return;

        Player p = e.getPlayer();
        p.openInventory(PositiveEffectPotionManager.getPotionGUI(p));
        p.getInventory().remove(e.getItem());

    }

    @EventHandler
    public void onSelectPotion(InventoryClickEvent e) {
        if (!e.getView().getTitle().equals(ChatColor.GREEN + "Potion Select GUI")) return;
        Player p = (Player) e.getWhoClicked();
        PotionMeta meta = (PotionMeta) e.getCurrentItem().getItemMeta();

        // TODO: for fun, make a switch case for a randomized potion
        PotionEffectType potionEffectType = meta.getBasePotionData().getType().getEffectType();
        Utils.givePositiveEffect(p, potionEffectType);
        p.sendMessage(ChatColor.AQUA + "You have gained the effect of the potion: " + potionEffectType.getName());
    }
}
