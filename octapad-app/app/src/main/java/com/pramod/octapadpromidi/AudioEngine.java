package com.pramod.octapadpromidi;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes3.dex */
public class AudioEngine {
    private static final int PAD_COUNT = 16;
    private static final String TAG = "AudioEngine";
    private Context context;
    private long nativeHandle;
    private byte[] waveCache;

    public static class SampleData {
        public Uri uri;
        public int soundId = 0;
        public boolean loaded = false;
    }

    private native long nativeCreateAudioEngine();

    private native void nativeDestroyAudioEngine();

    private native void nativeLoadSample(int i, short[] sArr, int i2);

    private native void nativePlaySample(int i, float f, float f2, boolean z, float f3, float f4, float f5, float f6, float f7, int i2, float f8, float f9);

    private native void nativeStopAll();

    private native void nativeStopPad(int i);

    static {
        try {
            System.loadLibrary("oboe_audio_engine");
            Log.i(TAG, "Oboe audio engine library loaded");
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Failed to load Oboe audio engine library", e);
        }
    }

    public AudioEngine(Context ctx) {
        this.nativeHandle = 0L;
        this.context = ctx;
        long jNativeCreateAudioEngine = nativeCreateAudioEngine();
        this.nativeHandle = jNativeCreateAudioEngine;
        if (jNativeCreateAudioEngine != 0) {
            Log.i(TAG, "Audio engine initialized with native Oboe");
        } else {
            Log.e(TAG, "Failed to initialize audio engine");
        }
    }

    public void start() {
    }

    public void stop() {
        if (this.nativeHandle != 0) {
            nativeDestroyAudioEngine();
            this.nativeHandle = 0L;
        }
    }

    public SampleData loadWavFromUri(int padIndex, Uri uri) throws IOException {
        try {
            if (this.nativeHandle == 0) {
                return null;
            }
            if (padIndex >= 0 && padIndex < 16) {
                AssetFileDescriptor afd = this.context.getContentResolver().openAssetFileDescriptor(uri, "r");
                if (afd == null) {
                    return null;
                }
                byte[] wavData = readAssetFileDescriptor(afd);
                afd.close();
                short[] pcmData = decodePcmFromWav(wavData);
                if (pcmData != null && pcmData.length != 0) {
                    SampleData sd = new SampleData();
                    sd.uri = uri;
                    sd.soundId = padIndex;
                    sd.loaded = true;
                    nativeLoadSample(padIndex, pcmData, pcmData.length);
                    Log.i(TAG, "Loaded WAV sample to pad " + padIndex + ": " + pcmData.length + " frames");
                    return sd;
                }
                Log.e(TAG, "Failed to decode PCM from WAV");
                return null;
            }
            Log.e(TAG, "Invalid pad index: " + padIndex);
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Error loading WAV from URI", e);
            return null;
        }
    }

    public SampleData loadRawSound(int padIndex, int resId) throws Resources.NotFoundException, IOException {
        try {
            if (this.nativeHandle == 0) {
                return null;
            }
            if (padIndex >= 0 && padIndex < 16) {
                InputStream is = this.context.getResources().openRawResource(resId);
                byte[] wavData = new byte[is.available()];
                is.read(wavData);
                is.close();
                short[] pcmData = decodePcmFromWav(wavData);
                if (pcmData != null && pcmData.length != 0) {
                    SampleData sd = new SampleData();
                    sd.soundId = resId;
                    sd.loaded = true;
                    nativeLoadSample(padIndex, pcmData, pcmData.length);
                    Log.i(TAG, "Loaded raw sound to pad " + padIndex + ": " + pcmData.length + " frames");
                    return sd;
                }
                Log.e(TAG, "Failed to decode PCM from raw resource");
                return null;
            }
            Log.e(TAG, "Invalid pad index: " + padIndex);
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Error loading raw sound", e);
            return null;
        }
    }

    public void unloadSample(SampleData sample) {
        if (sample != null) {
            sample.soundId = 0;
            sample.loaded = false;
            sample.uri = null;
        }
    }

    public void preloadSample(SampleData sample) {
    }

    public void playSample(int padIndex, SampleData sample, float volume, float pitch, int loopMode, boolean delayOn, float delayMs, float delayLevel, float eqLow, float eqMid, float eqHigh, int chokeGroup, float attackMs, float releaseMs) {
        try {
            if (this.nativeHandle != 0 && sample != null && sample.loaded) {
                float vol = Math.max(0.0f, Math.min(1.0f, volume));
                float rate = Math.max(0.5f, Math.min(2.0f, pitch));
                nativePlaySample(padIndex, vol, rate, delayOn, delayMs, delayLevel, eqLow, eqMid, eqHigh, chokeGroup, attackMs, releaseMs);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error playing sample", e);
        }
    }

    public void playSample(int padIndex, SampleData sample, float volume, float pitch, int loopMode) {
        playSample(padIndex, sample, volume, pitch, loopMode, false, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0, 0.0f, 0.0f);
    }

    public void stopPad(int padIndex) {
        if (this.nativeHandle != 0) {
            nativeStopPad(padIndex);
        }
    }

    public void stopAll() {
        if (this.nativeHandle != 0) {
            nativeStopAll();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:47:0x0108, code lost:
    
        if (r6 == 1) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x010a, code lost:
    
        android.util.Log.e(com.pramod.octapadpromidi.AudioEngine.TAG, "Unsupported WAV format: " + r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0120, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0121, code lost:
    
        if (r8 == 16) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0123, code lost:
    
        android.util.Log.e(com.pramod.octapadpromidi.AudioEngine.TAG, "Only 16-bit WAV is supported, found: " + r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0139, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x013a, code lost:
    
        if (r7 > 0) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x013c, code lost:
    
        android.util.Log.e(com.pramod.octapadpromidi.AudioEngine.TAG, "Invalid channel count: " + r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0152, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0153, code lost:
    
        if (r9 < 0) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0155, code lost:
    
        if (r10 > 0) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0158, code lost:
    
        r0 = r10 / (r7 * 2);
        r11 = new short[r0];
        r12 = r9;
        r13 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0160, code lost:
    
        if (r13 >= r0) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0162, code lost:
    
        r14 = 0;
        r15 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x0164, code lost:
    
        if (r15 >= r7) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0166, code lost:
    
        r3 = (r18[r12] & 255) | ((r18[r12 + 1] & 255) << 8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0177, code lost:
    
        if (r3 <= 32767) goto L89;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0179, code lost:
    
        r3 = r3 - 65536;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x017c, code lost:
    
        r14 = r14 + r3;
        r12 = r12 + 2;
        r15 = r15 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0186, code lost:
    
        r11[r13] = (short) (r14 / r7);
        r13 = r13 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0193, code lost:
    
        return r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x0194, code lost:
    
        android.util.Log.e(com.pramod.octapadpromidi.AudioEngine.TAG, "WAV data chunk not found");
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x019a, code lost:
    
        return null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private short[] decodePcmFromWav(byte[] r18) {
        /*
            Method dump skipped, instructions count: 433
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.pramod.octapadpromidi.AudioEngine.decodePcmFromWav(byte[]):short[]");
    }

    private byte[] readAssetFileDescriptor(AssetFileDescriptor afd) throws Exception {
        byte[] data = new byte[(int) afd.getLength()];
        InputStream is = afd.createInputStream();
        is.read(data);
        is.close();
        return data;
    }
}
