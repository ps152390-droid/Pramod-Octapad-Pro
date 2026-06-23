package com.google.android.gms.common.internal;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;

/* JADX INFO: compiled from: com.google.android.gms:play-services-basement@@18.3.0 */
/* JADX INFO: loaded from: classes.dex */
public final class Preconditions {
    private Preconditions() {
        throw new AssertionError("Uninstantiable");
    }

    public static void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    public static double checkArgumentInRange(double value, double lower, double upper, String valueName) {
        if (value < lower) {
            throw new IllegalArgumentException(zza("%s is out of range of [%f, %f] (too low)", valueName, Double.valueOf(lower), Double.valueOf(upper)));
        }
        if (value <= upper) {
            return value;
        }
        throw new IllegalArgumentException(zza("%s is out of range of [%f, %f] (too high)", valueName, Double.valueOf(lower), Double.valueOf(upper)));
    }

    public static void checkHandlerThread(Handler handler) {
        Looper looperMyLooper = Looper.myLooper();
        if (looperMyLooper != handler.getLooper()) {
            String name = looperMyLooper != null ? looperMyLooper.getThread().getName() : "null current looper";
            throw new IllegalStateException("Must be called on " + handler.getLooper().getThread().getName() + " thread, but got " + name + ".");
        }
    }

    public static void checkMainThread() {
        checkMainThread("Must be called on the main application thread");
    }

    @EnsuresNonNull({"#1"})
    public static String checkNotEmpty(String string) {
        if (TextUtils.isEmpty(string)) {
            throw new IllegalArgumentException("Given String is empty or null");
        }
        return string;
    }

    public static void checkNotGoogleApiHandlerThread() {
        checkNotGoogleApiHandlerThread("Must not be called on GoogleApiHandler thread.");
    }

    public static void checkNotMainThread() {
        checkNotMainThread("Must not be called on the main application thread");
    }

    @EnsuresNonNull({"#1"})
    public static <T> T checkNotNull(T t) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException("null reference");
    }

    public static int checkNotZero(int value) {
        if (value != 0) {
            return value;
        }
        throw new IllegalArgumentException("Given Integer is zero");
    }

    public static void checkState(boolean expression) {
        if (!expression) {
            throw new IllegalStateException();
        }
    }

    static String zza(String str, Object... objArr) {
        int iIndexOf;
        StringBuilder sb = new StringBuilder(str.length() + 48);
        int i = 0;
        int i2 = 0;
        while (i < 3 && (iIndexOf = str.indexOf("%s", i2)) != -1) {
            sb.append(str.substring(i2, iIndexOf));
            sb.append(objArr[i]);
            i2 = iIndexOf + 2;
            i++;
        }
        sb.append(str.substring(i2));
        if (i < 3) {
            sb.append(" [");
            sb.append(objArr[i]);
            for (int i3 = i + 1; i3 < 3; i3++) {
                sb.append(", ");
                sb.append(objArr[i3]);
            }
            sb.append("]");
        }
        return sb.toString();
    }

    public static void checkArgument(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }

    public static void checkMainThread(String errorMessage) {
        if (!com.google.android.gms.common.util.zzb.zza()) {
            throw new IllegalStateException(errorMessage);
        }
    }

    public static void checkNotGoogleApiHandlerThread(String errorMessage) {
        Looper looperMyLooper = Looper.myLooper();
        if (looperMyLooper != null) {
            String name = looperMyLooper.getThread().getName();
            if (name == "GoogleApiHandler" || (name != null && name.equals("GoogleApiHandler"))) {
                throw new IllegalStateException(errorMessage);
            }
        }
    }

    public static void checkNotMainThread(String errorMessage) {
        if (com.google.android.gms.common.util.zzb.zza()) {
            throw new IllegalStateException(errorMessage);
        }
    }

    @EnsuresNonNull({"#1"})
    public static <T> T checkNotNull(T t, Object errorMessage) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException(String.valueOf(errorMessage));
    }

    public static int checkNotZero(int value, Object errorMessage) {
        if (value != 0) {
            return value;
        }
        throw new IllegalArgumentException(String.valueOf(errorMessage));
    }

    public static void checkState(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
    }

    public static void checkArgument(boolean expression, String errorMessage, Object... errorMessageArgs) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(errorMessage, errorMessageArgs));
        }
    }

    public static float checkArgumentInRange(float value, float lower, float upper, String valueName) {
        if (value < lower) {
            throw new IllegalArgumentException(zza("%s is out of range of [%f, %f] (too low)", valueName, Float.valueOf(lower), Float.valueOf(upper)));
        }
        if (value > upper) {
            throw new IllegalArgumentException(zza("%s is out of range of [%f, %f] (too high)", valueName, Float.valueOf(lower), Float.valueOf(upper)));
        }
        return value;
    }

    @EnsuresNonNull({"#1"})
    public static String checkNotEmpty(String string, Object errorMessage) {
        if (TextUtils.isEmpty(string)) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
        return string;
    }

    public static long checkNotZero(long value) {
        if (value != 0) {
            return value;
        }
        throw new IllegalArgumentException("Given Long is zero");
    }

    public static void checkState(boolean expression, String errorMessage, Object... errorMessageArgs) {
        if (!expression) {
            throw new IllegalStateException(String.format(errorMessage, errorMessageArgs));
        }
    }

    public static long checkNotZero(long value, Object errorMessage) {
        if (value != 0) {
            return value;
        }
        throw new IllegalArgumentException(String.valueOf(errorMessage));
    }

    public static int checkArgumentInRange(int value, int lower, int upper, String valueName) {
        if (value < lower) {
            throw new IllegalArgumentException(zza("%s is out of range of [%d, %d] (too low)", valueName, Integer.valueOf(lower), Integer.valueOf(upper)));
        }
        if (value > upper) {
            throw new IllegalArgumentException(zza("%s is out of range of [%d, %d] (too high)", valueName, Integer.valueOf(lower), Integer.valueOf(upper)));
        }
        return value;
    }

    public static void checkHandlerThread(Handler handler, String errorMessage) {
        if (Looper.myLooper() == handler.getLooper()) {
        } else {
            throw new IllegalStateException(errorMessage);
        }
    }

    public static long checkArgumentInRange(long value, long lower, long upper, String valueName) {
        if (value < lower) {
            throw new IllegalArgumentException(zza("%s is out of range of [%d, %d] (too low)", valueName, Long.valueOf(lower), Long.valueOf(upper)));
        }
        if (value > upper) {
            throw new IllegalArgumentException(zza("%s is out of range of [%d, %d] (too high)", valueName, Long.valueOf(lower), Long.valueOf(upper)));
        }
        return value;
    }
}
