package org.betonquest.betonquest.quest.event.language;

import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.api.profiles.Profile;
import org.betonquest.betonquest.api.quest.action.PlayerAction;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;

/**
 * Changes player's language.
 */
public class LanguagePlayerAction implements PlayerAction {

    /**
     * The language to set.
     */
    private final String language;

    /**
     * The BetonQuest instance.
     */
    private final BetonQuest betonQuest;

    /**
     * Create the language event.
     *
     * @param language   the language to set
     * @param betonQuest the BetonQuest instance
     */
    public LanguagePlayerAction(final String language, final BetonQuest betonQuest) {
        this.language = language;
        this.betonQuest = betonQuest;
    }

    @Override
    public void execute(final Profile profile) throws QuestRuntimeException {
        betonQuest.getOfflinePlayerData(profile).setLanguage(language);
    }
}
