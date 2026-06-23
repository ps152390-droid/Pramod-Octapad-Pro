package com.google.firebase.heartbeatinfo;

import android.content.Context;
import android.util.Base64OutputStream;
import androidx.core.os.UserManagerCompat;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.annotations.concurrent.Background;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentContainer;
import com.google.firebase.components.ComponentFactory;
import com.google.firebase.components.Dependency;
import com.google.firebase.components.Qualified;
import com.google.firebase.heartbeatinfo.HeartBeatInfo;
import com.google.firebase.inject.Provider;
import com.google.firebase.platforminfo.UserAgentPublisher;
import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.zip.GZIPOutputStream;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class DefaultHeartBeatController implements HeartBeatController, HeartBeatInfo {
    private final Context applicationContext;
    private final Executor backgroundExecutor;
    private final Set<HeartBeatConsumer> consumers;
    private final Provider<HeartBeatInfoStorage> storageProvider;
    private final Provider<UserAgentPublisher> userAgentProvider;

    public Task<Void> registerHeartBeat() {
        if (this.consumers.size() <= 0) {
            return Tasks.forResult(null);
        }
        boolean inDirectBoot = !UserManagerCompat.isUserUnlocked(this.applicationContext);
        if (inDirectBoot) {
            return Tasks.forResult(null);
        }
        return Tasks.call(this.backgroundExecutor, new Callable() { // from class: com.google.firebase.heartbeatinfo.DefaultHeartBeatController$$ExternalSyntheticLambda1
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return this.f$0.m125lambda$registerHeartBeat$0$comgooglefirebaseheartbeatinfoDefaultHeartBeatController();
            }
        });
    }

    /* JADX INFO: renamed from: lambda$registerHeartBeat$0$com-google-firebase-heartbeatinfo-DefaultHeartBeatController, reason: not valid java name */
    /* synthetic */ Void m125lambda$registerHeartBeat$0$comgooglefirebaseheartbeatinfoDefaultHeartBeatController() throws Exception {
        synchronized (this) {
            this.storageProvider.get().storeHeartBeat(System.currentTimeMillis(), this.userAgentProvider.get().getUserAgent());
        }
        return null;
    }

    @Override // com.google.firebase.heartbeatinfo.HeartBeatController
    public Task<String> getHeartBeatsHeader() {
        boolean inDirectBoot = !UserManagerCompat.isUserUnlocked(this.applicationContext);
        if (inDirectBoot) {
            return Tasks.forResult("");
        }
        return Tasks.call(this.backgroundExecutor, new Callable() { // from class: com.google.firebase.heartbeatinfo.DefaultHeartBeatController$$ExternalSyntheticLambda0
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return this.f$0.m124lambda$getHeartBeatsHeader$1$comgooglefirebaseheartbeatinfoDefaultHeartBeatController();
            }
        });
    }

    /* JADX INFO: renamed from: lambda$getHeartBeatsHeader$1$com-google-firebase-heartbeatinfo-DefaultHeartBeatController, reason: not valid java name */
    /* synthetic */ String m124lambda$getHeartBeatsHeader$1$comgooglefirebaseheartbeatinfoDefaultHeartBeatController() throws Exception {
        String string;
        synchronized (this) {
            HeartBeatInfoStorage storage = this.storageProvider.get();
            List<HeartBeatResult> allHeartBeats = storage.getAllHeartBeats();
            storage.deleteAllHeartBeats();
            JSONArray array = new JSONArray();
            for (int i = 0; i < allHeartBeats.size(); i++) {
                HeartBeatResult result = allHeartBeats.get(i);
                JSONObject obj = new JSONObject();
                obj.put("agent", result.getUserAgent());
                obj.put("dates", new JSONArray((Collection) result.getUsedDates()));
                array.put(obj);
            }
            JSONObject output = new JSONObject();
            output.put("heartbeats", array);
            output.put("version", "2");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Base64OutputStream b64os = new Base64OutputStream(out, 11);
            try {
                GZIPOutputStream gzip = new GZIPOutputStream(b64os);
                try {
                    gzip.write(output.toString().getBytes("UTF-8"));
                    gzip.close();
                    b64os.close();
                    string = out.toString("UTF-8");
                } finally {
                }
            } finally {
            }
        }
        return string;
    }

    private DefaultHeartBeatController(final Context context, final String persistenceKey, Set<HeartBeatConsumer> consumers, Provider<UserAgentPublisher> userAgentProvider, Executor backgroundExecutor) {
        this((Provider<HeartBeatInfoStorage>) new Provider() { // from class: com.google.firebase.heartbeatinfo.DefaultHeartBeatController$$ExternalSyntheticLambda3
            @Override // com.google.firebase.inject.Provider
            public final Object get() {
                return DefaultHeartBeatController.lambda$new$2(context, persistenceKey);
            }
        }, consumers, backgroundExecutor, userAgentProvider, context);
    }

    static /* synthetic */ HeartBeatInfoStorage lambda$new$2(Context context, String persistenceKey) {
        return new HeartBeatInfoStorage(context, persistenceKey);
    }

    DefaultHeartBeatController(Provider<HeartBeatInfoStorage> testStorage, Set<HeartBeatConsumer> consumers, Executor executor, Provider<UserAgentPublisher> userAgentProvider, Context context) {
        this.storageProvider = testStorage;
        this.consumers = consumers;
        this.backgroundExecutor = executor;
        this.userAgentProvider = userAgentProvider;
        this.applicationContext = context;
    }

    public static Component<DefaultHeartBeatController> component() {
        final Qualified<Executor> backgroundExecutor = Qualified.qualified(Background.class, Executor.class);
        return Component.builder(DefaultHeartBeatController.class, HeartBeatController.class, HeartBeatInfo.class).add(Dependency.required((Class<?>) Context.class)).add(Dependency.required((Class<?>) FirebaseApp.class)).add(Dependency.setOf((Class<?>) HeartBeatConsumer.class)).add(Dependency.requiredProvider((Class<?>) UserAgentPublisher.class)).add(Dependency.required(backgroundExecutor)).factory(new ComponentFactory() { // from class: com.google.firebase.heartbeatinfo.DefaultHeartBeatController$$ExternalSyntheticLambda2
            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                return DefaultHeartBeatController.lambda$component$3(backgroundExecutor, componentContainer);
            }
        }).build();
    }

    static /* synthetic */ DefaultHeartBeatController lambda$component$3(Qualified backgroundExecutor, ComponentContainer c) {
        return new DefaultHeartBeatController((Context) c.get(Context.class), ((FirebaseApp) c.get(FirebaseApp.class)).getPersistenceKey(), (Set<HeartBeatConsumer>) c.setOf(HeartBeatConsumer.class), (Provider<UserAgentPublisher>) c.getProvider(UserAgentPublisher.class), (Executor) c.get(backgroundExecutor));
    }

    @Override // com.google.firebase.heartbeatinfo.HeartBeatInfo
    public synchronized HeartBeatInfo.HeartBeat getHeartBeatCode(String heartBeatTag) {
        long presentTime = System.currentTimeMillis();
        HeartBeatInfoStorage storage = this.storageProvider.get();
        boolean shouldSendGlobalHB = storage.shouldSendGlobalHeartBeat(presentTime);
        if (shouldSendGlobalHB) {
            storage.postHeartBeatCleanUp();
            return HeartBeatInfo.HeartBeat.GLOBAL;
        }
        return HeartBeatInfo.HeartBeat.NONE;
    }
}
