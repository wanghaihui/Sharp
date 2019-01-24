package com.wc.leak.watcher;

/**
 * Responsible for building {@link RefWatcher} instances. Subclasses should provide sane(合理的) defaults(默认值)
 * for the platform they support.
 */
public class RefWatcherBuilder<T extends RefWatcherBuilder<T>> {


    @SuppressWarnings("unchecked")
    protected final T self() {
        return (T) this;
    }
}
