package org.cubeville.simplenbt;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.cubeville.commons.commands.CommandParser;

import org.cubeville.cvtools.commands.CommandMapManager;
import org.cubeville.cvtools.events.*;

import org.cubeville.simplenbt.commands.armor.*;
import org.cubeville.simplenbt.commands.banner.*;
import org.cubeville.simplenbt.commands.block.sign.*;
import org.cubeville.simplenbt.commands.book.*;
import org.cubeville.simplenbt.commands.entity.*;
import org.cubeville.simplenbt.commands.firework.*;
import org.cubeville.simplenbt.commands.item.*;
import org.cubeville.simplenbt.commands.item.attributes.*;
import org.cubeville.simplenbt.commands.item.enchantments.*;
import org.cubeville.simplenbt.commands.item.flags.*;
import org.cubeville.simplenbt.commands.item.lore.*;
import org.cubeville.simplenbt.commands.itemframe.*;
import org.cubeville.simplenbt.commands.mob.*;
import org.cubeville.simplenbt.commands.mob.armorstand.*;
import org.cubeville.simplenbt.commands.mob.horse.*;
import org.cubeville.simplenbt.commands.mob.other.*;
import org.cubeville.simplenbt.commands.potion.*;
import org.cubeville.simplenbt.commands.selection.*;
import org.cubeville.simplenbt.commands.skull.*;


@SuppressWarnings("unused")
public class CVSimpleNbt extends JavaPlugin {

    CommandParser commandParser;
    
    public void onEnable() {
        CommandMapManager.registerMaps();

        commandParser = new CommandParser();

        // ARMOR
        commandParser.addCommand(new ArmorColor());
		
        // BANNER
        commandParser.addCommand(new BannerPatternAdd());
        commandParser.addCommand(new BannerPatternClear());
        commandParser.addCommand(new BannerPatternRemove());
        commandParser.addCommand(new BannerColor());
		
        // BLOCK -- sign
        commandParser.addCommand(new BlockSignClear());
        commandParser.addCommand(new BlockSignRemove());
        commandParser.addCommand(new BlockSignSet());
		
        // BOOK
        commandParser.addCommand(new BookAuthor());
        commandParser.addCommand(new BookColorize());
        commandParser.addCommand(new BookTitle());
        commandParser.addCommand(new BookUnsign());
		
        // ENTITY
        commandParser.addCommand(new EntityGlow());
        commandParser.addCommand(new EntityHide());
        commandParser.addCommand(new EntityInfo());
        commandParser.addCommand(new EntityInvulnerable());
        commandParser.addCommand(new EntityMove());
        commandParser.addCommand(new EntityMoveHere());
        commandParser.addCommand(new EntityName());
        commandParser.addCommand(new EntityPainting());
        commandParser.addCommand(new EntityRemove());
        commandParser.addCommand(new EntityRide());
        commandParser.addCommand(new EntityRotate());
        commandParser.addCommand(new EntitySilent());
        commandParser.addCommand(new EntityUUID());
        commandParser.addCommand(new EntityVelocity());
		
        // FIREWORK
        commandParser.addCommand(new FireworkEffectAdd());
        commandParser.addCommand(new FireworkEffectClear());
        commandParser.addCommand(new FireworkEffectRemove());
        commandParser.addCommand(new FireworkPower());
		
        // ITEM	
        commandParser.addCommand(new ItemDurability());
        commandParser.addCommand(new ItemName());
        commandParser.addCommand(new ItemPrintName());
        commandParser.addCommand(new ItemType());
		
        // ITEM --flags
        commandParser.addCommand(new ItemFlags());
        commandParser.addCommand(new ItemFlagsAdd());
        commandParser.addCommand(new ItemFlagsClear());
        commandParser.addCommand(new ItemFlagsRemove());
		
        // ITEM --attributes
        commandParser.addCommand(new ItemAttributesAdd());
        commandParser.addCommand(new ItemAttributesClear());
        commandParser.addCommand(new ItemAttributesRemove());
		
        // ITEM --enchantments
        commandParser.addCommand(new ItemEnchantmentsAdd());
        commandParser.addCommand(new ItemEnchantmentsClear());
        commandParser.addCommand(new ItemEnchantmentsRemove());

        // ITEM --lore
        commandParser.addCommand(new ItemLoreAdd());
        commandParser.addCommand(new ItemLoreClear());
        commandParser.addCommand(new ItemLoreRemove());

        // ITEMFRAME
        commandParser.addCommand(new ItemframeFix());
        commandParser.addCommand(new ItemframeHide());
        
        // MOB
        commandParser.addCommand(new MobAge());
        commandParser.addCommand(new MobAI());
        commandParser.addCommand(new MobAttributes());
        commandParser.addCommand(new MobEquipment());
        commandParser.addCommand(new MobItemPickup());
        commandParser.addCommand(new MobTame());
        commandParser.addCommand(new MobUntame());

        // MOB --armor stand
        commandParser.addCommand(new MobArmorStandMarker());
        commandParser.addCommand(new MobArmorStandPoses());
        commandParser.addCommand(new MobArmorStandSmall());
        commandParser.addCommand(new MobArmorStandVisible());

        // MOB --horse
        commandParser.addCommand(new MobHorseColor());
        commandParser.addCommand(new MobHorseStyle());
        commandParser.addCommand(new MobHorseUntame());
        commandParser.addCommand(new MobHorseVariant());

        // MOB --other (this includes all specific mobs that only have 1 class)
        commandParser.addCommand(new MobCreeperCharge());
        commandParser.addCommand(new MobOcelotType());
        commandParser.addCommand(new MobRabbitType());
        commandParser.addCommand(new MobSheepColor());
        commandParser.addCommand(new MobSlimeSize());
        commandParser.addCommand(new MobSnowmanDerp());
        commandParser.addCommand(new MobVillagerProfession());

        // OBJECT
        commandParser.addCommand(new ObjectDeselect());
        commandParser.addCommand(new ObjectSelect());
        commandParser.addCommand(new ObjectSelectNearest());

        // POTION
        commandParser.addCommand(new PotionEffectAdd());
        commandParser.addCommand(new PotionEffectClear());
        commandParser.addCommand(new PotionEffectRemove());
        commandParser.addCommand(new PotionType());

        // SKULL
        commandParser.addCommand(new SkullOwner());
        commandParser.addCommand(new SkullType());

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new EventBlockRemoval(), this);
        pm.registerEvents(new EventEntityDeath(), this);
        pm.registerEvents(new EventPlayerInteract(), this);
        pm.registerEvents(new EventPlayerInteractEntity(), this);
        pm.registerEvents(new EventPlayerQuit(), this);
    }

    public void onDisable() {
        CommandMapManager.unregisterMaps();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(command.getName().equals("snbt")) {
            return commandParser.execute(sender, args);
        }
        return false;
    }
    
}

