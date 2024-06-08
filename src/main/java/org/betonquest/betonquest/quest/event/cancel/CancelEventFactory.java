package org.betonquest.betonquest.quest.event.cancel;

import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.config.quest.QuestPackage;
import org.betonquest.betonquest.api.logger.BetonQuestLoggerFactory;
import org.betonquest.betonquest.api.quest.action.PlayerAction;
import org.betonquest.betonquest.api.quest.action.PlayerActionFactory;
import org.betonquest.betonquest.config.QuestCanceler;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.exceptions.ObjectNotFoundException;
import org.betonquest.betonquest.id.QuestCancelerID;
import org.betonquest.betonquest.quest.event.OnlineProfileRequiredPlayerAction;

/**
 * Factory for the cancel event.
 */
public class CancelEventFactory implements PlayerActionFactory {
    /**
     * Logger factory to create a logger for events.
     */
    private final BetonQuestLoggerFactory loggerFactory;

    /**
     * Creates a new cancel event factory.
     *
     * @param loggerFactory logger factory to use
     */
    public CancelEventFactory(final BetonQuestLoggerFactory loggerFactory) {
        this.loggerFactory = loggerFactory;
    }

    @Override
    public PlayerAction parseEvent(final Instruction instruction) throws InstructionParseException {
        final QuestPackage pack = instruction.getPackage();
        final String identifier = instruction.next();
        try {
            final QuestCancelerID cancelerID = new QuestCancelerID(pack, identifier);
            final QuestCanceler canceler = BetonQuest.getCanceler().get(cancelerID);
            return new OnlineProfileRequiredPlayerAction(loggerFactory.create(CancelPlayerAction.class), new CancelPlayerAction(canceler), pack);
        } catch (final ObjectNotFoundException e) {
            throw new InstructionParseException("Quest canceler '" + pack.getQuestPath() + "." + identifier + "' does not exist."
                    + " Ensure it was loaded without errors.", e);
        }
    }
}
