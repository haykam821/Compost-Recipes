package io.github.haykam821.compostrecipes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import io.github.haykam821.compostrecipes.Main;
import net.minecraft.block.ComposterBlock;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;

@Mixin(targets = {"net/minecraft/block/ComposterBlock$ComposterInventory"})
public class ComposterInventoryMixin extends BasicInventory {
	@Shadow
	private boolean dirty;

	@Shadow
	private IWorld world;

	public boolean canInsertInvStack(int i, ItemStack stack, Direction direction) {
		if (this.dirty) return false;
		if (direction != Direction.UP) return false;

		return ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.containsKey(stack.getItem()) || Main.canCompost(stack, this.world.getWorld());
	}
}