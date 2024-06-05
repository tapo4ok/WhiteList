package mdk.whitelist;

import com.google.gson.annotations.SerializedName;
import mdk.mutils.api.config.Config;

@Config
@Config.Package
public class WhiteListConfig {
    @SerializedName("enable")
    public boolean enable = false;
    @SerializedName("check.Name")
    public boolean eneble_check_Name = false;
    @SerializedName("replace.vanila")
    public boolean replace_vanila = false;
    @SerializedName("char")
    public String char_set = "qwertyuiopasdfghjklzxcvbnm1234567890_QWERTYUIOPSADFGHJKLZXCVBNM";
    @SerializedName("char_not")
    public String char_not_aloved_msg = "Username not allowed";
    @SerializedName("file")
    public String file = "whitelist_reborn";
    @SerializedName("no.whitelist.msg")
    public String no_whitelist_msg = "You're not that awesome to be whitelisted.";
}
