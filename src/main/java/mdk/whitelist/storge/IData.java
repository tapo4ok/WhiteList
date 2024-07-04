package mdk.whitelist.storge;

import java.io.Closeable;
import java.util.List;

public interface IData<T> extends Closeable {
    boolean addUser(T name);
    boolean removeUser(T name);
    boolean is(T name);
    List<T> toList();

    boolean addUser(T name, ActionInfo info);
    boolean removeUser(T name, ActionInfo info);
    boolean is(T name, ActionInfo info);
}
