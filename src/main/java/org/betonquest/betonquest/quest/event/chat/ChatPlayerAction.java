package org.betonquest.betonquest.quest.event.chat;

import org.betonquest.betonquest.api.profiles.Profile;
import org.betonquest.betonquest.api.quest.action.PlayerAction;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;
import org.bukkit.entity.Player;

/**
 * The chat event.
 */
public class ChatPlayerAction implements PlayerAction {

    /**
     * The messages.
     */
    private final String[] messages;

    /**
     * Creates a new chat event.
     *
     * @param messages the messages
     */
    public ChatPlayerAction(final String... messages) {
        this.messages = messages.clone();
    }

    @Override
    public void execute(final Profile profile) throws QuestRuntimeException {
        final Player player = profile.getOnlineProfile().get().getPlayer();
        for (final String message : messages) {
            player.chat(message.replace("%player%", player.getName()));
        }
    }
}
