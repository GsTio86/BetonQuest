package org.betonquest.betonquest.quest.event.run;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.quest.action.PlayerAction;
import org.betonquest.betonquest.api.quest.action.PlayerActionFactory;
import org.betonquest.betonquest.api.quest.action.StaticAction;
import org.betonquest.betonquest.api.quest.action.StaticActionFactory;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.id.ConditionID;
import org.betonquest.betonquest.id.EventID;
import org.betonquest.betonquest.quest.event.CallStaticPlayerActionAdapter;
import org.betonquest.betonquest.utils.PlayerConverter;

import java.util.List;

/**
 * Create new {@link RunForAllEvent} from instruction.
 */
public class RunForAllActionFactory implements StaticActionFactory, PlayerActionFactory {

    /**
     * Create new {@link RunForAllActionFactory}.
     */
    public RunForAllActionFactory() {
    }

    @Override
    public PlayerAction parseEvent(final Instruction instruction) throws InstructionParseException {
        return new CallStaticPlayerActionAdapter(parseStaticEvent(instruction));
    }

    @Override
    public StaticAction parseStaticEvent(final Instruction instruction) throws InstructionParseException {
        final List<EventID> events = instruction.getList(instruction.getOptional("events"), instruction::getEvent);
        final List<ConditionID> conditions = instruction.getList(instruction.getOptional("where"), instruction::getCondition);
        return new RunForAllEvent(PlayerConverter::getOnlineProfiles, events, conditions);
    }
}
