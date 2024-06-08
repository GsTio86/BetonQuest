package org.betonquest.betonquest.quest.event.logic;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.quest.action.Action;
import org.betonquest.betonquest.api.quest.action.ActionFactory;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.id.EventID;

import java.util.List;

/**
 * Factory to create FirstEvents from events from {@link Instruction}s.
 */
public class FirstEventFactory implements ActionFactory {

    /**
     * Empty constructor.
     */
    public FirstEventFactory() {

    }

    @Override
    public Action parseComposedEvent(final Instruction instruction) throws InstructionParseException {
        final List<EventID> list = instruction.getList(instruction::getEvent);

        return new FirstPlayerAction(list);
    }
}
