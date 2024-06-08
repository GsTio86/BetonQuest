package org.betonquest.betonquest.quest.event.experience;

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
import java.util.Optional;

/**
 * Factory for the experience event.
 */
public class ExperienceEventFactory implements PlayerActionFactory {
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
     * Create the experience event factory.
     *
     * @param loggerFactory logger factory to use
     * @param server        server to use
     * @param scheduler     scheduler to use
     * @param plugin        plugin to use
     */
    public ExperienceEventFactory(final BetonQuestLoggerFactory loggerFactory, final Server server, final BukkitScheduler scheduler, final Plugin plugin) {
        this.loggerFactory = loggerFactory;
        this.server = server;
        this.scheduler = scheduler;
        this.plugin = plugin;
    }

    @Override
    @SuppressWarnings("PMD.PrematureDeclaration")
    public PlayerAction parseEvent(final Instruction instruction) throws InstructionParseException {
        final VariableNumber amount = instruction.getVarNum();
        ExperienceModification experienceType = ExperienceModification.ADD_EXPERIENCE;
        String action = instruction.getOptional("action");
        if (instruction.hasArgument("level")) {
            experienceType = ExperienceModification.ADD_LEVEL;
        } else if (action != null) {
            action = action.toUpperCase(Locale.ROOT);

            final Optional<ExperienceModification> modification = ExperienceModification.getFromInstruction(action);
            if (modification.isPresent()) {
                experienceType = modification.get();
            } else {
                throw new InstructionParseException(action + " is not a valid experience modification type.");
            }
        }
        return new PrimaryServerThreadPlayerAction(
                new OnlineProfileRequiredPlayerAction(
                        loggerFactory.create(ExperiencePlayerAction.class), new ExperiencePlayerAction(experienceType, amount), instruction.getPackage()
                ), server, scheduler, plugin
        );
    }
}
