package org.betonquest.betonquest.quest.event.logic;

import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.api.profiles.Profile;
import org.betonquest.betonquest.api.quest.action.Action;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;
import org.betonquest.betonquest.id.EventID;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * The First event. Similar to the folder, except it runs linearly through a list of events and
 * stops after the first one succeeds. This is intended to be used with condition: syntax in events.
 */
public class FirstPlayerAction implements Action {
    /**
     * The events to run.
     */
    private final List<EventID> events;

    /**
     * Makes a new first event.
     *
     * @param eventIDList A list of events to execute in order.
     */
    public FirstPlayerAction(final List<EventID> eventIDList) {
        events = eventIDList;
    }

    @Override
    public void execute(@Nullable final Profile profile) throws QuestRuntimeException {
        for (final EventID event : events) {
            if (BetonQuest.event(profile, event)) {
                break;
            }
        }
    }
}
