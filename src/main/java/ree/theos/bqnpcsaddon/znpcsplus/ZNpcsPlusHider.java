package ree.theos.bqnpcsaddon.znpcsplus;

import lol.pyr.znpcsplus.api.event.NpcSpawnEvent;
import org.betonquest.betonquest.api.logger.BetonQuestLogger;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.BQNPCAdapter;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.NPCHider;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.NPCSupplierStandard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class ZNpcsPlusHider extends NPCHider {
    /**
     * Create and start a new NPC Hider.
     *
     * @param log      the custom logger for this class
     * @param supplier the supplier to create {@link BQNPCAdapter} instances
     * @param type     the type identifying an entry or null if matches everything
     */
    public ZNpcsPlusHider(final BetonQuestLogger log, final NPCSupplierStandard supplier, final String type) {
        super(log, supplier, type);
    }

    /**
     * Cancels npc sending to player if the hide conditions are met.
     *
     * @param event the spawn event to listen
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onSpawn(final NpcSpawnEvent event) {
        if (isHidden(event.getEntry().getId(), event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
