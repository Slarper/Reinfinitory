package furgl.infinitory.mixin.network;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import furgl.infinitory.config.Config;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerPlayNetworkHandler.class, priority = 999)
public abstract class ServerPlayNetworkHandlerMixin {
	
//	@ModifyConstant(method = "onCreativeInventoryAction", constant = @Constant(intValue = 45))
//	public int increaseMaxSlots(int maxSlot) {
//		return Integer.MAX_VALUE;
//	}

//	@ModifyConstant(method = "onCreativeInventoryAction", constant = @Constant(intValue = 45), require = 0)
//	public int increaseMaxSlots(int maxSlot) {
//		return Integer.MAX_VALUE;
//	}
//
//	@ModifyConstant(method = "onCreativeInventoryAction", constant = @Constant(intValue = 64), require = 0)
//	public int increaseMaxStackSize(int maxStackSize) {
//		return Config.maxStackSize;
//	}

	@Inject(method = "onCreativeInventoryAction", at = @At("HEAD"), cancellable = true)
	public void onCreativeInventoryAction(CreativeInventoryActionC2SPacket packet, CallbackInfo ci) {
		ServerPlayNetworkHandler self = ((ServerPlayNetworkHandler) (Object) this);

		NetworkThreadUtils.forceMainThread(packet, self, self.player.getWorld());
		if (self.player.interactionManager.isCreative()) {
			boolean bl = packet.getSlot() < 0;
			ItemStack itemStack = packet.getItemStack();
			NbtCompound nbtCompound = BlockItem.getBlockEntityNbt(itemStack);
			if (!itemStack.isEmpty() && nbtCompound != null && nbtCompound.contains("x") && nbtCompound.contains("y") && nbtCompound.contains("z")) {
				BlockPos blockPos = BlockEntity.posFromNbt(nbtCompound);
				if (self.player.world.canSetBlock(blockPos)) {
					BlockEntity blockEntity = self.player.world.getBlockEntity(blockPos);
					if (blockEntity != null) {
						blockEntity.setStackNbt(itemStack);
					}
				}
			}

			boolean bl2 = packet.getSlot() >= 1;
			boolean bl3 = itemStack.isEmpty() || itemStack.getDamage() >= 0 && itemStack.getCount() <= Config.maxStackSize && !itemStack.isEmpty();
			if (bl2 && bl3) {
				self.player.playerScreenHandler.getSlot(packet.getSlot()).setStack(itemStack);
				self.player.playerScreenHandler.sendContentUpdates();
			} else if (bl && bl3 && self.creativeItemDropThreshold < 200) {
				self.creativeItemDropThreshold += 20;
				self.player.dropItem(itemStack, true);
			}
		}

	}

}