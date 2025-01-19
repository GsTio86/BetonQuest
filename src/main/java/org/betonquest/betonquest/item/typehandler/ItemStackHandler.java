package org.betonquest.betonquest.item.typehandler;

import org.betonquest.betonquest.api.profiles.Profile;
import org.betonquest.betonquest.exceptions.QuestException;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Handles de/-serialization of ItemStack from/into QuestItem string format.
 *
 * @param <M> handled meta
 */
public interface ItemStackHandler<M extends ItemStack> {
    /**
     * Gets the class of meta this Handler works on.
     *
     * @return the ItemStack class for the Handler
     */
    Class<M> stackClass();

    /**
     * The keys this handler allows in {@link #set(String, String)} which are used for data identification.
     *
     * @return keys in lower case
     */
    Set<String> keys();

    /**
     * Converts the meta into QuestItem format.
     *
     * @param stack the meta to serialize
     * @return parsed values or null
     */
    @Nullable
    String serializeToString(M stack);

    /**
     * Converts the meta into QuestItem format if it is applicable to {@link #stackClass()}.
     * When the meta is not applicable it will return null.
     *
     * @param stack the meta to serialize
     * @return parsed values or null
     */
    @SuppressWarnings("unchecked")
    @Nullable
    default String rawSerializeToString(final ItemStack stack) {
        if (stackClass().isInstance(stack)) {
            return serializeToString((M) stack);
        }
        return null;
    }

    /**
     * Sets the data into the Handler.
     * <p>
     * The data may be the same as the key if it is just a keyword.
     *
     * @param key  the lower case key
     * @param data the associated data
     * @throws QuestException if the data is malformed or key not valid for handler
     */
    void set(String key, String data) throws QuestException;

    /**
     * Reconstitute this Handler data into the specified meta.
     *
     * @param stack the meta to populate
     */
    void populate(M stack);

    /**
     * Reconstitute this Handler data into the specified meta.
     * <p>
     * Defaults to {@link #populate(ItemStack)}.
     *
     * @param stack    the meta to populate
     * @param profile the optional profile for customized population
     */
    default void populate(final M stack, @Nullable final Profile profile) {
        populate(stack);
    }

    /**
     * Reconstitute this Handler data into the specified meta if it is applicable to {@link #stackClass()}.
     * <p>
     * When the meta is not applicable nothing changes.
     *
     * @param stack    the meta to populate
     * @param profile the profile for customized population
     */
    @SuppressWarnings("unchecked")
    default void rawPopulate(final ItemStack stack, @Nullable final Profile profile) {
        if (stackClass().isInstance(stack)) {
            populate((M) stack, profile);
        }
    }

    /**
     * Check to see if the specified ItemStack matches the Handler.
     *
     * @param stack the ItemStack to check
     * @return if the meta satisfies the requirement defined via {@link #set(String, String)}
     */
    boolean check(M stack);

    /**
     * Check to see if the specified ItemStack matches the Handler if it is applicable to {@link #stackClass()}.
     * <p>
     * When the meta is not applicable it will return {@code true}.
     *
     * @param stack the ItemStack to check
     * @return if the meta satisfies the requirement defined via {@link #set(String, String)}
     */
    @SuppressWarnings("unchecked")
    default boolean rawCheck(final ItemStack stack) {
        return !stackClass().isInstance(stack) || check((M) stack);
    }
}
