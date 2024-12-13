package org.betonquest.betonquest.quest.registry.type;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.exceptions.InstructionParseException;

/**
 * A factory to create a Quest Type from an {@link Instruction}.
 *
 * @param <T> the type to create
 */
public interface TypeFactory<T> {
    /**
     * Create a new {@link T} from an {@link Instruction}.
     *
     * @param instruction the instruction to parse
     * @return the newly created {@link T}
     * @throws InstructionParseException if the instruction cannot be parsed
     */
    T parseInstruction(Instruction instruction) throws InstructionParseException;
}
