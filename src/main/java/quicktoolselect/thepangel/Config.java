package quicktoolselect.thepangel;




import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
//? 1.20.1 {
//import net.minecraft.resources.ResourceLocation;
//?}


// Handling YACL config menu
public class Config {



    @SerialEntry
    public int holdTime = 10;

    public static ConfigClassHandler<Config> HANDLER = ConfigClassHandler.createBuilder(Config.class)
            //? > 1.20.1 {
            .id(Identifier.fromNamespaceAndPath("quicktoolselect", "qts_config"))
            //?} else {
            //.id(new ResourceLocation("quicktoolselect", "qts_config"))

            //?}
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("quicktoolselect.json5"))
                    .setJson5(true)
                    .build())
            .build();


    public static Screen createScreen(Screen parentScreen) {
        Config instance = HANDLER.instance();
        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Quick Tool Select Config"))
                .save(HANDLER::save)
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("Settings"))
                        .option(Option.<Integer>createBuilder()
                                .name(Component.literal("Hold Time"))
                                .description(OptionDescription.of(Component.literal("Set the hold time in ticks (20 ticks = 1s)")))
                                .binding(
                                        5,
                                        () -> instance.holdTime,
                                        newVal -> instance.holdTime = newVal
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

}

