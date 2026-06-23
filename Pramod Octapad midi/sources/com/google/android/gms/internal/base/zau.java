package com.google.android.gms.internal.base;

import android.os.Handler;
import android.os.Looper;

/* JADX INFO: compiled from: com.google.android.gms:play-services-base@@18.1.0 */
/* JADX INFO: loaded from: classes.dex */
public class zau extends Handler {
    public zau() {
    }

    public zau(Looper looper) {
        super(looper);
    }

    public zau(Looper looper, Handler.Callback callback) {
        super(looper, callback);
    }
}
