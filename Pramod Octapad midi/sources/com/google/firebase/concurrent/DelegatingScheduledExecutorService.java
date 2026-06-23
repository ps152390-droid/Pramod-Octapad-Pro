package com.google.firebase.concurrent;

import com.google.firebase.concurrent.DelegatingScheduledFuture;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* JADX INFO: loaded from: classes.dex */
class DelegatingScheduledExecutorService implements ScheduledExecutorService {
    private final ExecutorService delegate;
    private final ScheduledExecutorService scheduler;

    DelegatingScheduledExecutorService(ExecutorService delegate, ScheduledExecutorService scheduler) {
        this.delegate = delegate;
        this.scheduler = scheduler;
    }

    @Override // java.util.concurrent.ExecutorService
    public void shutdown() {
        throw new UnsupportedOperationException("Shutting down is not allowed.");
    }

    @Override // java.util.concurrent.ExecutorService
    public List<Runnable> shutdownNow() {
        throw new UnsupportedOperationException("Shutting down is not allowed.");
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isShutdown() {
        return this.delegate.isShutdown();
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isTerminated() {
        return this.delegate.isTerminated();
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return this.delegate.awaitTermination(timeout, unit);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> Future<T> submit(Callable<T> task) {
        return this.delegate.submit(task);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> Future<T> submit(Runnable task, T result) {
        return this.delegate.submit(task, result);
    }

    @Override // java.util.concurrent.ExecutorService
    public Future<?> submit(Runnable task) {
        return this.delegate.submit(task);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return this.delegate.invokeAll(tasks);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return this.delegate.invokeAll(tasks, timeout, unit);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> T invokeAny(Collection<? extends Callable<T>> collection) throws ExecutionException, InterruptedException {
        return (T) this.delegate.invokeAny(collection);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> T invokeAny(Collection<? extends Callable<T>> collection, long j, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        return (T) this.delegate.invokeAny(collection, j, timeUnit);
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable command) {
        this.delegate.execute(command);
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> schedule(final Runnable command, final long delay, final TimeUnit unit) {
        return new DelegatingScheduledFuture(new DelegatingScheduledFuture.Resolver() { // from class: com.google.firebase.concurrent.DelegatingScheduledExecutorService$$ExternalSyntheticLambda6
            @Override // com.google.firebase.concurrent.DelegatingScheduledFuture.Resolver
            public final ScheduledFuture addCompleter(DelegatingScheduledFuture.Completer completer) {
                return this.f$0.m94lambda$schedule$2$comgooglefirebaseconcurrentDelegatingScheduledExecutorService(command, delay, unit, completer);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$schedule$2$com-google-firebase-concurrent-DelegatingScheduledExecutorService, reason: not valid java name */
    /* synthetic */ ScheduledFuture m94lambda$schedule$2$comgooglefirebaseconcurrentDelegatingScheduledExecutorService(final Runnable command, long delay, TimeUnit unit, final DelegatingScheduledFuture.Completer completer) {
        return this.scheduler.schedule(new Runnable() { // from class: com.google.firebase.concurrent.DelegatingScheduledExecutorService$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.m93lambda$schedule$1$comgooglefirebaseconcurrentDelegatingScheduledExecutorService(command, completer);
            }
        }, delay, unit);
    }

    /* JADX INFO: renamed from: lambda$schedule$1$com-google-firebase-concurrent-DelegatingScheduledExecutorService, reason: not valid java name */
    /* synthetic */ void m93lambda$schedule$1$comgooglefirebaseconcurrentDelegatingScheduledExecutorService(final Runnable command, final DelegatingScheduledFuture.Completer completer) {
        this.delegate.execute(new Runnable() { // from class: com.google.firebase.concurrent.DelegatingScheduledExecutorService$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                DelegatingScheduledExecutorService.lambda$schedule$0(command, completer);
            }
        });
    }

    static /* synthetic */ void lambda$schedule$0(Runnable command, DelegatingScheduledFuture.Completer completer) {
        try {
            command.run();
            completer.set(null);
        } catch (Exception ex) {
            completer.setException(ex);
        }
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public <V> ScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit unit) {
        return new DelegatingScheduledFuture(new DelegatingScheduledFuture.Resolver() { // from class: com.google.firebase.concurrent.DelegatingScheduledExecutorService$$ExternalSyntheticLambda0
            @Override // com.google.firebase.concurrent.DelegatingScheduledFuture.Resolver
            public final ScheduledFuture addCompleter(DelegatingScheduledFuture.Completer completer) {
                return this.f$0.m96lambda$schedule$5$comgooglefirebaseconcurrentDelegatingScheduledExecutorService(callable, delay, unit, completer);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$schedule$5$com-google-firebase-concurrent-DelegatingScheduledExecutorService, reason: not valid java name */
    /* synthetic */ ScheduledFuture m96lambda$schedule$5$comgooglefirebaseconcurrentDelegatingScheduledExecutorService(final Callable callable, long delay, TimeUnit unit, final DelegatingScheduledFuture.Completer completer) {
        return this.scheduler.schedule(new Callable() { // from class: com.google.firebase.concurrent.DelegatingScheduledExecutorService$$ExternalSyntheticLambda2
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return this.f$0.m95lambda$schedule$4$comgooglefirebaseconcurrentDelegatingScheduledExecutorService(callable, completer);
            }
        }, delay, unit);
    }

    /* JADX INFO: renamed from: lambda$schedule$4$com-google-firebase-concurrent-DelegatingScheduledExecutorService, reason: not valid java name */
    /* synthetic */ Future m95lambda$schedule$4$comgooglefirebaseconcurrentDelegatingScheduledExecutorService(final Callable callable, final DelegatingScheduledFuture.Completer completer) throws Exception {
        return this.delegate.submit(new Runnable() { // from class: com.google.firebase.concurrent.DelegatingScheduledExecutorService$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                DelegatingScheduledExecutorService.lambda$schedule$3(callable, completer);
            }
        });
    }

    static /* synthetic */ void lambda$schedule$3(Callable callable, DelegatingScheduledFuture.Completer completer) {
        try {
            completer.set(callable.call());
        } catch (Exception ex) {
            completer.setException(ex);
        }
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit) {
        return new DelegatingScheduledFuture(new DelegatingScheduledFuture.Resolver() { // from class: com.google.firebase.concurrent.DelegatingScheduledExecutorService$$ExternalSyntheticLambda9
            @Override // com.google.firebase.concurrent.DelegatingScheduledFuture.Resolver
            public final ScheduledFuture addCompleter(DelegatingScheduledFuture.Completer completer) {
                return this.f$0.m98lambda$scheduleAtFixedRate$8$comgooglefirebaseconcurrentDelegatingScheduledExecutorService(command, initialDelay, period, unit, completer);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$scheduleAtFixedRate$8$com-google-firebase-concurrent-DelegatingScheduledExecutorService, reason: not valid java name */
    /* synthetic */ ScheduledFuture m98lambda$scheduleAtFixedRate$8$comgooglefirebaseconcurrentDelegatingScheduledExecutorService(final Runnable command, long initialDelay, long period, TimeUnit unit, final DelegatingScheduledFuture.Completer completer) {
        return this.scheduler.scheduleAtFixedRate(new Runnable() { // from class: com.google.firebase.concurrent.DelegatingScheduledExecutorService$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.m97lambda$scheduleAtFixedRate$7$comgooglefirebaseconcurrentDelegatingScheduledExecutorService(command, completer);
            }
        }, initialDelay, period, unit);
    }

    /* JADX INFO: renamed from: lambda$scheduleAtFixedRate$7$com-google-firebase-concurrent-DelegatingScheduledExecutorService, reason: not valid java name */
    /* synthetic */ void m97lambda$scheduleAtFixedRate$7$comgooglefirebaseconcurrentDelegatingScheduledExecutorService(final Runnable command, final DelegatingScheduledFuture.Completer completer) {
        this.delegate.execute(new Runnable() { // from class: com.google.firebase.concurrent.DelegatingScheduledExecutorService$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() throws Exception {
                DelegatingScheduledExecutorService.lambda$scheduleAtFixedRate$6(command, completer);
            }
        });
    }

    static /* synthetic */ void lambda$scheduleAtFixedRate$6(Runnable command, DelegatingScheduledFuture.Completer completer) throws Exception {
        try {
            command.run();
        } catch (Exception ex) {
            completer.setException(ex);
            throw ex;
        }
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit) {
        return new DelegatingScheduledFuture(new DelegatingScheduledFuture.Resolver() { // from class: com.google.firebase.concurrent.DelegatingScheduledExecutorService$$ExternalSyntheticLambda7
            @Override // com.google.firebase.concurrent.DelegatingScheduledFuture.Resolver
            public final ScheduledFuture addCompleter(DelegatingScheduledFuture.Completer completer) {
                return this.f$0.m100lambda$scheduleWithFixedDelay$11$comgooglefirebaseconcurrentDelegatingScheduledExecutorService(command, initialDelay, delay, unit, completer);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$scheduleWithFixedDelay$11$com-google-firebase-concurrent-DelegatingScheduledExecutorService, reason: not valid java name */
    /* synthetic */ ScheduledFuture m100lambda$scheduleWithFixedDelay$11$comgooglefirebaseconcurrentDelegatingScheduledExecutorService(final Runnable command, long initialDelay, long delay, TimeUnit unit, final DelegatingScheduledFuture.Completer completer) {
        return this.scheduler.scheduleWithFixedDelay(new Runnable() { // from class: com.google.firebase.concurrent.DelegatingScheduledExecutorService$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.m99lambda$scheduleWithFixedDelay$10$comgooglefirebaseconcurrentDelegatingScheduledExecutorService(command, completer);
            }
        }, initialDelay, delay, unit);
    }

    /* JADX INFO: renamed from: lambda$scheduleWithFixedDelay$10$com-google-firebase-concurrent-DelegatingScheduledExecutorService, reason: not valid java name */
    /* synthetic */ void m99lambda$scheduleWithFixedDelay$10$comgooglefirebaseconcurrentDelegatingScheduledExecutorService(final Runnable command, final DelegatingScheduledFuture.Completer completer) {
        this.delegate.execute(new Runnable() { // from class: com.google.firebase.concurrent.DelegatingScheduledExecutorService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                DelegatingScheduledExecutorService.lambda$scheduleWithFixedDelay$9(command, completer);
            }
        });
    }

    static /* synthetic */ void lambda$scheduleWithFixedDelay$9(Runnable command, DelegatingScheduledFuture.Completer completer) {
        try {
            command.run();
        } catch (Exception ex) {
            completer.setException(ex);
        }
    }
}
