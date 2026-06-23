package com.google.android.gms.common.internal;

import android.content.Context;

/* JADX INFO: compiled from: com.google.android.gms:play-services-base@@18.1.0 */
/* JADX INFO: loaded from: classes.dex */
public class TelemetryLogging {
    private TelemetryLogging() {
    }

    public static TelemetryLoggingClient getClient(Context context) {
        return getClient(context, TelemetryLoggingOptions.zaa);
    }

    public static TelemetryLoggingClient getClient(Context context, TelemetryLoggingOptions options) {
        return new com.google.android.gms.common.internal.service.zao(context, options);
    }
}
