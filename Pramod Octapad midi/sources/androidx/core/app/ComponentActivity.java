package androidx.core.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import androidx.collection.SimpleArrayMap;
import androidx.core.view.KeyEventDispatcher;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ReportFragment;

/* JADX INFO: loaded from: classes.dex */
public class ComponentActivity extends Activity implements LifecycleOwner, KeyEventDispatcher.Component {
    private SimpleArrayMap<Class<? extends ExtraData>, ExtraData> mExtraDataMap = new SimpleArrayMap<>();
    private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    @Deprecated
    public static class ExtraData {
    }

    @Deprecated
    public void putExtraData(ExtraData extraData) {
        this.mExtraDataMap.put((Class<? extends ExtraData>) extraData.getClass(), extraData);
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ReportFragment.injectIfNeededIn(this);
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        this.mLifecycleRegistry.markState(Lifecycle.State.CREATED);
        super.onSaveInstanceState(outState);
    }

    @Deprecated
    public <T extends ExtraData> T getExtraData(Class<T> extraDataClass) {
        return (T) this.mExtraDataMap.get(extraDataClass);
    }

    public Lifecycle getLifecycle() {
        return this.mLifecycleRegistry;
    }

    @Override // androidx.core.view.KeyEventDispatcher.Component
    public boolean superDispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        View decor = getWindow().getDecorView();
        if (decor != null && KeyEventDispatcher.dispatchBeforeHierarchy(decor, event)) {
            return true;
        }
        return super.dispatchKeyShortcutEvent(event);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent event) {
        View decor = getWindow().getDecorView();
        if (decor != null && KeyEventDispatcher.dispatchBeforeHierarchy(decor, event)) {
            return true;
        }
        return KeyEventDispatcher.dispatchKeyEvent(this, decor, this, event);
    }

    protected final boolean shouldDumpInternalState(String[] args) {
        return !shouldSkipDump(args);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0043  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static boolean shouldSkipDump(java.lang.String[] r4) {
        /*
            r0 = 0
            if (r4 == 0) goto L65
            int r1 = r4.length
            if (r1 <= 0) goto L65
            r1 = r4[r0]
            int r2 = r1.hashCode()
            r3 = 1
            switch(r2) {
                case -645125871: goto L39;
                case 100470631: goto L2f;
                case 472614934: goto L25;
                case 1159329357: goto L1b;
                case 1455016274: goto L11;
                default: goto L10;
            }
        L10:
            goto L43
        L11:
            java.lang.String r2 = "--autofill"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L10
            r1 = 0
            goto L44
        L1b:
            java.lang.String r2 = "--contentcapture"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L10
            r1 = 1
            goto L44
        L25:
            java.lang.String r2 = "--list-dumpables"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L10
            r1 = 3
            goto L44
        L2f:
            java.lang.String r2 = "--dump-dumpable"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L10
            r1 = 4
            goto L44
        L39:
            java.lang.String r2 = "--translation"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L10
            r1 = 2
            goto L44
        L43:
            r1 = -1
        L44:
            switch(r1) {
                case 0: goto L5d;
                case 1: goto L55;
                case 2: goto L4d;
                case 3: goto L48;
                case 4: goto L48;
                default: goto L47;
            }
        L47:
            goto L65
        L48:
            boolean r0 = androidx.core.os.BuildCompat.isAtLeastT()
            return r0
        L4d:
            int r1 = android.os.Build.VERSION.SDK_INT
            r2 = 31
            if (r1 < r2) goto L54
            r0 = 1
        L54:
            return r0
        L55:
            int r1 = android.os.Build.VERSION.SDK_INT
            r2 = 29
            if (r1 < r2) goto L5c
            r0 = 1
        L5c:
            return r0
        L5d:
            int r1 = android.os.Build.VERSION.SDK_INT
            r2 = 26
            if (r1 < r2) goto L64
            r0 = 1
        L64:
            return r0
        L65:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.app.ComponentActivity.shouldSkipDump(java.lang.String[]):boolean");
    }
}
