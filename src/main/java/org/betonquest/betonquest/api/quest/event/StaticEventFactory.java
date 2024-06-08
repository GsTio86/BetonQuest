package org.betonquest.betonquest.api.quest.event;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.quest.action.StaticActionFactory;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.jetbrains.annotations.ApiStatus;

/**
 * Factory to create a specific {@link StaticEvent} from {@link Instruction}s.
 *
 * @deprecated this class will be removed, use {@link StaticActionFactory}
 */
@Deprecated(forRemoval = true)
@ApiStatus.ScheduledForRemoval(inVersion = "3.0")
public interface StaticEventFactory extends StaticActionFactory {
    /**
     * Parses an instruction to create a {@link StaticEvent}.
     *
     * @param instruction instruction to parse
     * @return "static" event represented by the instruction
     * @throws InstructionParseException when the instruction cannot be parsed
     */
    @Override
    StaticEvent parseStaticEvent(Instruction instruction) throws InstructionParseException;
}
