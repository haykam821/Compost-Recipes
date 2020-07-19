package io.github.haykam821.compostrecipes.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.compostrecipes.CompostingRecipe;
import io.github.haykam821.compostrecipes.Main;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

@Mixin(ComposterBlock.class)
public class ComposterBlockMixin {
	@Shadow
	private static IntProperty LEVEL;

	@Redirect(method = "onUse", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;containsKey(Ljava/lang/Object;)Z", remap = false))
	private boolean containsKey(Object2FloatMap<ItemConvertible> map, Object key, BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult) {
		return map.containsKey(key) || Main.canCompost(new ItemStack((Item) key), world);
	}

	@Unique
	private static boolean handleCompostingRecipe(BlockState state, BlockPos pos, World world, CompostingRecipe recipe) {
		int currentLevel = state.get(LEVEL);

		float chance = recipe.getChance();
		int layers = recipe.getLayers();
		float experience = recipe.getExperience();

		if ((currentLevel != 0 || chance <= 0.0f) && world.getRandom().nextDouble() >= chance) {
			return false;
		}
		
		int newLevel = Math.min(currentLevel + layers, 7);
		world.setBlockState(pos, state.with(LEVEL, newLevel), 3);
		if (newLevel == 7) {
			world.getBlockTickScheduler().schedule(pos, state.getBlock(), 20);
		}

		if (experience >= 0.0f) {
			while (experience > 0) {
				int orb = ExperienceOrbEntity.roundToOrbSize((int) experience);
				experience -= orb;

				BlockPos orbSpawnPos = pos.add(0.5d, 0.5d, 0.5d);
				ExperienceOrbEntity orbEntity = new ExperienceOrbEntity(world.getWorld(), orbSpawnPos.getX(), orbSpawnPos.getY(), orbSpawnPos.getZ(), orb);
				orbEntity.addVelocity(0, 0.05D, 0);

				world.spawnEntity(orbEntity);
			}
		}
		return true;
	}

	@Inject(method = "addToComposter", at = @At("HEAD"), cancellable = true)
	private static void addIngredientToComposter(BlockState state, WorldAccess worldAccess, BlockPos pos, ItemStack input, CallbackInfoReturnable<Boolean> ci) {
		World world = worldAccess.getWorld();
		Inventory inputInventory = new SimpleInventory(input);

		Optional<CompostingRecipe> recipeOptional = world.getRecipeManager().getFirstMatch(Main.COMPOSTING_RECIPE_TYPE, inputInventory, world);
		if (recipeOptional.isPresent()) {
			CompostingRecipe recipe = recipeOptional.get();
			ci.setReturnValue(ComposterBlockMixin.handleCompostingRecipe(state, pos, world, recipe));
		}
	}
}