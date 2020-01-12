package io.github.haykam821.compostrecipes;

import net.fabricmc.api.ModInitializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Main implements ModInitializer {
	final static Identifier COMPOSTING_ID = new Identifier("compostrecipes", "composting");
	final static CompostingRecipeSerializer COMPOSTING_RECIPE_SERIALIZER = new CompostingRecipeSerializer(CompostingRecipe::new);
	public final static RecipeType<CompostingRecipe> COMPOSTING_RECIPE_TYPE = new RecipeType<CompostingRecipe>() {
		public String toString() {
			return COMPOSTING_ID.toString();
		}
	};

	@Override
	public void onInitialize() {
		Registry.register(Registry.RECIPE_SERIALIZER, COMPOSTING_ID, COMPOSTING_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_TYPE, COMPOSTING_ID, COMPOSTING_RECIPE_TYPE);
	}
}