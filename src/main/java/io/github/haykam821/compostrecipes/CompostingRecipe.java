package io.github.haykam821.compostrecipes;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CompostingRecipe implements Recipe<Inventory> {
	protected final RecipeType<CompostingRecipe> type;
	protected final Identifier id;
	protected final String group;
	protected final Ingredient input;
	protected final ItemStack output;
	protected final float chance;

	public CompostingRecipe(Identifier identifier, String string, Ingredient ingredient, ItemStack output, float chance) {
		this.type = Main.COMPOSTING_RECIPE_TYPE;
		this.id = identifier;
		this.group = string;
		this.input = ingredient;
		this.output = output;
		this.chance = chance;
   }

	public boolean matches(Inventory inventory, World world) {
		return this.input.test(inventory.getInvStack(0));
	}

	public ItemStack craft(Inventory inventory) {
		return this.output;
	}

	@Environment(EnvType.CLIENT)
	public boolean fits(int i, int j) {
		return true;
	}

	public DefaultedList<Ingredient> getPreviewInputs() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(this.input);
		return defaultedList;
	}

	public float getChance() {
		return this.chance;
	}

	@Environment(EnvType.CLIENT)
	public String getGroup() {
		return this.group;
	}

	public Identifier getId() {
		return this.id;
	}

	public RecipeType<CompostingRecipe> getType() {
		return this.type;
	}

	@Override
	public RecipeSerializer<CompostingRecipe> getSerializer() {
		return Main.COMPOSTING_RECIPE_SERIALIZER;
	}

	@Override
	public ItemStack getOutput() {
		return this.output;
	}
}