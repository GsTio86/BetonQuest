package org.betonquest.betonquest.api.quest.action;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.exceptions.InstructionParseException;

/**
 * Factory to create a specific {@link StaticAction} from {@link Instruction}s.
 */
public interface StaticActionFactory {
    /**
     * Parses an instruction to create a {@link StaticAction}.
     *
     * @param instruction instruction to parse
     * @return "static" event represented by the instruction
     * @throws InstructionParseException when the instruction cannot be parsed
     */
    StaticAction parseStaticEvent(Instruction instruction) throws InstructionParseException;
}
