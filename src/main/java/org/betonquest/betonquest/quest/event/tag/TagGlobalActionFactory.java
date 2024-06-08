package org.betonquest.betonquest.quest.event.tag;

import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.quest.action.PlayerAction;
import org.betonquest.betonquest.api.quest.action.PlayerActionFactory;
import org.betonquest.betonquest.api.quest.action.StaticAction;
import org.betonquest.betonquest.api.quest.action.StaticActionFactory;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.utils.Utils;

import java.util.Locale;

/**
 * Factory to create global tag events from {@link Instruction}s.
 */
public class TagGlobalActionFactory implements PlayerActionFactory, StaticActionFactory {
    /**
     * BetonQuest instance to provide to events.
     */
    private final BetonQuest betonQuest;

    /**
     * Create the global tag event factory.
     *
     * @param betonQuest BetonQuest instance to pass on
     */
    public TagGlobalActionFactory(final BetonQuest betonQuest) {
        this.betonQuest = betonQuest;
    }

    @Override
    public PlayerAction parseEvent(final Instruction instruction) throws InstructionParseException {
        final String action = instruction.next();
        final String[] tags = getTags(instruction);
        return switch (action.toLowerCase(Locale.ROOT)) {
            case "add" -> createAddTagEvent(tags);
            case "delete", "del" -> createDeleteTagEvent(tags);
            default -> throw new InstructionParseException("Unknown tag action: " + action);
        };
    }

    @Override
    public StaticAction parseStaticEvent(final Instruction instruction) throws InstructionParseException {
        final String action = instruction.next();
        final String[] tags = getTags(instruction);
        return switch (action.toLowerCase(Locale.ROOT)) {
            case "add" -> createStaticAddTagEvent(tags);
            case "delete", "del" -> createStaticDeleteTagEvent(tags);
            default -> throw new InstructionParseException("Unknown tag action: " + action);
        };
    }

    private String[] getTags(final Instruction instruction) throws InstructionParseException {
        final String[] tags;
        tags = instruction.getArray();
        for (int ii = 0; ii < tags.length; ii++) {
            tags[ii] = Utils.addPackage(instruction.getPackage(), tags[ii]);
        }
        return tags;
    }

    private StaticAction createStaticAddTagEvent(final String... tags) {
        final TagChanger tagChanger = new AddTagChanger(tags);
        return new StaticTagEvent(betonQuest.getGlobalData(), tagChanger);
    }

    private StaticAction createStaticDeleteTagEvent(final String... tags) {
        final TagChanger tagChanger = new DeleteTagChanger(tags);
        return new StaticTagEvent(betonQuest.getGlobalData(), tagChanger);
    }

    private PlayerAction createAddTagEvent(final String... tags) {
        final TagChanger tagChanger = new AddTagChanger(tags);
        return new TagPlayerAction(profile -> betonQuest.getGlobalData(), tagChanger);
    }

    private PlayerAction createDeleteTagEvent(final String... tags) {
        final TagChanger tagChanger = new DeleteTagChanger(tags);
        return new TagPlayerAction(profile -> betonQuest.getGlobalData(), tagChanger);
    }
}
