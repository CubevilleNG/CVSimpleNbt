package org.cubeville.simplenbt.commands.entity;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandResponse;

import org.cubeville.simplenbt.commands.CommandOnSelection;

public class EntityUUID extends CommandOnSelection {

    public EntityUUID() {
        super("entity uuid", Entity.class);
        setPermission("snbt.entity");
    }

    @Override
    public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) throws CommandExecutionException {

        Entity entity = getEntity(sender, null);
        return new CommandResponse("&aEntity's UUID: &e" + entity.getUniqueId());

    }
}
