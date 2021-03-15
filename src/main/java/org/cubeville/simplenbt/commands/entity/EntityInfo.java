package org.cubeville.simplenbt.commands.entity;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandResponse;
import org.cubeville.commons.utils.EntityUtils;
import org.cubeville.cvtools.commands.CommandMap;
import org.cubeville.cvtools.commands.CommandMapManager;

public class EntityInfo extends Command {

    public EntityInfo() {
        super("entity info");
        setPermission("snbt.entity");
        addFlag("detail");
    }

    @Override
    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {
        CommandMap commandMap = CommandMapManager.primaryMap;
        if (!commandMap.contains(player)) {
            throw new CommandExecutionException("&cPlease select an &6entity&c!");
        } else if (!(commandMap.get(player) instanceof Entity)) {
            throw new CommandExecutionException("&cPlease select an &6entity&c!");
        }
		
        CommandResponse cr = new CommandResponse("&6--------------------------");
        boolean detail = (flags.contains("detail"));
        Entity entity = (Entity) commandMap.get(player);
     
        for(String string: EntityUtils.getInfo((Entity) commandMap.get(player), detail)) {
            cr.addMessage(string);
        }

        if(entity instanceof LivingEntity) {
            Collection<PotionEffect> effects = ((LivingEntity) entity).getActivePotionEffects();
            if(effects.size() == 0) {
                cr.addMessage("&cNo active effects.");
            }
            else {
                cr.addMessage("&aActive effects:");
                for(PotionEffect effect: effects) {
                    cr.addMessage("&a" + effect.getType().getName() + ", duration: " + effect.getDuration() + ", level: " + effect.getAmplifier());
                }
            }
        }
        
        cr.addMessage("&6--------------------------");
        return cr;
    }
}
