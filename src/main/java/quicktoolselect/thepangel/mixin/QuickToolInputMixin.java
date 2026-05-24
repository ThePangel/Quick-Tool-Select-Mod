package quicktoolselect.thepangel.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import quicktoolselect.thepangel.Config;
import quicktoolselect.thepangel.StateManager;

// We Hijack handleKeybinds to add our hold middle mouse button logic.
@Mixin(Minecraft.class)
public class QuickToolInputMixin {

    @Final
    @Shadow
    public Options options;

    @Shadow
    private void pickBlockOrEntity() {
    }

    @Inject(at = @At("HEAD"), method = "handleKeybinds")

    private void handleKeybinds(CallbackInfo ci) {
        // This handles the release of the middle mouse button
        if (!this.options.keyPickItem.isDown()) {

            if (StateManager.tick_count >= 1) {

                StateManager.tick_count = 0;
                StateManager.selectBreak = false;

                this.pickBlockOrEntity();
            }

            StateManager.tick_count = 0;
            StateManager.selectBreak = false;

        // And this handles the press of the middle mouse button
        } else {

            if (!StateManager.selectBreak) {

                StateManager.tick_count++;
            }

            if (StateManager.tick_count >= Config.holdTime) {

                StateManager.tick_count = 0;
                StateManager.selectBreak = true;

                this.pickBlockOrEntity();
            }
            // Took me a while to figure this out, we consume the click or else it will stay in queue and run the vanilla action after ours
            this.options.keyPickItem.consumeClick();

        }
    }
}
