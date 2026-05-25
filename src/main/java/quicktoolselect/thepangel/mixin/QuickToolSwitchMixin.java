package quicktoolselect.thepangel.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import quicktoolselect.thepangel.StateManager;
//? >1.20.1 {
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.Tool;
//?}
//? 1.21.11 {
/*import net.minecraft.world.inventory.ClickType;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;
*/
//?}

//? ~26.1 {
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.world.inventory.ContainerInput;
//?}
//? if 1.21.1 || 1.20.1{
/*import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.inventory.ClickType;

*///?}
//? 1.20.1 {
/*import net.minecraft.world.item.enchantment.EnchantmentHelper;

*///?}
// We hijack the function that handles the block pick up/switch when middle mouse clicking in vanilla, makes our job easer since it already gives us the BlocPos
//? if >1.21.1 {
@Mixin(MultiPlayerGameMode.class)
//?}
//? if 1.21.1 || 1.20.1{
/*@Mixin(Block.class)
 *///?}
public class QuickToolSwitchMixin {

    //? if >1.21.1 {
    @Final
    @Shadow
    private Minecraft minecraft;
    //?}

    //? if >1.21.1 {
    @Inject(at = @At("HEAD"), method = "handlePickItemFromBlock", cancellable = true)
    //?}
    //? if 1.21.1 || 1.20.1 {
    /*@Inject(at = @At("HEAD"), method = "getCloneItemStack", cancellable = true)
     *///?}
    //? if >1.21.1 {
    private void handlePickItemFromBlock(BlockPos pos, boolean includeData, CallbackInfo ci) {
        //?}
        //? if 1.21.1  {
        //private void getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState, CallbackInfoReturnable<ItemStack> ci) {
        //Minecraft minecraft = Minecraft.getInstance();
        //?}
        //? 1.20.1 {
        /*private void getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, CallbackInfoReturnable<ItemStack> ci) {
        Minecraft minecraft = Minecraft.getInstance();
        *///?}
        if (StateManager.selectBreak) {
            if (minecraft.level == null || minecraft.player == null || minecraft.gameMode == null) {
                //? <=1.21.1 {
                /*ci.setReturnValue(ItemStack.EMPTY);
                 *///?}
                ci.cancel();
                return;


            }
            //? if >1.21.1 {
            BlockState blockState = minecraft.level.getBlockState(pos);
            //?}

            int swapToolSlot = 0;
            AbstractContainerMenu menu = minecraft.player.containerMenu;

            // Loop through Inventory and compare the tools till we get the best one
            for (int i = 0; i < menu.slots.size(); i++) {
                ItemStack item = menu.slots.get(i).getItem();
                ItemStack swapTool = menu.slots.get(swapToolSlot).getItem();

                //? > 1.20.1 {
                Tool slotToolComponent = item.get(DataComponents.TOOL);
                //?} else {
                /*boolean slotToolComponent = item.is(ItemTags.TOOLS);
                *///?}
                //? > 1.20.1 {
                if (slotToolComponent == null || !slotToolComponent.isCorrectForDrops(blockState)) {
                    continue;
                }
                //?} else {
                /*if (!slotToolComponent || !item.isCorrectToolForDrops(blockState)) {
                    continue;
                }
                *///?}
                //? > 1.20.1 {
                Tool swapToolComponent = swapTool.get(DataComponents.TOOL);
                //?} else {
                /*boolean swapToolComponent = swapTool.is(ItemTags.TOOLS);
                *///?}
                //? > 1.20.1 {
                if (swapToolComponent == null || !slotToolComponent.isCorrectForDrops(blockState)) {
                    swapToolSlot = i;
                    continue;
                }
                //?} else {
                /*if(!swapToolComponent || !swapTool.isCorrectToolForDrops(blockState)) {
                    swapToolSlot = i;
                    continue;
                }
                *///?}
                //? > 1.20.1 {
                int swapEfficiency = swapTool.getEnchantments().getLevel(minecraft.player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.EFFICIENCY));
                int itemEfficiency = item.getEnchantments().getLevel(minecraft.player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.EFFICIENCY));

                if (slotToolComponent.getMiningSpeed(blockState) + (itemEfficiency * itemEfficiency + 1) >= swapToolComponent.getMiningSpeed(blockState) + (swapEfficiency * swapEfficiency + 1)) {
                    swapToolSlot = i;
                }
                //?} else {
                /*int swapEfficiency = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, swapTool);
                int itemEfficiency = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, item);
                if (item.getDestroySpeed(blockState) + (itemEfficiency * itemEfficiency + 1) >= swapTool.getDestroySpeed(blockState) + (swapEfficiency * swapEfficiency + 1)) {
                    swapToolSlot = i;
                }
                *///?}
            }
            //? 1.21.1 || 1.20.1 {
            /*minecraft.gameMode.handleInventoryMouseClick(minecraft.player.containerMenu.containerId, swapToolSlot, minecraft.player.getInventory().selected, ClickType.SWAP, minecraft.player);
             *///?}
            //? 1.21.11 {
            //minecraft.gameMode.handleInventoryMouseClick(minecraft.player.containerMenu.containerId, swapToolSlot, minecraft.player.getInventory().getSelectedSlot(), ClickType.SWAP, minecraft.player);
            //?}

            //? ~26.1 {
            minecraft.gameMode.handleContainerInput(minecraft.player.containerMenu.containerId, swapToolSlot, minecraft.player.getInventory().getSelectedSlot(), ContainerInput.SWAP, minecraft.player);
            //?}
            // We have to cancel the vanilla process or else we will pick up the block after our tool swap
            //? <=1.21.1 {
            /*ci.setReturnValue(ItemStack.EMPTY);
            *///?}


            ci.cancel();

        }
    }


}




