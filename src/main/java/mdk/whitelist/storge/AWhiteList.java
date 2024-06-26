package mdk.whitelist.storge;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import mdk.mutils.Static;
import mdk.whitelist.IL;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class AWhiteList extends ArrayList<String> implements IData {
    @Override
    public void close() throws IOException {
        save();
    }

    @Override
    public boolean addUser(String name) {
        return add(name);
    }

    @Override
    public boolean is(String name) {
        return contains(name);
    }

    @Override
    public boolean removeUser(String name) {
        return remove(name);
    }

    public IL il;
    public AWhiteList(IL il) {
        this.il = il;
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
        if (contains(s)) return false;
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
        }
        else {
            return false;
        }
    }

    @Override
    public boolean addAll(Collection<? extends String> c) {
        if (super.addAll(c)) {
            save();
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (super.removeAll(c)) {
            save();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeIf(Predicate<? super String> filter) {
        if (super.removeIf(filter)) {
            save();
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void sort(Comparator<? super String> c) {
        super.sort(c);
        save();
    }

    @Override
    public void replaceAll(UnaryOperator<String> operator) {
        super.replaceAll(operator);
        save();
    }
}
