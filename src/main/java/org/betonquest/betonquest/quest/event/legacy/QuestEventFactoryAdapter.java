package org.betonquest.betonquest.quest.event.legacy;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.QuestEvent;
import org.betonquest.betonquest.api.quest.action.PlayerAction;
import org.betonquest.betonquest.api.quest.action.PlayerActionFactory;
import org.betonquest.betonquest.api.quest.action.StaticAction;
import org.betonquest.betonquest.api.quest.action.StaticActionFactory;
import org.betonquest.betonquest.exceptions.InstructionParseException;

/**
 * Adapter to let {@link PlayerActionFactory EventFactories} create {@link QuestEvent}s from the {@link PlayerAction}s and
 * {@link StaticAction}s they create.
 */
public class QuestEventFactoryAdapter implements QuestEventFactory {

    /**
     * The event factory to be adapted.
     */
    private final PlayerActionFactory factory;

    /**
     * The static event factory to be adapted.
     */
    private final StaticActionFactory staticFactory;

    /**
     * Create the factory from an {@link PlayerActionFactory}.
     *
     * @param factory       event factory to use
     * @param staticFactory static event factory to use
     */
    public QuestEventFactoryAdapter(final PlayerActionFactory factory, final StaticActionFactory staticFactory) {
        this.factory = factory;
        this.staticFactory = staticFactory;
    }

    @Override
    public QuestEventAdapter parseEventInstruction(final Instruction instruction) throws InstructionParseException {
        final PlayerAction event = factory.parseEvent(instruction.copy());
        final StaticAction staticAction = staticFactory.parseStaticEvent(instruction.copy());
        return new QuestEventAdapter(instruction, event, staticAction);
    }
}
