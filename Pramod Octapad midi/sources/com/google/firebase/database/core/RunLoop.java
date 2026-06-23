package com.google.firebase.database.core;

import java.util.concurrent.ScheduledFuture;

/* JADX INFO: loaded from: classes.dex */
public interface RunLoop {
    void restart();

    ScheduledFuture schedule(Runnable runnable, long j);

    void scheduleNow(Runnable runnable);

    void shutdown();
}
