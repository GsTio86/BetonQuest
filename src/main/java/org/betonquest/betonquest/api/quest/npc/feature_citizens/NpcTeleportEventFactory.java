package org.betonquest.betonquest.api.quest.npc.feature_citizens;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.quest.event.Event;
import org.betonquest.betonquest.api.quest.event.EventFactory;
import org.betonquest.betonquest.api.quest.event.StaticEvent;
import org.betonquest.betonquest.api.quest.event.StaticEventFactory;
import org.betonquest.betonquest.api.quest.event.nullable.NullableEventAdapter;
import org.betonquest.betonquest.api.quest.npc.NpcWrapper;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.exceptions.ObjectNotFoundException;
import org.betonquest.betonquest.id.NpcID;
import org.betonquest.betonquest.instruction.variable.location.VariableLocation;
import org.betonquest.betonquest.quest.registry.processor.NpcProcessor;

/**
 * Factory to create {@link NpcTeleportEvent}s from {@link Instruction}s.
 */
public class NpcTeleportEventFactory implements EventFactory, StaticEventFactory {
    /**
     * Processor to resolve Npc Ids.
     */
    private final NpcProcessor npcProcessor;

    /**
     * Create a new factory for Npc Teleport Events.
     *
     * @param npcProcessor the processor to resolve Npcs
     */
    public NpcTeleportEventFactory(final NpcProcessor npcProcessor) {
        this.npcProcessor = npcProcessor;
    }

    @Override
    public Event parseEvent(final Instruction instruction) throws InstructionParseException {
        return createNpcTeleportEvent(instruction);
    }

    @Override
    public StaticEvent parseStaticEvent(final Instruction instruction) throws InstructionParseException {
        return createNpcTeleportEvent(instruction);
    }

    private NullableEventAdapter createNpcTeleportEvent(final Instruction instruction) throws InstructionParseException {
        final NpcID npcID;
        try {
            npcID = new NpcID(instruction.getPackage(), instruction.next());
        } catch (final ObjectNotFoundException exception) {
            throw new InstructionParseException("Error while loading npc: " + exception.getMessage(), exception);
        }
        final NpcWrapper<?> npcWrapper = npcProcessor.getNpc(npcID);
        final VariableLocation location = instruction.getLocation();
        return new NullableEventAdapter(new NpcTeleportEvent(npcWrapper, location));
    }
}
