package com.google.firebase.database.android;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appcheck.AppCheckTokenResult;
import com.google.firebase.appcheck.interop.AppCheckTokenListener;
import com.google.firebase.appcheck.interop.InteropAppCheckTokenProvider;
import com.google.firebase.database.core.TokenProvider;
import com.google.firebase.inject.Deferred;
import com.google.firebase.inject.Provider;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes.dex */
public class AndroidAppCheckTokenProvider implements TokenProvider {
    private final Deferred<InteropAppCheckTokenProvider> deferredAppCheckProvider;
    private final AtomicReference<InteropAppCheckTokenProvider> internalAppCheck = new AtomicReference<>();

    public AndroidAppCheckTokenProvider(Deferred<InteropAppCheckTokenProvider> deferredAppCheckProvider) {
        this.deferredAppCheckProvider = deferredAppCheckProvider;
        deferredAppCheckProvider.whenAvailable(new Deferred.DeferredHandler() { // from class: com.google.firebase.database.android.AndroidAppCheckTokenProvider$$ExternalSyntheticLambda3
            @Override // com.google.firebase.inject.Deferred.DeferredHandler
            public final void handle(Provider provider) {
                this.f$0.m108lambda$new$0$comgooglefirebasedatabaseandroidAndroidAppCheckTokenProvider(provider);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$new$0$com-google-firebase-database-android-AndroidAppCheckTokenProvider, reason: not valid java name */
    /* synthetic */ void m108lambda$new$0$comgooglefirebasedatabaseandroidAndroidAppCheckTokenProvider(Provider authProvider) {
        this.internalAppCheck.set((InteropAppCheckTokenProvider) authProvider.get());
    }

    @Override // com.google.firebase.database.core.TokenProvider
    public void getToken(boolean forceRefresh, final TokenProvider.GetTokenCompletionListener listener) {
        InteropAppCheckTokenProvider appCheckProvider = this.internalAppCheck.get();
        if (appCheckProvider != null) {
            Task<AppCheckTokenResult> getTokenResult = appCheckProvider.getToken(forceRefresh);
            getTokenResult.addOnSuccessListener(new OnSuccessListener() { // from class: com.google.firebase.database.android.AndroidAppCheckTokenProvider$$ExternalSyntheticLambda0
                @Override // com.google.android.gms.tasks.OnSuccessListener
                public final void onSuccess(Object obj) {
                    listener.onSuccess(((AppCheckTokenResult) obj).getToken());
                }
            }).addOnFailureListener(new OnFailureListener() { // from class: com.google.firebase.database.android.AndroidAppCheckTokenProvider$$ExternalSyntheticLambda1
                @Override // com.google.android.gms.tasks.OnFailureListener
                public final void onFailure(Exception exc) {
                    listener.onError(exc.getMessage());
                }
            });
        } else {
            listener.onSuccess(null);
        }
    }

    @Override // com.google.firebase.database.core.TokenProvider
    public void addTokenChangeListener(final ExecutorService executorService, final TokenProvider.TokenChangeListener tokenListener) {
        this.deferredAppCheckProvider.whenAvailable(new Deferred.DeferredHandler() { // from class: com.google.firebase.database.android.AndroidAppCheckTokenProvider$$ExternalSyntheticLambda4
            @Override // com.google.firebase.inject.Deferred.DeferredHandler
            public final void handle(Provider provider) {
                ((InteropAppCheckTokenProvider) provider.get()).addAppCheckTokenListener(new AppCheckTokenListener() { // from class: com.google.firebase.database.android.AndroidAppCheckTokenProvider$$ExternalSyntheticLambda5
                    @Override // com.google.firebase.appcheck.interop.AppCheckTokenListener
                    public final void onAppCheckTokenChanged(AppCheckTokenResult appCheckTokenResult) {
                        executorService.execute(new Runnable() { // from class: com.google.firebase.database.android.AndroidAppCheckTokenProvider$$ExternalSyntheticLambda2
                            @Override // java.lang.Runnable
                            public final void run() {
                                tokenChangeListener.onTokenChange(appCheckTokenResult.getToken());
                            }
                        });
                    }
                });
            }
        });
    }

    @Override // com.google.firebase.database.core.TokenProvider
    public void removeTokenChangeListener(TokenProvider.TokenChangeListener tokenListener) {
    }
}
