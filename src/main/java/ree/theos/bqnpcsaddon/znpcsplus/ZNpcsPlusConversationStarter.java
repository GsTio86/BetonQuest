package ree.theos.bqnpcsaddon.znpcsplus;

import lol.pyr.znpcsplus.api.event.NpcInteractEvent;
import lol.pyr.znpcsplus.api.interaction.InteractionType;
import lol.pyr.znpcsplus.api.npc.NpcEntry;
import org.betonquest.betonquest.api.logger.BetonQuestLogger;
import org.betonquest.betonquest.api.logger.BetonQuestLoggerFactory;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.NPCConversationStarter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Starts new conversations with FancyNpcs NPCs.
 */
public class ZNpcsPlusConversationStarter extends NPCConversationStarter<NpcEntry> {
    /**
     * Initializes the listener.
     *
     * @param loggerFactory the logger factory used to create logger for the started conversations
     * @param log           the custom logger instance for this class
     */
    public ZNpcsPlusConversationStarter(final BetonQuestLoggerFactory loggerFactory, final BetonQuestLogger log) {
        super(loggerFactory, log);
    }

    @Override
    protected Listener newLeftClickListener() {
        return new Listener() {
            /**
             * Starts the conversation on left click.
             */
            @EventHandler(ignoreCancelled = true)
            public void onLeft(final NpcInteractEvent event) {
                if (event.getClickType() == InteractionType.LEFT_CLICK && interactLogic(event)) {
                    event.setCancelled(true);
                }
            }
        };
    }

    @Override
    protected Listener newRightClickListener() {
        return new Listener() {
            /**
             * Starts the conversation on right click.
             */
            @EventHandler(ignoreCancelled = true)
            public void onRight(final NpcInteractEvent event) {
                if (event.getClickType() == InteractionType.RIGHT_CLICK && interactLogic(event)) {
                    event.setCancelled(true);
                }
            }
        };
    }

    private boolean interactLogic(final NpcInteractEvent event) {
        final NpcEntry npc = event.getEntry();
        return super.interactLogic(event.getPlayer(), () -> new ZNpcsPlusBQAdapter(npc));
    }
}
