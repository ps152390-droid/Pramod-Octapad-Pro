package com.google.firebase.database.ktx;

import com.google.firebase.database.DataSnapshot;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;

/* JADX INFO: Add missing generic type declarations: [T] */
/* JADX INFO: compiled from: SafeCollector.common.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u001f\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005H\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u0006\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0007¸\u0006\t"}, d2 = {"kotlinx/coroutines/flow/internal/SafeCollector_commonKt$unsafeFlow$1", "Lkotlinx/coroutines/flow/Flow;", "collect", "", "collector", "Lkotlinx/coroutines/flow/FlowCollector;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core", "kotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$$inlined$unsafeFlow$1", "kotlinx/coroutines/flow/FlowKt__TransformKt$map$$inlined$unsafeTransform$1"}, k = 1, mv = {1, 7, 1}, xi = 176)
public final class DatabaseKt$values$$inlined$map$1<T> implements Flow<T> {
    final /* synthetic */ Flow $this_unsafeTransform$inlined;

    /* JADX INFO: renamed from: com.google.firebase.database.ktx.DatabaseKt$values$$inlined$map$1$2, reason: invalid class name */
    /* JADX INFO: compiled from: Emitters.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032\u0006\u0010\u0004\u001a\u0002H\u0002H\u008a@¢\u0006\u0004\b\u0005\u0010\u0006¨\u0006\b"}, d2 = {"<anonymous>", "", "T", "R", "value", "emit", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$1$1", "kotlinx/coroutines/flow/FlowKt__TransformKt$map$$inlined$unsafeTransform$1$2"}, k = 3, mv = {1, 7, 1}, xi = 176)
    public static final class AnonymousClass2<T> implements FlowCollector {
        final /* synthetic */ FlowCollector $this_unsafeFlow;

        /* JADX INFO: renamed from: com.google.firebase.database.ktx.DatabaseKt$values$$inlined$map$1$2$1, reason: invalid class name */
        /* JADX INFO: compiled from: Emitters.kt */
        @Metadata(k = 3, mv = {1, 7, 1}, xi = 176)
        @DebugMetadata(c = "com.google.firebase.database.ktx.DatabaseKt$values$$inlined$map$1$2", f = "Database.kt", i = {}, l = {224}, m = "emit", n = {}, s = {})
        public static final class AnonymousClass1 extends ContinuationImpl {
            Object L$0;
            int label;
            /* synthetic */ Object result;

            public AnonymousClass1(Continuation continuation) {
                super(continuation);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object obj) {
                this.result = obj;
                this.label |= Integer.MIN_VALUE;
                return AnonymousClass2.this.emit(null, this);
            }
        }

        public AnonymousClass2(FlowCollector flowCollector) {
            this.$this_unsafeFlow = flowCollector;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public final Object emit$$forInline(Object value, Continuation $completion) {
            InlineMarker.mark(4);
            new AnonymousClass1($completion);
            InlineMarker.mark(5);
            FlowCollector flowCollector = this.$this_unsafeFlow;
            DataSnapshot it = (DataSnapshot) value;
            Intrinsics.reifiedOperationMarker(4, "T");
            Object value2 = it.getValue(Object.class);
            InlineMarker.mark(0);
            flowCollector.emit(value2, $completion);
            InlineMarker.mark(1);
            return Unit.INSTANCE;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
        @Override // kotlinx.coroutines.flow.FlowCollector
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final java.lang.Object emit(java.lang.Object r8, kotlin.coroutines.Continuation r9) throws java.lang.Throwable {
            /*
                r7 = this;
                boolean r0 = r9 instanceof com.google.firebase.database.ktx.DatabaseKt$values$$inlined$map$1.AnonymousClass2.AnonymousClass1
                if (r0 == 0) goto L14
                r0 = r9
                com.google.firebase.database.ktx.DatabaseKt$values$$inlined$map$1$2$1 r0 = (com.google.firebase.database.ktx.DatabaseKt$values$$inlined$map$1.AnonymousClass2.AnonymousClass1) r0
                int r1 = r0.label
                r2 = -2147483648(0xffffffff80000000, float:-0.0)
                r1 = r1 & r2
                if (r1 == 0) goto L14
                int r9 = r0.label
                int r9 = r9 - r2
                r0.label = r9
                goto L19
            L14:
                com.google.firebase.database.ktx.DatabaseKt$values$$inlined$map$1$2$1 r0 = new com.google.firebase.database.ktx.DatabaseKt$values$$inlined$map$1$2$1
                r0.<init>(r9)
            L19:
                r9 = r0
                java.lang.Object r0 = r9.result
                java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
                int r2 = r9.label
                switch(r2) {
                    case 0: goto L32;
                    case 1: goto L2d;
                    default: goto L25;
                }
            L25:
                java.lang.IllegalStateException r8 = new java.lang.IllegalStateException
                java.lang.String r9 = "call to 'resume' before 'invoke' with coroutine"
                r8.<init>(r9)
                throw r8
            L2d:
                r8 = 0
                kotlin.ResultKt.throwOnFailure(r0)
                goto L59
            L32:
                kotlin.ResultKt.throwOnFailure(r0)
                r2 = r7
                kotlinx.coroutines.flow.FlowCollector r2 = r2.$this_unsafeFlow
                r3 = 0
                r4 = r9
                kotlin.coroutines.Continuation r4 = (kotlin.coroutines.Continuation) r4
                com.google.firebase.database.DataSnapshot r8 = (com.google.firebase.database.DataSnapshot) r8
                r4 = 0
                r5 = 4
                java.lang.String r6 = "T"
                kotlin.jvm.internal.Intrinsics.reifiedOperationMarker(r5, r6)
                java.lang.Class<java.lang.Object> r5 = java.lang.Object.class
                r6 = r5
                java.lang.Class r6 = (java.lang.Class) r6
                java.lang.Object r8 = r8.getValue(r5)
                r4 = 1
                r9.label = r4
                java.lang.Object r8 = r2.emit(r8, r9)
                if (r8 != r1) goto L58
                return r1
            L58:
                r8 = r3
            L59:
                kotlin.Unit r8 = kotlin.Unit.INSTANCE
                return r8
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.database.ktx.DatabaseKt$values$$inlined$map$1.AnonymousClass2.emit(java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
        }
    }

    public DatabaseKt$values$$inlined$map$1(Flow flow) {
        this.$this_unsafeTransform$inlined = flow;
    }

    @Override // kotlinx.coroutines.flow.Flow
    public Object collect(FlowCollector collector, Continuation $completion) {
        Flow flow = this.$this_unsafeTransform$inlined;
        Intrinsics.needClassReification();
        Object objCollect = flow.collect(new AnonymousClass2(collector), $completion);
        return objCollect == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : Unit.INSTANCE;
    }

    public Object collect$$forInline(FlowCollector collector, Continuation $completion) {
        InlineMarker.mark(4);
        new ContinuationImpl($completion) { // from class: com.google.firebase.database.ktx.DatabaseKt$values$$inlined$map$1.1
            int label;
            /* synthetic */ Object result;

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object obj) {
                this.result = obj;
                this.label |= Integer.MIN_VALUE;
                return DatabaseKt$values$$inlined$map$1.this.collect(null, this);
            }
        };
        InlineMarker.mark(5);
        Flow flow = this.$this_unsafeTransform$inlined;
        Intrinsics.needClassReification();
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(collector);
        InlineMarker.mark(0);
        flow.collect(anonymousClass2, $completion);
        InlineMarker.mark(1);
        return Unit.INSTANCE;
    }
}
