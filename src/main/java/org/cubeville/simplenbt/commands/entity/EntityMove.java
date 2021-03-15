package org.cubeville.simplenbt.commands.entity;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterUUID;
import org.cubeville.commons.commands.CommandParameterVector;
import org.cubeville.commons.commands.CommandResponse;

import org.cubeville.simplenbt.commands.CommandOnSelection;

public class EntityMove extends CommandOnSelection {

    public EntityMove() {
        super("entity move", Entity.class);
        setPermission("snbt.entity");
        addBaseParameter(new CommandParameterVector());
        addParameter("uuid", true, new CommandParameterUUID());
    }

    @Override
    public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) throws CommandExecutionException {

        Entity entity = getEntity(sender, (UUID) parameters.get("uuid"));

        Location location = entity.getLocation();
        location = location.add((Vector) baseParameters.get(0));
        entity.teleport(location);

        return new CommandResponse("&aEntity moved.");
    }

}
