package mdk.whitelist.bungee;

import mdk.mutils.config.Config;
import mdk.mutils.config.SimpleConfig;
import mdk.mutils.lang.ILang;
import mdk.mutils.lang.Lang;
import mdk.whitelist.AWhiteList;
import mdk.whitelist.IL;
import mdk.whitelist.WhiteListConfig;
import net.md_5.bungee.api.plugin.Plugin;

public class WhiteListPlugin extends Plugin implements IL {
    @Config.Instance
    public static SimpleConfig<WhiteListConfig> config;
    @Lang
    public static ILang lang;
    public static AWhiteList list;
    @Override
    public void onEnable() {
        lang.load(this.getClass().getClassLoader().getResourceAsStream(String.format("%s.txt", config.getConfig().lang)));
        getProxy().getPluginManager().registerListener(this, new Event());
        this.getProxy().getPluginManager().registerCommand(this, new WhiteListCommand());
        list = new AWhiteList(this);
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
