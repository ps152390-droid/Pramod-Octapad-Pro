package com.google.firebase.concurrent;

import com.google.android.gms.common.internal.Preconditions;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
final class SequentialExecutor implements Executor {
    private static final Logger log = Logger.getLogger(SequentialExecutor.class.getName());
    private final Executor executor;
    private final Deque<Runnable> queue = new ArrayDeque();
    private WorkerRunningState workerRunningState = WorkerRunningState.IDLE;
    private long workerRunCount = 0;
    private final QueueWorker worker = new QueueWorker();

    enum WorkerRunningState {
        IDLE,
        QUEUING,
        QUEUED,
        RUNNING
    }

    static /* synthetic */ long access$308(SequentialExecutor x0) {
        long j = x0.workerRunCount;
        x0.workerRunCount = 1 + j;
        return j;
    }

    SequentialExecutor(Executor executor) {
        this.executor = (Executor) Preconditions.checkNotNull(executor);
    }

    @Override // java.util.concurrent.Executor
    public void execute(final Runnable task) {
        Preconditions.checkNotNull(task);
        synchronized (this.queue) {
            if (this.workerRunningState != WorkerRunningState.RUNNING && this.workerRunningState != WorkerRunningState.QUEUED) {
                long oldRunCount = this.workerRunCount;
                Runnable submittedTask = new Runnable() { // from class: com.google.firebase.concurrent.SequentialExecutor.1
                    @Override // java.lang.Runnable
                    public void run() {
                        task.run();
                    }

                    public String toString() {
                        return task.toString();
                    }
                };
                this.queue.add(submittedTask);
                this.workerRunningState = WorkerRunningState.QUEUING;
                try {
                    this.executor.execute(this.worker);
                    removed = this.workerRunningState != WorkerRunningState.QUEUING;
                    boolean alreadyMarkedQueued = removed;
                    if (alreadyMarkedQueued) {
                        return;
                    }
                    synchronized (this.queue) {
                        if (this.workerRunCount == oldRunCount && this.workerRunningState == WorkerRunningState.QUEUING) {
                            this.workerRunningState = WorkerRunningState.QUEUED;
                        }
                    }
                    return;
                } catch (Error | RuntimeException t) {
                    synchronized (this.queue) {
                        if ((this.workerRunningState != WorkerRunningState.IDLE && this.workerRunningState != WorkerRunningState.QUEUING) || !this.queue.removeLastOccurrence(submittedTask)) {
                            removed = false;
                        }
                        if (!(t instanceof RejectedExecutionException) || removed) {
                            throw t;
                        }
                    }
                    return;
                }
            }
            this.queue.add(task);
        }
    }

    private final class QueueWorker implements Runnable {

        @CheckForNull
        Runnable task;

        private QueueWorker() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                workOnQueue();
            } catch (Error e) {
                synchronized (SequentialExecutor.this.queue) {
                    SequentialExecutor.this.workerRunningState = WorkerRunningState.IDLE;
                    throw e;
                }
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:19:0x004e, code lost:
        
            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:22:0x0054, code lost:
        
            r0 = r0 | java.lang.Thread.interrupted();
            r2 = null;
         */
        /* JADX WARN: Code restructure failed: missing block: B:23:0x0056, code lost:
        
            r8.task.run();
         */
        /* JADX WARN: Code restructure failed: missing block: B:26:0x005e, code lost:
        
            r3 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:28:0x0060, code lost:
        
            r3 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:29:0x0061, code lost:
        
            com.google.firebase.concurrent.SequentialExecutor.log.log(java.util.logging.Level.SEVERE, "Exception while executing runnable " + r8.task, (java.lang.Throwable) r3);
         */
        /* JADX WARN: Code restructure failed: missing block: B:30:0x007f, code lost:
        
            r8.task = null;
         */
        /* JADX WARN: Code restructure failed: missing block: B:31:0x0082, code lost:
        
            r8.task = null;
         */
        /* JADX WARN: Code restructure failed: missing block: B:32:0x0085, code lost:
        
            throw r3;
         */
        /* JADX WARN: Code restructure failed: missing block: B:51:?, code lost:
        
            return;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private void workOnQueue() {
            /*
                r8 = this;
                r0 = 0
                r1 = 0
            L2:
                com.google.firebase.concurrent.SequentialExecutor r2 = com.google.firebase.concurrent.SequentialExecutor.this     // Catch: java.lang.Throwable -> L89
                java.util.Deque r2 = com.google.firebase.concurrent.SequentialExecutor.access$100(r2)     // Catch: java.lang.Throwable -> L89
                monitor-enter(r2)     // Catch: java.lang.Throwable -> L89
                if (r1 != 0) goto L2d
                com.google.firebase.concurrent.SequentialExecutor r3 = com.google.firebase.concurrent.SequentialExecutor.this     // Catch: java.lang.Throwable -> L86
                com.google.firebase.concurrent.SequentialExecutor$WorkerRunningState r3 = com.google.firebase.concurrent.SequentialExecutor.access$200(r3)     // Catch: java.lang.Throwable -> L86
                com.google.firebase.concurrent.SequentialExecutor$WorkerRunningState r4 = com.google.firebase.concurrent.SequentialExecutor.WorkerRunningState.RUNNING     // Catch: java.lang.Throwable -> L86
                if (r3 != r4) goto L20
                monitor-exit(r2)     // Catch: java.lang.Throwable -> L86
                if (r0 == 0) goto L1f
                java.lang.Thread r2 = java.lang.Thread.currentThread()
                r2.interrupt()
            L1f:
                return
            L20:
                com.google.firebase.concurrent.SequentialExecutor r3 = com.google.firebase.concurrent.SequentialExecutor.this     // Catch: java.lang.Throwable -> L86
                com.google.firebase.concurrent.SequentialExecutor.access$308(r3)     // Catch: java.lang.Throwable -> L86
                com.google.firebase.concurrent.SequentialExecutor r3 = com.google.firebase.concurrent.SequentialExecutor.this     // Catch: java.lang.Throwable -> L86
                com.google.firebase.concurrent.SequentialExecutor$WorkerRunningState r4 = com.google.firebase.concurrent.SequentialExecutor.WorkerRunningState.RUNNING     // Catch: java.lang.Throwable -> L86
                com.google.firebase.concurrent.SequentialExecutor.access$202(r3, r4)     // Catch: java.lang.Throwable -> L86
                r1 = 1
            L2d:
                com.google.firebase.concurrent.SequentialExecutor r3 = com.google.firebase.concurrent.SequentialExecutor.this     // Catch: java.lang.Throwable -> L86
                java.util.Deque r3 = com.google.firebase.concurrent.SequentialExecutor.access$100(r3)     // Catch: java.lang.Throwable -> L86
                java.lang.Object r3 = r3.poll()     // Catch: java.lang.Throwable -> L86
                java.lang.Runnable r3 = (java.lang.Runnable) r3     // Catch: java.lang.Throwable -> L86
                r8.task = r3     // Catch: java.lang.Throwable -> L86
                if (r3 != 0) goto L4f
                com.google.firebase.concurrent.SequentialExecutor r3 = com.google.firebase.concurrent.SequentialExecutor.this     // Catch: java.lang.Throwable -> L86
                com.google.firebase.concurrent.SequentialExecutor$WorkerRunningState r4 = com.google.firebase.concurrent.SequentialExecutor.WorkerRunningState.IDLE     // Catch: java.lang.Throwable -> L86
                com.google.firebase.concurrent.SequentialExecutor.access$202(r3, r4)     // Catch: java.lang.Throwable -> L86
                monitor-exit(r2)     // Catch: java.lang.Throwable -> L86
                if (r0 == 0) goto L4e
                java.lang.Thread r2 = java.lang.Thread.currentThread()
                r2.interrupt()
            L4e:
                return
            L4f:
                monitor-exit(r2)     // Catch: java.lang.Throwable -> L86
                boolean r2 = java.lang.Thread.interrupted()     // Catch: java.lang.Throwable -> L89
                r0 = r0 | r2
                r2 = 0
                java.lang.Runnable r3 = r8.task     // Catch: java.lang.Throwable -> L5e java.lang.RuntimeException -> L60
                r3.run()     // Catch: java.lang.Throwable -> L5e java.lang.RuntimeException -> L60
                r8.task = r2     // Catch: java.lang.Throwable -> L89
            L5d:
                goto L2
            L5e:
                r3 = move-exception
                goto L82
            L60:
                r3 = move-exception
                java.util.logging.Logger r4 = com.google.firebase.concurrent.SequentialExecutor.access$400()     // Catch: java.lang.Throwable -> L5e
                java.util.logging.Level r5 = java.util.logging.Level.SEVERE     // Catch: java.lang.Throwable -> L5e
                java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L5e
                r6.<init>()     // Catch: java.lang.Throwable -> L5e
                java.lang.String r7 = "Exception while executing runnable "
                java.lang.StringBuilder r6 = r6.append(r7)     // Catch: java.lang.Throwable -> L5e
                java.lang.Runnable r7 = r8.task     // Catch: java.lang.Throwable -> L5e
                java.lang.StringBuilder r6 = r6.append(r7)     // Catch: java.lang.Throwable -> L5e
                java.lang.String r6 = r6.toString()     // Catch: java.lang.Throwable -> L5e
                r4.log(r5, r6, r3)     // Catch: java.lang.Throwable -> L5e
                r8.task = r2     // Catch: java.lang.Throwable -> L89
                goto L5d
            L82:
                r8.task = r2     // Catch: java.lang.Throwable -> L89
                throw r3     // Catch: java.lang.Throwable -> L89
            L86:
                r3 = move-exception
                monitor-exit(r2)     // Catch: java.lang.Throwable -> L86
                throw r3     // Catch: java.lang.Throwable -> L89
            L89:
                r2 = move-exception
                if (r0 == 0) goto L93
                java.lang.Thread r3 = java.lang.Thread.currentThread()
                r3.interrupt()
            L93:
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.concurrent.SequentialExecutor.QueueWorker.workOnQueue():void");
        }

        public String toString() {
            Runnable currentlyRunning = this.task;
            return currentlyRunning != null ? "SequentialExecutorWorker{running=" + currentlyRunning + "}" : "SequentialExecutorWorker{state=" + SequentialExecutor.this.workerRunningState + "}";
        }
    }

    public String toString() {
        return "SequentialExecutor@" + System.identityHashCode(this) + "{" + this.executor + "}";
    }
}
