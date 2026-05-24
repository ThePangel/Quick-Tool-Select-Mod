package quicktoolselect.thepangel;



//? !bare_bones {
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
//?}

// Handling YACL config menu
public class Config {
    public static Integer holdTime = 10;

    //? !bare_bones {
    public static Screen createScreen(Screen parentScreen) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Quick Tool Select Config"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("Settings"))
                        .option(Option.<Integer>createBuilder()
                                .name(Component.literal("Hold Time"))
                                .description(OptionDescription.of(Component.literal("Set the hold time in ticks (20 ticks = 1s)")))
                                .binding(
                                        10,
                                        () -> holdTime,
                                        newVal -> holdTime = newVal
                                )
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(2, 40)
                                        .step(1))
                                .build()
                        )
                        .build()
                )
                .build()
                .generateScreen(parentScreen);
    }
    //?}
}
