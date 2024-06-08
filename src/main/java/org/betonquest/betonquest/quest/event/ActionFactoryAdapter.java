package org.betonquest.betonquest.quest.event;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.quest.action.ActionFactory;
import org.betonquest.betonquest.api.quest.action.PlayerAction;
import org.betonquest.betonquest.api.quest.action.PlayerActionFactory;
import org.betonquest.betonquest.api.quest.action.StaticAction;
import org.betonquest.betonquest.api.quest.action.StaticActionFactory;
import org.betonquest.betonquest.exceptions.InstructionParseException;

/**
 * Factory adapter for that will provide both {@link PlayerAction} and {@link StaticAction} implementations
 * from the supplied {@link ActionFactory}.
 */
public class ActionFactoryAdapter implements PlayerActionFactory, StaticActionFactory {
    /**
     * Composed event factory used to create new actions and static actions.
     */
    private final ActionFactory actionFactory;

    /**
     * Create a new ComposedEventFactoryAdapter to create {@link PlayerAction}s and {@link StaticAction}s from it.
     *
     * @param actionFactory the factory used to parse the instruction.
     */
    public ActionFactoryAdapter(final ActionFactory actionFactory) {
        this.actionFactory = actionFactory;
    }

    @Override
    public PlayerAction parseEvent(final Instruction instruction) throws InstructionParseException {
        return actionFactory.parseComposedEvent(instruction);
    }

    @Override
    public StaticAction parseStaticEvent(final Instruction instruction) throws InstructionParseException {
        return actionFactory.parseComposedEvent(instruction);
    }
}
