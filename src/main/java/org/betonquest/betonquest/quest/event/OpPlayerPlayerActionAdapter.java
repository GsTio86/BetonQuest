package org.betonquest.betonquest.quest.event;

import org.betonquest.betonquest.api.profiles.Profile;
import org.betonquest.betonquest.api.quest.action.PlayerAction;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;
import org.bukkit.entity.Player;

/**
 * Adapt an event to be run as Op.
 * <p>
 * Gives the player op, executes the nested event and then reverts the operation if necessary.
 */
public class OpPlayerPlayerActionAdapter implements PlayerAction {

    /**
     * The event to execute as Op.
     */
    private final PlayerAction event;

    /**
     * Creates a new OpPlayerEventAdapter.
     *
     * @param event the event to execute as op.
     */
    public OpPlayerPlayerActionAdapter(final PlayerAction event) {
        this.event = event;
    }

    @Override
    public void execute(final Profile profile) throws QuestRuntimeException {
        final Player player = profile.getOnlineProfile().get().getPlayer();
        final boolean previousOp = player.isOp();
        try {
            player.setOp(true);
            event.execute(profile);
        } finally {
            player.setOp(previousOp);
        }
    }
}
