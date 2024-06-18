package ree.theos.bqnpcsaddon.znpcsplus;

import lol.pyr.znpcsplus.api.npc.NpcEntry;
import org.betonquest.betonquest.api.logger.BetonQuestLogger;
import org.betonquest.betonquest.api.profiles.OnlineProfile;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.BQNPCAdapter;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.NPCConversation;
import org.betonquest.betonquest.id.ConversationID;
import org.bukkit.Location;

/**
 * Represents a conversation with FancyNpcs NPC.
 */
public class ZNpcsPlusConversation extends NPCConversation {
    /**
     * NPC for this conversation.
     */
    private final NpcEntry npcEntry;

    /**
     * {@inheritDoc}
     *
     * @param npcAdapter the npc adapter used for this conversation
     */
    public ZNpcsPlusConversation(final BetonQuestLogger log, final OnlineProfile onlineProfile, final ConversationID conversationID,
            final Location center, final NpcEntry npcEntry, final BQNPCAdapter npcAdapter) {
        super(log, onlineProfile, conversationID, center, npcAdapter);
        this.npcEntry = npcEntry;
    }

    /**
     * This will return the NPC associated with this conversation only after the conversation is created (all player options are listed and
     * ready to receive player input)
     * TODO is this javadoc valid?
     *
     * @return the NPC entry or null if it's too early
     */
    public NpcEntry getZNpcsPlusNpcEntry() {
        return npcEntry;
    }
}
