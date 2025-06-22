package de.craftery.jumpnrunintensify;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.session.SessionManager;
import de.craftery.jumpnrunintensify.handler.RandomPotionEffectHandler;
import de.craftery.jumpnrunintensify.handler.SoundFlagHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class Jumpnrunintensify extends JavaPlugin {
    public static final Logger LOGGER = LoggerFactory.getLogger("craftinghomes");

    public static StateFlag SOUND_FLAG;
    public static StateFlag RANDOM_POTION_FLAG;
    public static StateFlag BLINDNESS_FLAG;
    public static StateFlag SLOWNESS_FLAG;
    public static StateFlag FREEZE_FLAG;
    public static StateFlag NO_JUMP_FLAG;
    public static StateFlag SPEED_BOOST_FLAG;
    public static StateFlag NAUSEA_FLAG;

    @Override
    public void onLoad() {
        LOGGER.info("Registering WorldGuard Flags");

        SOUND_FLAG = registerFlag("jnr-sound-effect");
        RANDOM_POTION_FLAG = registerFlag("jnr-random-potion");
        BLINDNESS_FLAG = registerFlag("jnr-blindness");
        SLOWNESS_FLAG = registerFlag("jnr-slowness");
        FREEZE_FLAG = registerFlag("jnr-freeze");
        NO_JUMP_FLAG = registerFlag("jnr-no-jump");
        SPEED_BOOST_FLAG = registerFlag("jnr-speed-boost");
        NAUSEA_FLAG = registerFlag("jnr-nausea");

    }

    @Override
    public void onEnable() {
        LOGGER.info("Starting plugin");

        SessionManager sm = WorldGuard.getInstance().getPlatform().getSessionManager();
        sm.registerHandler(SoundFlagHandler.FACTORY, null);
        sm.registerHandler(RandomPotionEffectHandler.FACTORY, null);
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
