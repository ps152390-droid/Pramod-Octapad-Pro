package com.google.firebase.database.ktx;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.channels.ChannelsKt;
import kotlinx.coroutines.channels.ProducerScope;

/* JADX INFO: compiled from: Database.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000\u001f\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0016¨\u0006\t"}, d2 = {"com/google/firebase/database/ktx/DatabaseKt$snapshots$1$listener$1", "Lcom/google/firebase/database/ValueEventListener;", "onCancelled", "", "error", "Lcom/google/firebase/database/DatabaseError;", "onDataChange", "snapshot", "Lcom/google/firebase/database/DataSnapshot;", "com.google.firebase-firebase-database"}, k = 1, mv = {1, 7, 1}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
public final class DatabaseKt$snapshots$1$listener$1 implements ValueEventListener {
    final /* synthetic */ ProducerScope<DataSnapshot> $$this$callbackFlow;
    final /* synthetic */ Query $this_snapshots;

    /* JADX WARN: Multi-variable type inference failed */
    DatabaseKt$snapshots$1$listener$1(Query $receiver, ProducerScope<? super DataSnapshot> producerScope) {
        this.$this_snapshots = $receiver;
        this.$$this$callbackFlow = producerScope;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onDataChange$lambda-0, reason: not valid java name */
    public static final void m123onDataChange$lambda0(ProducerScope $this$callbackFlow, DataSnapshot snapshot) {
        Intrinsics.checkNotNullParameter($this$callbackFlow, "$$this$callbackFlow");
        Intrinsics.checkNotNullParameter(snapshot, "$snapshot");
        ChannelsKt.trySendBlocking($this$callbackFlow, snapshot);
    }

    @Override // com.google.firebase.database.ValueEventListener
    public void onDataChange(final DataSnapshot snapshot) {
        Intrinsics.checkNotNullParameter(snapshot, "snapshot");
        Repo repo = this.$this_snapshots.getRepo();
        final ProducerScope<DataSnapshot> producerScope = this.$$this$callbackFlow;
        repo.scheduleNow(new Runnable() { // from class: com.google.firebase.database.ktx.DatabaseKt$snapshots$1$listener$1$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                DatabaseKt$snapshots$1$listener$1.m123onDataChange$lambda0(producerScope, snapshot);
            }
        });
    }

    @Override // com.google.firebase.database.ValueEventListener
    public void onCancelled(DatabaseError error) {
        Intrinsics.checkNotNullParameter(error, "error");
        CoroutineScopeKt.cancel(this.$$this$callbackFlow, "Error getting Query snapshot", error.toException());
    }
}
