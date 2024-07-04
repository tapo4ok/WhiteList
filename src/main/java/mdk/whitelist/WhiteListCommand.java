package mdk.whitelist;

import com.google.common.collect.ImmutableList;
import mdk.mutils.config.SimpleConfig;
import mdk.whitelist.storge.ActionInfo;
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
            "list",
            "char",
            "help"
    };

    private final String[] w2 = new String[] {
            "true",
            "false"
    };
    public WhiteListCommand(IL l) {
        super("rwhitelist");
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
                sender.sendMessage(WhiteList.lang.format("whitelist.set", false));
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
                sender.sendMessage(WhiteList.lang.format("whitelist.set", true));
                try {
                    config.Save();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case "add": {
                ActionInfo info = new ActionInfo();
                if (WhiteList.list.addUser(args[1], info)) {
                    sender.sendMessage(WhiteList.lang.format("whitelist.add", args[1]));
                }
                else {
                    for (StackTraceElement element : info.elements) {
                        sender.sendMessage(String.format("%s:%s", info.getLevel(element), element.getMethodName()));
                    }
                }
                break;
            }
            case "remove": {
                ActionInfo info = new ActionInfo();
                if (WhiteList.list.removeUser(args[1], info)) {
                    sender.sendMessage(WhiteList.lang.format("whitelist.remove", args[1]));
                }
                else {
                    for (StackTraceElement element : info.elements) {
                        sender.sendMessage(String.format("%s:%s", info.getLevel(element), element.getMethodName()));
                    }
                }
                break;
            }
            case "list": {
                sender.sendMessage(WhiteList.list.toList().toString());
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
            case "help": {
                sender.sendMessage(new String[]{
                        String.format("1: /%s add <user_name>; remove user in whitelist", getName()),
                        String.format("2: /%s remove <user_name>; add user to whitelist", getName()),
                        String.format("3: /%s list; get player list in whitelist", getName()),
                        String.format("4: /%s on; enable whitelist", getName()),
                        String.format("5: /%s off; disable whitelist", getName()),
                });
                break;
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
