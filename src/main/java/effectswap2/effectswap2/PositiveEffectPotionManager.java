package effectswap2.effectswap2;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;

public class PositiveEffectPotionManager {

    // the EffectSwap.effect are the effects in the hashmap
    private static ItemStack createPositiveEffectPotion(PotionEffectType effectType) {
        ItemStack itemStack = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setColor(Color.SILVER);
        String effectName = effectType.getName();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GOLD + "" + ChatColor.ITALIC + "Positive Effect Potion");

        ArrayList<String> lore = new ArrayList<>();
        lore.add("This potion grants you the positive effect stored within this potion");
        lore.add(effectName);

        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;

    }

    public static ItemStack getPotion(PotionEffectType effectType) {
        return createPositiveEffectPotion(effectType);
    }

    public static boolean isPositivePotion(ItemStack potion) {
        if (!(potion.getItemMeta().hasLore())) return false;

        List<String> consumePotionLore = potion.getItemMeta().getLore();
        System.out.println(consumePotionLore);
        return consumePotionLore.contains("This potion grants you the positive effect stored within this potion");
    }

    public static boolean isPositivePotionRecipe(ArrayList<ItemStack> recipe) {
        if (recipe.size() != 4) return false;
        for (ItemStack item : recipe) {  // too lazy to create a shapeless crafting class be like
            if (!isPositivePotion(item)) {
                return false;
            }
        }

        return true;
    }

    public static Inventory getPotionGUI(Player p) {

        Inventory gui = Bukkit.createInventory(p, 54, ChatColor.GREEN + "Potion Select GUI");

        // whole part just converting PotionEffectType into Potion
        ArrayList<PotionEffectType> positiveEffect = EffectSwap2.positiveEffectList;
        ArrayList<ItemStack> potionItems = new ArrayList<>();
        for (PotionEffectType effectType : positiveEffect) {
            ItemStack potion = new ItemStack(Material.POTION);
            PotionMeta meta = (PotionMeta) potion.getItemMeta();
            meta.setBasePotionData(new PotionData(PotionType.valueOf(effectType.getName())));
            potion.setItemMeta(meta);
            potionItems.add(potion);
        }


        ItemStack[] potions = potionItems.toArray(new ItemStack[potionItems.size()]);  // TODO: try it with size 0
        gui.setContents(potions);
        return gui;
    }
}
