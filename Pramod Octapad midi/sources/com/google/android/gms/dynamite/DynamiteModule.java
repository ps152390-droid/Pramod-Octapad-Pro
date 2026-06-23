package com.google.android.gms.dynamite;

import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.os.IBinder;
import android.os.IInterface;
import android.util.Log;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.errorprone.annotations.ResultIgnorabilityUnspecified;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/* JADX INFO: compiled from: com.google.android.gms:play-services-basement@@18.3.0 */
/* JADX INFO: loaded from: classes.dex */
public final class DynamiteModule {
    public static final int LOCAL = -1;
    public static final int NONE = 0;
    public static final int NO_SELECTION = 0;
    public static final int REMOTE = 1;
    private static Boolean zzb;
    private static String zzc;
    private static boolean zzd;
    private static zzq zzk;
    private static zzr zzl;
    private final Context zzj;
    private static int zze = -1;
    private static Boolean zzf = null;
    private static final ThreadLocal zzg = new ThreadLocal();
    private static final ThreadLocal zzh = new zzd();
    private static final VersionPolicy.IVersions zzi = new zze();
    public static final VersionPolicy PREFER_REMOTE = new zzf();
    public static final VersionPolicy PREFER_LOCAL = new zzg();
    public static final VersionPolicy PREFER_REMOTE_VERSION_NO_FORCE_STAGING = new zzh();
    public static final VersionPolicy PREFER_HIGHEST_OR_LOCAL_VERSION = new zzi();
    public static final VersionPolicy PREFER_HIGHEST_OR_LOCAL_VERSION_NO_FORCE_STAGING = new zzj();
    public static final VersionPolicy PREFER_HIGHEST_OR_REMOTE_VERSION = new zzk();
    public static final VersionPolicy zza = new zzl();

    /* JADX INFO: compiled from: com.google.android.gms:play-services-basement@@18.3.0 */
    public static class DynamiteLoaderClassLoader {
        public static ClassLoader sClassLoader;
    }

    /* JADX INFO: compiled from: com.google.android.gms:play-services-basement@@18.3.0 */
    public static class LoadingException extends Exception {
        /* synthetic */ LoadingException(String str, zzp zzpVar) {
            super(str);
        }

        /* synthetic */ LoadingException(String str, Throwable th, zzp zzpVar) {
            super(str, th);
        }
    }

    /* JADX INFO: compiled from: com.google.android.gms:play-services-basement@@18.3.0 */
    public interface VersionPolicy {

        /* JADX INFO: compiled from: com.google.android.gms:play-services-basement@@18.3.0 */
        public interface IVersions {
            int zza(Context context, String str);

            int zzb(Context context, String str, boolean z) throws LoadingException;
        }

        /* JADX INFO: compiled from: com.google.android.gms:play-services-basement@@18.3.0 */
        public static class SelectionResult {
            public int localVersion = 0;
            public int remoteVersion = 0;
            public int selection = 0;
        }

        SelectionResult selectModule(Context context, String str, IVersions iVersions) throws LoadingException;
    }

    private DynamiteModule(Context context) {
        Preconditions.checkNotNull(context);
        this.zzj = context;
    }

    public static int getLocalVersion(Context context, String moduleId) {
        try {
            Class<?> clsLoadClass = context.getApplicationContext().getClassLoader().loadClass("com.google.android.gms.dynamite.descriptors." + moduleId + ".ModuleDescriptor");
            Field declaredField = clsLoadClass.getDeclaredField("MODULE_ID");
            Field declaredField2 = clsLoadClass.getDeclaredField("MODULE_VERSION");
            if (Objects.equal(declaredField.get(null), moduleId)) {
                return declaredField2.getInt(null);
            }
            Log.e("DynamiteModule", "Module descriptor id '" + String.valueOf(declaredField.get(null)) + "' didn't match expected id '" + moduleId + "'");
            return 0;
        } catch (ClassNotFoundException e) {
            Log.w("DynamiteModule", "Local module descriptor class for " + moduleId + " not found.");
            return 0;
        } catch (Exception e2) {
            Log.e("DynamiteModule", "Failed to load module descriptor class: ".concat(String.valueOf(e2.getMessage())));
            return 0;
        }
    }

    public static int getRemoteVersion(Context context, String moduleId) {
        return zza(context, moduleId, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:108:0x023c A[DONT_GENERATE] */
    /* JADX WARN: Removed duplicated region for block: B:109:0x0242 A[DONT_GENERATE] */
    /* JADX WARN: Removed duplicated region for block: B:112:0x024f A[DONT_GENERATE] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0081 A[Catch: all -> 0x02aa, TRY_LEAVE, TryCatch #3 {all -> 0x02aa, blocks: (B:5:0x0029, B:9:0x0073, B:14:0x007b, B:17:0x0081, B:20:0x0089, B:93:0x01ec, B:94:0x01f7, B:96:0x01f9, B:98:0x01fb, B:99:0x0203, B:117:0x0261, B:118:0x0278, B:101:0x0205, B:103:0x0223, B:105:0x0232, B:115:0x0258, B:116:0x0260, B:119:0x0279, B:120:0x02a9), top: B:138:0x0029, inners: #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0087  */
    @com.google.errorprone.annotations.ResultIgnorabilityUnspecified
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.google.android.gms.dynamite.DynamiteModule load(android.content.Context r17, com.google.android.gms.dynamite.DynamiteModule.VersionPolicy r18, java.lang.String r19) throws com.google.android.gms.dynamite.DynamiteModule.LoadingException {
        /*
            Method dump skipped, instruction units count: 726
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.dynamite.DynamiteModule.load(android.content.Context, com.google.android.gms.dynamite.DynamiteModule$VersionPolicy, java.lang.String):com.google.android.gms.dynamite.DynamiteModule");
    }

    /* JADX WARN: Removed duplicated region for block: B:121:0x01c7 A[Catch: all -> 0x01ce, TryCatch #7 {all -> 0x01ce, blocks: (B:3:0x0002, B:64:0x00e0, B:66:0x00e6, B:71:0x0107, B:93:0x0160, B:97:0x016f, B:121:0x01c7, B:122:0x01ca, B:114:0x01bd, B:69:0x00ec, B:125:0x01cd, B:4:0x0003, B:7:0x0009, B:8:0x0025, B:62:0x00dd, B:21:0x0048, B:43:0x009d, B:46:0x00a0, B:55:0x00bb, B:63:0x00df, B:61:0x00c1), top: B:136:0x0002, inners: #6, #8 }] */
    /* JADX WARN: Removed duplicated region for block: B:146:? A[Catch: all -> 0x01ce, SYNTHETIC, TRY_LEAVE, TryCatch #7 {all -> 0x01ce, blocks: (B:3:0x0002, B:64:0x00e0, B:66:0x00e6, B:71:0x0107, B:93:0x0160, B:97:0x016f, B:121:0x01c7, B:122:0x01ca, B:114:0x01bd, B:69:0x00ec, B:125:0x01cd, B:4:0x0003, B:7:0x0009, B:8:0x0025, B:62:0x00dd, B:21:0x0048, B:43:0x009d, B:46:0x00a0, B:55:0x00bb, B:63:0x00df, B:61:0x00c1), top: B:136:0x0002, inners: #6, #8 }] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:115:0x01c0 -> B:140:0x01c2). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int zza(android.content.Context r10, java.lang.String r11, boolean r12) {
        /*
            Method dump skipped, instruction units count: 467
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.dynamite.DynamiteModule.zza(android.content.Context, java.lang.String, boolean):int");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:64:0x00e8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static int zzb(android.content.Context r8, java.lang.String r9, boolean r10, boolean r11) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 236
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.dynamite.DynamiteModule.zzb(android.content.Context, java.lang.String, boolean, boolean):int");
    }

    private static DynamiteModule zzc(Context context, String str) {
        Log.i("DynamiteModule", "Selected local version of ".concat(String.valueOf(str)));
        return new DynamiteModule(context);
    }

    private static void zzd(ClassLoader classLoader) throws LoadingException {
        zzr zzrVar;
        zzp zzpVar = null;
        try {
            IBinder iBinder = (IBinder) classLoader.loadClass("com.google.android.gms.dynamiteloader.DynamiteLoaderV2").getConstructor(new Class[0]).newInstance(new Object[0]);
            if (iBinder == null) {
                zzrVar = null;
            } else {
                IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.dynamite.IDynamiteLoaderV2");
                zzrVar = iInterfaceQueryLocalInterface instanceof zzr ? (zzr) iInterfaceQueryLocalInterface : new zzr(iBinder);
            }
            zzl = zzrVar;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new LoadingException("Failed to instantiate dynamite loader", e, zzpVar);
        }
    }

    private static boolean zze(Cursor cursor) {
        zzn zznVar = (zzn) zzg.get();
        if (zznVar == null || zznVar.zza != null) {
            return false;
        }
        zznVar.zza = cursor;
        return true;
    }

    private static boolean zzf(Context context) {
        if (Boolean.TRUE.equals(null) || Boolean.TRUE.equals(zzf)) {
            return true;
        }
        boolean zBooleanValue = false;
        if (zzf == null) {
            ProviderInfo providerInfoResolveContentProvider = context.getPackageManager().resolveContentProvider("com.google.android.gms.chimera", 0);
            if (GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(context, 10000000) == 0 && providerInfoResolveContentProvider != null && "com.google.android.gms".equals(providerInfoResolveContentProvider.packageName)) {
                zBooleanValue = true;
            }
            Boolean boolValueOf = Boolean.valueOf(zBooleanValue);
            zzf = boolValueOf;
            zBooleanValue = boolValueOf.booleanValue();
            if (zBooleanValue && providerInfoResolveContentProvider.applicationInfo != null && (providerInfoResolveContentProvider.applicationInfo.flags & 129) == 0) {
                Log.i("DynamiteModule", "Non-system-image GmsCore APK, forcing V1");
                zzd = true;
            }
        }
        if (!zBooleanValue) {
            Log.e("DynamiteModule", "Invalid GmsCore APK, remote loading disabled.");
        }
        return zBooleanValue;
    }

    private static zzq zzg(Context context) {
        zzq zzqVar;
        synchronized (DynamiteModule.class) {
            zzq zzqVar2 = zzk;
            if (zzqVar2 != null) {
                return zzqVar2;
            }
            try {
                IBinder iBinder = (IBinder) context.createPackageContext("com.google.android.gms", 3).getClassLoader().loadClass("com.google.android.gms.chimera.container.DynamiteLoaderImpl").newInstance();
                if (iBinder == null) {
                    zzqVar = null;
                } else {
                    IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.dynamite.IDynamiteLoader");
                    zzqVar = iInterfaceQueryLocalInterface instanceof zzq ? (zzq) iInterfaceQueryLocalInterface : new zzq(iBinder);
                }
                if (zzqVar != null) {
                    zzk = zzqVar;
                    return zzqVar;
                }
            } catch (Exception e) {
                Log.e("DynamiteModule", "Failed to load IDynamiteLoader from GmsCore: " + e.getMessage());
            }
            return null;
        }
    }

    @ResultIgnorabilityUnspecified
    public Context getModuleContext() {
        return this.zzj;
    }

    public IBinder instantiate(String className) throws LoadingException {
        try {
            return (IBinder) this.zzj.getClassLoader().loadClass(className).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new LoadingException("Failed to instantiate module class: ".concat(String.valueOf(className)), e, null);
        }
    }
}
