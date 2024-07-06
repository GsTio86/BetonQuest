package ree.theos.bqnpcsaddon.fancynpcs;

import de.oliver.fancynpcs.api.Npc;
import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.api.profiles.OnlineProfile;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.BQNPCAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * FancyNpcs Compatibility Adapter for general BetonQuest NPC behaviour.
 */
public class FancyNpcsBQAdapter implements BQNPCAdapter<Npc> {
    /**
     * The FancyNpcs NPC instance.
     */
    private final Npc npc;

    /**
     * Create a new FancyNpcs NPC Adapter.
     *
     * @param npc the FancyNpcs NPC instance
     */
    public FancyNpcsBQAdapter(final Npc npc) {
        this.npc = npc;
    }

    @Override
    public Npc getOriginal() {
        return npc;
    }

    @Override
    public String getId() {
        return npc.getData().getName();
    }

    @Override
    public String getName() {
        return npc.getData().getDisplayName();
    }

    @Override
    public String getFormattedName() {
        // TODO is this the full name?
        return npc.getData().getName();
    }

    @Override
    public Location getLocation() {
        return npc.getData().getLocation();
    }

    @Override
    public void teleport(final Location location) {
        npc.getData().setLocation(location);
        npc.moveForAll();
    }

    @Override
    public boolean isSpawned() {
        return npc.getData().isSpawnEntity();
    }

    @Override
    public void show(final OnlineProfile onlineProfile) {
        Bukkit.getScheduler().runTaskAsynchronously(BetonQuest.getInstance(), () -> {
            final Player player = onlineProfile.getPlayer();
            if (Boolean.TRUE != npc.getIsVisibleForPlayer().get(player.getUniqueId())) {
                npc.spawn(player);
            }
        });
    }

    @Override
    public void hide(final OnlineProfile onlineProfile) {
        Bukkit.getScheduler().runTaskAsynchronously(BetonQuest.getInstance(), () -> {
            final Player player = onlineProfile.getPlayer();
            if (Boolean.FALSE != npc.getIsVisibleForPlayer().get(player.getUniqueId())) {
                npc.remove(player);
            }
        });
    }
}
