package org.betonquest.betonquest.quest.event.hunger;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.VariableNumber;
import org.betonquest.betonquest.api.logger.BetonQuestLoggerFactory;
import org.betonquest.betonquest.api.quest.action.PlayerAction;
import org.betonquest.betonquest.api.quest.action.PlayerActionFactory;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.quest.event.OnlineProfileRequiredPlayerAction;
import org.betonquest.betonquest.quest.event.PrimaryServerThreadPlayerAction;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Locale;

/**
 * Factory for the hunger event.
 */
public class HungerEventFactory implements PlayerActionFactory {
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
     * Create the hunger event factory.
     *
     * @param loggerFactory logger factory to use
     * @param server        server to use
     * @param scheduler     scheduler to use
     * @param plugin        plugin to use
     */
    public HungerEventFactory(final BetonQuestLoggerFactory loggerFactory, final Server server, final BukkitScheduler scheduler, final Plugin plugin) {
        this.loggerFactory = loggerFactory;
        this.server = server;
        this.scheduler = scheduler;
        this.plugin = plugin;
    }

    @Override
    public PlayerAction parseEvent(final Instruction instruction) throws InstructionParseException {
        try {
            final Hunger hunger = Hunger.valueOf(instruction.next().toUpperCase(Locale.ROOT).trim());
            final VariableNumber amount = instruction.getVarNum();
            return new PrimaryServerThreadPlayerAction(
                    new OnlineProfileRequiredPlayerAction(
                            loggerFactory.create(HungerEventFactory.class), new HungerPlayerAction(hunger, amount), instruction.getPackage()
                    ), server, scheduler, plugin);
        } catch (final IllegalArgumentException e) {
            throw new InstructionParseException("Error while parsing action! Must be 'set', 'give', or 'take'.", e);
        }
    }
}
