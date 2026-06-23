package com.google.firebase.database.core.utilities;

/* JADX INFO: loaded from: classes.dex */
public class DefaultClock implements Clock {
    @Override // com.google.firebase.database.core.utilities.Clock
    public long millis() {
        return System.currentTimeMillis();
    }
}
