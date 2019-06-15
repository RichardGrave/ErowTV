package graver.erowtv.recipes;

public final class RedstoneRecipes {
	
	//Dont instantiate or subclass the class
	private RedstoneRecipes() {
	}
	
	/**
	 * Create recipe for a copy button (also needs a copy FROM and TO block).
	 * Place on a copy FROM or TO block.
	 * On use it should use the coordinates from both block to calculate the space between
	 * them to copy all the blocks to a JSON file
	 * 
	 * @param key with NamespacedKey
	 * @return ShapedRecipe
	 */
	//TODO:RG use for something else
//	public static ShapedRecipe createCopyDoItButton(NamespacedKey key) {
//		ItemStack itemStack = new ItemStack(Material.WOOD_BUTTON, 1);
//		
//		//Get meta from item so we can change it
//		ItemMeta meta = itemStack.getItemMeta();
//		meta.setDisplayName(CustomItem.COPY_DO_IT_BUTTON.getCustomItemName());
//		
//		//Add glow to item
//		meta.addEnchant(Enchantment.LUCK, 1, true);
//		
//		//Add item lore to overwrite default enchantment text
//		List<String> itemLore = new ArrayList<String>();
//		itemLore.add(CustomItem.COPY_DO_IT_BUTTON.getLore());
//		meta.setLore(itemLore);
//		// Set the meta of the sword to the edited meta.
//		itemStack.setItemMeta(meta);
//		
//		ShapedRecipe recipe = new ShapedRecipe(key, itemStack);
//
//		//Set shape and ingredients
//		recipe.shape("ECE", "CBC", "ECE");
//		
//		recipe.setIngredient('E', Material.EMERALD_BLOCK);
//		recipe.setIngredient('C', Material.COMPASS);
//		recipe.setIngredient('B', Material.WOOD_BUTTON);
//
//		return recipe;
//	}
}
