package org.betonquest.betonquest.quest.registry.processor;

import org.betonquest.betonquest.api.config.quest.QuestPackage;
import org.betonquest.betonquest.api.logger.BetonQuestLogger;
import org.betonquest.betonquest.bstats.CompositeInstructionMetricsSupplier;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.exceptions.ObjectNotFoundException;
import org.betonquest.betonquest.id.ID;
import org.betonquest.betonquest.quest.registry.type.QuestTypeRegistry;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

/**
 * Does the logic around a quest type and stores their type registry.
 * Also provides their BStats metrics.
 *
 * @param <I> the {@link ID} identifying the type
 * @param <S> the playerless type
 * @param <P> the player type
 */
public abstract class TypedQuestProcessor<I extends ID, S, P> extends QuestProcessor<I, TrippleFactory.Wrapper<S, P>> {
    /**
     * Available types.
     */
    protected final QuestTypeRegistry<P, S, ?, ?> types;

    /**
     * Type name used for logging.
     */
    private final String readable;

    /**
     * Section name and/or BStats topic identifier.
     */
    private final String internal;

    /**
     * Create a new QuestProcessor to store and execute type logic.
     *
     * @param log      the custom logger for this class
     * @param types    the available types
     * @param readable the type name used for logging, with first letter in upper case
     * @param internal the section name and/or bstats topic identifier
     */
    public TypedQuestProcessor(final BetonQuestLogger log, final QuestTypeRegistry<P, S, ?, ?> types,
                               final String readable, final String internal) {
        super(log);
        this.types = types;
        this.readable = readable;
        this.internal = internal;
    }

    /**
     * Gets the bstats metric supplier for registered and active types.
     *
     * @return the metric with its type identifier
     */
    public Map.Entry<String, CompositeInstructionMetricsSupplier<?>> metricsSupplier() {
        return Map.entry(internal, new CompositeInstructionMetricsSupplier<>(values::keySet, types::keySet));
    }

    @Override
    public void load(final QuestPackage pack) {
        final ConfigurationSection section = pack.getConfig().getConfigurationSection(internal);
        if (section == null) {
            return;
        }
        final String packName = pack.getQuestPath();
        for (final String key : section.getKeys(false)) {
            if (key.contains(" ")) {
                log.warn(pack, readable + " name cannot contain spaces: '" + key + "' (in " + packName + " package)");
            } else {
                loadKey(key, pack, packName);
            }
        }
    }

    private void loadKey(final String key, final QuestPackage pack, final String packName) {
        final I identifier;
        try {
            identifier = getIdentifier(pack, key);
        } catch (final ObjectNotFoundException e) {
            log.warn(pack, "Error while loading " + readable + " '" + packName + "." + key + "': " + e.getMessage(), e);
            return;
        }
        final String type;
        try {
            type = identifier.getInstruction().getPart(0);
        } catch (final InstructionParseException e) {
            log.warn(pack, readable + " type not defined in '" + packName + "." + key + "'", e);
            return;
        }
        final TrippleFactory<S, P> factory = types.getFactory(type);
        if (factory == null) {
            log.warn(pack, readable + " type " + type + " is not registered, check if it's"
                    + " spelled correctly in '" + identifier + "' " + readable + ".");
            return;
        }

        try {
            final TrippleFactory.Wrapper<S, P> parsed = factory.parseInstruction(identifier.getInstruction());
            values.put(identifier, parsed);
            log.debug(pack, "  " + readable + " '" + identifier + "' loaded");
        } catch (final InstructionParseException e) {
            log.warn(pack, "Error in '" + identifier + "' " + readable + " (" + type + "): " + e.getMessage(), e);
        }
    }

    /**
     * Creates a new type ID to store the created type with it.
     *
     * @param pack       the package the ID is in
     * @param identifier the id instruction string
     * @return the new typed ID
     * @throws ObjectNotFoundException if the ID could not be parsed
     */
    protected abstract I getIdentifier(QuestPackage pack, String identifier) throws ObjectNotFoundException;
}
