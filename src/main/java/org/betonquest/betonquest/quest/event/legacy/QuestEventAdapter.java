package org.betonquest.betonquest.quest.event.legacy;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.QuestEvent;
import org.betonquest.betonquest.api.profiles.Profile;
import org.betonquest.betonquest.api.quest.action.PlayerAction;
import org.betonquest.betonquest.api.quest.action.StaticAction;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Adapter for {@link PlayerAction} and {@link StaticAction} to fit the old convention of {@link QuestEvent}.
 */
public class QuestEventAdapter extends QuestEvent {

    /**
     * The normal event to be adapted.
     */
    private final PlayerAction event;

    /**
     * The "static" event to be adapted if present. May be {@code null}!
     */
    @Nullable
    private final StaticAction staticAction;

    /**
     * Create a quest event from an {@link PlayerAction} and a {@link StaticAction}. If the event does not support "static"
     * execution ({@code staticness = false}) then no {@link StaticAction} instance must be provided.
     *
     * @param instruction  instruction used to create the events
     * @param event        event to use
     * @param staticAction static event to use or null if no static execution is supported
     * @throws InstructionParseException if the instruction contains errors
     */
    public QuestEventAdapter(final Instruction instruction, final PlayerAction event, @Nullable final StaticAction staticAction) throws InstructionParseException {
        super(instruction, false);
        this.event = event;
        this.staticAction = staticAction;
        staticness = staticAction != null;
        persistent = true;
    }

    @Override
    protected Void execute(@Nullable final Profile profile) throws QuestRuntimeException {
        if (profile == null) {
            Objects.requireNonNull(staticAction);
            staticAction.execute();
        } else {
            event.execute(profile);
        }
        return null;
    }
}
