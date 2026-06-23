package com.google.firebase.database;

import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.interop.InteropAppCheckTokenProvider;
import com.google.firebase.auth.internal.InternalAuthProvider;
import com.google.firebase.database.android.AndroidAppCheckTokenProvider;
import com.google.firebase.database.android.AndroidAuthTokenProvider;
import com.google.firebase.database.core.DatabaseConfig;
import com.google.firebase.database.core.RepoInfo;
import com.google.firebase.database.core.TokenProvider;
import com.google.firebase.inject.Deferred;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
class FirebaseDatabaseComponent {
    private final FirebaseApp app;
    private final TokenProvider appCheckProvider;
    private final TokenProvider authProvider;
    private final Map<RepoInfo, FirebaseDatabase> instances = new HashMap();

    FirebaseDatabaseComponent(FirebaseApp app, Deferred<InternalAuthProvider> authProvider, Deferred<InteropAppCheckTokenProvider> appCheckProvider) {
        this.app = app;
        this.authProvider = new AndroidAuthTokenProvider(authProvider);
        this.appCheckProvider = new AndroidAppCheckTokenProvider(appCheckProvider);
    }

    synchronized FirebaseDatabase get(RepoInfo repo) {
        FirebaseDatabase database;
        database = this.instances.get(repo);
        if (database == null) {
            DatabaseConfig config = new DatabaseConfig();
            if (!this.app.isDefaultApp()) {
                config.setSessionPersistenceKey(this.app.getName());
            }
            config.setFirebaseApp(this.app);
            config.setAuthTokenProvider(this.authProvider);
            config.setAppCheckTokenProvider(this.appCheckProvider);
            database = new FirebaseDatabase(this.app, repo, config);
            this.instances.put(repo, database);
        }
        return database;
    }
}
