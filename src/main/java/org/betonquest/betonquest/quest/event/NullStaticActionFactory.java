package org.betonquest.betonquest.quest.event;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.quest.action.StaticAction;
import org.betonquest.betonquest.api.quest.action.StaticActionFactory;

/**
 * Factory for "static" events that always returns null.
 */
public class NullStaticActionFactory implements StaticActionFactory {
    /**
     * Create the factory.
     */
    public NullStaticActionFactory() {
    }

    @Override
    public StaticAction parseStaticEvent(final Instruction instruction) {
        return null;
    }
}
