package graver.erowtv.recipes;

public final class ToolsRecipes {
	
	//Dont instantiate or subclass the class
	private ToolsRecipes() {
	}


	//TODO:RG Keep this first. Not sure if we will reuse it

//	/**
//	 * Create recipe for a tool sign.
//	 * Right-Click to create the tool from the sign text. Example YoutubeSubCounter.
//	 *
//	 * @param key with NamespacedKey
//	 * @return ShapedRecipe
//	 */
//	public static ShapedRecipe createToolSign(NamespacedKey key) {
//		//Add key to recipebook
//		ErowTV.addnamespacedKeyRecipe(key);
//		ItemStack itemStack = new ItemStack(Material.SPRUCE_SIGN, 1);
//
//		//Get meta from item so we can change it
//		ItemMeta meta = itemStack.getItemMeta();
//		meta.setDisplayName(Enumerations.CustomItem.SPECIAL_SIGN.getCustomItemName());
//		//Add glow to item
//		meta.addEnchant(Enchantment.LUCK, 1, true);
//
//		//Add item lore to overwrite default enchantment text
//		List<String> itemLore = new ArrayList<String>();
//		itemLore.add(Enumerations.CustomItem.SPECIAL_SIGN.getLore());
//		meta.setLore(itemLore);
//		// Set the meta of the block to the edited meta.
//		itemStack.setItemMeta(meta);
//
//		ShapedRecipe recipe = new ShapedRecipe(key, itemStack);
//
//		//Set shape and ingredients
//		recipe.shape("S S", " C ", "S S");
//
//		recipe.setIngredient('S', Material.SPRUCE_SIGN);
//		recipe.setIngredient('C', Material.CRAFTING_TABLE);
//
//		return recipe;
//	}
}
