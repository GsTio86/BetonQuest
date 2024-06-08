package org.betonquest.betonquest.quest.event.run;

import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.api.quest.action.StaticAction;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;
import org.betonquest.betonquest.id.EventID;
import org.betonquest.betonquest.quest.event.CallStaticPlayerActionAdapter;

import java.util.List;

/**
 * Runs specified events player independently.
 * <p>
 * Although the implementation is a {@link StaticAction}, using it in a static context does not make much sense.
 * Recommended usage is to wrap it in a {@link CallStaticPlayerActionAdapter} and using it to call static events
 * from non-static context.
 */
public class RunIndependentEvent implements StaticAction {

    /**
     * List of Events to run.
     */
    private final List<EventID> events;

    /**
     * Create a new RunIndependentEvent instance.
     *
     * @param events the events to run
     */
    public RunIndependentEvent(final List<EventID> events) {
        this.events = events;
    }

    @Override
    public void execute() throws QuestRuntimeException {
        for (final EventID event : events) {
            BetonQuest.event(null, event);
        }
    }
}
