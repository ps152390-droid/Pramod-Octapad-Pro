package com.google.firebase.database.ktx;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.core.Repo;
import com.google.firebase.database.ktx.ChildEvent;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.channels.ChannelsKt;
import kotlinx.coroutines.channels.ProducerScope;

/* JADX INFO: compiled from: Database.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000'\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u001a\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0016J\u001a\u0010\u000b\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0016J\u001a\u0010\f\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0016J\u0010\u0010\r\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0016¨\u0006\u000e"}, d2 = {"com/google/firebase/database/ktx/DatabaseKt$childEvents$1$listener$1", "Lcom/google/firebase/database/ChildEventListener;", "onCancelled", "", "error", "Lcom/google/firebase/database/DatabaseError;", "onChildAdded", "snapshot", "Lcom/google/firebase/database/DataSnapshot;", "previousChildName", "", "onChildChanged", "onChildMoved", "onChildRemoved", "com.google.firebase-firebase-database"}, k = 1, mv = {1, 7, 1}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
public final class DatabaseKt$childEvents$1$listener$1 implements ChildEventListener {
    final /* synthetic */ ProducerScope<ChildEvent> $$this$callbackFlow;
    final /* synthetic */ Query $this_childEvents;

    /* JADX WARN: Multi-variable type inference failed */
    DatabaseKt$childEvents$1$listener$1(Query $receiver, ProducerScope<? super ChildEvent> producerScope) {
        this.$this_childEvents = $receiver;
        this.$$this$callbackFlow = producerScope;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onChildAdded$lambda-0, reason: not valid java name */
    public static final void m119onChildAdded$lambda0(ProducerScope $this$callbackFlow, DataSnapshot snapshot, String $previousChildName) {
        Intrinsics.checkNotNullParameter($this$callbackFlow, "$$this$callbackFlow");
        Intrinsics.checkNotNullParameter(snapshot, "$snapshot");
        ChannelsKt.trySendBlocking($this$callbackFlow, new ChildEvent.Added(snapshot, $previousChildName));
    }

    @Override // com.google.firebase.database.ChildEventListener
    public void onChildAdded(final DataSnapshot snapshot, final String previousChildName) {
        Intrinsics.checkNotNullParameter(snapshot, "snapshot");
        Repo repo = this.$this_childEvents.getRepo();
        final ProducerScope<ChildEvent> producerScope = this.$$this$callbackFlow;
        repo.scheduleNow(new Runnable() { // from class: com.google.firebase.database.ktx.DatabaseKt$childEvents$1$listener$1$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                DatabaseKt$childEvents$1$listener$1.m119onChildAdded$lambda0(producerScope, snapshot, previousChildName);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onChildChanged$lambda-1, reason: not valid java name */
    public static final void m120onChildChanged$lambda1(ProducerScope $this$callbackFlow, DataSnapshot snapshot, String $previousChildName) {
        Intrinsics.checkNotNullParameter($this$callbackFlow, "$$this$callbackFlow");
        Intrinsics.checkNotNullParameter(snapshot, "$snapshot");
        ChannelsKt.trySendBlocking($this$callbackFlow, new ChildEvent.Changed(snapshot, $previousChildName));
    }

    @Override // com.google.firebase.database.ChildEventListener
    public void onChildChanged(final DataSnapshot snapshot, final String previousChildName) {
        Intrinsics.checkNotNullParameter(snapshot, "snapshot");
        Repo repo = this.$this_childEvents.getRepo();
        final ProducerScope<ChildEvent> producerScope = this.$$this$callbackFlow;
        repo.scheduleNow(new Runnable() { // from class: com.google.firebase.database.ktx.DatabaseKt$childEvents$1$listener$1$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                DatabaseKt$childEvents$1$listener$1.m120onChildChanged$lambda1(producerScope, snapshot, previousChildName);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onChildRemoved$lambda-2, reason: not valid java name */
    public static final void m122onChildRemoved$lambda2(ProducerScope $this$callbackFlow, DataSnapshot snapshot) {
        Intrinsics.checkNotNullParameter($this$callbackFlow, "$$this$callbackFlow");
        Intrinsics.checkNotNullParameter(snapshot, "$snapshot");
        ChannelsKt.trySendBlocking($this$callbackFlow, new ChildEvent.Removed(snapshot));
    }

    @Override // com.google.firebase.database.ChildEventListener
    public void onChildRemoved(final DataSnapshot snapshot) {
        Intrinsics.checkNotNullParameter(snapshot, "snapshot");
        Repo repo = this.$this_childEvents.getRepo();
        final ProducerScope<ChildEvent> producerScope = this.$$this$callbackFlow;
        repo.scheduleNow(new Runnable() { // from class: com.google.firebase.database.ktx.DatabaseKt$childEvents$1$listener$1$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                DatabaseKt$childEvents$1$listener$1.m122onChildRemoved$lambda2(producerScope, snapshot);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onChildMoved$lambda-3, reason: not valid java name */
    public static final void m121onChildMoved$lambda3(ProducerScope $this$callbackFlow, DataSnapshot snapshot, String $previousChildName) {
        Intrinsics.checkNotNullParameter($this$callbackFlow, "$$this$callbackFlow");
        Intrinsics.checkNotNullParameter(snapshot, "$snapshot");
        ChannelsKt.trySendBlocking($this$callbackFlow, new ChildEvent.Moved(snapshot, $previousChildName));
    }

    @Override // com.google.firebase.database.ChildEventListener
    public void onChildMoved(final DataSnapshot snapshot, final String previousChildName) {
        Intrinsics.checkNotNullParameter(snapshot, "snapshot");
        Repo repo = this.$this_childEvents.getRepo();
        final ProducerScope<ChildEvent> producerScope = this.$$this$callbackFlow;
        repo.scheduleNow(new Runnable() { // from class: com.google.firebase.database.ktx.DatabaseKt$childEvents$1$listener$1$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                DatabaseKt$childEvents$1$listener$1.m121onChildMoved$lambda3(producerScope, snapshot, previousChildName);
            }
        });
    }

    @Override // com.google.firebase.database.ChildEventListener
    public void onCancelled(DatabaseError error) {
        Intrinsics.checkNotNullParameter(error, "error");
        CoroutineScopeKt.cancel(this.$$this$callbackFlow, "Error getting Query childEvent", error.toException());
    }
}
