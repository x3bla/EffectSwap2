package effectswap2.effectswap2.listeners;

import effectswap2.effectswap2.EffectSwap2;
import effectswap2.effectswap2.PositiveEffectPotionManager;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemCraftListener implements Listener {
    private static final NamespacedKey effectRerollKey = new NamespacedKey(EffectSwap2.getInstance(), "reroll");
    private static final NamespacedKey luckyEffectKey = new NamespacedKey(EffectSwap2.getInstance(), "lucky");
    private static final NamespacedKey effectRemoveKey = new NamespacedKey(EffectSwap2.getInstance(), "remove");
    private static final NamespacedKey potionTokenKey = new NamespacedKey(EffectSwap2.getInstance(), "token");

    @EventHandler
    public void onCraft(PrepareItemCraftEvent e) {
        ItemStack[] matrix = e.getInventory().getMatrix();
        if (matrix.length != 9) return;  // return if matrix is incomplete

        boolean resultIsEffectReroll = true;
        boolean resultIsLuckyPotion = true;
        boolean resultIsEffectRemove = true;
        boolean resultIsPotionToken = true;
        ArrayList<ItemStack> recipe = new ArrayList<>();

        for (int x = 0; x < 9; x++) {
            ItemStack item = matrix[x];
            recipe.add(item);
            Material type = null;
            if (item != null) {
                type = item.getType();
                if (item.getAmount() != 1) {  // allows crafting of the item if there's only 1 item in each slot
                    resultIsEffectReroll = false;  // also prevents item duping bug
                    resultIsLuckyPotion = false;  // when you put 3 items in the same place, it becomes 4, idk why
                    resultIsEffectRemove = false;
                    resultIsPotionToken = false;
                    break;
                }
            }

            if (type != getRerollRecipeSlot(x)) {
                resultIsEffectReroll = false;
            }
            if (type != getLuckyRecipeSlot(x)) {
                resultIsLuckyPotion = false;
            }
            if (type != getRemoveRecipeSlot(x)) {
                resultIsEffectRemove = false;
            }
            if (PositiveEffectPotionManager.isPositivePotionRecipe(recipe)) {
                resultIsPotionToken = false;
            }
        }

        if (resultIsEffectReroll) {
            e.getInventory().setResult(createEffectReroll());
        } else if (resultIsLuckyPotion) {
            e.getInventory().setResult(createLuckyPotion());
        } else if (resultIsEffectRemove) {
            e.getInventory().setResult(createRemoveNegativeEffect());
        } else if (resultIsPotionToken) {
            e.getInventory().setResult(createPositivePotionToken());
        }
    }

    public ItemStack createEffectReroll() {
        ItemStack itemStack = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setColor(Color.WHITE);
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.ITALIC + "Strange Concoction");
        
        ArrayList<String> lore = new ArrayList<>();
        lore.add("This potion has a 50/50 chance of removing a negative effect or adding a new negative effect");
        lore.add("Better pray to RNGesus");
        
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(effectRerollKey, PersistentDataType.STRING, "reroll");
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public ItemStack createLuckyPotion() {
        ItemStack itemStack = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setColor(Color.SILVER);
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.ITALIC + "Lucky Potion");

        ArrayList<String> lore = new ArrayList<>();
        lore.add("This potion has a 20/80 chance of giving you a new positive effect, or nothing.");
        lore.add("Better pray to RNGesus");

        meta.setLore(lore);
        meta.getPersistentDataContainer().set(luckyEffectKey, PersistentDataType.STRING, "lucky");
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public ItemStack createRemoveNegativeEffect() {
        ItemStack itemStack = new ItemStack(Material.POTION);
        ItemMeta meta =  itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GREEN + "" +
                ChatColor.ITALIC + "Positive Potion Token");

        ArrayList<String> lore = new ArrayList<>();
        lore.add("WARNING!!! WILL DISAPPEAR UPON USE REGARDLESS OF WHETHER YOU CHOSE A POTION OR NOT");
        lore.add("This token allows you to redeem a positive potion of your choice");
        lore.add("Right-Click to use");
        lore.add("Effects are limited by what B3n has allowed");

        meta.setLore(lore);
        meta.getPersistentDataContainer().set(potionTokenKey, PersistentDataType.STRING, "token");
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public ItemStack createPositivePotionToken() {
        ItemStack itemStack = new ItemStack(Material.RABBIT_FOOT);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.WHITE + "" +
                ChatColor.ITALIC + "Strange But Really Good Concoction");

        ArrayList<String> lore = new ArrayList<>();
        lore.add("This potion removes all of your negative effects");
        lore.add("Might taste funky");
        lore.add("RNGesus is not in control but doesn't mean you should stop praying to RNGesus");

        meta.setLore(lore);
        meta.getPersistentDataContainer().set(effectRemoveKey, PersistentDataType.STRING, "remove");
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static boolean itemIsEffectReroll(ItemStack item) {
        return (item != null && item.getType() == Material.POTION && item.getItemMeta().getPersistentDataContainer()
                .has(effectRerollKey, PersistentDataType.STRING));
    }

    public static boolean itemIsLuckyPotion(ItemStack item) {
        return (item != null && item.getType() == Material.POTION && item.getItemMeta().getPersistentDataContainer()
                .has(luckyEffectKey, PersistentDataType.STRING));
    }

    public static boolean itemIsRemoveNegativeEffect(ItemStack item) {
        return (item != null && item.getType() == Material.POTION && item.getItemMeta().getPersistentDataContainer()
                .has(effectRemoveKey, PersistentDataType.STRING));
    }

    public static boolean itemIsPositivePotionToken(ItemStack item) {
        return (item != null && item.getType() == Material.RABBIT_FOOT && item.getItemMeta().getPersistentDataContainer()
                .has(potionTokenKey, PersistentDataType.STRING));
    }


    public static Material getRerollRecipeSlot(int x) {
        EffectSwap2.getInstance().reloadConfig();
        List<String> rerollRecipe = EffectSwap2.getInstance().getConfig().getStringList("reroll-recipe");
        Material type = Material.valueOf(rerollRecipe.get(x));
        if (type == Material.AIR)
            return null;
        return type;
    }

    public static Material getLuckyRecipeSlot(int x) {
        EffectSwap2.getInstance().reloadConfig();
        List<String> luckyRecipe = EffectSwap2.getInstance().getConfig().getStringList("lucky-recipe");
        Material type = Material.valueOf(luckyRecipe.get(x));
        if (type == Material.AIR)
            return null;
        return type;
    }

    public static Material getRemoveRecipeSlot(int x) {
        EffectSwap2.getInstance().reloadConfig();
        List<String> removeRecipe = EffectSwap2.getInstance().getConfig().getStringList("remove-recipe");
        Material type = Material.valueOf(removeRecipe.get(x));
        if (type == Material.AIR)
            return null;
        return type;
    }

    // useless but still here just in case of shaped recipe
    public static Material getPotionTokenSlot(int x) {
        EffectSwap2.getInstance().reloadConfig();
        List<String> tokenRecipe = EffectSwap2.getInstance().getConfig().getStringList("remove-recipe");
        Material type = Material.valueOf(tokenRecipe.get(x));
        if (type == Material.AIR)
            return null;
        return type;
    }
}
