package org.betonquest.betonquest.quest.event;

import org.betonquest.betonquest.api.quest.action.StaticAction;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Wrapper for {@link StaticAction}s to be executed on the primary server thread.
 */
public class PrimaryServerThreadStaticEvent extends PrimaryServerThreadEventFrame<StaticAction> implements StaticAction {
    /**
     * Wrap the given {@link StaticAction} for execution on the primary server thread.
     * The {@link Server}, {@link BukkitScheduler} and {@link Plugin} are used to
     * determine if the current thread is the primary server thread and to
     * schedule the execution onto it in case it isn't.
     *
     * @param syncedEvent event to synchronize
     * @param server      server for primary thread identification
     * @param scheduler   scheduler for primary thread scheduling
     * @param plugin      plugin to associate with the scheduled task
     */
    public PrimaryServerThreadStaticEvent(final StaticAction syncedEvent, final Server server,
                                          final BukkitScheduler scheduler, final Plugin plugin) {
        super(syncedEvent, server, scheduler, plugin);
    }

    @Override
    public void execute() throws QuestRuntimeException {
        execute(syncedEvent::execute);
    }
}
