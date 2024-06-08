package org.betonquest.betonquest.quest.event.point;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.quest.action.Action;
import org.betonquest.betonquest.api.quest.action.ActionFactory;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.utils.Utils;

/**
 * Factory to create delete points events from {@link Instruction}s.
 */
public class DeletePointEventFactory implements ActionFactory {

    /**
     * Create the delete points event factory.
     */
    public DeletePointEventFactory() {
    }

    @Override
    public Action parseComposedEvent(final Instruction instruction) throws InstructionParseException {
        final String category = Utils.addPackage(instruction.getPackage(), instruction.next());
        return new DeletePointPlayerAction(category);
    }
}
