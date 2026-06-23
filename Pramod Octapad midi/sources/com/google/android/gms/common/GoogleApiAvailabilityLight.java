package com.google.android.gms.common;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.gms.common.util.DeviceProperties;
import com.google.android.gms.common.wrappers.Wrappers;
import com.google.errorprone.annotations.ResultIgnorabilityUnspecified;

/* JADX INFO: compiled from: com.google.android.gms:play-services-basement@@18.3.0 */
/* JADX INFO: loaded from: classes.dex */
public class GoogleApiAvailabilityLight {
    public static final String GOOGLE_PLAY_SERVICES_PACKAGE = "com.google.android.gms";
    public static final String GOOGLE_PLAY_STORE_PACKAGE = "com.android.vending";
    static final String TRACKING_SOURCE_DIALOG = "d";
    static final String TRACKING_SOURCE_NOTIFICATION = "n";
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE = GooglePlayServicesUtilLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;
    private static final GoogleApiAvailabilityLight zza = new GoogleApiAvailabilityLight();

    GoogleApiAvailabilityLight() {
    }

    public static GoogleApiAvailabilityLight getInstance() {
        return zza;
    }

    public void cancelAvailabilityErrorNotifications(Context context) {
        GooglePlayServicesUtilLight.cancelAvailabilityErrorNotifications(context);
    }

    public int getApkVersion(Context context) {
        return GooglePlayServicesUtilLight.getApkVersion(context);
    }

    public int getClientVersion(Context context) {
        return GooglePlayServicesUtilLight.getClientVersion(context);
    }

    @Deprecated
    public Intent getErrorResolutionIntent(int errorCode) {
        return getErrorResolutionIntent(null, errorCode, null);
    }

    public PendingIntent getErrorResolutionPendingIntent(Context context, int errorCode, int requestCode) {
        return getErrorResolutionPendingIntent(context, errorCode, requestCode, null);
    }

    public String getErrorString(int errorCode) {
        return GooglePlayServicesUtilLight.getErrorString(errorCode);
    }

    @ResultIgnorabilityUnspecified
    public int isGooglePlayServicesAvailable(Context context) {
        return isGooglePlayServicesAvailable(context, GOOGLE_PLAY_SERVICES_VERSION_CODE);
    }

    public boolean isPlayServicesPossiblyUpdating(Context context, int errorCode) {
        return GooglePlayServicesUtilLight.isPlayServicesPossiblyUpdating(context, errorCode);
    }

    public boolean isPlayStorePossiblyUpdating(Context context, int errorCode) {
        return GooglePlayServicesUtilLight.isPlayStorePossiblyUpdating(context, errorCode);
    }

    public boolean isUninstalledAppPossiblyUpdating(Context context, String packageName) {
        return GooglePlayServicesUtilLight.zza(context, packageName);
    }

    public boolean isUserResolvableError(int errorCode) {
        return GooglePlayServicesUtilLight.isUserRecoverableError(errorCode);
    }

    public void verifyGooglePlayServicesIsAvailable(Context context, int minApkVersion) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        GooglePlayServicesUtilLight.ensurePlayServicesAvailable(context, minApkVersion);
    }

    public Intent getErrorResolutionIntent(Context context, int errorCode, String trackingSource) {
        switch (errorCode) {
            case 1:
            case 2:
                if (context != null && DeviceProperties.isWearableWithoutPlayStore(context)) {
                    Intent intent = new Intent("com.google.android.clockwork.home.UPDATE_ANDROID_WEAR_ACTION");
                    intent.setPackage("com.google.android.wearable.app");
                    return intent;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("gcore_");
                sb.append(GOOGLE_PLAY_SERVICES_VERSION_CODE);
                sb.append("-");
                if (!TextUtils.isEmpty(trackingSource)) {
                    sb.append(trackingSource);
                }
                sb.append("-");
                if (context != null) {
                    sb.append(context.getPackageName());
                }
                sb.append("-");
                if (context != null) {
                    try {
                        sb.append(Wrappers.packageManager(context).getPackageInfo(context.getPackageName(), 0).versionCode);
                        break;
                    } catch (PackageManager.NameNotFoundException e) {
                    }
                }
                String string = sb.toString();
                Intent intent2 = new Intent("android.intent.action.VIEW");
                Uri.Builder builderAppendQueryParameter = Uri.parse("market://details").buildUpon().appendQueryParameter("id", "com.google.android.gms");
                if (!TextUtils.isEmpty(string)) {
                    builderAppendQueryParameter.appendQueryParameter("pcampaignid", string);
                }
                intent2.setData(builderAppendQueryParameter.build());
                intent2.setPackage("com.android.vending");
                intent2.addFlags(524288);
                return intent2;
            case 3:
                Uri uriFromParts = Uri.fromParts("package", "com.google.android.gms", null);
                Intent intent3 = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent3.setData(uriFromParts);
                return intent3;
            default:
                return null;
        }
    }

    public PendingIntent getErrorResolutionPendingIntent(Context context, int errorCode, int requestCode, String trackingSource) {
        Intent errorResolutionIntent = getErrorResolutionIntent(context, errorCode, trackingSource);
        if (errorResolutionIntent == null) {
            return null;
        }
        return PendingIntent.getActivity(context, requestCode, errorResolutionIntent, com.google.android.gms.internal.common.zzd.zza | 134217728);
    }

    public int isGooglePlayServicesAvailable(Context context, int minApkVersion) {
        int minApkVersion2 = GooglePlayServicesUtilLight.isGooglePlayServicesAvailable(context, minApkVersion);
        if (GooglePlayServicesUtilLight.isPlayServicesPossiblyUpdating(context, minApkVersion2)) {
            return 18;
        }
        return minApkVersion2;
    }
}
