package mdk.mutils.api.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mdk.mutils.utils.lang.ILang;
import mdk.mutils.utils.lang.Lang;

public class Static {
    public static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create();

    public static final ILang lang = new Lang();
}
