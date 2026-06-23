package com.google.firebase.database;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.channels.ProduceKt;
import kotlinx.coroutines.channels.ProducerScope;

/* JADX INFO: compiled from: Database.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\u0010\u0003\u001a\u00020\u0002*\b\u0012\u0004\u0012\u00020\u00010\u0000H\u008a@"}, d2 = {"Lkotlinx/coroutines/channels/ProducerScope;", "Lcom/google/firebase/database/DataSnapshot;", "", "<anonymous>"}, k = 3, mv = {1, 7, 1})
@DebugMetadata(c = "com.google.firebase.database.DatabaseKt$snapshots$1", f = "Database.kt", i = {}, l = {86}, m = "invokeSuspend", n = {}, s = {})
final class DatabaseKt$snapshots$1 extends SuspendLambda implements Function2<ProducerScope<? super DataSnapshot>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Query $this_snapshots;
    private /* synthetic */ Object L$0;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    DatabaseKt$snapshots$1(Query query, Continuation<? super DatabaseKt$snapshots$1> continuation) {
        super(2, continuation);
        this.$this_snapshots = query;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        DatabaseKt$snapshots$1 databaseKt$snapshots$1 = new DatabaseKt$snapshots$1(this.$this_snapshots, continuation);
        databaseKt$snapshots$1.L$0 = obj;
        return databaseKt$snapshots$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(ProducerScope<? super DataSnapshot> producerScope, Continuation<? super Unit> continuation) {
        return ((DatabaseKt$snapshots$1) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object $result) throws Throwable {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                ProducerScope $this$callbackFlow = (ProducerScope) this.L$0;
                Query query = this.$this_snapshots;
                final ValueEventListener listener = query.addValueEventListener(new DatabaseKt$snapshots$1$listener$1(query, $this$callbackFlow));
                Intrinsics.checkNotNullExpressionValue(listener, "Query.snapshots\n  get() …  }\n          }\n        )");
                final Query query2 = this.$this_snapshots;
                this.label = 1;
                if (ProduceKt.awaitClose($this$callbackFlow, new Function0<Unit>() { // from class: com.google.firebase.database.DatabaseKt$snapshots$1.1
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super(0);
                    }

                    @Override // kotlin.jvm.functions.Function0
                    public /* bridge */ /* synthetic */ Unit invoke() {
                        invoke2();
                        return Unit.INSTANCE;
                    }

                    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
                    public final void invoke2() {
                        query2.removeEventListener(listener);
                    }
                }, this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                ResultKt.throwOnFailure($result);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return Unit.INSTANCE;
    }
}
