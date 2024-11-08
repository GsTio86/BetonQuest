package org.betonquest.betonquest.item.typehandler;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.item.QuestItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("PMD.CommentRequired")
public class NbtHandler {
    private QuestItem.Existence existence = QuestItem.Existence.WHATEVER;

    private Map<String, Object> nbtData = new HashMap<>();

    public NbtHandler() {
    }

    /**
     * Parses input data with key:type:value format.
     * Example: "key1:int:123,key2:string:hello,key3:boolean:true"
     */
    public void parse(final String data) throws InstructionParseException {
        try {
            String[] entries = data.split(",");
            for (String entry : entries) {
                String[] keyTypeValue = entry.split(":", 3);
                if (keyTypeValue.length != 3) {
                    throw new InstructionParseException("Invalid NBT data format: " + entry);
                }
                String key = keyTypeValue[0];
                String type = keyTypeValue[1].toLowerCase();
                String value = keyTypeValue[2];
                switch (type) {
                    case "int":
                        nbtData.put(key, Integer.parseInt(value));
                        break;
                    case "double":
                        nbtData.put(key, Double.parseDouble(value));
                        break;
                    case "float":
                        nbtData.put(key, Float.parseFloat(value));
                        break;
                    case "boolean":
                        nbtData.put(key, Boolean.parseBoolean(value));
                        break;
                    case "string":
                    default:
                        nbtData.put(key, value);
                        break;
                }
            }
        } catch (Exception e) {
            throw new InstructionParseException("Could not parse NBT data: " + data, e);
        }
    }

    public void require(final Map<String, String> nbt) {
        this.existence = QuestItem.Existence.REQUIRED;
        this.nbtData.putAll(nbt);
    }

    public void forbid() {
        this.existence = QuestItem.Existence.FORBIDDEN;
    }

    public QuestItem.Existence getExistence() {
        return existence;
    }

    public boolean has() {
        return existence == QuestItem.Existence.REQUIRED;
    }

    public Map<String, Object> getNbtData() {
        return nbtData;
    }

    /**
     * Checks if the item has the required NBT data.
     */
    public boolean check(final ItemStack itemStack) {
        if (existence == QuestItem.Existence.WHATEVER) {
            return true;
        }
        NBTItem nbtItem = new NBTItem(itemStack);
        if (existence == QuestItem.Existence.FORBIDDEN) {
            return nbtData.keySet().stream().noneMatch(nbtItem::hasKey);
        }
        if (existence == QuestItem.Existence.REQUIRED) {
            for (Map.Entry<String, Object> entry : nbtData.entrySet()) {
                String key = entry.getKey();
                Object expectedValue = entry.getValue();
                if (!nbtItem.hasTag(key)) {
                    return false;
                }
                boolean matches = false;

                if (expectedValue instanceof String) {
                    matches = nbtItem.getString(key).equalsIgnoreCase((String) expectedValue);
                } else if (expectedValue instanceof Double) {
                    matches = Double.valueOf(nbtItem.getDouble(key)).equals(expectedValue);
                } else if (expectedValue instanceof Boolean) {
                    matches = Boolean.valueOf(nbtItem.getBoolean(key)).equals(expectedValue);
                } else if (expectedValue instanceof Integer) {
                    matches = Integer.valueOf(nbtItem.getInteger(key)).equals(expectedValue);
                } else if (expectedValue instanceof Float) {
                    matches = Float.valueOf(nbtItem.getFloat(key)).equals(expectedValue);
                }

                if (!matches) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        if (existence != QuestItem.Existence.REQUIRED) {
            return "";
        }
        StringBuilder result = new StringBuilder("nbt: ");
        nbtData.forEach((key, value) -> result.append(key).append(":").append(value).append(", "));
        return result.toString().replaceAll(", $", "");
    }
}
