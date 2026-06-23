package com.google.firebase;

/* JADX INFO: loaded from: classes.dex */
final class AutoValue_StartupTime extends StartupTime {
    private final long elapsedRealtime;
    private final long epochMillis;
    private final long uptimeMillis;

    AutoValue_StartupTime(long epochMillis, long elapsedRealtime, long uptimeMillis) {
        this.epochMillis = epochMillis;
        this.elapsedRealtime = elapsedRealtime;
        this.uptimeMillis = uptimeMillis;
    }

    @Override // com.google.firebase.StartupTime
    public long getEpochMillis() {
        return this.epochMillis;
    }

    @Override // com.google.firebase.StartupTime
    public long getElapsedRealtime() {
        return this.elapsedRealtime;
    }

    @Override // com.google.firebase.StartupTime
    public long getUptimeMillis() {
        return this.uptimeMillis;
    }

    public String toString() {
        return "StartupTime{epochMillis=" + this.epochMillis + ", elapsedRealtime=" + this.elapsedRealtime + ", uptimeMillis=" + this.uptimeMillis + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof StartupTime)) {
            return false;
        }
        StartupTime that = (StartupTime) o;
        return this.epochMillis == that.getEpochMillis() && this.elapsedRealtime == that.getElapsedRealtime() && this.uptimeMillis == that.getUptimeMillis();
    }

    public int hashCode() {
        int h$ = 1 * 1000003;
        long j = this.epochMillis;
        int h$2 = (h$ ^ ((int) (j ^ (j >>> 32)))) * 1000003;
        long j2 = this.elapsedRealtime;
        long j3 = this.uptimeMillis;
        return ((h$2 ^ ((int) (j2 ^ (j2 >>> 32)))) * 1000003) ^ ((int) (j3 ^ (j3 >>> 32)));
    }
}
