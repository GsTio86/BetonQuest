package org.betonquest.betonquest.quest.event.chat;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.logger.BetonQuestLoggerFactory;
import org.betonquest.betonquest.api.quest.action.PlayerAction;
import org.betonquest.betonquest.api.quest.action.PlayerActionFactory;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.quest.event.OnlineProfileRequiredPlayerAction;
import org.betonquest.betonquest.quest.event.PrimaryServerThreadPlayerAction;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * The chat event factory.
 */
public class ChatEventFactory implements PlayerActionFactory {
    /**
     * Logger factory to create a logger for events.
     */
    private final BetonQuestLoggerFactory loggerFactory;

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
     * Create the chat event factory.
     *
     * @param loggerFactory logger factory to use
     * @param server        server to use
     * @param scheduler     scheduler to use
     * @param plugin        plugin to use
     */
    public ChatEventFactory(final BetonQuestLoggerFactory loggerFactory, final Server server, final BukkitScheduler scheduler, final Plugin plugin) {
        this.loggerFactory = loggerFactory;
        this.server = server;
        this.scheduler = scheduler;
        this.plugin = plugin;
    }

    @Override
    public PlayerAction parseEvent(final Instruction instruction) throws InstructionParseException {
        final String[] messages = String.join(" ", instruction.getAllParts()).split("\\|");
        return new PrimaryServerThreadPlayerAction(
                new OnlineProfileRequiredPlayerAction(
                        loggerFactory.create(ChatPlayerAction.class), new ChatPlayerAction(messages), instruction.getPackage()),
                server, scheduler, plugin
        );
    }
}
