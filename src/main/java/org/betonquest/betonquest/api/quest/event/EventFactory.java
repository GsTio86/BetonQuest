package org.betonquest.betonquest.api.quest.event;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.quest.action.PlayerActionFactory;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.jetbrains.annotations.ApiStatus;

/**
 * Factory to create a specific {@link Event} from {@link Instruction}s.
 *
 * @deprecated this class will be removed, use {@link PlayerActionFactory}
 */
@Deprecated(forRemoval = true)
@ApiStatus.ScheduledForRemoval(inVersion = "3.0")
public interface EventFactory extends PlayerActionFactory {
    /**
     * Parses an instruction to create a normal {@link Event}.
     *
     * @param instruction instruction to parse
     * @return normal event represented by the instruction
     * @throws InstructionParseException when the instruction cannot be parsed
     */
    @Override
    Event parseEvent(Instruction instruction) throws InstructionParseException;
}
