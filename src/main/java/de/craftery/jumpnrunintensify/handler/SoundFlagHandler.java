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
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SoundFlagHandler extends FlagValueChangeHandler<StateFlag.State>{
    private static final Random random = new Random();

    private static final List<Sound> SCARY_SOUNDS = Arrays.asList(
            Sound.AMBIENT_CAVE,
            Sound.ENTITY_GHAST_SCREAM,
            Sound.MUSIC_DISC_11,
            Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR,
            Sound.ENTITY_WITHER_SPAWN,
            Sound.ENTITY_LIGHTNING_BOLT_THUNDER,
            Sound.ENTITY_EVOKER_PREPARE_SUMMON,
            Sound.ENTITY_RAVAGER_ROAR,
            Sound.ENTITY_WARDEN_ROAR,
            Sound.ENTITY_GHAST_WARN
    );


    protected SoundFlagHandler(Session session) {
        super(session, Jumpnrunintensify.SOUND_FLAG);
    }

    public static final Factory FACTORY = new Factory();
    public static class Factory extends Handler.Factory<SoundFlagHandler> {
        @Override
        public SoundFlagHandler create(Session session) {
            return new SoundFlagHandler(session);
        }
    }

    @Override
    public boolean onCrossBoundary(LocalPlayer player, Location from, Location to, ApplicableRegionSet toSet, Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {
        for (ProtectedRegion region : entered) {
            StateFlag.State flagValue = region.getFlag(Jumpnrunintensify.SOUND_FLAG);

            if (flagValue == StateFlag.State.ALLOW) {
                Player bukkitPlayer = Bukkit.getPlayer(player.getUniqueId());
                if (bukkitPlayer != null) {
                    int randomIndex = random.nextInt(SCARY_SOUNDS.size());
                    Sound soundToPlay = SCARY_SOUNDS.get(randomIndex);

                    bukkitPlayer.playSound(bukkitPlayer.getLocation(), soundToPlay, 1f, 1.5f);
                }
            }
        }

        return true;
    }

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
