package org.betonquest.betonquest.api.quest.action;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.exceptions.InstructionParseException;

/**
 * Factory to create a specific {@link Action} from {@link Instruction}s.
 */
public interface ActionFactory {
    /**
     * Parses an instruction to create a {@link Action}.
     *
     * @param instruction instruction to parse
     * @return hybrid event represented by the instruction
     * @throws InstructionParseException when the instruction cannot be parsed
     */
    Action parseComposedEvent(Instruction instruction) throws InstructionParseException;
}
