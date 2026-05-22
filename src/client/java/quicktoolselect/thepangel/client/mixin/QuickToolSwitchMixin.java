package quicktoolselect.thepangel.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import quicktoolselect.thepangel.client.StateManager;

// We hijack the function that handles the block pick up/switch when middle mouse clicking in vanilla, makes our job easer since it already gives us the BlocPos
@Mixin(MultiPlayerGameMode.class)
public class QuickToolSwitchMixin {


    @Final
    @Shadow
    private Minecraft minecraft;


    @Inject(at = @At("HEAD"), method = "handlePickItemFromBlock", cancellable = true)
    private void handlePickItemFromBlock(BlockPos pos, boolean includeData, CallbackInfo ci) {

        if (StateManager.selectBreak) {
            if (minecraft.level == null || minecraft.player == null || minecraft.gameMode == null) {
                ci.cancel();
                return;
            }
            BlockState blockState = minecraft.level.getBlockState(pos);

            int swapToolSlot = 0;
            AbstractContainerMenu menu = minecraft.player.containerMenu;

            // Loop through Inventory and compare the tools till we get the best one
            for (int i = 0; i < menu.slots.size(); i++) {
                ItemStack item = menu.slots.get(i).getItem();
                ItemStack swapTool = menu.slots.get(swapToolSlot).getItem();

                Tool slotToolComponent = item.get(DataComponents.TOOL);

                if (slotToolComponent == null || !slotToolComponent.isCorrectForDrops(blockState)) {
                    continue;
                }


                Tool swapToolComponent = swapTool.get(DataComponents.TOOL);
                if (swapToolComponent == null || !slotToolComponent.isCorrectForDrops(blockState)) {
                    swapToolSlot = i;
                    continue;
                }

                int swapEfficiency = swapTool.getEnchantments().getLevel(minecraft.player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.EFFICIENCY));
                int itemEfficiency = item.getEnchantments().getLevel(minecraft.player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.EFFICIENCY));

                if (slotToolComponent.getMiningSpeed(blockState) + (itemEfficiency * itemEfficiency + 1) >= swapToolComponent.getMiningSpeed(blockState) + (swapEfficiency * swapEfficiency + 1)) {
                    swapToolSlot = i;
                }
            }

            minecraft.gameMode.handleContainerInput(minecraft.player.containerMenu.containerId, swapToolSlot, minecraft.player.getInventory().getSelectedSlot(), ContainerInput.SWAP, minecraft.player);

            // We have to cancel the vanilla process or else we will pick up the block after our tool swap
            ci.cancel();

        }
    }


}




