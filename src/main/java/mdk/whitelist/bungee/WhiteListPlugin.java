package mdk.whitelist.bungee;

import mdk.mutils.Anot;
import mdk.mutils.annotations.Config;
import mdk.mutils.config.SimpleConfig;
import mdk.mutils.lang.ILang;
import mdk.mutils.annotations.Lang;
import mdk.whitelist.storge.IData;
import mdk.whitelist.IL;
import mdk.whitelist.WhiteListConfig;
import net.md_5.bungee.api.plugin.Plugin;

public class WhiteListPlugin extends Plugin implements IL {
    @Config.Instance
    public static SimpleConfig<WhiteListConfig> config;
    @Lang
    public static ILang lang;
    public static IData list;
    @Override
    public void onEnable() {
        Anot.init(this);
        lang.load(this.getClass().getClassLoader().getResourceAsStream(String.format("%s.txt", config.getConfig().lang)));
        getProxy().getPluginManager().registerListener(this, new Event());
        this.getProxy().getPluginManager().registerCommand(this, new WhiteListCommand());
        try {
            list = (IData) Class.forName(config.getConfig().storage_type).getConstructor(IL.class, ILang.class).newInstance(this, lang);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
