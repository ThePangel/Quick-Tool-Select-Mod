package quicktoolselect.thepangel;

//? !bare_bones {
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
//?}

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.fabricmc.api.ModInitializer;


public class QuickToolSelect implements ModInitializer {
    public static final String MOD_ID = "quicktoolselect";
    private static final Logger LOGGER = LogManager.getLogger(QuickToolSelect.MOD_ID);

    @Override
    public void onInitialize() {
        //? !bare_bones  {
        // If the user for some reason doesn't have mod menu this command will open the settings screen
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommands.literal("quicktool")
                    .executes(context -> {
                        context.getSource().getClient().execute(() -> {
                            context.getSource().getClient().setScreen(quicktoolselect.thepangel.Config.createScreen(null));
                        });
                        return 1;
                    })
            );
        });
        //?}
        LOGGER.info("Quick Tool Select Initialized!");

    }
}