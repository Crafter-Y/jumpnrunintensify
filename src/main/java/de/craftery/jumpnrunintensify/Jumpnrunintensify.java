package de.craftery.jumpnrunintensify;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.session.SessionManager;
import de.craftery.jumpnrunintensify.handler.SoundFlagHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class Jumpnrunintensify extends JavaPlugin {
    public static final Logger LOGGER = LoggerFactory.getLogger("craftinghomes");

    public static StateFlag SOUND_FLAG;

    @Override
    public void onLoad() {
        LOGGER.info("Registering WorldGuard Flags");

        SOUND_FLAG = registerFlag("jnr-sound-effect");
    }

    @Override
    public void onEnable() {
        LOGGER.info("Starting plugin");

        SessionManager sm = WorldGuard.getInstance().getPlatform().getSessionManager();
        sm.registerHandler(SoundFlagHandler.FACTORY, null);
    }

    private StateFlag registerFlag(String name) {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag(name, true);
            registry.register(flag);
            return flag;
        } catch (FlagConflictException e) {
            // some other plugin registered a flag by the same name already.
            // you can use the existing flag, but this may cause conflicts - be sure to check type
            Flag<?> existing = registry.get(name);
            if (existing instanceof StateFlag) {
                return (StateFlag) existing;
            } else {
                LOGGER.error("WorldGuard flag '" + name + "' could not be registered!");
                return null;
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
