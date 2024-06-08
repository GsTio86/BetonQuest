package org.betonquest.betonquest.quest.event.point;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.quest.action.PlayerAction;
import org.betonquest.betonquest.api.quest.action.PlayerActionFactory;
import org.betonquest.betonquest.api.quest.action.StaticAction;
import org.betonquest.betonquest.api.quest.action.StaticActionFactory;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.quest.event.CallStaticPlayerActionAdapter;
import org.betonquest.betonquest.utils.Utils;

/**
 * Factory for the delete global point event.
 */
public class DeleteGlobalPointActionFactory implements PlayerActionFactory, StaticActionFactory {

    /**
     * Creates a new DeleteGlobalPointEventFactory.
     */
    public DeleteGlobalPointActionFactory() {
    }

    @Override
    public StaticAction parseStaticEvent(final Instruction instruction) throws InstructionParseException {
        final String category = Utils.addPackage(instruction.getPackage(), instruction.next());
        return new DeleteGlobalPointEvent(category);
    }

    @Override
    public PlayerAction parseEvent(final Instruction instruction) throws InstructionParseException {
        return new CallStaticPlayerActionAdapter(parseStaticEvent(instruction));
    }
}
