package com.google.android.gms.common.api.internal;

import android.os.SystemClock;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.common.internal.ConnectionTelemetryConfiguration;
import com.google.android.gms.common.internal.MethodInvocation;
import com.google.android.gms.common.internal.RootTelemetryConfigManager;
import com.google.android.gms.common.internal.RootTelemetryConfiguration;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/* JADX INFO: compiled from: com.google.android.gms:play-services-base@@18.1.0 */
/* JADX INFO: loaded from: classes.dex */
final class zacd implements OnCompleteListener {
    private final GoogleApiManager zaa;
    private final int zab;
    private final ApiKey zac;
    private final long zad;
    private final long zae;

    zacd(GoogleApiManager googleApiManager, int i, ApiKey apiKey, long j, long j2, String str, String str2) {
        this.zaa = googleApiManager;
        this.zab = i;
        this.zac = apiKey;
        this.zad = j;
        this.zae = j2;
    }

    static zacd zaa(GoogleApiManager googleApiManager, int i, ApiKey apiKey) {
        boolean methodTimingTelemetryEnabled;
        if (!googleApiManager.zaF()) {
            return null;
        }
        RootTelemetryConfiguration config = RootTelemetryConfigManager.getInstance().getConfig();
        if (config == null) {
            methodTimingTelemetryEnabled = true;
        } else {
            if (!config.getMethodInvocationTelemetryEnabled()) {
                return null;
            }
            methodTimingTelemetryEnabled = config.getMethodTimingTelemetryEnabled();
            zabq zabqVarZak = googleApiManager.zak(apiKey);
            if (zabqVarZak != null) {
                if (!(zabqVarZak.zaf() instanceof BaseGmsClient)) {
                    return null;
                }
                BaseGmsClient baseGmsClient = (BaseGmsClient) zabqVarZak.zaf();
                if (baseGmsClient.hasConnectionInfo() && !baseGmsClient.isConnecting()) {
                    ConnectionTelemetryConfiguration connectionTelemetryConfigurationZab = zab(zabqVarZak, baseGmsClient, i);
                    if (connectionTelemetryConfigurationZab == null) {
                        return null;
                    }
                    zabqVarZak.zaq();
                    methodTimingTelemetryEnabled = connectionTelemetryConfigurationZab.getMethodTimingTelemetryEnabled();
                }
            }
        }
        return new zacd(googleApiManager, i, apiKey, methodTimingTelemetryEnabled ? System.currentTimeMillis() : 0L, methodTimingTelemetryEnabled ? SystemClock.elapsedRealtime() : 0L, null, null);
    }

    private static ConnectionTelemetryConfiguration zab(zabq zabqVar, BaseGmsClient baseGmsClient, int i) {
        int[] methodInvocationMethodKeyAllowlist;
        int[] methodInvocationMethodKeyDisallowlist;
        ConnectionTelemetryConfiguration telemetryConfiguration = baseGmsClient.getTelemetryConfiguration();
        if (telemetryConfiguration == null || !telemetryConfiguration.getMethodInvocationTelemetryEnabled() || ((methodInvocationMethodKeyAllowlist = telemetryConfiguration.getMethodInvocationMethodKeyAllowlist()) != null ? !ArrayUtils.contains(methodInvocationMethodKeyAllowlist, i) : !((methodInvocationMethodKeyDisallowlist = telemetryConfiguration.getMethodInvocationMethodKeyDisallowlist()) == null || !ArrayUtils.contains(methodInvocationMethodKeyDisallowlist, i))) || zabqVar.zac() >= telemetryConfiguration.getMaxMethodInvocationsLogged()) {
            return null;
        }
        return telemetryConfiguration;
    }

    @Override // com.google.android.gms.tasks.OnCompleteListener
    public final void onComplete(Task task) {
        zabq zabqVarZak;
        int version;
        int i;
        int i2;
        int i3;
        int errorCode;
        long j;
        long jCurrentTimeMillis;
        int iElapsedRealtime;
        if (this.zaa.zaF()) {
            RootTelemetryConfiguration config = RootTelemetryConfigManager.getInstance().getConfig();
            if ((config == null || config.getMethodInvocationTelemetryEnabled()) && (zabqVarZak = this.zaa.zak(this.zac)) != null && (zabqVarZak.zaf() instanceof BaseGmsClient)) {
                BaseGmsClient baseGmsClient = (BaseGmsClient) zabqVarZak.zaf();
                boolean methodTimingTelemetryEnabled = this.zad > 0;
                int gCoreServiceId = baseGmsClient.getGCoreServiceId();
                if (config != null) {
                    methodTimingTelemetryEnabled &= config.getMethodTimingTelemetryEnabled();
                    int batchPeriodMillis = config.getBatchPeriodMillis();
                    int maxMethodInvocationsInBatch = config.getMaxMethodInvocationsInBatch();
                    version = config.getVersion();
                    if (baseGmsClient.hasConnectionInfo() && !baseGmsClient.isConnecting()) {
                        ConnectionTelemetryConfiguration connectionTelemetryConfigurationZab = zab(zabqVarZak, baseGmsClient, this.zab);
                        if (connectionTelemetryConfigurationZab == null) {
                            return;
                        }
                        boolean z = connectionTelemetryConfigurationZab.getMethodTimingTelemetryEnabled() && this.zad > 0;
                        maxMethodInvocationsInBatch = connectionTelemetryConfigurationZab.getMaxMethodInvocationsLogged();
                        methodTimingTelemetryEnabled = z;
                    }
                    i = batchPeriodMillis;
                    i2 = maxMethodInvocationsInBatch;
                } else {
                    version = 0;
                    i = 5000;
                    i2 = 100;
                }
                GoogleApiManager googleApiManager = this.zaa;
                if (task.isSuccessful()) {
                    i3 = 0;
                    errorCode = 0;
                } else if (task.isCanceled()) {
                    i3 = 100;
                    errorCode = -1;
                } else {
                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        Status status = ((ApiException) exception).getStatus();
                        int statusCode = status.getStatusCode();
                        ConnectionResult connectionResult = status.getConnectionResult();
                        errorCode = connectionResult == null ? -1 : connectionResult.getErrorCode();
                        i3 = statusCode;
                    } else {
                        i3 = 101;
                        errorCode = -1;
                    }
                }
                if (methodTimingTelemetryEnabled) {
                    long j2 = this.zad;
                    jCurrentTimeMillis = System.currentTimeMillis();
                    j = j2;
                    iElapsedRealtime = (int) (SystemClock.elapsedRealtime() - this.zae);
                } else {
                    j = 0;
                    jCurrentTimeMillis = 0;
                    iElapsedRealtime = -1;
                }
                googleApiManager.zay(new MethodInvocation(this.zab, i3, errorCode, j, jCurrentTimeMillis, null, null, gCoreServiceId, iElapsedRealtime), version, i, i2);
            }
        }
    }
}
