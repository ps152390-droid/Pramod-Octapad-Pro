package com.google.android.gms.common.api;

import com.google.android.gms.common.api.Result;

/* JADX INFO: compiled from: com.google.android.gms:play-services-base@@18.1.0 */
/* JADX INFO: loaded from: classes.dex */
public abstract class OptionalPendingResult<R extends Result> extends PendingResult<R> {
    public abstract R get();

    public abstract boolean isDone();
}
