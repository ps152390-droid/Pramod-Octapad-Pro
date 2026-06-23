package com.google.firebase.internal;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.GetTokenResult;

/* JADX INFO: compiled from: com.google.firebase:firebase-auth-interop@@20.0.0 */
/* JADX INFO: loaded from: classes.dex */
public interface InternalTokenProvider {
    Task<GetTokenResult> getAccessToken(boolean z);

    String getUid();
}
