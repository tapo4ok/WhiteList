package mdk.whitelist;

import mdk.mutils.api.config.Config;
import mdk.mutils.api.config.SimpleConfig;
import mdk.mutils.api.config.Static;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public final class WhiteList extends JavaPlugin implements IL {
    public static SimpleConfig<WhiteListConfig> config;
    public static AWhiteList list;

    @Override
    public void onEnable() {
        config = new SimpleConfig<>(WhiteListConfig.class, Config.Type.JSON, getLogger(), getDataFolder());
        Static.lang.load(this.getClassLoader().getResourceAsStream(String.format("%s.txt", config.getConfig().lang)));

        list = new AWhiteList(this);
        PluginManager pm = Bukkit.getServer().getPluginManager();

        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap map = (CommandMap)commandMapField.get(Bukkit.getServer());


            Command command = new WhiteListCommand(this);

            Permission permission = new Permission("rwhitelist");
            permission.setDefault(Permission.DEFAULT_PERMISSION);

            command.setPermission(permission.getName());
            pm.addPermission(permission);

            map.register("minecraft", command);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        pm.registerEvents(new Event(), this);
    }

    @Override
    public void onDisable() {
        try {
            config.Save();
        } catch (Exception e) {
            e.printStackTrace();
        }
        list.save();
    }

    @Override
    public SimpleConfig<WhiteListConfig> getConfig0() {
        return config;
    }
}
