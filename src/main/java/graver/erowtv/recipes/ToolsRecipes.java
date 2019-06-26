package graver.erowtv.recipes;

import graver.erowtv.constants.Enumerations;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public final class ToolsRecipes {
	
	//Dont instantiate or subclass the class
	private ToolsRecipes() {
	}

//	/**
//	 * Create recipe for a paste sign.
//	 * Right-Click a Paste Sign's and paste what was saved to a yml with the Copy blocks.
//	 *
//	 * @param key with NamespacedKey
//	 * @return ShapedRecipe
//	 */
//	public static ShapedRecipe createPasteSign(NamespacedKey key) {
//		ItemStack itemStack = new ItemStack(Material.SPRUCE_SIGN, 1);
//
//		//Get meta from item so we can change it
//		ItemMeta meta = itemStack.getItemMeta();
//		meta.setDisplayName(Enumerations.CustomItem.PASTE_SIGN.getCustomItemName());
//		//Add glow to item
//		meta.addEnchant(Enchantment.LUCK, 1, true);
//
//		//Add item lore to overwrite default enchantment text
//		List<String> itemLore = new ArrayList<String>();
//		itemLore.add(Enumerations.CustomItem.PASTE_SIGN.getLore());
//		meta.setLore(itemLore);
//		// Set the meta of the block to the edited meta.
//		itemStack.setItemMeta(meta);
//
//		ShapedRecipe recipe = new ShapedRecipe(key, itemStack);
//
//		//Set shape and ingredients
//		recipe.shape("SCS", "C C", "SCS");
//
//		recipe.setIngredient('S', Material.SPRUCE_SIGN);
//		recipe.setIngredient('C', Material.COMPASS);
//
//		return recipe;
//	}

//	/**
//	 * Create recipe for a timer sign.
//	 * Right-Click to start a timer with time from sign texts.
//	 *
//	 * @param key with NamespacedKey
//	 * @return ShapedRecipe
//	 */
//	public static ShapedRecipe createTimerSign(NamespacedKey key) {
//		ItemStack itemStack = new ItemStack(Material.SPRUCE_SIGN, 1);
//
//		//Get meta from item so we can change it
//		ItemMeta meta = itemStack.getItemMeta();
//		meta.setDisplayName(Enumerations.CustomItem.CLOCK_SIGN.getCustomItemName());
//		//Add glow to item
//		meta.addEnchant(Enchantment.LUCK, 1, true);
//
//		//Add item lore to overwrite default enchantment text
//		List<String> itemLore = new ArrayList<String>();
//		itemLore.add(Enumerations.CustomItem.CLOCK_SIGN.getLore());
//		meta.setLore(itemLore);
//		// Set the meta of the block to the edited meta.
//		itemStack.setItemMeta(meta);
//
//		ShapedRecipe recipe = new ShapedRecipe(key, itemStack);
//
//		//Set shape and ingredients
//		recipe.shape("SCS", "C C", "SCS");
//
//		recipe.setIngredient('S', Material.SPRUCE_SIGN);
//		recipe.setIngredient('C', Material.CLOCK);
//
//		return recipe;
//	}

	/**
	 * Create recipe for a tool sign.
	 * Right-Click to create the tool from the sign text. Example YoutubeSubCounter.
	 *
	 * @param key with NamespacedKey
	 * @return ShapedRecipe
	 */
	public static ShapedRecipe createToolSign(NamespacedKey key) {
		ItemStack itemStack = new ItemStack(Material.SPRUCE_SIGN, 1);

		//Get meta from item so we can change it
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(Enumerations.CustomItem.TOOL_SIGN.getCustomItemName());
		//Add glow to item
		meta.addEnchant(Enchantment.LUCK, 1, true);

		//Add item lore to overwrite default enchantment text
		List<String> itemLore = new ArrayList<String>();
		itemLore.add(Enumerations.CustomItem.TOOL_SIGN.getLore());
		meta.setLore(itemLore);
		// Set the meta of the block to the edited meta.
		itemStack.setItemMeta(meta);

		ShapedRecipe recipe = new ShapedRecipe(key, itemStack);

		//Set shape and ingredients
		recipe.shape("S S", " C ", "S S");

		recipe.setIngredient('S', Material.SPRUCE_SIGN);
		recipe.setIngredient('C', Material.CRAFTING_TABLE);

		return recipe;
	}
}
