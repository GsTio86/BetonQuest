package org.betonquest.betonquest.api.quest.action;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.exceptions.InstructionParseException;

/**
 * Factory to create a specific {@link PlayerAction} from {@link Instruction}s.
 */
public interface PlayerActionFactory {
    /**
     * Parses an instruction to create a normal {@link PlayerAction}.
     *
     * @param instruction instruction to parse
     * @return normal event represented by the instruction
     * @throws InstructionParseException when the instruction cannot be parsed
     */
    PlayerAction parseEvent(Instruction instruction) throws InstructionParseException;
}
