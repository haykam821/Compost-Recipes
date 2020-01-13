package io.github.haykam821.compostrecipes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import io.github.haykam821.compostrecipes.Main;
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

	public boolean canInsertInvStack(int i, ItemStack itemStack, Direction direction) {
		if (direction == null) return false;
		return !this.dirty && direction == Direction.UP && Main.canCompost(itemStack, this.world.getWorld());
	}
}