package mdk.whitelist.storge;

import java.io.Closeable;

public interface IData extends Closeable {
    boolean addUser(String name);
    boolean removeUser(String name);
    boolean is(String name);
}
