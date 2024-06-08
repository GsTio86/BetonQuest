package org.betonquest.betonquest.api.quest.event;

import org.betonquest.betonquest.api.profiles.Profile;
import org.betonquest.betonquest.api.quest.action.Action;
import org.jetbrains.annotations.ApiStatus;

/**
 * An {@link Event} which can be executed with a profile or static.
 * <p>
 * Common usage is when containing {@link org.betonquest.betonquest.api.Variable Variable}s can require a
 * {@link Profile Profile}.
 *
 * @deprecated this class will be removed, use {@link Action}
 */
@Deprecated(forRemoval = true)
@ApiStatus.ScheduledForRemoval(inVersion = "3.0")
public interface ComposedEvent extends Action {
}
