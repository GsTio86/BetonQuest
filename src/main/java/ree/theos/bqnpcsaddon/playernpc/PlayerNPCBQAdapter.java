package ree.theos.bqnpcsaddon.playernpc;

import dev.sergiferry.playernpc.api.NPC;
import org.betonquest.betonquest.api.profiles.OnlineProfile;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.BQNPCAdapter;
import org.bukkit.ChatColor;
import org.bukkit.Location;

/**
 * PlayerNPC Compatibility Adapter for general BetonQuest NPC behaviour.
 */
public class PlayerNPCBQAdapter implements BQNPCAdapter<NPC> {
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

    @Override
    public final NPC getOriginal() {
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

    @Override
    public boolean isSpawned() {
        return !npc.isInvisible();
    }

    @Override
    public void show(final OnlineProfile onlineProfile) {
        if (npc instanceof NPC.Global global) {
            global.show(onlineProfile.getPlayer());
        } else if (npc instanceof NPC.Personal personal
                && personal.getPlayer().getUniqueId().equals(onlineProfile.getPlayerUUID())) {
            personal.show();
        }
    }

    @Override
    public void hide(final OnlineProfile onlineProfile) {
        if (npc instanceof NPC.Global global) {
            global.hide(onlineProfile.getPlayer());
        } else if (npc instanceof NPC.Personal personal
                && personal.getPlayer().getUniqueId().equals(onlineProfile.getPlayerUUID())) {
            personal.hide();
        }
    }
}
