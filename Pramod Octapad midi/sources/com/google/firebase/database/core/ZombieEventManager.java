package com.google.firebase.database.core;

import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.core.view.QuerySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ZombieEventManager implements EventRegistrationZombieListener {
    private static ZombieEventManager defaultInstance = new ZombieEventManager();
    final HashMap<EventRegistration, List<EventRegistration>> globalEventRegistrations = new HashMap<>();

    private ZombieEventManager() {
    }

    public static ZombieEventManager getInstance() {
        return defaultInstance;
    }

    public void recordEventRegistration(EventRegistration registration) {
        synchronized (this.globalEventRegistrations) {
            List<EventRegistration> registrationList = this.globalEventRegistrations.get(registration);
            if (registrationList == null) {
                registrationList = new ArrayList();
                this.globalEventRegistrations.put(registration, registrationList);
            }
            registrationList.add(registration);
            if (!registration.getQuerySpec().isDefault()) {
                EventRegistration defaultRegistration = registration.clone(QuerySpec.defaultQueryAtPath(registration.getQuerySpec().getPath()));
                List<EventRegistration> registrationList2 = this.globalEventRegistrations.get(defaultRegistration);
                if (registrationList2 == null) {
                    registrationList2 = new ArrayList();
                    this.globalEventRegistrations.put(defaultRegistration, registrationList2);
                }
                registrationList2.add(registration);
            }
            registration.setIsUserInitiated(true);
            registration.setOnZombied(this);
        }
    }

    private void unRecordEventRegistration(EventRegistration zombiedRegistration) {
        EventRegistration defaultRegistration;
        List<EventRegistration> registrationList;
        synchronized (this.globalEventRegistrations) {
            boolean found = false;
            List<EventRegistration> registrationList2 = this.globalEventRegistrations.get(zombiedRegistration);
            if (registrationList2 != null) {
                int i = 0;
                while (true) {
                    if (i >= registrationList2.size()) {
                        break;
                    }
                    if (registrationList2.get(i) != zombiedRegistration) {
                        i++;
                    } else {
                        found = true;
                        registrationList2.remove(i);
                        break;
                    }
                }
                if (registrationList2.isEmpty()) {
                    this.globalEventRegistrations.remove(zombiedRegistration);
                }
            }
            Utilities.hardAssert(found || !zombiedRegistration.isUserInitiated());
            if (!zombiedRegistration.getQuerySpec().isDefault() && (registrationList = this.globalEventRegistrations.get((defaultRegistration = zombiedRegistration.clone(QuerySpec.defaultQueryAtPath(zombiedRegistration.getQuerySpec().getPath()))))) != null) {
                int i2 = 0;
                while (true) {
                    if (i2 >= registrationList.size()) {
                        break;
                    }
                    if (registrationList.get(i2) != zombiedRegistration) {
                        i2++;
                    } else {
                        registrationList.remove(i2);
                        break;
                    }
                }
                if (registrationList.isEmpty()) {
                    this.globalEventRegistrations.remove(defaultRegistration);
                }
            }
        }
    }

    public void zombifyForRemove(EventRegistration registration) {
        synchronized (this.globalEventRegistrations) {
            List<EventRegistration> registrationList = this.globalEventRegistrations.get(registration);
            if (registrationList != null && !registrationList.isEmpty()) {
                if (registration.getQuerySpec().isDefault()) {
                    HashSet<QuerySpec> zombiedQueries = new HashSet<>();
                    for (int i = registrationList.size() - 1; i >= 0; i--) {
                        EventRegistration currentRegistration = registrationList.get(i);
                        if (!zombiedQueries.contains(currentRegistration.getQuerySpec())) {
                            zombiedQueries.add(currentRegistration.getQuerySpec());
                            currentRegistration.zombify();
                        }
                    }
                } else {
                    registrationList.get(0).zombify();
                }
            }
        }
    }

    @Override // com.google.firebase.database.core.EventRegistrationZombieListener
    public void onZombied(EventRegistration zombiedInstance) {
        unRecordEventRegistration(zombiedInstance);
    }
}
