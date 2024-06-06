package mdk.whitelist;

import com.google.gson.annotations.SerializedName;
import mdk.mutils.config.Config;

@Config
@Config.Package
public class WhiteListConfig {
    @SerializedName("enable")
    public boolean enable = false;
    @SerializedName("check.Name")
    public boolean eneble_check_Name = false;
    @SerializedName("char")
    public String char_set = "qwertyuiopasdfghjklzxcvbnm1234567890_QWERTYUIOPSADFGHJKLZXCVBNM";
    @SerializedName("file")
    public String file = "whitelist_reload";
    @SerializedName("lang")
    public String lang = "en";
    public String storage_type = "mdk.whitelist.storge.AWhiteList";
}
