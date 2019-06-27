package graver.erowtv.recipes;

import graver.erowtv.constants.Enumerations.CustomItem;
import graver.erowtv.main.ErowTV;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public final class MiscellaneousRecipes {

	//Dont instantiate or subclass the class
	private MiscellaneousRecipes() {
	}
	
	//TODO:RG maak nog een block perameter copy en paste JSON function
	
	/**
	 * Create recipe for a stick that places a total of 8 blocks (2*xas,2*yas,2*zas)
	 * @param key with NamespacedKey
	 * @return ShapedRecipe
	 */
	public static ShapedRecipe createTwoByTwo(NamespacedKey key) {
		//Add key to recipebook
		ErowTV.addnamespacedKeyRecipe(key);
		ItemStack itemStack = new ItemStack(Material.STICK, 1);
		
		//Get meta from item so we can change it
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(CustomItem.TWO_BY_TWO.getCustomItemName());
		//Add glow to item
		meta.addEnchant(Enchantment.LUCK, 1, true);
		
		//Add item lore to overwrite default enchantment text
		List<String> itemLore = new ArrayList<String>();
		itemLore.add(CustomItem.TWO_BY_TWO.getLore());
		meta.setLore(itemLore);
		// Set the meta of the block to the edited meta.
		itemStack.setItemMeta(meta);
		
		ShapedRecipe recipe = new ShapedRecipe(key, itemStack);

		//Set shape and ingredients
		recipe.shape("D D", " S ", "D D");
		
		recipe.setIngredient('D', Material.DIAMOND);
		recipe.setIngredient('S', Material.STICK);

		return recipe;
	}
	
}
