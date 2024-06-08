package org.betonquest.betonquest.quest.event.weather;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.VariableNumber;
import org.betonquest.betonquest.api.common.function.ConstantSelector;
import org.betonquest.betonquest.api.common.function.Selector;
import org.betonquest.betonquest.api.common.function.Selectors;
import org.betonquest.betonquest.api.logger.BetonQuestLoggerFactory;
import org.betonquest.betonquest.api.quest.action.Action;
import org.betonquest.betonquest.api.quest.action.PlayerAction;
import org.betonquest.betonquest.api.quest.action.PlayerActionFactory;
import org.betonquest.betonquest.api.quest.action.StaticAction;
import org.betonquest.betonquest.api.quest.action.StaticActionFactory;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.quest.event.DoNothingStaticEvent;
import org.betonquest.betonquest.quest.event.OnlineProfileRequiredPlayerAction;
import org.betonquest.betonquest.quest.event.PrimaryServerThreadAction;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * Factory to create weather events from {@link Instruction}s.
 */
public class WeatherActionFactory implements PlayerActionFactory, StaticActionFactory {
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
     * Creates the weather event factory.
     *
     * @param loggerFactory logger factory to use
     * @param server        server to use
     * @param scheduler     scheduler to use
     * @param plugin        plugin to use
     */
    public WeatherActionFactory(final BetonQuestLoggerFactory loggerFactory, final Server server, final BukkitScheduler scheduler, final Plugin plugin) {
        this.loggerFactory = loggerFactory;
        this.server = server;
        this.scheduler = scheduler;
        this.plugin = plugin;
    }

    private Action parseComposedEvent(final Instruction instruction) throws InstructionParseException {
        final Weather weather = parseWeather(instruction.next());
        final Selector<World> worldSelector = parseWorld(instruction.getOptional("world"));
        final VariableNumber duration = instruction.getVarNum(instruction.getOptional("duration", "0"));
        return new PrimaryServerThreadAction(new WeatherPlayerAction(weather, worldSelector, duration),
                server, scheduler, plugin);
    }

    @Override
    public PlayerAction parseEvent(final Instruction instruction) throws InstructionParseException {
        return new OnlineProfileRequiredPlayerAction(loggerFactory.create(WeatherPlayerAction.class), parseComposedEvent(instruction), instruction.getPackage());
    }

    @Override
    public StaticAction parseStaticEvent(final Instruction instruction) throws InstructionParseException {
        if (instruction.copy().getOptional("world") == null) {
            return new DoNothingStaticEvent();
        } else {
            return parseComposedEvent(instruction);
        }
    }

    private Weather parseWeather(final String weatherName) throws InstructionParseException {
        return switch (weatherName.toLowerCase(Locale.ROOT)) {
            case "sun", "clear" -> Weather.SUN;
            case "rain" -> Weather.RAIN;
            case "storm" -> Weather.STORM;
            default ->
                    throw new InstructionParseException("Unknown weather state (valid options are: sun, clear, rain, storm): " + weatherName);
        };
    }

    private Selector<World> parseWorld(@Nullable final String worldName) {
        if (worldName == null) {
            return Selectors.fromPlayer(Player::getWorld);
        } else {
            final World world = server.getWorld(worldName);
            return new ConstantSelector<>(world);
        }
    }
}
