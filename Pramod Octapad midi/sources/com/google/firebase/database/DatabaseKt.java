package com.google.firebase.database;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowKt;

/* JADX INFO: compiled from: Database.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000B\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\u001a\u0012\u0010\u0006\u001a\u00020\u0007*\u00020\b2\u0006\u0010\u000e\u001a\u00020\u000f\u001a\u001a\u0010\u0006\u001a\u00020\u0007*\u00020\b2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011\u001a\u0012\u0010\u0006\u001a\u00020\u0007*\u00020\b2\u0006\u0010\u0010\u001a\u00020\u0011\u001a\u001c\u0010\u0012\u001a\u0004\u0018\u0001H\u0013\"\u0006\b\u0000\u0010\u0013\u0018\u0001*\u00020\fH\u0086\b¢\u0006\u0002\u0010\u0014\u001a\u001c\u0010\u0012\u001a\u0004\u0018\u0001H\u0013\"\u0006\b\u0000\u0010\u0013\u0018\u0001*\u00020\u0015H\u0086\b¢\u0006\u0002\u0010\u0016\u001a!\u0010\u0017\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00130\u0001\"\n\b\u0000\u0010\u0013\u0018\u0001*\u00020\u0018*\u00020\u0003H\u0086\b\"\u001b\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\"\u0015\u0010\u0006\u001a\u00020\u0007*\u00020\b8F¢\u0006\u0006\u001a\u0004\b\t\u0010\n\"\u001b\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u0001*\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\r\u0010\u0005¨\u0006\u0019"}, d2 = {"childEvents", "Lkotlinx/coroutines/flow/Flow;", "Lcom/google/firebase/database/ChildEvent;", "Lcom/google/firebase/database/Query;", "getChildEvents", "(Lcom/google/firebase/database/Query;)Lkotlinx/coroutines/flow/Flow;", "database", "Lcom/google/firebase/database/FirebaseDatabase;", "Lcom/google/firebase/Firebase;", "getDatabase", "(Lcom/google/firebase/Firebase;)Lcom/google/firebase/database/FirebaseDatabase;", "snapshots", "Lcom/google/firebase/database/DataSnapshot;", "getSnapshots", "app", "Lcom/google/firebase/FirebaseApp;", ImagesContract.URL, "", "getValue", "T", "(Lcom/google/firebase/database/DataSnapshot;)Ljava/lang/Object;", "Lcom/google/firebase/database/MutableData;", "(Lcom/google/firebase/database/MutableData;)Ljava/lang/Object;", "values", "", "com.google.firebase-firebase-database"}, k = 2, mv = {1, 7, 1}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
public final class DatabaseKt {
    public static final FirebaseDatabase getDatabase(Firebase $this$database) {
        Intrinsics.checkNotNullParameter($this$database, "<this>");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        Intrinsics.checkNotNullExpressionValue(firebaseDatabase, "getInstance()");
        return firebaseDatabase;
    }

    public static final FirebaseDatabase database(Firebase $this$database, String url) {
        Intrinsics.checkNotNullParameter($this$database, "<this>");
        Intrinsics.checkNotNullParameter(url, "url");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(url);
        Intrinsics.checkNotNullExpressionValue(firebaseDatabase, "getInstance(url)");
        return firebaseDatabase;
    }

    public static final FirebaseDatabase database(Firebase $this$database, FirebaseApp app) {
        Intrinsics.checkNotNullParameter($this$database, "<this>");
        Intrinsics.checkNotNullParameter(app, "app");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(app);
        Intrinsics.checkNotNullExpressionValue(firebaseDatabase, "getInstance(app)");
        return firebaseDatabase;
    }

    public static final FirebaseDatabase database(Firebase $this$database, FirebaseApp app, String url) {
        Intrinsics.checkNotNullParameter($this$database, "<this>");
        Intrinsics.checkNotNullParameter(app, "app");
        Intrinsics.checkNotNullParameter(url, "url");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(app, url);
        Intrinsics.checkNotNullExpressionValue(firebaseDatabase, "getInstance(app, url)");
        return firebaseDatabase;
    }

    public static final /* synthetic */ <T> T getValue(DataSnapshot dataSnapshot) {
        Intrinsics.checkNotNullParameter(dataSnapshot, "<this>");
        Intrinsics.needClassReification();
        return (T) dataSnapshot.getValue(new GenericTypeIndicator<T>() { // from class: com.google.firebase.database.DatabaseKt.getValue.1
        });
    }

    public static final /* synthetic */ <T> T getValue(MutableData mutableData) {
        Intrinsics.checkNotNullParameter(mutableData, "<this>");
        Intrinsics.needClassReification();
        return (T) mutableData.getValue(new GenericTypeIndicator<T>() { // from class: com.google.firebase.database.DatabaseKt.getValue.2
        });
    }

    public static final Flow<DataSnapshot> getSnapshots(Query $this$snapshots) {
        Intrinsics.checkNotNullParameter($this$snapshots, "<this>");
        return FlowKt.callbackFlow(new DatabaseKt$snapshots$1($this$snapshots, null));
    }

    public static final Flow<ChildEvent> getChildEvents(Query $this$childEvents) {
        Intrinsics.checkNotNullParameter($this$childEvents, "<this>");
        return FlowKt.callbackFlow(new DatabaseKt$childEvents$1($this$childEvents, null));
    }

    public static final /* synthetic */ <T> Flow<T> values(Query $this$values) {
        Intrinsics.checkNotNullParameter($this$values, "<this>");
        Flow<DataSnapshot> snapshots = getSnapshots($this$values);
        Intrinsics.needClassReification();
        return new DatabaseKt$values$$inlined$map$1(snapshots);
    }
}
