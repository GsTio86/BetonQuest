package org.betonquest.betonquest.menu.config;

import org.betonquest.betonquest.api.config.quest.QuestPackage;
import org.betonquest.betonquest.exceptions.ObjectNotFoundException;
import org.betonquest.betonquest.id.ConditionID;
import org.betonquest.betonquest.id.EventID;
import org.betonquest.betonquest.variables.GlobalVariableResolver;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class to help parsing of yml config files.
 */
@SuppressWarnings({"PMD.PreserveStackTrace", "PMD.AbstractClassWithoutAbstractMethod", "PMD.CommentRequired",
        "PMD.TooManyMethods"})
public abstract class SimpleYMLSection {
    /**
     * Prefix for exception messages.
     */
    public static final String RPG_MENU_CONFIG_SETTING = "RPGMenuConfig setting ";

    /**
     * Quest Package this section is in.
     */
    protected final QuestPackage pack;

    /**
     * Backing section containing values.
     */
    protected final ConfigurationSection config;

    /**
     * The Identifier of this section.
     */
    protected final String name;

    /**
     * Creates a new section for getting validated values.
     *
     * @param pack   the pack the section is in
     * @param name   the name of the
     * @param config the backing config providing values
     * @throws InvalidConfigurationException if the backing configuration is empty
     */
    public SimpleYMLSection(final QuestPackage pack, final String name, final ConfigurationSection config) throws InvalidConfigurationException {
        this.pack = pack;
        this.config = config;
        this.name = name;
        if (config.getKeys(false).isEmpty()) {
            throw new InvalidSimpleConfigException("RPGMenuConfig is invalid or empty!");
        }
    }

    private String resolveGlobalVariable(final String string) {
        return GlobalVariableResolver.resolve(pack, string);
    }

    /**
     * Parse string from config file.
     *
     * @param key where to search
     * @return requested String
     * @throws Missing if string is not given
     */
    protected final String getString(final String key) throws Missing {
        final String string = config.getString(key);
        if (string == null) {
            throw new Missing(key);
        } else {
            return resolveGlobalVariable(string);
        }
    }

    /**
     * Parse a list of strings from config file.
     *
     * @param key where to search
     * @return requested List of String
     * @throws Missing if no list is not given
     */
    protected final List<String> getStringList(final String key) throws Missing {
        final List<String> list = config.getStringList(key);
        if (list.isEmpty()) {
            throw new Missing(key);
        } else {
            return list.stream().map(this::resolveGlobalVariable).toList();
        }
    }

    /**
     * Parse a list of multiple strings, separated by ',' from config file.
     *
     * @param key where to search
     * @return requested List of String
     * @throws Missing if no strings are given
     */
    protected final List<String> getStrings(final String key) throws Missing {
        final List<String> list = new ArrayList<>();
        final String[] args = getString(key).split(",");
        for (final String arg : args) {
            final String argTrim = arg.trim();
            if (!argTrim.isEmpty()) {
                list.add(argTrim);
            }
        }
        return list;
    }

    /**
     * Parse an integer from config file.
     *
     * @param key where to search
     * @return requested Int
     * @throws Missing if nothing is given
     * @throws Invalid if given string is not an integer
     */
    protected final int getInt(final String key) throws Missing, Invalid {
        final String stringInt = this.getString(key);
        try {
            return Integer.parseInt(stringInt);
        } catch (final NumberFormatException e) {
            throw new Invalid(key, "Invalid number format for '" + stringInt + "'");
        }
    }

    /**
     * Parse a boolean from config file.
     *
     * @param key where to search
     * @return requested Boolean
     * @throws Missing if nothing is given
     * @throws Invalid if given string is not a boolean
     */
    @SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
    protected final boolean getBoolean(final String key) throws Missing, Invalid {
        final String stringBoolean = this.getString(key).trim();
        if ("true".equalsIgnoreCase(stringBoolean)) {
            return true;
        } else if ("false".equalsIgnoreCase(stringBoolean)) {
            return false;
        } else {
            throw new Invalid(key);
        }
    }

    /**
     * Parse a list of events from config file.
     *
     * @param key  where to search
     * @param pack configuration package of this file
     * @return requested EventIDs
     * @throws Missing if nothing is given
     * @throws Invalid if one of the events can't be found
     */
    protected final List<EventID> getEvents(final String key, final QuestPackage pack) throws Missing, Invalid {
        final List<String> strings = getStrings(key);
        final List<EventID> events = new ArrayList<>(strings.size());
        for (final String string : strings) {
            try {
                events.add(new EventID(pack, string));
            } catch (final ObjectNotFoundException e) {
                throw new Invalid(key, e);
            }
        }
        return events;
    }

    /**
     * Parse a list of conditions from config file.
     *
     * @param key  where to search
     * @param pack configuration package of this file
     * @return requested ConditionIDs
     * @throws Missing if nothing is given
     * @throws Invalid if one of the conditions can't be found
     */
    protected final List<ConditionID> getConditions(final String key, final QuestPackage pack) throws Missing, Invalid {
        final List<String> strings = getStrings(key);
        final List<ConditionID> conditions = new ArrayList<>(strings.size());
        for (final String string : strings) {
            try {
                conditions.add(new ConditionID(pack, string));
            } catch (final ObjectNotFoundException e) {
                throw new Invalid(key, e);
            }
        }
        return conditions;
    }

    /**
     * A config setting which uses a given default value if not set.
     *
     * @param <T> the type of the setting
     */
    protected abstract class DefaultSetting<T> {

        private final T value;

        @SuppressWarnings({"PMD.ConstructorCallsOverridableMethod", "PMD.LocalVariableCouldBeFinal"})
        public DefaultSetting(final T defaultValue) throws Invalid {
            T configValue;
            try {
                configValue = of();
            } catch (final Missing missing) {
                configValue = defaultValue;
            }
            value = configValue;
        }

        @SuppressWarnings("PMD.ShortMethodName")
        protected abstract T of() throws Missing, Invalid;

        public final T get() {
            return value;
        }
    }

    /**
     * A config setting which doesn't throw a Missing exception if not specified.
     *
     * @param <T> the type of the setting
     */
    protected abstract class OptionalSetting<T> {
        @Nullable
        private final T optional;

        @SuppressWarnings({"PMD.ConstructorCallsOverridableMethod", "PMD.LocalVariableCouldBeFinal"})
        public OptionalSetting() throws Invalid {
            T optionalSetting;
            try {
                optionalSetting = of();
            } catch (final Missing missing) {
                optionalSetting = null;
            }
            optional = optionalSetting;
        }

        @SuppressWarnings("PMD.ShortMethodName")
        protected abstract T of() throws Missing, Invalid;

        @Nullable
        public final T get() {
            return optional;
        }
    }

    /**
     * Thrown when the config could not be loaded due to an error.
     */
    public class InvalidSimpleConfigException extends InvalidConfigurationException {
        @Serial
        private static final long serialVersionUID = 5231741827329435199L;

        private final String message;

        private final String cause;

        public InvalidSimpleConfigException(final String cause) {
            super();
            this.cause = cause;
            this.message = "Could not load '" + name + "':" + this.cause;
        }

        @Override
        public String getMessage() {
            return this.message;
        }

        public final String getName() {
            return name;
        }
    }

    /**
     * Thrown when a setting is missing.
     */
    public class Missing extends InvalidSimpleConfigException {
        @Serial
        private static final long serialVersionUID = 1827433702663413827L;

        public Missing(final String missingSetting) {
            super(RPG_MENU_CONFIG_SETTING + missingSetting + " is missing!");
        }
    }

    /**
     * Thrown when a setting is invalid.
     */
    public class Invalid extends InvalidSimpleConfigException {
        @Serial
        private static final long serialVersionUID = -4898301219445719212L;

        public Invalid(final String invalidSetting) {
            super(RPG_MENU_CONFIG_SETTING + invalidSetting + " is invalid!");
        }

        public Invalid(final String invalidSetting, final String cause) {
            super(RPG_MENU_CONFIG_SETTING + invalidSetting + " is invalid: " + cause);
        }

        public Invalid(final String invalidSetting, final Throwable cause) {
            super(RPG_MENU_CONFIG_SETTING + invalidSetting + " is invalid: " + cause.getMessage());
        }
    }
}
