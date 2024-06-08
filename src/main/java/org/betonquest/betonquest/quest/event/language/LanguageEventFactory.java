package org.betonquest.betonquest.quest.event.language;

import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.quest.action.PlayerAction;
import org.betonquest.betonquest.api.quest.action.PlayerActionFactory;
import org.betonquest.betonquest.exceptions.InstructionParseException;

/**
 * Factory to create language events from {@link Instruction}s.
 */
public class LanguageEventFactory implements PlayerActionFactory {

    /**
     * The Betonquest instance.
     */
    private final BetonQuest betonQuest;

    /**
     * Create the language event factory.
     *
     * @param betonQuest the BetonQuest instance
     */
    public LanguageEventFactory(final BetonQuest betonQuest) {
        this.betonQuest = betonQuest;
    }

    @Override
    public PlayerAction parseEvent(final Instruction instruction) throws InstructionParseException {
        final String language = instruction.next();
        return new LanguagePlayerAction(language, betonQuest);
    }
}
