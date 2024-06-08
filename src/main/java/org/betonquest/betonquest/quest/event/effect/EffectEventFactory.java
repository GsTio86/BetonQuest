package org.betonquest.betonquest.quest.event.effect;

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
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Factory to create effect events from {@link Instruction}s.
 */
public class EffectEventFactory implements PlayerActionFactory {
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
     * Create the effect event factory.
     *
     * @param loggerFactory logger factory to use
     * @param server        server to use
     * @param scheduler     scheduler to use
     * @param plugin        plugin to use
     */
    public EffectEventFactory(final BetonQuestLoggerFactory loggerFactory, final Server server, final BukkitScheduler scheduler, final Plugin plugin) {
        this.loggerFactory = loggerFactory;
        this.server = server;
        this.scheduler = scheduler;
        this.plugin = plugin;
    }

    @Override
    public PlayerAction parseEvent(final Instruction instruction) throws InstructionParseException {
        final PotionEffectType effect = PotionEffectType.getByName(instruction.next());
        if (effect == null) {
            throw new InstructionParseException("Unknown effect type: " + instruction.current());
        }
        try {
            final VariableNumber duration = instruction.getVarNum();
            final VariableNumber amplifier = instruction.getVarNum();
            final boolean ambient = instruction.hasArgument("ambient");
            final boolean hidden = instruction.hasArgument("hidden");
            final boolean icon = !instruction.hasArgument("noicon");
            return new PrimaryServerThreadPlayerAction(new OnlineProfileRequiredPlayerAction(
                    loggerFactory.create(EffectPlayerAction.class), new EffectPlayerAction(effect, duration, amplifier, ambient, hidden, icon), instruction.getPackage()),
                    server, scheduler, plugin);
        } catch (final InstructionParseException e) {
            throw new InstructionParseException("Could not parse effect duration and amplifier", e);
        }
    }
}
