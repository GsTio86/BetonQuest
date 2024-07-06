package ree.theos.bqnpcsaddon.znpcsplus;

import lol.pyr.znpcsplus.api.hologram.Hologram;
import lol.pyr.znpcsplus.api.npc.NpcEntry;
import lol.pyr.znpcsplus.util.NpcLocation;
import org.betonquest.betonquest.api.profiles.OnlineProfile;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.BQNPCAdapter;
import org.bukkit.ChatColor;
import org.bukkit.Location;

/**
 * FancyNpcs Compatibility Adapter for general BetonQuest NPC behaviour.
 */
public class ZNpcsPlusBQAdapter implements BQNPCAdapter<NpcEntry> {
    /**
     * The ZNpcsPlus NPC instance.
     */
    private final NpcEntry entry;

    /**
     * Create a new ZNpcsPlus NPC Adapter.
     *
     * @param npcEntry the FancyNpcs NPC entry
     */
    public ZNpcsPlusBQAdapter(final NpcEntry npcEntry) {
        this.entry = npcEntry;
    }

    @Override
    public NpcEntry getOriginal() {
        return entry;
    }

    @Override
    public String getId() {
        return entry.getId();
    }

    @Override
    public String getName() {
        return ChatColor.stripColor(getHoloName());
    }

    @Override
    public String getFormattedName() {
        return getHoloName();
    }

    @Override
    public Location getLocation() {
        return entry.getNpc().getLocation().toBukkitLocation(entry.getNpc().getWorld());
    }

    @Override
    public void teleport(final Location location) {
        entry.getNpc().setLocation(new NpcLocation(location));
    }

    @Override
    public boolean isSpawned() {
        return entry.getNpc().isEnabled();
    }

    @Override
    public void show(final OnlineProfile onlineProfile) {
        entry.getNpc().show(onlineProfile.getPlayer());
    }

    @Override
    public void hide(final OnlineProfile onlineProfile) {
        entry.getNpc().hide(onlineProfile.getPlayer());
    }

    private String getHoloName() {
        final Hologram hologram = entry.getNpc().getHologram();
        if (hologram.lineCount() == 0) {
            return "";
        }
        return hologram.getLine(0);
    }
}
