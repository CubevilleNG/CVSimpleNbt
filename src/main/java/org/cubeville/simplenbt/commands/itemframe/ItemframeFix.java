package org.cubeville.simplenbt.commands.itemframe;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterBoolean;
import org.cubeville.commons.commands.CommandResponse;

import org.cubeville.cvtools.commands.CommandMap;
import org.cubeville.cvtools.commands.CommandMapManager;

public class ItemframeFix extends Command
{
    public ItemframeFix() {
        super("itemframe fix");
        setPermission("snbt.itemframe.fix");
        addOptionalBaseParameter(new CommandParameterBoolean());
    }

    @Override
    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        CommandMap commandMap = CommandMapManager.primaryMap;
        if (!commandMap.contains(player)) {
            throw new CommandExecutionException("&cPlease select an &6entity&c!");
        }

        if(!(commandMap.get(player) instanceof ItemFrame)) {
            throw new CommandExecutionException("&cPlease select an &6Item Frame&c!");
        }

        ItemFrame frame = (ItemFrame) commandMap.get(player);
        if(baseParameters.size() > 0 && ((boolean) baseParameters.get(0)) == false) {
            frame.setFixed(false);
        }
        else {
            frame.setFixed(true);
        }

        return null;
    }
    
}
