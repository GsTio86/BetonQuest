package ree.theos.bqnpcsaddon.playernpc;

import dev.sergiferry.playernpc.api.NPC;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.BQNPCAdapter;
import org.bukkit.ChatColor;
import org.bukkit.Location;

/**
 * PlayerNPC Compatibility Adapter for general BetonQuest NPC behaviour.
 */
public class PlayerNPCBQAdapter implements BQNPCAdapter {
    /**
     * The PlayerNPC NPC instance.
     */
    private final NPC npc;

    /**
     * Create a new PlayerNPC NPC Adapter.
     *
     * @param npc the global NPC
     */
    public PlayerNPCBQAdapter(final NPC npc) {
        this.npc = npc;
    }

    /**
     * Gets the real FancyNpcs NPC.
     *
     * @return the adapted Citizens NPC
     */
    public final NPC getPlayerNPCEntry() {
        return npc;
    }

    @Override
    public String getId() {
        return npc.getFullID();
    }

    @Override
    public String getName() {
        return ChatColor.stripColor(npc.getTabListName());
    }

    @Override
    public String getFormattedName() {
        return npc.getTabListName();
    }

    @Override
    public Location getLocation() {
        return npc.getLocation();
    }

    @Override
    public void teleport(final Location location) {
        npc.teleport(location);
    }
}
