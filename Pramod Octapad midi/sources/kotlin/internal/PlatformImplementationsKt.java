package kotlin.internal;

import androidx.constraintlayout.widget.ConstraintLayout;
import kotlin.KotlinVersion;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: PlatformImplementations.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0004\u001a \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u0005H\u0001\u001a\"\u0010\b\u001a\u0002H\t\"\n\b\u0000\u0010\t\u0018\u0001*\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0083\b¢\u0006\u0002\u0010\f\u001a\b\u0010\r\u001a\u00020\u0005H\u0002\"\u0010\u0010\u0000\u001a\u00020\u00018\u0000X\u0081\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"IMPLEMENTATIONS", "Lkotlin/internal/PlatformImplementations;", "apiVersionIsAtLeast", "", "major", "", "minor", "patch", "castToBaseType", "T", "", "instance", "(Ljava/lang/Object;)Ljava/lang/Object;", "getJavaVersion", "kotlin-stdlib"}, k = 2, mv = {1, 7, 1}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
public final class PlatformImplementationsKt {
    public static final PlatformImplementations IMPLEMENTATIONS;

    /* JADX WARN: Removed duplicated region for block: B:38:0x00d3 A[Catch: ClassCastException -> 0x00d7, ClassNotFoundException -> 0x0115, TRY_ENTER, TryCatch #2 {ClassCastException -> 0x00d7, blocks: (B:38:0x00d3, B:41:0x00d9, B:42:0x00de), top: B:67:0x00d1, outer: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00d9 A[Catch: ClassCastException -> 0x00d7, ClassNotFoundException -> 0x0115, TryCatch #2 {ClassCastException -> 0x00d7, blocks: (B:38:0x00d3, B:41:0x00d9, B:42:0x00de), top: B:67:0x00d1, outer: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x00c4 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    static {
        /*
            Method dump skipped, instruction units count: 368
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.internal.PlatformImplementationsKt.<clinit>():void");
    }

    private static final /* synthetic */ <T> T castToBaseType(Object obj) throws ClassNotFoundException {
        try {
            Intrinsics.reifiedOperationMarker(1, "T");
            return (T) obj;
        } catch (ClassCastException e) {
            ClassLoader classLoader = obj.getClass().getClassLoader();
            Intrinsics.reifiedOperationMarker(4, "T");
            ClassLoader classLoader2 = Object.class.getClassLoader();
            if (!Intrinsics.areEqual(classLoader, classLoader2)) {
                throw new ClassNotFoundException("Instance class was loaded from a different classloader: " + classLoader + ", base type classloader: " + classLoader2, e);
            }
            throw e;
        }
    }

    private static final int getJavaVersion() {
        String version = System.getProperty("java.specification.version");
        if (version == null) {
            return 65542;
        }
        int firstDot = StringsKt.indexOf$default((CharSequence) version, '.', 0, false, 6, (Object) null);
        if (firstDot < 0) {
            try {
                return Integer.parseInt(version) * 65536;
            } catch (NumberFormatException e) {
                return 65542;
            }
        }
        int secondDot = StringsKt.indexOf$default((CharSequence) version, '.', firstDot + 1, false, 4, (Object) null);
        if (secondDot < 0) {
            secondDot = version.length();
        }
        String firstPart = version.substring(0, firstDot);
        Intrinsics.checkNotNullExpressionValue(firstPart, "this as java.lang.String…ing(startIndex, endIndex)");
        String secondPart = version.substring(firstDot + 1, secondDot);
        Intrinsics.checkNotNullExpressionValue(secondPart, "this as java.lang.String…ing(startIndex, endIndex)");
        try {
            return (Integer.parseInt(firstPart) * 65536) + Integer.parseInt(secondPart);
        } catch (NumberFormatException e2) {
            return 65542;
        }
    }

    public static final boolean apiVersionIsAtLeast(int major, int minor, int patch) {
        return KotlinVersion.CURRENT.isAtLeast(major, minor, patch);
    }
}
