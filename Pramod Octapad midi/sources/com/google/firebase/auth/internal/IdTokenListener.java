package com.google.firebase.auth.internal;

import com.google.firebase.internal.InternalTokenResult;

/* JADX INFO: compiled from: com.google.firebase:firebase-auth-interop@@20.0.0 */
/* JADX INFO: loaded from: classes.dex */
public interface IdTokenListener {
    void onIdTokenChanged(InternalTokenResult internalTokenResult);
}
