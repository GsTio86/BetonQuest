package org.betonquest.betonquest.quest.event;

import org.betonquest.betonquest.api.quest.action.StaticAction;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;

import java.util.Arrays;

/**
 * A static event that is composed of other static events executed in sequence. If an error occurs execution is stopped
 * at that point.
 */
public class SequentialStaticEvent implements StaticAction {

    /**
     * Events to be executed.
     */
    private final StaticAction[] staticActions;

    /**
     * Create a static event sequence. The events at the front of the array will be executed first, at the end will be
     * executed last.
     *
     * @param staticActions events to be executed
     */
    public SequentialStaticEvent(final StaticAction... staticActions) {
        this.staticActions = Arrays.copyOf(staticActions, staticActions.length);
    }

    @Override
    public void execute() throws QuestRuntimeException {
        for (final StaticAction staticAction : staticActions) {
            staticAction.execute();
        }
    }
}
