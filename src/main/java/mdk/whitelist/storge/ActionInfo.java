package mdk.whitelist.storge;

import mdk.mutils.lang.ILang;

import java.util.Stack;
import java.util.logging.Level;

public class ActionInfo {
    public static final int INFO = 0;
    public static final int WARN = 1;
    public static final int ERROR = 2;
    public boolean cancel = false;
    public Stack<StackTraceElement> elements = new Stack<>();
    public void addStack(String method, int level) {
        elements.push(new StackTraceElement("", method, "", level));
    }

    public void addStackTrans(String method, int level, ILang lang,  Object... data) {
        elements.push(new StackTraceElement("", lang.format(method, data), "", level));
    }

    public Level getLevel(StackTraceElement element) {
        switch (element.getLineNumber()) {
            case INFO:
                return Level.INFO;
            case WARN:
                return Level.WARNING;
            case ERROR:
                return Level.SEVERE;
            default:
                return Level.ALL;
        }
    }
}
