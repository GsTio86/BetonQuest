package org.betonquest.betonquest.quest.event.kill;

import org.betonquest.betonquest.api.profiles.Profile;
import org.betonquest.betonquest.api.quest.action.PlayerAction;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;
import org.bukkit.entity.Player;

/**
 * Kills the player.
 */
public class KillPlayerAction implements PlayerAction {

    /**
     * Creates a new kill event.
     */
    public KillPlayerAction() {
    }

    @Override
    public void execute(final Profile profile) throws QuestRuntimeException {
        final Player player = profile.getOnlineProfile().get().getPlayer();
        player.setHealth(0);
    }
}
