package io.github.haykam821.compostrecipes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At;

import io.github.haykam821.compostrecipes.Main;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.minecraft.block.ComposterBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;

@Mixin(ComposterBlock.class)
public class ComposterBlockMixin {
	@Redirect(method = "onUse", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;containsKey(Ljava/lang/Object;)Z"), remap = false)
	private boolean containsKey(Object2FloatMap<ItemConvertible> map, Object key) {
		ClientWorld world = MinecraftClient.getInstance().world;
		Inventory fakeInv = new BasicInventory(new ItemStack((Item) key));
		return world.getRecipeManager().getFirstMatch(Main.COMPOSTING_RECIPE_TYPE, fakeInv, world).orElse(null) != null;
	}

	@Redirect(method = "addToComposter", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;getFloat(Ljava/lang/Object;)F"), remap = false)
	private static float getFloat(Object2FloatMap<ItemConvertible> map, Object key) {
		ClientWorld world = MinecraftClient.getInstance().world;
		Inventory fakeInv = new BasicInventory(new ItemStack((Item) key));
		return world.getRecipeManager().getFirstMatch(Main.COMPOSTING_RECIPE_TYPE, fakeInv, world).get().getChance();
	}
}