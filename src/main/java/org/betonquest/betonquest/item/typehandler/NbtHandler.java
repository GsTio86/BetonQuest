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
    private Existence existence = Existence.WHATEVER;

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
                    case "integer":
                        nbtData.put(key, Integer.parseInt(value));
                        break;
                    case "long":
                        nbtData.put(key, Long.parseLong(value));
                        break;
                    case "byte":
                        nbtData.put(key, Byte.parseByte(value));
                        break;
                    case "double":
                        nbtData.put(key, Double.parseDouble(value));
                        break;
                    case "float":
                        nbtData.put(key, Float.parseFloat(value));
                        break;
                    case "short":
                        nbtData.put(key, Short.parseShort(value));
                        break;
                    default:
                    case "string":
                        nbtData.put(key, value);
                }
            }
        } catch (Exception e) {
            throw new InstructionParseException("Could not parse NBT data: " + data, e);
        }
    }

    public void require(final Map<String, String> nbt) {
        this.existence = Existence.REQUIRED;
        this.nbtData.putAll(nbt);
    }

    public void forbid() {
        this.existence = Existence.FORBIDDEN;
    }

    public Existence getExistence() {
        return existence;
    }

    public boolean has() {
        return existence == Existence.REQUIRED;
    }

    public Map<String, Object> getNbtData() {
        return nbtData;
    }

    /**
     * Checks if the item has the required NBT data.
     */
    public boolean check(final ItemStack itemStack) {
        if (existence == Existence.WHATEVER) {
            return true;
        }
        NBTItem nbtItem = new NBTItem(itemStack);
        if (existence == Existence.FORBIDDEN) {
            return nbtData.keySet().stream().noneMatch(nbtItem::hasKey);
        }
        if (existence == Existence.REQUIRED) {
            for (Map.Entry<String, Object> entry : nbtData.entrySet()) {
                String key = entry.getKey();
                Object expectedValue = entry.getValue();
                if (!nbtItem.hasTag(key)) {
                    return false;
                }
                boolean matches = false;

                if (expectedValue instanceof Integer) {
                    matches = nbtItem.getInteger(key).equals(expectedValue);
                } else if (expectedValue instanceof Double) {
                    matches = nbtItem.getDouble(key).equals(expectedValue);
                } else if (expectedValue instanceof Float) {
                    matches = nbtItem.getFloat(key).equals(expectedValue);
                } else if (expectedValue instanceof Boolean) {
                    matches = nbtItem.getBoolean(key).equals(expectedValue);
                } else if (expectedValue instanceof Short) {
                    matches = nbtItem.getShort(key).equals(expectedValue);
                } else if (expectedValue instanceof Long) {
                    matches = nbtItem.getLong(key).equals(expectedValue);
                } else if (expectedValue instanceof String) {
                    matches = nbtItem.getString(key).equalsIgnoreCase((String) expectedValue);
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
        if (existence != Existence.REQUIRED) {
            return "";
        }
        // CustomModelData is a special case, it's not an NBT tag but it's still useful to show it
        if (nbtData.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder("nbts:");
        for (Map.Entry<String, Object> entry : nbtData.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String type = getValueType(value);

            if (key.equals("CustomModelData") || key.equals("display") || key.contains("itemsadder")) {
                continue;
            }
            if (type.equals("unknown")) {
                continue;
            }
            result.append(key).append(":")
                    .append(type).append(":")
                    .append(value).append(",");
        }
        return result.toString();
    }

    public String getValueType(Object value) {
        if (value instanceof Integer) {
            return "int";
        } else if (value instanceof Double) {
            return "double";
        } else if (value instanceof Float) {
            return "float";
        } else if (value instanceof Boolean) {
            return "boolean";
        } else if (value instanceof Short) {
            return "short";
        } else if (value instanceof Long) {
            return "long";
        } else if (value instanceof String) {
            return "string";
        }
        return "unknown";
    }
}
