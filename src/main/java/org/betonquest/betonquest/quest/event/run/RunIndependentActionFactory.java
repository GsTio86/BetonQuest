package org.betonquest.betonquest.quest.event.run;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.quest.action.PlayerAction;
import org.betonquest.betonquest.api.quest.action.PlayerActionFactory;
import org.betonquest.betonquest.api.quest.action.StaticAction;
import org.betonquest.betonquest.api.quest.action.StaticActionFactory;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.id.EventID;
import org.betonquest.betonquest.quest.event.CallStaticPlayerActionAdapter;

import java.util.List;

/**
 * Create new {@link RunIndependentEvent} from instruction.
 */
public class RunIndependentActionFactory implements StaticActionFactory, PlayerActionFactory {

    /**
     * Create new {@link RunIndependentActionFactory}.
     */
    public RunIndependentActionFactory() {
    }

    @Override
    public StaticAction parseStaticEvent(final Instruction instruction) throws InstructionParseException {
        final List<EventID> events = instruction.getList(instruction.getOptional("events"), instruction::getEvent);
        return new RunIndependentEvent(events);
    }

    @Override
    public PlayerAction parseEvent(final Instruction instruction) throws InstructionParseException {
        return new CallStaticPlayerActionAdapter(parseStaticEvent(instruction));
    }
}
