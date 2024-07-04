package mdk.whitelist;

import mdk.mutils.Anot;
import mdk.mutils.annotations.Config;
import mdk.mutils.config.SimpleConfig;
import mdk.mutils.lang.ILang;
import mdk.mutils.annotations.Lang;
import mdk.whitelist.storge.IData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public final class WhiteList extends JavaPlugin implements IL {
    @Config.Instance("mdk.whitelist.WhiteListConfig")
    public static SimpleConfig<WhiteListConfig> config;
    @Lang
    public static ILang lang;
    public static IData list;

    @Override
    public void onEnable() {
        Anot.init(this);
        lang.load(this.getClassLoader().getResourceAsStream(String.format("%s.txt", config.getConfig().lang)));
        try {
            list = (IData) Class.forName(config.getConfig().storage_type).getConstructor(IL.class, ILang.class).newInstance(this, lang);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            list.close();
            config.Save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public SimpleConfig<WhiteListConfig> getConfig0() {
        return config;
    }
}
