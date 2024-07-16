package org.betonquest.betonquest.quest.registry.type;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.logger.BetonQuestLogger;
import org.betonquest.betonquest.api.logger.BetonQuestLoggerFactory;
import org.betonquest.betonquest.api.quest.PlayerQuestFactory;
import org.betonquest.betonquest.api.quest.PlayerlessQuestFactory;
import org.betonquest.betonquest.api.quest.QuestFactory;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.quest.QuestTypeAdapter;
import org.betonquest.betonquest.quest.legacy.FromClassLegacyTypeFactory;
import org.betonquest.betonquest.quest.legacy.LegacyTypeFactory;
import org.betonquest.betonquest.quest.registry.processor.TrippleFactory;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Stores the Types that can be used in BetonQuest.
 *
 * @param <P> the player variant of the type
 * @param <S> the playerless variant of the type
 * @param <T> the type extending {@link P} and {@link S}
 * @param <L> the legacy structure based on the {@link org.betonquest.betonquest.Instruction Instruction}
 *            as defined in the {@link org.betonquest.betonquest.api API package}
 */
public abstract class QuestTypeRegistry<P, S, T, L> {
    /**
     * Custom {@link BetonQuestLogger} instance for this class.
     */
    private final BetonQuestLogger log;

    /**
     * Logger factory to create class specific logger for quest type factories.
     */
    private final BetonQuestLoggerFactory loggerFactory;

    /**
     * Name of the type to display in register log.
     */
    private final String typeName;

    /**
     * Map of registered legacy factories.
     */
    private final Map<String, TrippleFactory<S, P>> types = new HashMap<>();

    /**
     * Create a new type registry.
     *
     * @param log           the logger that will be used for logging
     * @param loggerFactory the logger factory to create a new logger for the legacy quest type factory created
     * @param typeName      the name of the type to use in the register log message
     */
    public QuestTypeRegistry(final BetonQuestLogger log, final BetonQuestLoggerFactory loggerFactory, final String typeName) {
        this.log = log;
        this.loggerFactory = loggerFactory;
        this.typeName = typeName;
    }

    /**
     * Registers a type with its name and the class used to create instances of the type.
     *
     * @param name   the name of the type
     * @param lClass the class object for the type
     * @deprecated replaced by {@link #register(String, PlayerQuestFactory, PlayerlessQuestFactory)}
     */
    @Deprecated
    public void register(final String name, final Class<? extends L> lClass) {
        log.debug("Registering " + name + " [legacy]" + typeName + " type");
        types.put(name, getFromClassLegacyTypeFactory(new FromClassLegacyTypeFactory<>(loggerFactory.create(lClass), lClass, typeName)));
    }

    /**
     * Create a new legacy type factory which will be used to create new instances of the {@link L}.
     *
     * @param factory the legacy factory to adapt to new format
     * @return the legacy factory to store
     * @deprecated only used for legacy type converting
     */
    @Deprecated
    protected abstract TrippleFactory<S, P> getFromClassLegacyTypeFactory(FromClassLegacyTypeFactory<? extends L, L> factory);

    /**
     * Registers a type that does not support playerless execution with its name
     * and a player factory to create new player instances.
     *
     * @param name    the name of the type
     * @param factory the player factory to create the type
     */
    public void register(final String name, final PlayerQuestFactory<P> factory) {
        registerInternal(name, factory, null);
    }

    /**
     * Registers a type and a factory to create new playerless instances.
     *
     * @param name              the name of the type
     * @param playerlessFactory the playerless factory to create the type
     */
    public void register(final String name, final PlayerlessQuestFactory<S> playerlessFactory) {
        registerInternal(name, null, playerlessFactory);
    }

    /**
     * Registers a type with its name and a factory to create player and playerless instances.
     *
     * @param name    the name of the type
     * @param factory the factory to create the player and playerless type instances
     */
    public void register(final String name, final QuestFactory<T> factory) {
        final QuestTypeAdapter<T, P, S> adapter = getAdapter(factory);
        register(name, adapter, adapter);
    }

    /**
     * Get a new adapter of {@link PlayerQuestFactory} and {@link PlayerlessQuestFactory} from the {@link QuestFactory}.
     *
     * @param factory the factory to adapt
     * @return the adapter to store
     */
    protected abstract QuestTypeAdapter<T, P, S> getAdapter(QuestFactory<T> factory);

    /**
     * Registers a type with its name and a single factory to create both player and playerless instances.
     *
     * @param name    the name of the type
     * @param factory the factory to create the player and playerless variant
     * @param <C>     the type of factory that creates both normal and playerless instances of the type
     */
    public <C extends PlayerQuestFactory<P> & PlayerlessQuestFactory<S>> void registerCombined(final String name, final C factory) {
        register(name, factory, factory);
    }

    /**
     * Registers a type with its name and two factories to create player and playerless instances.
     *
     * @param name              the name of the type
     * @param playerFactory     the player factory to create the type
     * @param playerlessFactory the playerless factory to create the type
     */
    public void register(final String name, final PlayerQuestFactory<P> playerFactory, final PlayerlessQuestFactory<S> playerlessFactory) {
        registerInternal(name, playerFactory, playerlessFactory);
    }

    /**
     * Either the player factory or the playerless factory has to be present.
     *
     * @see #getLegacyFactoryAdapter(PlayerQuestFactory, PlayerlessQuestFactory)
     */
    private void registerInternal(final String name, @Nullable final PlayerQuestFactory<P> playerFactory,
                                  @Nullable final PlayerlessQuestFactory<S> playerlessFactory) {
        log.debug("Registering " + name + " " + typeName + " type");
        types.put(name, new FactoryEntry<>(playerlessFactory, playerFactory));
    }

    /**
     * Get a new adapter to the legacy factory from the new type format.
     * <p>
     * Either the player factory or the playerless factory has to be present.
     *
     * @param playerFactory     the player factory to create the type
     * @param playerlessFactory the playerless factory to create the type
     * @return the legacy factory to store
     */
    protected abstract LegacyTypeFactory<L> getLegacyFactoryAdapter(
            @Nullable PlayerQuestFactory<P> playerFactory,
            @Nullable PlayerlessQuestFactory<S> playerlessFactory);

    /**
     * Fetches the factory to create the type registered with the given name.
     *
     * @param name the name of the type
     * @return a factory to create the type
     */
    @Nullable
    public TrippleFactory<S, P> getFactory(final String name) {
        return types.get(name);
    }

    /**
     * Gets the keys of all registered types.
     *
     * @return the actual key set
     */
    public Set<String> keySet() {
        return types.keySet();
    }

    public record FactoryEntry<S, P>(@Nullable PlayerlessQuestFactory<S> playerlessFactory,
                                     @Nullable PlayerQuestFactory<P> playerFactory) implements TrippleFactory<S, P> {
        public FactoryEntry {
            if (playerlessFactory == null && playerFactory == null) {
                throw new IllegalArgumentException("Either the playerless or player factory must be present!");
            }
        }

        @Override
        public Wrapper<S, P> parseInstruction(final Instruction instruction) throws InstructionParseException {
            final S playerlessType = playerlessFactory == null ? null : playerlessFactory.parsePlayerless(instruction.copy());
            final P playerType = playerFactory == null ? null : playerFactory.parsePlayer(instruction.copy());
            return new Wrapper<>(instruction, playerlessType, playerType);
        }
    }
}
