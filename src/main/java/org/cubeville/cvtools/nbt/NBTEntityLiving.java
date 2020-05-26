package org.cubeville.cvtools.nbt;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import net.minecraft.server.v1_15_R1.AttributeInstance;
import net.minecraft.server.v1_15_R1.AttributeMapBase;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.NBTTagString;

public class NBTEntityLiving extends NBTEntity {

	EntityLiving livingEntity;
	LivingEntity entityLiving;
	
	public NBTEntityLiving(Entity entity) {
		super(entity);
		livingEntity = (EntityLiving) nmsEntity;
		entityLiving = (LivingEntity) entity;
	}
	
	@Override
	public EntityLiving getRawNMSEntity() {
		return livingEntity;
	}
	
	@Override
	public LivingEntity getRawEntity() {
		return entityLiving;
	}
	
	public Map<String, Double> getAttributes() {
		Map<String, Double> attributeList = new HashMap<>();
		AttributeMapBase base = livingEntity.getAttributeMap();
		for (AttributeInstance instance: base.a()) {
			getAttributeInstance(base, "poo");
			attributeList.put(instance.getAttribute().getName(), instance.getValue());
		}
		return attributeList;
	}
	
	public void addAttribute(AttributeType type, double value) {
		AttributeMapBase base = livingEntity.getAttributeMap();
		AttributeInstance instance = getAttributeInstance(base, type.toString());
		instance.setValue(value);
		System.out.println(instance.getAttribute().getName() + ":" + instance.getValue());
		base.a(instance);
	}
	
	public void setAbsorption(float f) {
		livingEntity.setAbsorptionHearts(f);
	}
	
	public AttributeInstance getAttributeInstance(AttributeMapBase base, String name) {
		for(AttributeInstance instance: base.a()) {
			System.out.println(name);
			System.out.println(instance.getAttribute().getName());
			System.out.println("_________________");
			if (instance.getAttribute().getName().equals(name)) {
				return instance;
			}
		}
		return null;
	}
	
	public static enum AttributeType {
		GENERIC_ARMOR(NBTTagString.a("generic.armor")),
		GENERIC_ARMOR_TOUGHNESS(NBTTagString.a("generic.armorToughness")),
		GENERIC_ATTACK_DAMAGE(NBTTagString.a("generic.attackDamage")),
		GENERIC_ATTACKS_SPEED(NBTTagString.a("generic.attackSpeed")),
		GENERIC_FOLLOW_RANGE(NBTTagString.a("generic.followRange")),
		GENERIC_KNOCKBACK_RESISTANCE(NBTTagString.a("generic.knockbackResistance")),
		GENERIC_LUCK(NBTTagString.a("generic.luck")),
		GENERIC_MAX_HEALTH(NBTTagString.a("generic.maxHealth")),
		GENERIC_MOVEMENT_SPEED(NBTTagString.a("generic.movementSpeed")),
		HORSE_JUMP_STRENGTH(NBTTagString.a("horse.jumpStrength"));
		
		private final NBTTagString type;
		
		private AttributeType(final NBTTagString type) {
			this.type = type;
		}
		
		public NBTTagString toTagString() {
			return type;
		}
		
		@Override
		public String toString() {
			return type.toString().replaceAll("\"", "");
		}
	}
}
