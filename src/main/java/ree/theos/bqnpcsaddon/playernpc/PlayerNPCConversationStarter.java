package ree.theos.bqnpcsaddon.playernpc;

import dev.sergiferry.playernpc.api.NPC;
import org.betonquest.betonquest.api.logger.BetonQuestLogger;
import org.betonquest.betonquest.api.logger.BetonQuestLoggerFactory;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.NPCConversationStarter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Starts new conversations with PlayerNPC NPCs.
 */
public class PlayerNPCConversationStarter extends NPCConversationStarter<NPC> {
    /**
     * Initializes the listener.
     *
     * @param loggerFactory the logger factory used to create logger for the started conversations
     * @param log           the custom logger instance for this class
     */
    public PlayerNPCConversationStarter(final BetonQuestLoggerFactory loggerFactory, final BetonQuestLogger log) {
        super(loggerFactory, log);
    }

    @Override
    protected Listener newLeftClickListener() {
        return new Listener() {
            @EventHandler
            public void onLeft(final NPC.Events.Interact event) {
                if (event.isLeftClick() && interactLogic(event.getPlayer(), () -> new PlayerNPCBQAdapter(event.getNPC()))) {
                    event.setCancelled(true);
                }
            }
        };
    }

    @Override
    protected Listener newRightClickListener() {
        return new Listener() {
            @EventHandler
            public void onRight(final NPC.Events.Interact event) {
                if (event.isRightClick() && interactLogic(event.getPlayer(), () -> new PlayerNPCBQAdapter(event.getNPC()))) {
                    event.setCancelled(true);
                }
            }
        };
    }
}
