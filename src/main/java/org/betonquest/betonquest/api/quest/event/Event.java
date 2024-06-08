package org.betonquest.betonquest.api.quest.event;

import org.betonquest.betonquest.api.quest.action.PlayerAction;
import org.jetbrains.annotations.ApiStatus;

/**
 * Interface for quest-events that are executed for a profile. It represents the normal event as described in the
 * BetonQuest user documentation. It does not represent the "static" variant though, see {@link StaticEvent}.
 *
 * @deprecated this class will be removed, use {@link PlayerAction}
 */
@Deprecated(forRemoval = true)
@ApiStatus.ScheduledForRemoval(inVersion = "3.0")
public interface Event extends PlayerAction {
}
