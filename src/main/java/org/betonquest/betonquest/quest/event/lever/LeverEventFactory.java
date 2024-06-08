package org.betonquest.betonquest.quest.event.lever;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.quest.action.Action;
import org.betonquest.betonquest.api.quest.action.ActionFactory;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.quest.event.PrimaryServerThreadAction;
import org.betonquest.betonquest.utils.location.CompoundLocation;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Factory for {@link LeverPlayerAction}.
 */
public class LeverEventFactory implements ActionFactory {
    /**
     * Server to use for syncing to the primary server thread.
     */
    private final Server server;

    /**
     * Scheduler to use for syncing to the primary server thread.
     */
    private final BukkitScheduler scheduler;

    /**
     * Plugin to use for syncing to the primary server thread.
     */
    private final Plugin plugin;

    /**
     * Create a new LeverEventFactory.
     *
     * @param server    the server to use for syncing to the primary server thread
     * @param scheduler the scheduler to use for syncing to the primary server thread
     * @param plugin    the plugin to use for syncing to the primary server thread
     */
    public LeverEventFactory(final Server server, final BukkitScheduler scheduler, final Plugin plugin) {
        this.server = server;
        this.scheduler = scheduler;
        this.plugin = plugin;
    }

    @Override
    public Action parseComposedEvent(final Instruction instruction) throws InstructionParseException {
        final CompoundLocation location = instruction.getLocation();
        final StateType stateType = instruction.getEnum(StateType.class);
        return new PrimaryServerThreadAction(
                new LeverPlayerAction(stateType, location),
                server, scheduler, plugin
        );
    }
}
