package io.github.haykam821.compostrecipes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import io.github.haykam821.compostrecipes.CompostingRecipe;
import io.github.haykam821.compostrecipes.Main;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

@Mixin(ComposterBlock.class)
public class ComposterBlockMixin {
	@Shadow
	private static IntProperty LEVEL;

	@Redirect(method = "onUse", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;containsKey(Ljava/lang/Object;)Z", remap = false))
	private boolean containsKey(Object2FloatMap<ItemConvertible> map, Object key, BlockState blockState, World world,
			BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult) {
		return Main.canCompost(new ItemStack((Item) key), world);
	}

	private static boolean addToComposter(BlockState blockState, IWorld iWorld, BlockPos blockPos, ItemStack itemStack) {
		int currentLevel = (Integer) blockState.get(LEVEL);

		World world = iWorld.getWorld();
		Inventory fakeInv = new BasicInventory(itemStack);

		CompostingRecipe recipe = world.getRecipeManager().getFirstMatch(Main.COMPOSTING_RECIPE_TYPE, fakeInv, world).get();
		float chance = recipe.getChance();
		int layers = recipe.getLayers();

		if ((currentLevel != 0 || chance <= 0.0F) && iWorld.getRandom().nextDouble() >= (double) chance) {
			return false;
		} else {
			int newLevel = Math.min(currentLevel + layers, 7);
			iWorld.setBlockState(blockPos, (BlockState) blockState.with(LEVEL, newLevel), 3);
			if (newLevel == 7) {
				iWorld.getBlockTickScheduler().schedule(blockPos, blockState.getBlock(), 20);
			}

			return true;
		}
	}
}