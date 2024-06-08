package org.betonquest.betonquest.api.quest.event;

import org.betonquest.betonquest.api.quest.action.StaticAction;
import org.jetbrains.annotations.ApiStatus;

/**
 * Interface for "static" quest-events.
 * It represents the "static" event as described in the BetonQuest user documentation.
 * They may act on all players, only online player or even no player at all; this is implementation detail.
 * For the normal event variant see {@link Event}.
 *
 * @deprecated this class will be removed, use {@link StaticAction}
 */
@Deprecated(forRemoval = true)
@ApiStatus.ScheduledForRemoval(inVersion = "3.0")
public interface StaticEvent extends StaticAction {
}
