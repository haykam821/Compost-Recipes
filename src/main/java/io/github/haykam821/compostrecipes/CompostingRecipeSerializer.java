package io.github.haykam821.compostrecipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;

public class CompostingRecipeSerializer implements RecipeSerializer<CompostingRecipe> {
	private final CompostingRecipeSerializer.RecipeFactory recipeFactory;

	public CompostingRecipeSerializer(CompostingRecipeSerializer.RecipeFactory recipeFactory) {
		this.recipeFactory = recipeFactory;
	}

	@Override
	public CompostingRecipe read(Identifier identifier, JsonObject jsonObject) {
		String string = JsonHelper.getString(jsonObject, "group", "");

		// Input item
		JsonElement jsonElement = JsonHelper.hasArray(jsonObject, "ingredient")
				? JsonHelper.getArray(jsonObject, "ingredient")
				: JsonHelper.getObject(jsonObject, "ingredient");
		Ingredient ingredient = Ingredient.fromJson((JsonElement) jsonElement);

		// Chance, layers, and experience
		float chance = JsonHelper.getFloat(jsonObject, "chance", 1.0F);
		int layers = JsonHelper.getInt(jsonObject, "layers", 1);
		float experience = JsonHelper.getFloat(jsonObject, "experience", 0.0F);

		return this.recipeFactory.create(identifier, string, ingredient, ItemStack.EMPTY, chance, layers, experience);
	}

	@Override
	public CompostingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
 		String string = packetByteBuf.readString(32767);
		Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
		float chance = packetByteBuf.readFloat();
		int layers = packetByteBuf.readInt();
		float experience = packetByteBuf.readFloat();

		return this.recipeFactory.create(identifier, string, ingredient, ItemStack.EMPTY, chance, layers, experience);
	}

	@Override
	public void write(PacketByteBuf packetByteBuf, CompostingRecipe compostingRecipe) {
		packetByteBuf.writeString(compostingRecipe.group);
		compostingRecipe.input.write(packetByteBuf);
		packetByteBuf.writeFloat(compostingRecipe.chance);
		packetByteBuf.writeInt(compostingRecipe.layers);
		packetByteBuf.writeFloat(compostingRecipe.experience);
	}

	interface RecipeFactory {
		CompostingRecipe create(Identifier identifier, String string, Ingredient ingredient, ItemStack itemStack, float chance, int layers, float experience);
	}
}