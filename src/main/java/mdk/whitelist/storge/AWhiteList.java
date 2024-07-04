package mdk.whitelist.storge;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import mdk.mutils.Static;
import mdk.mutils.lang.ILang;
import mdk.whitelist.IL;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AWhiteList extends ArrayList<String> implements IData<String> {
    @Override
    public void close() throws IOException {
        save();
    }

    @Override
    public boolean addUser(String name) {
        if (is(name)) {
            return false;
        }
        return add(name);
    }

    @Override
    public boolean is(String name) {
        return contains(name);
    }

    @Override
    public boolean removeUser(String name) {
        if (!is(name)) {
            return false;
        }
        return remove(name);
    }

    public IL il;
    public ILang lang;
    public AWhiteList(IL il, ILang lang) {
        this.il = il;
        this.lang = lang;
        if (!(size()>0)) {
            load();
        }
    }


    public void load() {
        clear();
        try {
            JsonArray array = Static.GSON.fromJson(new InputStreamReader(new FileInputStream(il.getConfig0().getConfig().file+".json")), JsonArray.class);
            Iterator<JsonElement> iterator = array.iterator();
            while (iterator.hasNext()) {
                String element = iterator.next().getAsString();

                add(element);
            }
        } catch (Exception e) {
            e.printStackTrace();
            save();
        }
    }

    public void save() {
        try {
            OutputStream stream = new FileOutputStream(il.getConfig0().getConfig().file+".json");

            stream.write(Static.GSON.toJson(this).getBytes(StandardCharsets.UTF_8));
            stream.flush();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean add(String s) {
        if (super.add(s)) {
            save();
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (super.remove(o)) {
            save();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void sort(Comparator<? super String> c) {
        super.sort(c);
        save();
    }

    @Override
    public List<String> toList() {
        return this;
    }

    @Override
    public boolean is(String name, ActionInfo info) {
        return contains(name);
    }

    @Override
    public boolean removeUser(String name, ActionInfo info) {
        if (!is(name)) {
            info.addStackTrans("data.error.remove", ActionInfo.ERROR, lang, name);
            info.cancel = true;
            return false;
        }
        return remove(name);
    }

    @Override
    public boolean addUser(String name, ActionInfo info) {
        if (is(name)) {
            info.addStackTrans("data.error.add", ActionInfo.ERROR, lang, name);
            info.cancel = true;
            return false;
        }
        return add(name);
    }
}
