package org.betonquest.betonquest.quest.event.stage;

import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.api.profile.Profile;
import org.betonquest.betonquest.api.quest.QuestException;
import org.betonquest.betonquest.api.quest.event.Event;
import org.betonquest.betonquest.api.quest.event.EventFactory;
import org.betonquest.betonquest.id.ObjectiveID;
import org.betonquest.betonquest.instruction.Instruction;
import org.betonquest.betonquest.instruction.variable.VariableNumber;
import org.betonquest.betonquest.instruction.variable.VariableString;
import org.betonquest.betonquest.objective.StageObjective;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * Factory to create stage events to modify a StageObjective.
 */
public class StageEventFactory implements EventFactory {
    /**
     * BetonQuest instance.
     */
    private final BetonQuest betonQuest;

    /**
     * Creates the stage event factory.
     *
     * @param betonQuest BetonQuest instance
     */
    public StageEventFactory(final BetonQuest betonQuest) {
        this.betonQuest = betonQuest;
    }

    @Override
    public Event parseEvent(final Instruction instruction) throws QuestException {
        final ObjectiveID objectiveID = instruction.getID(ObjectiveID::new);
        final String action = instruction.next();
        return switch (action.toLowerCase(Locale.ROOT)) {
            case "set" -> createSetEvent(instruction, objectiveID);
            case "increase" -> createIncreaseEvent(instruction, objectiveID);
            case "decrease" -> createDecreaseEvent(instruction, objectiveID);
            default -> throw new QuestException("Unknown action '" + action + "'");
        };
    }

    private Event createSetEvent(final Instruction instruction, final ObjectiveID objectiveID) throws QuestException {
        final VariableString variableString = instruction.get(VariableString::new);
        return new StageEvent(profile -> getStageObjective(objectiveID).setStage(profile, variableString.getValue(profile)));
    }

    private Event createIncreaseEvent(final Instruction instruction, final ObjectiveID objectiveID) throws QuestException {
        final VariableNumber amount = getVariableNumber(instruction);
        return new StageEvent(profile -> getStageObjective(objectiveID).increaseStage(profile, getAmount(profile, amount)));
    }

    private Event createDecreaseEvent(final Instruction instruction, final ObjectiveID objectiveID) throws QuestException {
        final VariableNumber amount = getVariableNumber(instruction);
        return new StageEvent(profile -> getStageObjective(objectiveID).decreaseStage(profile, getAmount(profile, amount)));
    }

    @Nullable
    private VariableNumber getVariableNumber(final Instruction instruction) throws QuestException {
        if (instruction.hasNext()) {
            final String stringAmount = instruction.next();
            if (!stringAmount.matches("condition(s)?:.+")) {
                return instruction.get(stringAmount, VariableNumber::new);
            }
        }
        return null;
    }

    private int getAmount(final Profile profile, @Nullable final VariableNumber amount) throws QuestException {
        if (amount == null) {
            return 1;
        }
        final int targetAmount = amount.getValue(profile).intValue();
        if (targetAmount <= 0) {
            throw new QuestException("Amount must be greater than 0");
        }
        return targetAmount;
    }

    private StageObjective getStageObjective(final ObjectiveID objectiveID) throws QuestException {
        if (betonQuest.getObjective(objectiveID) instanceof final StageObjective stageObjective) {
            return stageObjective;
        }
        throw new QuestException("Objective '" + objectiveID.getFullID() + "' is not a stage objective");
    }
}
