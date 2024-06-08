package org.betonquest.betonquest.quest.event.teleport;

import org.betonquest.betonquest.api.profiles.Profile;
import org.betonquest.betonquest.api.quest.action.PlayerAction;
import org.betonquest.betonquest.conversation.Conversation;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;
import org.betonquest.betonquest.utils.location.CompoundLocation;
import org.bukkit.Location;

/**
 * Teleports the player to specified location
 */
public class TeleportPlayerAction implements PlayerAction {
    /**
     * Location to teleport to.
     */
    private final CompoundLocation location;

    /**
     * Create a new teleport event that teleports the player to the given location.
     *
     * @param location location to teleport to
     */
    public TeleportPlayerAction(final CompoundLocation location) {
        this.location = location;
    }

    @Override
    public void execute(final Profile profile) throws QuestRuntimeException {
        final Conversation conv = Conversation.getConversation(profile);
        if (conv != null) {
            conv.endConversation();
        }
        final Location playerLocation = location.getLocation(profile);
        profile.getOnlineProfile().get().getPlayer().teleport(playerLocation);
    }
}
