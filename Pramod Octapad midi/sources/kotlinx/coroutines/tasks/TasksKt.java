package kotlinx.coroutines.tasks;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.concurrent.CancellationException;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.sequences.Sequence;
import kotlinx.coroutines.CancellableContinuation;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.ChildHandle;
import kotlinx.coroutines.ChildJob;
import kotlinx.coroutines.CompletableDeferred;
import kotlinx.coroutines.CompletableDeferredKt;
import kotlinx.coroutines.Deferred;
import kotlinx.coroutines.DisposableHandle;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.selects.SelectClause0;
import kotlinx.coroutines.selects.SelectClause1;

/* JADX INFO: compiled from: Tasks.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000\u0016\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003\u001a&\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0007\u001a(\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0002\u001a\u001c\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001\u001a!\u0010\b\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\t\u001a)\u0010\b\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0087@ø\u0001\u0000¢\u0006\u0002\u0010\n\u001a+\u0010\u000b\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0082@ø\u0001\u0000¢\u0006\u0002\u0010\n\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\f"}, d2 = {"asDeferred", "Lkotlinx/coroutines/Deferred;", "T", "Lcom/google/android/gms/tasks/Task;", "cancellationTokenSource", "Lcom/google/android/gms/tasks/CancellationTokenSource;", "asDeferredImpl", "asTask", "await", "(Lcom/google/android/gms/tasks/Task;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "(Lcom/google/android/gms/tasks/Task;Lcom/google/android/gms/tasks/CancellationTokenSource;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "awaitImpl", "kotlinx-coroutines-play-services"}, k = 2, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
public final class TasksKt {
    public static final <T> Task<T> asTask(final Deferred<? extends T> deferred) {
        final CancellationTokenSource cancellation = new CancellationTokenSource();
        final TaskCompletionSource source = new TaskCompletionSource(cancellation.getToken());
        deferred.invokeOnCompletion(new Function1<Throwable, Unit>() { // from class: kotlinx.coroutines.tasks.TasksKt.asTask.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
                invoke2(th);
                return Unit.INSTANCE;
            }

            /* JADX WARN: Type inference incomplete: some casts might be missing */
            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(Throwable th) {
                if (th instanceof CancellationException) {
                    cancellation.cancel();
                    return;
                }
                Throwable completionExceptionOrNull = deferred.getCompletionExceptionOrNull();
                if (completionExceptionOrNull == null) {
                    source.setResult((T) deferred.getCompleted());
                    return;
                }
                TaskCompletionSource<T> taskCompletionSource = source;
                RuntimeExecutionException runtimeExecutionException = completionExceptionOrNull instanceof Exception ? (Exception) completionExceptionOrNull : null;
                if (runtimeExecutionException == null) {
                    runtimeExecutionException = new RuntimeExecutionException(completionExceptionOrNull);
                }
                taskCompletionSource.setException(runtimeExecutionException);
            }
        });
        return source.getTask();
    }

    public static final <T> Deferred<T> asDeferred(Task<T> task) {
        return asDeferredImpl(task, null);
    }

    public static final <T> Deferred<T> asDeferred(Task<T> task, CancellationTokenSource cancellationTokenSource) {
        return asDeferredImpl(task, cancellationTokenSource);
    }

    private static final <T> Deferred<T> asDeferredImpl(Task<T> task, final CancellationTokenSource cancellationTokenSource) {
        final CompletableDeferred deferred = CompletableDeferredKt.CompletableDeferred$default(null, 1, null);
        if (task.isComplete()) {
            Exception e = task.getException();
            if (e == null) {
                if (task.isCanceled()) {
                    Job.DefaultImpls.cancel$default((Job) deferred, (CancellationException) null, 1, (Object) null);
                } else {
                    deferred.complete(task.getResult());
                }
            } else {
                deferred.completeExceptionally(e);
            }
        } else {
            task.addOnCompleteListener(DirectExecutor.INSTANCE, new OnCompleteListener() { // from class: kotlinx.coroutines.tasks.TasksKt$$ExternalSyntheticLambda0
                @Override // com.google.android.gms.tasks.OnCompleteListener
                public final void onComplete(Task task2) {
                    TasksKt.m1680asDeferredImpl$lambda0(deferred, task2);
                }
            });
        }
        if (cancellationTokenSource != null) {
            deferred.invokeOnCompletion(new Function1<Throwable, Unit>() { // from class: kotlinx.coroutines.tasks.TasksKt.asDeferredImpl.2
                {
                    super(1);
                }

                @Override // kotlin.jvm.functions.Function1
                public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
                    invoke2(th);
                    return Unit.INSTANCE;
                }

                /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
                public final void invoke2(Throwable it) {
                    cancellationTokenSource.cancel();
                }
            });
        }
        return new Deferred<T>() { // from class: kotlinx.coroutines.tasks.TasksKt.asDeferredImpl.3
            @Override // kotlinx.coroutines.Job
            public ChildHandle attachChild(ChildJob child) {
                return deferred.attachChild(child);
            }

            @Override // kotlinx.coroutines.Deferred
            public Object await(Continuation<? super T> continuation) {
                return deferred.await(continuation);
            }

            @Override // kotlinx.coroutines.Job
            @Deprecated(level = DeprecationLevel.HIDDEN, message = "Since 1.2.0, binary compatibility with versions <= 1.1.x")
            public /* synthetic */ void cancel() {
                deferred.cancel();
            }

            @Override // kotlinx.coroutines.Job
            public void cancel(CancellationException cause) {
                deferred.cancel(cause);
            }

            @Override // kotlinx.coroutines.Job
            @Deprecated(level = DeprecationLevel.HIDDEN, message = "Since 1.2.0, binary compatibility with versions <= 1.1.x")
            public /* synthetic */ boolean cancel(Throwable cause) {
                return deferred.cancel(cause);
            }

            @Override // kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
            public <R> R fold(R initial, Function2<? super R, ? super CoroutineContext.Element, ? extends R> operation) {
                return (R) deferred.fold(initial, operation);
            }

            @Override // kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
            public <E extends CoroutineContext.Element> E get(CoroutineContext.Key<E> key) {
                return (E) deferred.get(key);
            }

            @Override // kotlinx.coroutines.Job
            public CancellationException getCancellationException() {
                return deferred.getCancellationException();
            }

            @Override // kotlinx.coroutines.Job
            public Sequence<Job> getChildren() {
                return deferred.getChildren();
            }

            @Override // kotlinx.coroutines.Deferred
            public T getCompleted() {
                return deferred.getCompleted();
            }

            @Override // kotlinx.coroutines.Deferred
            public Throwable getCompletionExceptionOrNull() {
                return deferred.getCompletionExceptionOrNull();
            }

            @Override // kotlin.coroutines.CoroutineContext.Element
            public CoroutineContext.Key<?> getKey() {
                return deferred.getKey();
            }

            @Override // kotlinx.coroutines.Deferred
            public SelectClause1<T> getOnAwait() {
                return deferred.getOnAwait();
            }

            @Override // kotlinx.coroutines.Job
            public SelectClause0 getOnJoin() {
                return deferred.getOnJoin();
            }

            @Override // kotlinx.coroutines.Job
            public DisposableHandle invokeOnCompletion(Function1<? super Throwable, Unit> handler) {
                return deferred.invokeOnCompletion(handler);
            }

            @Override // kotlinx.coroutines.Job
            public DisposableHandle invokeOnCompletion(boolean onCancelling, boolean invokeImmediately, Function1<? super Throwable, Unit> handler) {
                return deferred.invokeOnCompletion(onCancelling, invokeImmediately, handler);
            }

            @Override // kotlinx.coroutines.Job
            public boolean isActive() {
                return deferred.isActive();
            }

            @Override // kotlinx.coroutines.Job
            public boolean isCancelled() {
                return deferred.isCancelled();
            }

            @Override // kotlinx.coroutines.Job
            public boolean isCompleted() {
                return deferred.isCompleted();
            }

            @Override // kotlinx.coroutines.Job
            public Object join(Continuation<? super Unit> continuation) {
                return deferred.join(continuation);
            }

            @Override // kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
            public CoroutineContext minusKey(CoroutineContext.Key<?> key) {
                return deferred.minusKey(key);
            }

            @Override // kotlin.coroutines.CoroutineContext
            public CoroutineContext plus(CoroutineContext context) {
                return deferred.plus(context);
            }

            @Override // kotlinx.coroutines.Job
            @Deprecated(level = DeprecationLevel.ERROR, message = "Operator '+' on two Job objects is meaningless. Job is a coroutine context element and `+` is a set-sum operator for coroutine contexts. The job to the right of `+` just replaces the job the left of `+`.")
            public Job plus(Job other) {
                return deferred.plus(other);
            }

            @Override // kotlinx.coroutines.Job
            public boolean start() {
                return deferred.start();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: asDeferredImpl$lambda-0, reason: not valid java name */
    public static final void m1680asDeferredImpl$lambda0(CompletableDeferred $deferred, Task it) {
        Exception e = it.getException();
        if (e == null) {
            if (it.isCanceled()) {
                Job.DefaultImpls.cancel$default((Job) $deferred, (CancellationException) null, 1, (Object) null);
                return;
            } else {
                $deferred.complete(it.getResult());
                return;
            }
        }
        $deferred.completeExceptionally(e);
    }

    public static final <T> Object await(Task<T> task, Continuation<? super T> continuation) {
        return awaitImpl(task, null, continuation);
    }

    public static final <T> Object await(Task<T> task, CancellationTokenSource cancellationTokenSource, Continuation<? super T> continuation) {
        return awaitImpl(task, cancellationTokenSource, continuation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final <T> Object awaitImpl(Task<T> task, final CancellationTokenSource cancellationTokenSource, Continuation<? super T> continuation) throws Exception {
        if (task.isComplete()) {
            Exception e = task.getException();
            if (e == null) {
                if (task.isCanceled()) {
                    throw new CancellationException("Task " + task + " was cancelled normally.");
                }
                return task.getResult();
            }
            throw e;
        }
        CancellableContinuationImpl cancellable$iv = new CancellableContinuationImpl(IntrinsicsKt.intercepted(continuation), 1);
        cancellable$iv.initCancellability();
        final CancellableContinuationImpl cont = cancellable$iv;
        task.addOnCompleteListener(DirectExecutor.INSTANCE, new OnCompleteListener() { // from class: kotlinx.coroutines.tasks.TasksKt$awaitImpl$2$1
            @Override // com.google.android.gms.tasks.OnCompleteListener
            public final void onComplete(Task<T> task2) {
                Exception e2 = task2.getException();
                if (e2 == null) {
                    if (task2.isCanceled()) {
                        CancellableContinuation.DefaultImpls.cancel$default(cont, null, 1, null);
                        return;
                    }
                    Continuation continuation2 = cont;
                    Result.Companion companion = Result.INSTANCE;
                    continuation2.resumeWith(Result.m130constructorimpl(task2.getResult()));
                    return;
                }
                Continuation continuation3 = cont;
                Result.Companion companion2 = Result.INSTANCE;
                continuation3.resumeWith(Result.m130constructorimpl(ResultKt.createFailure(e2)));
            }
        });
        if (cancellationTokenSource != null) {
            cont.invokeOnCancellation(new Function1<Throwable, Unit>() { // from class: kotlinx.coroutines.tasks.TasksKt$awaitImpl$2$2
                {
                    super(1);
                }

                @Override // kotlin.jvm.functions.Function1
                public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
                    invoke2(th);
                    return Unit.INSTANCE;
                }

                /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
                public final void invoke2(Throwable it) {
                    cancellationTokenSource.cancel();
                }
            });
        }
        Object result = cancellable$iv.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }
}
