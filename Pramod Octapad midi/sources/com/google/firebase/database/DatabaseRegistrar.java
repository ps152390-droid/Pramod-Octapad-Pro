package com.google.firebase.database;

import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.interop.InteropAppCheckTokenProvider;
import com.google.firebase.auth.internal.InternalAuthProvider;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentContainer;
import com.google.firebase.components.ComponentFactory;
import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.components.Dependency;
import com.google.firebase.platforminfo.LibraryVersionComponent;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DatabaseRegistrar implements ComponentRegistrar {
    private static final String LIBRARY_NAME = "fire-rtdb";

    @Override // com.google.firebase.components.ComponentRegistrar
    public List<Component<?>> getComponents() {
        return Arrays.asList(Component.builder(FirebaseDatabaseComponent.class).name(LIBRARY_NAME).add(Dependency.required((Class<?>) FirebaseApp.class)).add(Dependency.deferred((Class<?>) InternalAuthProvider.class)).add(Dependency.deferred((Class<?>) InteropAppCheckTokenProvider.class)).factory(new ComponentFactory() { // from class: com.google.firebase.database.DatabaseRegistrar$$ExternalSyntheticLambda0
            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                return DatabaseRegistrar.lambda$getComponents$0(componentContainer);
            }
        }).build(), LibraryVersionComponent.create(LIBRARY_NAME, BuildConfig.VERSION_NAME));
    }

    static /* synthetic */ FirebaseDatabaseComponent lambda$getComponents$0(ComponentContainer c) {
        return new FirebaseDatabaseComponent((FirebaseApp) c.get(FirebaseApp.class), c.getDeferred(InternalAuthProvider.class), c.getDeferred(InteropAppCheckTokenProvider.class));
    }
}
