package de.craftery.jumpnrunintensify.handler;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.FlagValueChangeHandler;
import com.sk89q.worldguard.session.handler.Handler;
import de.craftery.jumpnrunintensify.Jumpnrunintensify;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomPotionEffectHandler extends FlagValueChangeHandler<StateFlag.State> {
    private static final Random random = new Random();

    private static final List<EffectConfig> SCARY_EFFECTS = Arrays.asList(
            new EffectConfig(PotionEffectType.BLINDNESS, 3, 1),
            new EffectConfig(PotionEffectType.NAUSEA, 7, 1),
            new EffectConfig(PotionEffectType.SLOWNESS, 3, 2),
            new EffectConfig(PotionEffectType.POISON, 3, 1),
            new EffectConfig(PotionEffectType.WITHER, 3, 1)
    );

    protected RandomPotionEffectHandler(Session session) {
        super(session, Jumpnrunintensify.RANDOM_POTION_FLAG);
    }

    public static final RandomPotionEffectHandler.Factory FACTORY = new RandomPotionEffectHandler.Factory();
    public static class Factory extends Handler.Factory<RandomPotionEffectHandler> {
        @Override
        public RandomPotionEffectHandler create(Session session) {
            return new RandomPotionEffectHandler(session);
        }
    }

    @Override
    public boolean onCrossBoundary(LocalPlayer player, Location from, Location to, ApplicableRegionSet toSet, Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {
        for (ProtectedRegion region : entered) {
            StateFlag.State flagValue = region.getFlag(Jumpnrunintensify.RANDOM_POTION_FLAG);

            if (flagValue == StateFlag.State.ALLOW) {
                Player bukkitPlayer = Bukkit.getPlayer(player.getUniqueId());
                if (bukkitPlayer != null) {
                    int randomIndex = random.nextInt(SCARY_EFFECTS.size());
                    EffectConfig chosenEffect = SCARY_EFFECTS.get(randomIndex);

                    int durationTicks = chosenEffect.durationSeconds * 20;

                    PotionEffect potionEffect = new PotionEffect(
                            chosenEffect.type,
                            durationTicks,
                            chosenEffect.amplifier,
                            false, // ambient
                            false  // icon
                    );

                    bukkitPlayer.addPotionEffect(potionEffect);
                }
            }
        }

        return true;
    }

    private record EffectConfig (PotionEffectType type, int durationSeconds, int amplifier) {}

    @Override
    protected void onInitialValue(LocalPlayer player, ApplicableRegionSet set, StateFlag.State value) {}

    @Override
    protected boolean onSetValue(LocalPlayer player, Location from, Location to, ApplicableRegionSet toSet, StateFlag.State currentValue, StateFlag.State lastValue, MoveType moveType) {
        return false;
    }

    @Override
    protected boolean onAbsentValue(LocalPlayer player, Location from, Location to, ApplicableRegionSet toSet, StateFlag.State lastValue, MoveType moveType) {
        return false;
    }
}
