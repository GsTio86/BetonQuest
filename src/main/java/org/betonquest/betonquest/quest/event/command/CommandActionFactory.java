package org.betonquest.betonquest.quest.event.command;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.bukkit.command.SilentCommandSender;
import org.betonquest.betonquest.api.bukkit.command.SilentConsoleCommandSender;
import org.betonquest.betonquest.api.logger.BetonQuestLoggerFactory;
import org.betonquest.betonquest.api.quest.action.Action;
import org.betonquest.betonquest.api.quest.action.PlayerAction;
import org.betonquest.betonquest.api.quest.action.StaticAction;
import org.betonquest.betonquest.api.quest.action.StaticActionFactory;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.quest.event.PrimaryServerThreadAction;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Creates a new CommandEvent from an {@link Instruction}.
 */
public class CommandActionFactory extends BaseCommandEventFactory implements StaticActionFactory {

    /**
     * Command sender to run the commands as.
     * <p>
     * {@link SilentConsoleCommandSender} is used to keep console and log clean.
     */
    private final CommandSender silentSender;

    /**
     * Create the command event factory.
     *
     * @param loggerFactory logger factory to use
     * @param server        server to use
     * @param scheduler     scheduler scheduler to use
     * @param plugin        plugin to use
     */
    public CommandActionFactory(final BetonQuestLoggerFactory loggerFactory, final Server server,
                                final BukkitScheduler scheduler, final Plugin plugin) {
        super(loggerFactory, server, scheduler, plugin);
        this.silentSender = new SilentConsoleCommandSender(loggerFactory.create(SilentCommandSender.class,
                "CommandEvent"), server.getConsoleSender());
    }

    private Action parseComposedEvent(final Instruction instruction) throws InstructionParseException {
        return new PrimaryServerThreadAction(
                new CommandPlayerAction(parseCommands(instruction), silentSender, server), server, scheduler, plugin);
    }

    @Override
    public PlayerAction parseEvent(final Instruction instruction) throws InstructionParseException {
        return parseComposedEvent(instruction);
    }

    @Override
    public StaticAction parseStaticEvent(final Instruction instruction) throws InstructionParseException {
        return parseComposedEvent(instruction);
    }
}
