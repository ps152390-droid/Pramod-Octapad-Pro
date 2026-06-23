package com.google.android.gms.internal.common;

import android.os.Build;

/* JADX INFO: compiled from: com.google.android.gms:play-services-basement@@18.3.0 */
/* JADX INFO: loaded from: classes.dex */
public final class zzd {
    public static final int zza;

    static {
        zza = Build.VERSION.SDK_INT >= 23 ? 67108864 : 0;
    }
}
