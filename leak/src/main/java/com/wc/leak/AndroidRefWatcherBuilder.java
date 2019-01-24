package com.wc.leak;

import android.content.Context;
import android.support.annotation.NonNull;

import com.wc.leak.watcher.RefWatcherBuilder;

/** A {@link RefWatcherBuilder} with appropriate Android defaults(适当的Android默认值). */
public class AndroidRefWatcherBuilder extends RefWatcherBuilder<AndroidRefWatcherBuilder> {

    private final Context context;

    AndroidRefWatcherBuilder(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * Sets a custom {@link AbstractAnalysisResultService} to listen to analysis results. This
     * overrides any call to {@link #heapDumpListener(HeapDump.Listener)}.
     */
    public @NonNull AndroidRefWatcherBuilder listenerServiceClass(
            @NonNull Class<? extends AbstractAnalysisResultService> listenerServiceClass) {
        enableDisplayLeakActivity = DisplayLeakService.class.isAssignableFrom(listenerServiceClass);
        return heapDumpListener(new ServiceHeapDumpListener(context, listenerServiceClass));
    }
}
