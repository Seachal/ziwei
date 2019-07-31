package com.laka.live.util;

/**
 * Created by luwies on 16/3/4.
 */
public class RuntimeCheck {
    public static final String LIVE_TOOL_PROCESSNAME = "tool";

    public static final String LIVE_REMOTE_PROCESSNAME = "remote";

    private static boolean sIsUiProcess = false;

    private static boolean sIsToolProcess = false;

    private static boolean sIsRemoteProcess = false;

    public static void init(String processName) {
        if (processName.contains(LIVE_TOOL_PROCESSNAME)) {
            sIsToolProcess = true;
        } else if (processName.contains(LIVE_REMOTE_PROCESSNAME)) {
            sIsRemoteProcess = true;
        } else {
            sIsUiProcess = true;
        }
    }

    public static boolean isUiProcess() {
        return sIsUiProcess;
    }

    public static boolean isToolProcess() {
        return sIsToolProcess;
    }

    public static boolean isRemoteProcess() {
        return sIsRemoteProcess;
    }

    public static void checkToolProcess() {
        if (sIsToolProcess == false) {
            throw new RuntimeException("Must run in tool process");
        }
    }

    public static void setToolProcess() {
        sIsToolProcess = true;
        sIsRemoteProcess = false;
        sIsUiProcess = false;

    }
}
