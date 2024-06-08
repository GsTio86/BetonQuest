package org.betonquest.betonquest.quest.event.notify;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.logger.BetonQuestLoggerFactory;
import org.betonquest.betonquest.api.quest.action.PlayerAction;
import org.betonquest.betonquest.api.quest.action.PlayerActionFactory;
import org.betonquest.betonquest.api.quest.action.StaticAction;
import org.betonquest.betonquest.api.quest.action.StaticActionFactory;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.quest.event.CallStaticPlayerActionAdapter;
import org.betonquest.betonquest.quest.event.OnlineProfileGroupStaticEventAdapter;
import org.betonquest.betonquest.utils.PlayerConverter;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Factory for the notify all event.
 */
public class NotifyAllActionFactory extends NotifyEventFactory implements PlayerActionFactory, StaticActionFactory {

    /**
     * Creates the notify all event factory.
     *
     * @param loggerFactory Logger factory to use for creating the event logger.
     * @param server        Server to use for syncing to the primary server thread.
     * @param scheduler     Scheduler to use for syncing to the primary server thread.
     * @param plugin        Plugin to use for syncing to the primary server thread.
     */
    public NotifyAllActionFactory(final BetonQuestLoggerFactory loggerFactory, final Server server, final BukkitScheduler scheduler, final Plugin plugin) {
        super(loggerFactory, server, scheduler, plugin);
    }

    @Override
    public PlayerAction parseEvent(final Instruction instruction) throws InstructionParseException {
        return new CallStaticPlayerActionAdapter(parseStaticEvent(instruction));
    }

    @Override
    public StaticAction parseStaticEvent(final Instruction instruction) throws InstructionParseException {
        return new OnlineProfileGroupStaticEventAdapter(PlayerConverter::getOnlineProfiles, super.parseEvent(instruction));
    }
}
