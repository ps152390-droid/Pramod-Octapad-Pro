package com.google.android.gms.common;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.errorprone.annotations.CheckReturnValue;
import com.google.errorprone.annotations.RestrictedInheritance;

/* JADX INFO: compiled from: com.google.android.gms:play-services-basement@@18.3.0 */
/* JADX INFO: loaded from: classes.dex */
@CheckReturnValue
@RestrictedInheritance(allowedOnPath = ".*javatests.*/com/google/android/gms/common/.*", explanation = "Sub classing of GMS Core's APIs are restricted to testing fakes.", link = "go/gmscore-restrictedinheritance")
public class PackageSignatureVerifier {
    static volatile zzac zza;
    private static zzad zzb;

    private static zzad zza(Context context) {
        zzad zzadVar;
        synchronized (PackageSignatureVerifier.class) {
            if (zzb == null) {
                zzb = new zzad(context);
            }
            zzadVar = zzb;
        }
        return zzadVar;
    }

    public PackageVerificationResult queryPackageSignatureVerified(Context context, String callingPackage) {
        boolean zHonorsDebugCertificates = GooglePlayServicesUtilLight.honorsDebugCertificates(context);
        zza(context);
        if (!zzn.zzf()) {
            throw new zzae();
        }
        String strConcat = String.valueOf(callingPackage).concat(true != zHonorsDebugCertificates ? "-0" : "-1");
        if (zza != null && zza.zza.equals(strConcat)) {
            return zza.zzb;
        }
        zza(context);
        zzx zzxVarZzc = zzn.zzc(callingPackage, zHonorsDebugCertificates, false, false);
        if (zzxVarZzc.zza) {
            zza = new zzac(strConcat, PackageVerificationResult.zzd(callingPackage, zzxVarZzc.zzd));
            return zza.zzb;
        }
        Preconditions.checkNotNull(zzxVarZzc.zzb);
        return PackageVerificationResult.zza(callingPackage, zzxVarZzc.zzb, zzxVarZzc.zzc);
    }

    public PackageVerificationResult queryPackageSignatureVerifiedWithRetry(Context context, String callingPackage) {
        try {
            PackageVerificationResult packageVerificationResultQueryPackageSignatureVerified = queryPackageSignatureVerified(context, callingPackage);
            packageVerificationResultQueryPackageSignatureVerified.zzb();
            return packageVerificationResultQueryPackageSignatureVerified;
        } catch (SecurityException e) {
            PackageVerificationResult packageVerificationResultQueryPackageSignatureVerified2 = queryPackageSignatureVerified(context, callingPackage);
            if (!packageVerificationResultQueryPackageSignatureVerified2.zzc()) {
                return packageVerificationResultQueryPackageSignatureVerified2;
            }
            Log.e("PkgSignatureVerifier", "Got flaky result during package signature verification", e);
            return packageVerificationResultQueryPackageSignatureVerified2;
        }
    }
}
