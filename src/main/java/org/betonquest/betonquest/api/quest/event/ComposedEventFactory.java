package org.betonquest.betonquest.api.quest.event;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.quest.action.ActionFactory;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.jetbrains.annotations.ApiStatus;

/**
 * Factory to create a specific {@link ComposedEvent} from {@link Instruction}s.
 *
 * @deprecated this class will be removed, use {@link ActionFactory}
 */
@Deprecated(forRemoval = true)
@ApiStatus.ScheduledForRemoval(inVersion = "3.0")
public interface ComposedEventFactory extends ActionFactory {
    /**
     * Parses an instruction to create a {@link ComposedEvent}.
     *
     * @param instruction instruction to parse
     * @return hybrid event represented by the instruction
     * @throws InstructionParseException when the instruction cannot be parsed
     */
    @Override
    ComposedEvent parseComposedEvent(Instruction instruction) throws InstructionParseException;
}
