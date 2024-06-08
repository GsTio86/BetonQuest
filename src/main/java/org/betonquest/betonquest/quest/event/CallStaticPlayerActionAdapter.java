package org.betonquest.betonquest.quest.event;

import org.betonquest.betonquest.api.profiles.Profile;
import org.betonquest.betonquest.api.quest.action.PlayerAction;
import org.betonquest.betonquest.api.quest.action.StaticAction;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;

/**
 * Adapter to allow executing a "static" event with the API of a normal event.
 */
public class CallStaticPlayerActionAdapter implements PlayerAction {

    /**
     * The "static" event to execute.
     */
    private final StaticAction event;

    /**
     * Create a normal event that will execute the given "static" event.
     *
     * @param event "static" event to execute
     */
    public CallStaticPlayerActionAdapter(final StaticAction event) {
        this.event = event;
    }

    @Override
    public void execute(final Profile profile) throws QuestRuntimeException {
        event.execute();
    }
}
