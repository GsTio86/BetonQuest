package org.betonquest.betonquest.quest.event.door;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.quest.action.Action;
import org.betonquest.betonquest.api.quest.action.ActionFactory;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.quest.event.PrimaryServerThreadAction;
import org.betonquest.betonquest.utils.location.CompoundLocation;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Locale;

/**
 * Factory to create door events from {@link Instruction}s.
 */
public class DoorEventFactory implements ActionFactory {
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
     * Create the door event factory.
     *
     * @param server    server to use
     * @param scheduler scheduler to use
     * @param plugin    plugin to use
     */
    public DoorEventFactory(final Server server, final BukkitScheduler scheduler, final Plugin plugin) {
        this.server = server;
        this.scheduler = scheduler;
        this.plugin = plugin;
    }

    @Override
    public Action parseComposedEvent(final Instruction instruction) throws InstructionParseException {
        return new PrimaryServerThreadAction(createDoorEvent(instruction), server, scheduler, plugin);
    }

    private DoorPlayerAction createDoorEvent(final Instruction instruction) throws InstructionParseException {
        final CompoundLocation location = instruction.getLocation();
        final String action = instruction.next();
        return switch (action.toLowerCase(Locale.ROOT)) {
            case "on" -> createOpenDoorEvent(location);
            case "off" -> createCloseDoorEvent(location);
            case "toggle" -> createToggleDoorEvent(location);
            default ->
                    throw new InstructionParseException("Unknown door action (valid options are: on, off, toggle): " + action);
        };
    }

    private DoorPlayerAction createOpenDoorEvent(final CompoundLocation location) {
        return new DoorPlayerAction(location, door -> door.setOpen(true));
    }

    private DoorPlayerAction createCloseDoorEvent(final CompoundLocation location) {
        return new DoorPlayerAction(location, door -> door.setOpen(false));
    }

    private DoorPlayerAction createToggleDoorEvent(final CompoundLocation location) {
        return new DoorPlayerAction(location, door -> door.setOpen(!door.isOpen()));
    }
}
