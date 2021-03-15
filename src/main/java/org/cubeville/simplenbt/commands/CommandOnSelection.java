package org.cubeville.simplenbt.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import org.cubeville.commons.commands.BaseCommand;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.cvtools.commands.CommandMap;
import org.cubeville.cvtools.commands.CommandMapManager;

public abstract class CommandOnSelection extends BaseCommand {

    private Class selectionType;

    public CommandOnSelection(String fullCommand, Class selectionType) {
        super(fullCommand);
        this.selectionType = selectionType;
    }

    public Entity getEntity(CommandSender sender, UUID uuid)
        throws CommandExecutionException {
        if(uuid != null) {
            Entity entity = Bukkit.getEntity(uuid);
            if(entity == null) {
                throw new CommandExecutionException("&cNo entity with that UUID found!");
            }
            return entity;
        }
        else {
            if(!(sender instanceof Player)) {
                throw new CommandExecutionException("&cCan't be used on console without UUID parameter!");
            }
            Player player = (Player) sender;
            CommandMap commandMap = CommandMapManager.primaryMap;
            if(commandMap.contains(player) && commandMap.get(player) instanceof Entity) {
                return (Entity) commandMap.get(player);
            }
            else {
                throw new CommandExecutionException("&cPlease select an &6entity&c!");
            }
        }
    }
    
}
