package org.cubeville.cvtools.nbt;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.inventory.ItemStack;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.comphenix.protocol.wrappers.nbt.NbtList;
import com.comphenix.protocol.wrappers.nbt.NbtType;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;

import net.minecraft.server.v1_16_R3.NBTTagCompound;

public class Attributes {
    public enum Operation {
        ADD_NUMBER(0),
        MULTIPLY_PERCENTAGE(1),
        ADD_PERCENTAGE(2);
        private int id;
        
        private Operation(int id) {
            this.id = id;
        }
        
        public int getId() {
            return id;
        }
        
        public static Operation fromId(int id) {
            // Linear scan is very fast for small N
            for (Operation op : values()) {
                if (op.getId() == id) {
                    return op;
                }
            }
            throw new IllegalArgumentException("Corrupt operation ID " + id + " detected.");
        }
    }
    
    public static class AttributeType {
        private static ConcurrentMap<String, AttributeType> LOOKUP = Maps.newConcurrentMap();
        public static final AttributeType GENERIC_MAX_HEALTH = new AttributeType("generic.maxHealth").register();
        public static final AttributeType GENERIC_FOLLOW_RANGE = new AttributeType("generic.followRange").register();
        public static final AttributeType GENERIC_ATTACK_DAMAGE = new AttributeType("generic.attackDamage").register();
        public static final AttributeType GENERIC_MOVEMENT_SPEED = new AttributeType("generic.movementSpeed").register();
        public static final AttributeType GENERIC_KNOCKBACK_RESISTANCE = new AttributeType("generic.knockbackResistance").register();
        public static final AttributeType GENERIC_ATTACK_SPEED = new AttributeType("generic.attackSpeed").register();
        public static final AttributeType GENERIC_ARMOR = new AttributeType("generic.armor").register();
        public static final AttributeType GENERIC_ARMOR_TOUGHNESS = new AttributeType("generic.armorToughness").register();
        
        private final String minecraftId;
        
        /**
         * Construct a new attribute type.
         * <p>
         * Remember to {@link #register()} the type.
         * @param minecraftId - the ID of the type.
         */
        public AttributeType(String minecraftId) {
            this.minecraftId = minecraftId;
        }
        
        /**
         * Retrieve the associated minecraft ID.
         * @return The associated ID.
         */
        public String getMinecraftId() {
            return minecraftId;
        }
        
        /**
         * Register the type in the central registry.
         * @return The registered type.
         */
        // Constructors should have no side-effects!  
        public AttributeType register() {
            AttributeType old = LOOKUP.putIfAbsent(minecraftId, this);
            return old != null ? old : this;
        }
        
        /**
         * Retrieve the attribute type associated with a given ID.
         * @param minecraftId The ID to search for.
         * @return The attribute type, or NULL if not found.
         */
        public static AttributeType fromId(String minecraftId) {
            return LOOKUP.get(minecraftId);
        }
        
        /**
         * Retrieve every registered attribute type.
         * @return Every type.
         */
        public static Iterable<AttributeType> values() {
            return LOOKUP.values();
        }
        
    	public static AttributeType getAttributeTypeByName(String string) {
    		if (string.equalsIgnoreCase("armor")) {
    			return AttributeType.GENERIC_ARMOR;
    		} else if (string.equalsIgnoreCase("toughness")) {
    			return AttributeType.GENERIC_ARMOR_TOUGHNESS;
    		} else if (string.equalsIgnoreCase("damage")) {
    			return AttributeType.GENERIC_ATTACK_DAMAGE;
    		} else if (string.equalsIgnoreCase("attspeed")) {
    			return AttributeType.GENERIC_ATTACK_SPEED;
    		} else if (string.equalsIgnoreCase("kbresist")) {
    			return AttributeType.GENERIC_KNOCKBACK_RESISTANCE;
    		} else if (string.equalsIgnoreCase("health")) {
    			return AttributeType.GENERIC_MAX_HEALTH;
    		} else if (string.equalsIgnoreCase("movespeed")) {
    			return AttributeType.GENERIC_MOVEMENT_SPEED;
    		} else {
    			throw new IllegalArgumentException(string + " is no valid attribute type!");
    		}
    	}
    }

    public static class Attribute {
        private NbtCompound data;

        private Attribute(Builder builder) {
            data = NbtFactory.ofCompound("");
            setAmount(builder.amount);
            setOperation(builder.operation);
            setAttributeType(builder.type);
            setName(builder.name);
            setUUID(builder.uuid);
            setSlot(builder.slot);
        }
        
        private Attribute(NbtCompound data) {
            this.data = data;
        }
        
        public double getAmount() {
            return data.getDouble("Amount");
        }

        public void setAmount(double amount) {
            data.put("Amount", amount);
        }

        public Operation getOperation() {
            return Operation.fromId(data.getInteger("Operation"));
        }

        public void setOperation(@Nonnull Operation operation) {
            Preconditions.checkNotNull(operation, "operation cannot be NULL.");
            data.put("Operation", operation.getId());
        }

        public AttributeType getAttributeType() {
            return AttributeType.fromId(data.getString("AttributeName"));
        }

        public void setAttributeType(@Nonnull AttributeType type) {
            Preconditions.checkNotNull(type, "type cannot be NULL.");
            data.put("AttributeName", type.getMinecraftId());
        }

        public String getName() {
            return data.getString("Name");
        }

        public void setName(@Nonnull String name) {
            data.put("Name", name);
        }

        public UUID getUUID() {
            return new UUID(data.getLong("UUIDMost"), data.getLong("UUIDLeast"));
        }

        public void setUUID(@Nonnull UUID id) {
            Preconditions.checkNotNull("id", "id cannot be NULL.");
            data.put("UUIDLeast", id.getLeastSignificantBits());
            data.put("UUIDMost", id.getMostSignificantBits());
        }
        
        public String getSlot() {
        	return data.getString("Slot");
        }
        
        public void setSlot(@Nonnull String slot) {
        	data.put("Slot", slot);
        }

        /**
         * Construct a new attribute builder with a random UUID and default operation of adding numbers.
         * @return The attribute builder.
         */
        public static Builder newBuilder() {
            return new Builder().uuid(UUID.randomUUID()).operation(Operation.ADD_NUMBER);
        }
        
        // Makes it easier to construct an attribute
        public static class Builder {
            private double amount;
            private Operation operation = Operation.ADD_NUMBER;
            private AttributeType type;
            private String name;
            private UUID uuid;
            private String slot;

            private Builder() {
                // Don't make this accessible
            }
            
            public Builder amount(double amount) {
                this.amount = amount;
                return this;
            }
            public Builder operation(Operation operation) {
                this.operation = operation;
                return this;
            }
            public Builder type(AttributeType type) {
                this.type = type;
                return this;
            }
            public Builder name(String name) {
                this.name = name;
                return this;
            }
            public Builder uuid(UUID uuid) {
                this.uuid = uuid;
                return this;
            }
            public Builder slot(String slot) {
            	this.slot = slot;
            	return this;
            }
            public Attribute build() {
                return new Attribute(this);
            }
        }
    }
    
    // This may be modified
    public ItemStack stack;
    private NbtList<Map<String, NbtBase<?>>> attributes;
    
    public Attributes(ItemStack stack) {
        // Create a CraftItemStack (under the hood)
        this.stack = MinecraftReflection.getBukkitItemStack(stack);
        
        // Load NBT
        NbtCompound nbt = (NbtCompound) NbtFactory.fromItemTag(this.stack);
        this.attributes = nbt.getListOrDefault("AttributeModifiers");
        this.attributes.setElementType(NbtType.TAG_COMPOUND);
    }
    
    public Attributes(org.bukkit.entity.Entity e) {
        // TODO
    	// net.minecraft.server.v1_16_R3.Entity entity = (net.minecraft.server.v1_16_R3.Entity) ((CraftEntity) e).getHandle();
    	// NBTTagCompound nbt = new NBTTagCompound();
    	// entity.f(nbt);
    }
    
    /**
     * Retrieve the modified item stack.
     * @return The modified item stack.
     */
    public ItemStack getStack() {
        return stack;
    }
    
    /**
     * Retrieve the number of attributes.
     * @return Number of attributes.
     */
    public int size() {
        return attributes.size();
    }
    
    /**
     * Add a new attribute to the list.
     * @param attribute - the new attribute.
     */
    public void add(Attribute attribute) {
        attributes.add(attribute.data);
    }
    
    /**
     * Remove the first instance of the given attribute.
     * <p>
     * The attribute will be removed using its UUID.
     * @param attribute - the attribute to remove.
     * @return TRUE if the attribute was removed, FALSE otherwise.
     */
    public boolean remove(Attribute attribute) {
        UUID uuid = attribute.getUUID();
        
        for (Iterator<Attribute> it = values().iterator(); it.hasNext(); ) {
            if (Objects.equal(it.next().getUUID(), uuid)) {
                it.remove();
                return true;
            }
        }
        return false;
    }
    
    public void clear() {
        attributes.getValue().clear();
    }
    
    /**
     * Retrieve the attribute at a given index.
     * @param index - the index to look up.
     * @return The attribute at that index.
     */
    public Attribute get(int index) {
        return new Attribute((NbtCompound) attributes.getValue().get(index));
    }

    // We can't make Attributes itself iterable without splitting it up into separate classes
    public Iterable<Attribute> values() {
        return new Iterable<Attribute>() {
            @Override
            public Iterator<Attribute> iterator() {
                // Generics disgust me sometimes
                return Iterators.transform(
                        attributes.getValue().iterator(), 
                        new Function<NbtBase<Map<String, NbtBase<?>>>, Attribute>() {
                    @Override
                    public Attribute apply(@Nullable NbtBase<Map<String, NbtBase<?>>> element) {
                        return new Attribute((NbtCompound) element);
                    }
                });
            }
        };
    }
    
	public static enum EquipmentSlot {
		MAIN_HAND("mainhand"),
		OFF_HAND("offhand"),
		HEAD("head"),
		CHEST("chest"),
		LEGS("legs"),
		FEET("feet");
		
		private String slot;
		
		private EquipmentSlot(final String slot) {
			this.slot = slot;
		}
		
		@Override
		public String toString() {
			return slot;
		}
	}
}
