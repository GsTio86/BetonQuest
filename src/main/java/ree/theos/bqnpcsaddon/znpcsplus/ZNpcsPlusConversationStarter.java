package ree.theos.bqnpcsaddon.znpcsplus;

import lol.pyr.znpcsplus.api.event.NpcInteractEvent;
import lol.pyr.znpcsplus.api.interaction.InteractionType;
import lol.pyr.znpcsplus.api.npc.NpcEntry;
import org.betonquest.betonquest.api.logger.BetonQuestLogger;
import org.betonquest.betonquest.api.logger.BetonQuestLoggerFactory;
import org.betonquest.betonquest.api.profiles.OnlineProfile;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.BQNPCAdapter;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.NPCConversationStarter;
import org.betonquest.betonquest.id.ConversationID;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Starts new conversations with FancyNpcs NPCs.
 */
public class ZNpcsPlusConversationStarter extends NPCConversationStarter {
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

    @Override
    protected void startConversation(final OnlineProfile onlineProfile, final ConversationID conversationID, final BQNPCAdapter npc) {
        if (!(npc instanceof ZNpcsPlusBQAdapter)) {
            throw new IllegalArgumentException("The NPC Adapter is not a FancyNpcs Adapter!");
        }
        new ZNpcsPlusConversation(loggerFactory.create(ZNpcsPlusConversation.class), onlineProfile, conversationID,
                npc.getLocation(), ((ZNpcsPlusBQAdapter) npc).getZNpcsPlusEntry(), npc);
    }

    private boolean interactLogic(final NpcInteractEvent event) {
        final NpcEntry npc = event.getEntry();
        return super.interactLogic(event.getPlayer(), () -> new ZNpcsPlusBQAdapter(npc));
    }
}
