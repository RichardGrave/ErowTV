package graver.erowtv.recipes;

import graver.erowtv.constants.Enumerations.CustomItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public final class BuildingBlocksRecipes {
	
	//Dont instantiate or subclass the class
	private BuildingBlocksRecipes() {
	}
	
	/**
	 * Create recipe for a copy FROM block (also needs a copy TO block).
	 * With copy FROM and TO blocks you can calculate x, y and z to copy the block within.
	 * 
	 * @param key with NamespacedKey
	 * @return ShapedRecipe
	 */
	public static ShapedRecipe createCopyFromBlock(NamespacedKey key) {
		ItemStack itemStack = new ItemStack(Material.EMERALD_BLOCK, 1);
		
		//Get meta from item so we can change it
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(CustomItem.COPY_FROM_BLOCK.getCustomItemName());
		//Add glow to item
		meta.addEnchant(Enchantment.LUCK, 1, true);

		//Add item lore to overwrite default enchantment text
		List<String> itemLore = new ArrayList<String>();
		itemLore.add(CustomItem.COPY_FROM_BLOCK.getLore());
		meta.setLore(itemLore);
		// Set the meta of the block to the edited meta.
		itemStack.setItemMeta(meta);

		ShapedRecipe recipe = new ShapedRecipe(key, itemStack);

		//Set shape and ingredients
		recipe.shape("E E", "C C", "E E");

		recipe.setIngredient('E', Material.EMERALD_BLOCK);
		recipe.setIngredient('C', Material.COMPASS);

		return recipe;
	}

	/**
	 * Create recipe for a copy TO block (also needs a copy FROM block).
	 * With copy FROM and TO blocks you can calculate x, y and z to copy the block within.
	 *
	 * @param key with NamespacedKey
	 * @return ShapedRecipe
	 */
	public static ShapedRecipe createCopyToBlock(NamespacedKey key) {
		ItemStack itemStack = new ItemStack(Material.EMERALD_BLOCK, 1);

		//Get meta from item so we can change it
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(CustomItem.COPY_TO_BLOCK.getCustomItemName());
		//Add glow to item
		meta.addEnchant(Enchantment.LUCK, 1, true);

		//Add item lore to overwrite default enchantment text
		List<String> itemLore = new ArrayList<String>();
		itemLore.add(CustomItem.COPY_TO_BLOCK.getLore());
		meta.setLore(itemLore);
		// Set the meta of the block to the edited meta.
		itemStack.setItemMeta(meta);

		ShapedRecipe recipe = new ShapedRecipe(key, itemStack);

		//Set shape and ingredients
		recipe.shape("ECE", "   ", "ECE");

		recipe.setIngredient('E', Material.EMERALD_BLOCK);
		recipe.setIngredient('C', Material.COMPASS);

		return recipe;
	}

	/**
	 * Create recipe for a paste block.
	 * Paste something that was saved to a yml with the Copy blocks.
	 *
	 * @param key with NamespacedKey
	 * @return ShapedRecipe
	 */
	public static ShapedRecipe createPasteBlock(NamespacedKey key) {
		ItemStack itemStack = new ItemStack(Material.DIAMOND_BLOCK, 1);

		//Get meta from item so we can change it
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(CustomItem.PASTE_BLOCK.getCustomItemName());
		//Add glow to item
		meta.addEnchant(Enchantment.LUCK, 1, true);

		//Add item lore to overwrite default enchantment text
		List<String> itemLore = new ArrayList<String>();
		itemLore.add(CustomItem.PASTE_BLOCK.getLore());
		meta.setLore(itemLore);
		// Set the meta of the block to the edited meta.
		itemStack.setItemMeta(meta);

		ShapedRecipe recipe = new ShapedRecipe(key, itemStack);

		//Set shape and ingredients
		recipe.shape("D D", " W ", "D D");

		recipe.setIngredient('D', Material.DIAMOND_BLOCK);
		recipe.setIngredient('W', Material.COMPASS);

		return recipe;
	}

	/**
	 * Create recipe for a copy FROM block (also needs a copy TO block).
	 * With copy FROM and TO blocks you can calculate x, y and z to copy the block within.
	 *
	 * @param key with NamespacedKey
	 * @return ShapedRecipe
	 */
	public static ShapedRecipe createDestroyFromBlock(NamespacedKey key) {
		ItemStack itemStack = new ItemStack(Material.PRISMARINE, 1);

		//Get meta from item so we can change it
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(CustomItem.DESTROY_FROM_BLOCK.getCustomItemName());
		//Add glow to item
		meta.addEnchant(Enchantment.LUCK, 1, true);

		//Add item lore to overwrite default enchantment text
		List<String> itemLore = new ArrayList<String>();
		itemLore.add(CustomItem.DESTROY_FROM_BLOCK.getLore());
		meta.setLore(itemLore);
		// Set the meta of the block to the edited meta.
		itemStack.setItemMeta(meta);

		ShapedRecipe recipe = new ShapedRecipe(key, itemStack);

		//Set shape and ingredients
		recipe.shape("P P", "T T", "P P");

		recipe.setIngredient('P', Material.PRISMARINE);
		recipe.setIngredient('T', Material.TNT);

		return recipe;
	}

	/**
	 * Create recipe for a copy TO block (also needs a copy FROM block).
	 * With copy FROM and TO blocks you can calculate x, y and z to copy the block within.
	 *
	 * @param key with NamespacedKey
	 * @return ShapedRecipe
	 */
	public static ShapedRecipe createDestroyToBlock(NamespacedKey key) {
		ItemStack itemStack = new ItemStack(Material.PRISMARINE, 1);

		//Get meta from item so we can change it
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(CustomItem.DESTROY_TO_BLOCK.getCustomItemName());
		//Add glow to item
		meta.addEnchant(Enchantment.LUCK, 1, true);

		//Add item lore to overwrite default enchantment text
		List<String> itemLore = new ArrayList<String>();
		itemLore.add(CustomItem.DESTROY_TO_BLOCK.getLore());
		meta.setLore(itemLore);
		// Set the meta of the block to the edited meta.
		itemStack.setItemMeta(meta);
		
		ShapedRecipe recipe = new ShapedRecipe(key, itemStack);

		//Set shape and ingredients
		recipe.shape("PTP", "   ", "PTP");
		
		recipe.setIngredient('P', Material.PRISMARINE);
		recipe.setIngredient('T', Material.TNT);

		return recipe;
	}
}
