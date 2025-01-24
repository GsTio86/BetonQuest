package org.betonquest.betonquest.item.typehandler;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.changeme.nbtapi.utils.MinecraftVersion;
import org.betonquest.betonquest.api.quest.QuestException;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("PMD.CommentRequired")
public class NbtHandler implements ItemStackHandler<ItemStack> {
    private Existence existence = Existence.WHATEVER;

    private Map<String, Object> nbtData = new HashMap<>();

    public NbtHandler() {
    }

    public void require(final Map<String, String> nbt) {
        this.existence = Existence.REQUIRED;
        this.nbtData.putAll(nbt);
    }

    @Override
    public Class<ItemStack> stackClass() {
        return ItemStack.class;
    }

    @Override
    public Set<String> keys() {
        return Set.of("nbts");
    }

    @Nullable
    @Override
    public String serializeToString(final ItemStack stack) {
        ReadWriteNBT readWriteNBT = NBT.itemStackToNBT(stack);
        if (readWriteNBT != null) {
            StringBuilder result = new StringBuilder("nbts:");
            for (String key : readWriteNBT.getKeys()) {
                if (key.equals("CustomModelData") || key.equals("display") || key.contains("itemsadder")) {
                    continue;
                }
                switch (readWriteNBT.getType(key)) {
                    case NBTTagInt -> result.append(key).append(":int:").append(readWriteNBT.getInteger(key)).append(",");
                    case NBTTagLong -> result.append(key).append(":long:").append(readWriteNBT.getLong(key)).append(",");
                    case NBTTagByte -> result.append(key).append(":byte:").append(readWriteNBT.getByte(key)).append(",");
                    case NBTTagDouble -> result.append(key).append(":double:").append(readWriteNBT.getDouble(key)).append(",");
                    case NBTTagFloat -> result.append(key).append(":float:").append(readWriteNBT.getFloat(key)).append(",");
                    case NBTTagShort -> result.append(key).append(":short:").append(readWriteNBT.getShort(key)).append(",");
                    case NBTTagString -> result.append(key).append(":string:").append(readWriteNBT.getString(key)).append(",");
                }
            }
            result.deleteCharAt(result.length() - 1);
            return result.toString();
        }
        return null;
    }

    /**
     * Parses input data with key:type:value format.
     * Example: "key1:int:123,key2:string:hello,key3:boolean:true"
     */
    @Override
    public void set(final String argKey, final String data) throws QuestException {
        if (argKey.equals("nbts")) {
            try {
                String[] entries = data.split(",");
                for (String entry : entries) {
                    String[] keyTypeValue = entry.split(":", 3);
                    if (keyTypeValue.length != 3) {
                        throw new QuestException("Invalid NBT data format: " + entry);
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
                this.existence = Existence.REQUIRED;
            } catch (Exception e) {
                this.existence = Existence.FORBIDDEN;
                throw new QuestException("Could not parse NBT data: " + data, e);
            }
        }
    }

    @Override
    public void populate(ItemStack stack) {
        if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
            NBT.modifyComponents(stack, nbt -> {
                ReadWriteNBT customNbt = nbt.getOrCreateCompound("minecraft:custom_data");
                for (Map.Entry<String, Object> entry : nbtData.entrySet()) {
                    String nbtKey = entry.getKey();
                    Object nbtObject = entry.getValue();

                    if (nbtObject instanceof Integer value) {
                        customNbt.setInteger(nbtKey, value);
                    } else if (nbtObject instanceof Long value) {
                        customNbt.setLong(nbtKey, value);
                    } else if (nbtObject instanceof Byte value) {
                        customNbt.setByte(nbtKey, value);
                    } else if (nbtObject instanceof Double value) {
                        customNbt.setDouble(nbtKey, value);
                    } else if (nbtObject instanceof Float value) {
                        customNbt.setFloat(nbtKey, value);
                    } else if (nbtObject instanceof Short value) {
                        customNbt.setShort(nbtKey, value);
                    } else if (nbtObject instanceof String value) {
                        customNbt.setString(nbtKey, value);
                    }
                }
            });
        } else {
            for (Map.Entry<String, Object> entry : nbtData.entrySet()) {
                String nbtKey = entry.getKey();
                Object nbtObject = entry.getValue();

                NBT.modify(stack, nbt -> {
                    if (nbtObject instanceof Integer value) {
                        nbt.setInteger(nbtKey, value);
                    } else if (nbtObject instanceof Long value) {
                        nbt.setLong(nbtKey, value);
                    } else if (nbtObject instanceof Byte value) {
                        nbt.setByte(nbtKey, value);
                    } else if (nbtObject instanceof Double value) {
                        nbt.setDouble(nbtKey, value);
                    } else if (nbtObject instanceof Float value) {
                        nbt.setFloat(nbtKey, value);
                    } else if (nbtObject instanceof Short value) {
                        nbt.setShort(nbtKey, value);
                    } else if (nbtObject instanceof String value) {
                        nbt.setString(nbtKey, value);
                    }
                });
            }
        }
    }

    @Override
    public boolean check(ItemStack stack) {
        if (existence == Existence.WHATEVER) {
            return true;
        }
        NBTItem nbtItem = new NBTItem(stack);
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
        result.deleteCharAt(result.length() - 1);
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
