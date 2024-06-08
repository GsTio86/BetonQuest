package org.betonquest.betonquest.quest.event.conversation;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.logger.BetonQuestLoggerFactory;
import org.betonquest.betonquest.api.quest.action.PlayerAction;
import org.betonquest.betonquest.api.quest.action.PlayerActionFactory;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.quest.event.OnlineProfileRequiredPlayerAction;

/**
 * Factory to create conversation cancel events from {@link Instruction}s.
 */
public class CancelConversationEventFactory implements PlayerActionFactory {
    /**
     * Logger factory to create a logger for events.
     */
    private final BetonQuestLoggerFactory loggerFactory;

    /**
     * Create the conversation cancel event factory.
     *
     * @param loggerFactory logger factory to use
     */
    public CancelConversationEventFactory(final BetonQuestLoggerFactory loggerFactory) {
        this.loggerFactory = loggerFactory;
    }

    @Override
    public PlayerAction parseEvent(final Instruction instruction) throws InstructionParseException {
        return new OnlineProfileRequiredPlayerAction(
                loggerFactory.create(CancelConversationPlayerAction.class), new CancelConversationPlayerAction(), instruction.getPackage());
    }
}
