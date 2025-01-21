package org.betonquest.betonquest.quest.condition.npc;

import org.betonquest.betonquest.api.quest.QuestException;
import org.betonquest.betonquest.api.quest.condition.PlayerCondition;
import org.betonquest.betonquest.api.quest.condition.PlayerConditionFactory;
import org.betonquest.betonquest.api.quest.condition.PlayerlessCondition;
import org.betonquest.betonquest.api.quest.condition.PlayerlessConditionFactory;
import org.betonquest.betonquest.api.quest.condition.nullable.NullableConditionAdapter;
import org.betonquest.betonquest.id.NpcID;
import org.betonquest.betonquest.instruction.Instruction;
import org.betonquest.betonquest.instruction.variable.VariableNumber;
import org.betonquest.betonquest.instruction.variable.location.VariableLocation;
import org.betonquest.betonquest.quest.registry.processor.NpcProcessor;

/**
 * Factory to create {@link NpcLocationCondition}s from {@link Instruction}s.
 */
public class NpcLocationConditionFactory implements PlayerConditionFactory, PlayerlessConditionFactory {
    /**
     * Processor to get npc.
     */
    private final NpcProcessor npcProcessor;

    /**
     * Create a new factory for NPC Location Conditions.
     *
     * @param npcProcessor the processor to get npc
     */
    public NpcLocationConditionFactory(final NpcProcessor npcProcessor) {
        this.npcProcessor = npcProcessor;
    }

    @Override
    public PlayerCondition parsePlayer(final Instruction instruction) throws QuestException {
        return parseNpcLocationCondition(instruction);
    }

    @Override
    public PlayerlessCondition parsePlayerless(final Instruction instruction) throws QuestException {
        return parseNpcLocationCondition(instruction);
    }

    private NullableConditionAdapter parseNpcLocationCondition(final Instruction instruction) throws QuestException {
        final NpcID npcId = instruction.getID(NpcID::new);
        final VariableLocation location = instruction.get(VariableLocation::new);
        final VariableNumber radius = instruction.get(VariableNumber::new);
        return new NullableConditionAdapter(new NpcLocationCondition(npcProcessor, npcId, location, radius));
    }
}
