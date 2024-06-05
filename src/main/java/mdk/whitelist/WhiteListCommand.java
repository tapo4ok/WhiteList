package mdk.whitelist;

import com.google.common.collect.ImmutableList;
import mdk.mutils.api.config.SimpleConfig;
import mdk.whitelist.bungee.WhiteListPlugin;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

public class WhiteListCommand extends Command {
    private final String[] w1 = new String[] {
            "off",
            "on",
            "add",
            "remove",
            "load",
            "list",
            "char",
            "replace"
    };

    private final String[] w2 = new String[] {
            "true",
            "false"
    };
    public WhiteListCommand(IL l) {
        super(l.getConfig0().getConfig().replace_vanila ? "rwhitelist" : "whitelist");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(args.length>0)) {
            sender.sendMessage(String.format("/%s (%s)", getName(), String.join("/", w1)));
            return false;
        }
        switch (args[0]) {
            case "off":
            {
                SimpleConfig<WhiteListConfig> config = WhiteList.config;
                config.getConfig().enable = false;
                sender.sendMessage("Whitelist set false");
                try {
                    config.Save();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case "on":
            {
                SimpleConfig<WhiteListConfig> config = WhiteList.config;
                config.getConfig().enable = true;
                sender.sendMessage("Whitelist set true");
                try {
                    config.Save();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case "add": {
                if (WhiteListPlugin.list.add(args[1])) {
                    sender.sendMessage("Whitelist add "+ args[1]);
                }
                else {
                    sender.sendMessage("Error");
                }
                break;
            }
            case "remove": {
                if (WhiteListPlugin.list.remove(args[1])) {
                    sender.sendMessage("Whitelist remove "+ args[1]);
                }
                else {
                    sender.sendMessage("Error");
                }
                break;
            }
            case "list": {
                sender.sendMessage(WhiteList.list.toString());
                break;
            }
            case "load": {
                WhiteList.list.load();
                break;
            }
            case "char": {
                SimpleConfig<WhiteListConfig> config = WhiteList.config;
                config.getConfig().eneble_check_Name = Boolean.parseBoolean(args[1]);
                try {
                    config.Save();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sender.sendMessage("char set " + args[1]);
                break;
            }
            case "replace": {
                SimpleConfig<WhiteListConfig> config = WhiteList.config;
                config.getConfig().replace_vanila = Boolean.parseBoolean(args[1]);
                try {
                    config.Save();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sender.sendMessage("replace vanila set " + args[1]);
                sender.sendMessage("Please restart the server.");

            }
        }


        return true;
    }


    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        if (args.length == 0) {
            return ImmutableList.of();
        } else if (args.length == 1) {
            String lastWord = args[args.length - 1];
            ArrayList<String> matchedPlayers = new ArrayList();
            Iterator var7 = Arrays.stream(w1).iterator();
            while(var7.hasNext()) {
                String name = (String)var7.next();
                if (StringUtil.startsWithIgnoreCase(name, lastWord)) {
                    matchedPlayers.add(name);
                }
            }

            Collections.sort(matchedPlayers, String.CASE_INSENSITIVE_ORDER);
            return matchedPlayers;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")) {
                String lastWord = args[args.length - 1];

                Player senderPlayer = sender instanceof Player ? (Player) sender : null;

                ArrayList<String> matchedPlayers = new ArrayList<String>();
                for (Player player : sender.getServer().getOnlinePlayers()) {
                    String name = player.getName();
                    if ((senderPlayer == null || senderPlayer.canSee(player)) && StringUtil.startsWithIgnoreCase(name, lastWord)) {
                        matchedPlayers.add(name);
                    }
                }

                Collections.sort(matchedPlayers, String.CASE_INSENSITIVE_ORDER);
                return matchedPlayers;
            } else if (args[0].equalsIgnoreCase("char")) {
                String lastWord = args[args.length - 1];
                ArrayList<String> matchedPlayers = new ArrayList();
                Iterator var7 = Arrays.stream(w2).iterator();
                while(var7.hasNext()) {
                    String name = (String)var7.next();
                    if (StringUtil.startsWithIgnoreCase(name, lastWord)) {
                        matchedPlayers.add(name);
                    }
                }

                Collections.sort(matchedPlayers, String.CASE_INSENSITIVE_ORDER);
                return matchedPlayers;
            }
        }

        return ImmutableList.of();
    }
}
