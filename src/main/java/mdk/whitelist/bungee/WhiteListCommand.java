package mdk.whitelist.bungee;

import com.google.common.collect.ImmutableList;
import mdk.mutils.config.SimpleConfig;
import mdk.whitelist.WhiteListConfig;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.apache.commons.lang.Validate;

import java.util.*;

public class WhiteListCommand extends Command implements TabExecutor {
    private final String[] w1 = new String[] {
            "off",
            "on",
            "add",
            "remove",
            "list",
            "char"
    };

    private final String[] w2 = new String[] {
            "true",
            "false"
    };

    public WhiteListCommand() {
        super("whitelist", "minecraft.command.whitelist");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(args.length>0)) {
            sender.sendMessage(new TextComponent(String.format("/%s (%s)", getName(), String.join("/", w1))));
            return;
        }
        switch (args[0]) {
            case "off":

            {
                SimpleConfig<WhiteListConfig> config = WhiteListPlugin.config;
                config.getConfig().enable = false;
                sender.sendMessage(new TextComponent(WhiteListPlugin.lang.format("whitelist.set", false)));
                try {
                    config.Save();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case "on":
            {
                SimpleConfig<WhiteListConfig> config = WhiteListPlugin.config;
                config.getConfig().enable = true;
                sender.sendMessage(new TextComponent(WhiteListPlugin.lang.format("whitelist.set", true)));
                try {
                    config.Save();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case "add": {
                if (WhiteListPlugin.list.addUser(args[1])) {
                    sender.sendMessage(new TextComponent(WhiteListPlugin.lang.format("whitelist.add", args[1])));
                }
                else {
                    sender.sendMessage(new TextComponent(WhiteListPlugin.lang.get("whitelist.err")));
                }
                break;
            }
            case "remove": {
                if (WhiteListPlugin.list.removeUser(args[1])) {
                    sender.sendMessage(new TextComponent(WhiteListPlugin.lang.format("whitelist.remove", args[1])));
                }
                else {
                    sender.sendMessage(new TextComponent(WhiteListPlugin.lang.get("whitelist.err")));
                }
                break;
            }
            case "list": {
                sender.sendMessage(new TextComponent(WhiteListPlugin.list.toString()));
                break;
            }
            case "char": {
                WhiteListPlugin.config.getConfig().eneble_check_Name = Boolean.parseBoolean(args[1]);
                sender.sendMessage(new TextComponent("char set " + args[1]));
                break;
            }
        }
    }


    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");

        if (args.length == 0) {
            return ImmutableList.of();
        } else if (args.length == 1) {
            ArrayList<String> matchedPlayers = new ArrayList();
            Iterator var7 = Arrays.stream(w1).iterator();
            while(var7.hasNext()) {
                matchedPlayers.add((String)var7.next());
            }

            Collections.sort(matchedPlayers, String.CASE_INSENSITIVE_ORDER);
            return matchedPlayers;
        } else if (args[0].equalsIgnoreCase("char")) {
                String lastWord = args[args.length - 1];
                ArrayList<String> matchedPlayers = new ArrayList();
                Iterator var7 = Arrays.stream(w2).iterator();
                while(var7.hasNext()) {
                    matchedPlayers.add((String)var7.next());
                }

                Collections.sort(matchedPlayers, String.CASE_INSENSITIVE_ORDER);
                return matchedPlayers;
        }

        return ImmutableList.of();
    }
}
