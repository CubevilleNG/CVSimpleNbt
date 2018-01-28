package org.cubeville.simplenbt.commands.entity;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandResponse;
import org.cubeville.commons.utils.EntityUtils;
import org.cubeville.cvtools.commands.CommandMap;
import org.cubeville.cvtools.commands.CommandMapManager;

import net.minecraft.server.v1_12_R1.PacketPlayOutMount;

public class EntityRide extends Command {

    public EntityRide() {
        super("entity ride");
        setPermission("snbt.entity");
        addFlag("reverse");
        addFlag("stack");
        addFlag("unstack");
        addFlag("armorstand");
    }

    @Override
    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) 
        throws CommandExecutionException {

        if(checkMoreThanOne(flags.contains("reverse"), flags.contains("stack"), flags.contains("unstack"), flags.contains("armorstand")))
            throw new CommandExecutionException("Incompatible parameters.");
        
        if (flags.contains("reverse") && !flags.contains("stack") && flags.contains("unstack")) {
            dismountAll(player);
            return null;
        }

        CommandMap commandMap = CommandMapManager.primaryMap;
        if (!commandMap.contains(player)) {
            throw new CommandExecutionException("&cPlease select an &6entity&c!");
        } else if (!(commandMap.get(player) instanceof Entity)) {
            throw new CommandExecutionException("&cPlease select an &6entity&c!");
        }
		
        Entity entity = (Entity) commandMap.get(player);

        if (flags.contains("reverse")) {
            if (getHighestEntity(player) != entity) {
                stackHighestEntity(player, entity);
            }
        } else if (flags.contains("stack")) {
        } else if (flags.contains("unstack")) {
            dismountAll(entity);
        } else if (flags.contains("armorstand")) {
            ArmorStand armorStand = (ArmorStand) EntityUtils.getNearestEntity(player.getLocation(), EntityUtils.getEntitiesByType(player.getWorld().getNearbyEntities(player.getLocation(), 10, 10, 10), EntityType.ARMOR_STAND));
            if(armorStand == null) throw new CommandExecutionException("No armor stand within 10 blocks radius found.");
            armorStand.setPassenger(entity);
        } else {
        	if (!containsEntity(entity, player)) getHighestEntity(entity).setPassenger(player);
        }
		
        return new CommandResponse("&aEntity ride successfully executed!");
    }
    
    public static boolean containsEntity(Entity vehicle, Entity passenger) {
    	boolean higher = true;
    	
    	if (vehicle == passenger) return true;
    	
    	while (higher) {
    		if (!passenger.isEmpty()) {
    			if (passenger.getPassenger() == vehicle) return true;
    			else passenger = passenger.getPassenger();
    		} else break;
    	}
    	
		return false;
    	
    }
	
    public static Entity getHighestEntity(Entity e) {
        boolean higher = true;
 
        for(int eb = 0; eb < 1000 && higher; eb++) {
            if(e.isEmpty()) break;
            e = e.getPassenger();
        }

        return e;
    }
	
    public static void stackHighestEntity(Entity vehicle, Entity passenger) {
    	if (containsEntity(vehicle, passenger)) return;
    	
        vehicle = getHighestEntity(vehicle);
        if (vehicle == passenger)
            return;
        vehicle.setPassenger(passenger);
        if (vehicle instanceof Player) {
            Player player = (Player) vehicle;
            PacketPlayOutMount packet = new PacketPlayOutMount(((CraftPlayer)player).getHandle());
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
        }
    }
	
    public static void dismountAll(Entity e) {
        boolean more = true;
	
        while (more)
            if (!e.isEmpty()) {
                Entity ex = e.getPassenger();
                e.eject();
                if (e instanceof Player) {
                    PacketPlayOutMount packet = new PacketPlayOutMount(((CraftPlayer)e).getHandle());
                    ((CraftPlayer)e).getHandle().playerConnection.sendPacket(packet);
                }
                e = ex;
            } else
                more = false;
    }
		
}
