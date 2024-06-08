package org.betonquest.betonquest.quest.event.journal;

import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.api.profiles.Profile;
import org.betonquest.betonquest.api.quest.action.PlayerAction;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;

/**
 * Gives journal to the player.
 */
public class GiveJournalPlayerAction implements PlayerAction {

    /**
     * Creates a new GiveJournalEvent.
     */
    public GiveJournalPlayerAction() {
    }

    @Override
    public void execute(final Profile profile) throws QuestRuntimeException {
        BetonQuest.getInstance().getPlayerData(profile).getJournal().addToInv();
    }
}
