package com.vaibhavparashar.plexaframe.debug;

import com.vaibhavparashar.plexaframe.thread.PlexaFrameThreadPoolManager;
import com.vaibhavparashar.plexaframe.thread.RebuildScheduler;

public class DebugLogger {

    public static void log() {
        System.out.println(
                "[PlexaFrame Debug] Queue: " + RebuildScheduler.queued() +
                        " | Threads: " + PlexaFrameThreadPoolManager.getActiveThreads()
        );
    }
}
