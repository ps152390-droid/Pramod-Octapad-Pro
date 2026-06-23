package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: compiled from: com.google.android.gms:play-services-basement@@18.3.0 */
/* JADX INFO: loaded from: classes.dex */
public abstract class zzac extends com.google.android.gms.internal.common.zzb implements IGmsCallbacks {
    public zzac() {
        super("com.google.android.gms.common.internal.IGmsCallbacks");
    }

    @Override // com.google.android.gms.internal.common.zzb
    protected final boolean zza(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        switch (i) {
            case 1:
                int i3 = parcel.readInt();
                IBinder strongBinder = parcel.readStrongBinder();
                Bundle bundle = (Bundle) com.google.android.gms.internal.common.zzc.zza(parcel, Bundle.CREATOR);
                com.google.android.gms.internal.common.zzc.zzb(parcel);
                onPostInitComplete(i3, strongBinder, bundle);
                break;
            case 2:
                int i4 = parcel.readInt();
                Bundle bundle2 = (Bundle) com.google.android.gms.internal.common.zzc.zza(parcel, Bundle.CREATOR);
                com.google.android.gms.internal.common.zzc.zzb(parcel);
                zzb(i4, bundle2);
                break;
            case 3:
                int i5 = parcel.readInt();
                IBinder strongBinder2 = parcel.readStrongBinder();
                zzk zzkVar = (zzk) com.google.android.gms.internal.common.zzc.zza(parcel, zzk.CREATOR);
                com.google.android.gms.internal.common.zzc.zzb(parcel);
                zzc(i5, strongBinder2, zzkVar);
                break;
            default:
                return false;
        }
        parcel2.writeNoException();
        return true;
    }
}
