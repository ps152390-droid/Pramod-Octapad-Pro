package com.google.firebase.database.ktx;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.Query;
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
@Metadata(d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\u0010\u0003\u001a\u00020\u0002*\b\u0012\u0004\u0012\u00020\u00010\u0000H\u008a@"}, d2 = {"Lkotlinx/coroutines/channels/ProducerScope;", "Lcom/google/firebase/database/ktx/ChildEvent;", "", "<anonymous>"}, k = 3, mv = {1, 7, 1})
@DebugMetadata(c = "com.google.firebase.database.ktx.DatabaseKt$childEvents$1", f = "Database.kt", i = {}, l = {205}, m = "invokeSuspend", n = {}, s = {})
final class DatabaseKt$childEvents$1 extends SuspendLambda implements Function2<ProducerScope<? super ChildEvent>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Query $this_childEvents;
    private /* synthetic */ Object L$0;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    DatabaseKt$childEvents$1(Query query, Continuation<? super DatabaseKt$childEvents$1> continuation) {
        super(2, continuation);
        this.$this_childEvents = query;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        DatabaseKt$childEvents$1 databaseKt$childEvents$1 = new DatabaseKt$childEvents$1(this.$this_childEvents, continuation);
        databaseKt$childEvents$1.L$0 = obj;
        return databaseKt$childEvents$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(ProducerScope<? super ChildEvent> producerScope, Continuation<? super Unit> continuation) {
        return ((DatabaseKt$childEvents$1) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object $result) throws Throwable {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                ProducerScope $this$callbackFlow = (ProducerScope) this.L$0;
                Query query = this.$this_childEvents;
                final ChildEventListener listener = query.addChildEventListener(new DatabaseKt$childEvents$1$listener$1(query, $this$callbackFlow));
                Intrinsics.checkNotNullExpressionValue(listener, "Query.childEvents\n  get(…  }\n          }\n        )");
                final Query query2 = this.$this_childEvents;
                this.label = 1;
                if (ProduceKt.awaitClose($this$callbackFlow, new Function0<Unit>() { // from class: com.google.firebase.database.ktx.DatabaseKt$childEvents$1.1
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
