package com.pramod.octapadpromidi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;
import android.media.midi.MidiOutputPort;
import android.media.midi.MidiReceiver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.documentfile.provider.DocumentFile;
import com.pramod.octapadpromidi.AudioEngine;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class MainActivity extends Activity {
    private static final long HIT_BLOCK_MS = 5;
    private static final String KEY_EDIT_MODE = "edit_mode";
    private static final String KEY_KIT_INDEX = "kit_index";
    private static final String KEY_LAST_LIST_FOLDER_URI = "last_list_folder_uri";
    private static final int MAX_KITS = 50;
    private static final int PAD_COUNT = 8;
    private static final String PREF_NAME = "OctapadSettings";
    private static final int REQ_LIST_FOLDER = 2003;
    private static final int REQ_LOAD_FOLDER = 2002;
    private static final int REQ_PICK_SINGLE_WAV = 5001;
    private static final int REQ_SAVE_FOLDER = 2001;
    private View advControlBar;
    private AudioEngine.SampleData assistSoundId;
    private Uri assistSoundUri;
    private AudioEngine audioEngine;
    private Button btnEditMode;
    private Button btnEq;
    private Button btnLoadKit;
    private Button btnLoops;
    private Button btnNextKit;
    private Button btnPrevKit;
    private Button btnRenameKit;
    private Button btnSaveKit;
    private CheckBox chkDelay;
    private View fxControlBar;
    private MidiManager midiManager;
    private MidiOutputPort midiOutputPort;
    private MidiDevice openedMidiDevice;
    private SharedPreferences prefs;
    private SeekBar seekChokeGroup;
    private SeekBar seekDelayLevel;
    private SeekBar seekDelayTime;
    private SeekBar seekEqHigh;
    private SeekBar seekEqLow;
    private SeekBar seekEqMid;
    private SeekBar seekPitch;
    private SeekBar seekVolume;
    private TextView txtKitName;
    private TextView txtSelectedPad;
    private TextView txtMidiStatus;
    private Button[] pads = new Button[8];
    private Uri[] selectedWavUris = new Uri[8];
    private int[] selectedRawResIds = new int[8];
    private float[] padVolume = new float[8];
    private float[] padPitch = new float[8];
    private boolean[] padDelayOn = new boolean[8];
    private float[] padDelayTime = new float[8];
    private float[] padDelayLevel = new float[8];
    private float[] padEqHigh = new float[8];
    private float[] padEqMid = new float[8];
    private float[] padEqLow = new float[8];
    private int[] padChokeGroup = new int[8];
    private int selectedPad = 0;
    private boolean editMode = false;
    private int kitIndex = 1;
    private String currentKitName = "KIT 1";
    private String pendingSaveKitName = null;
    private int copySourcePad = -1;
    private int swapSourcePad = -1;
    private AudioEngine.SampleData[] samples = new AudioEngine.SampleData[8];
    private int[] activePointerId = new int[8];
    private int currentPresetKit = 0;
    private final String[] presetKitNames = new String[25];
    private final int[][] presetKits = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, 25, 8);
    private long[] lastHitTime = new long[8];

    static /* synthetic */ int access$1208(MainActivity x0) {
        int i = x0.kitIndex;
        x0.kitIndex = i + 1;
        return i;
    }

    static /* synthetic */ int access$1210(MainActivity x0) {
        int i = x0.kitIndex;
        x0.kitIndex = i - 1;
        return i;
    }

    private void initPresets() {
        String[] strArr = this.presetKitNames;
        strArr[0] = "Intro Patch";
        strArr[1] = "Dadra Kaharwa";
        strArr[2] = "Duff Patch";
        strArr[3] = "Kaharwa Dadra Manjira";
        strArr[4] = "Deepchandi Patch";
        strArr[5] = "Bhanda Huk Patch";
        strArr[6] = "Disco Patch";
        strArr[7] = "Dholak Manjira Patch";
        int i = 8;
        strArr[8] = "Dhumal Patch";
        strArr[9] = "Gaura Gauri Patch";
        strArr[10] = "Tiger Dhumal Patch";
        strArr[11] = "Groomer Patch";
        strArr[12] = "Dandiya Patch";
        strArr[13] = "CG Patch";
        strArr[14] = "Jasgeet Manjira Patch";
        strArr[15] = "Jasgeet Jhanj Patch";
        strArr[16] = "CG Sambalpuri";
        strArr[17] = "Panthi Patch";
        strArr[18] = "Nagpuri Patch";
        strArr[19] = "Percussion Patch";
        strArr[20] = "Aana N Gori Ab";
        strArr[21] = "Chham Chham Baje Patch";
        strArr[22] = "CG Slow Karma Patch";
        strArr[23] = "CG Karma Patch";
        strArr[24] = "Drum Set Western Patch";
        int i2 = 0;
        while (i2 < 25) {
            String suffix = i2 == 0 ? "" : String.valueOf(i2 + 1);
            int[][] iArr = this.presetKits;
            int[] iArr2 = new int[i];
            iArr2[0] = getResources().getIdentifier("crash" + suffix, "raw", getPackageName());
            iArr2[1] = getResources().getIdentifier("tom" + suffix, "raw", getPackageName());
            iArr2[2] = getResources().getIdentifier("rim" + suffix, "raw", getPackageName());
            iArr2[3] = getResources().getIdentifier("clap" + suffix, "raw", getPackageName());
            iArr2[4] = getResources().getIdentifier("kick" + suffix, "raw", getPackageName());
            iArr2[5] = getResources().getIdentifier("snare" + suffix, "raw", getPackageName());
            iArr2[6] = getResources().getIdentifier("ohat" + suffix, "raw", getPackageName());
            iArr2[7] = getResources().getIdentifier("chat" + suffix, "raw", getPackageName());
            iArr[i2] = iArr2;
            i2++;
            i = 8;
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(5894);
    }

    private void setupMidi() {
        if (Build.VERSION.SDK_INT >= 23) {
            MidiManager midiManager = (MidiManager) getSystemService("midi");
            this.midiManager = midiManager;
            if (midiManager == null) {
                return;
            }
            MidiDeviceInfo[] infos = midiManager.getDevices();
            for (MidiDeviceInfo info : infos) {
                openMidiDevice(info);
            }
            this.midiManager.registerDeviceCallback(new MidiManager.DeviceCallback() { // from class: com.pramod.octapadpromidi.MainActivity.1
                @Override // android.media.midi.MidiManager.DeviceCallback
                public void onDeviceAdded(MidiDeviceInfo device) {
                    MainActivity.this.openMidiDevice(device);
                }

                @Override // android.media.midi.MidiManager.DeviceCallback
                public void onDeviceRemoved(MidiDeviceInfo device) throws IOException {
                    if (MainActivity.this.openedMidiDevice != null && MainActivity.this.openedMidiDevice.getInfo().getId() == device.getId()) {
                        MainActivity.this.closeMidiDevice();
                        ((TextView) MainActivity.this.findViewById(R.id.txtMidiStatus)).setText("MIDI disconnected");
                    }
                }
            }, new Handler(Looper.getMainLooper()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openMidiDevice(MidiDeviceInfo info) {
        if (Build.VERSION.SDK_INT >= 23 && info.getOutputPortCount() > 0) {
            this.midiManager.openDevice(info, new MidiManager.OnDeviceOpenedListener() { // from class: com.pramod.octapadpromidi.MainActivity.2
                @Override // android.media.midi.MidiManager.OnDeviceOpenedListener
                public void onDeviceOpened(MidiDevice device) {
                    MainActivity.this.openedMidiDevice = device;
                    MainActivity.this.midiOutputPort = device.openOutputPort(0);
                    if (MainActivity.this.midiOutputPort != null) {
                        MainActivity.this.txtMidiStatus.setText("MIDI connected");
                        MainActivity.this.midiOutputPort.connect(new MidiReceiver() { // from class: com.pramod.octapadpromidi.MainActivity.2.1
                            @Override // android.media.midi.MidiReceiver
                            public void onSend(byte[] msg, int offset, int count, long timestamp) {
                                int end = offset + count;
                                int status = 0;
                                for (int i = offset; i < end; i++) {
                                    int value = msg[i] & 0xFF;
                                    if (value >= 0x80) {
                                        status = value;
                                        continue;
                                    }
                                    if ((status & 0xF0) == 0x90) {
                                        if (i + 1 >= end) {
                                            break;
                                        }
                                        int note = value;
                                        int velocity = msg[i + 1] & 0xFF;
                                        if (velocity > 0) {
                                            MainActivity.this.handleMidiNoteOn((byte) note, (byte) velocity);
                                        }
                                        i++;
                                        continue;
                                    }
                                    if ((status & 0xF0) == 0x80) {
                                        i++;
                                        continue;
                                    }
                                }
                            }
                        });
                    }
                }
            }, new Handler(Looper.getMainLooper()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeMidiDevice() throws IOException {
        try {
            MidiOutputPort midiOutputPort = this.midiOutputPort;
            if (midiOutputPort != null) {
                midiOutputPort.close();
                this.midiOutputPort = null;
            }
            MidiDevice midiDevice = this.openedMidiDevice;
            if (midiDevice != null) {
                midiDevice.close();
                this.openedMidiDevice = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleMidiNoteOn(byte note, byte velocity) {
        int padIndex = -1;
        switch (note) {
            case 36:
                padIndex = 4;
                break;
            case 37:
                padIndex = 2;
                break;
            case 38:
            case 40:
                padIndex = 5;
                break;
            case 39:
                padIndex = 3;
                break;
            case 42:
            case 44:
                padIndex = 7;
                break;
            case 45:
            case 47:
            case ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE /* 48 */:
            case 50:
                padIndex = 1;
                break;
            case 46:
                padIndex = 6;
                break;
            case ConstraintLayout.LayoutParams.Table.LAYOUT_EDITOR_ABSOLUTEX /* 49 */:
                padIndex = 0;
                break;
        }
        if (padIndex == -1) {
            padIndex = note % 8;
        }
        final int finalPadIndex = padIndex;
        // Trigger audio immediately on the MIDI thread to minimize latency
        playPadSoundImmediate(finalPadIndex);
        // Update UI feedback on the main thread
        runOnUiThread(new Runnable() { // from class: com.pramod.octapadpromidi.MainActivity.3
            @Override // java.lang.Runnable
            public void run() {
                try {
                    MainActivity.this.pads[finalPadIndex].setPressed(true);
                } catch (Exception e) {
                }
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.pramod.octapadpromidi.MainActivity.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            MainActivity.this.pads[finalPadIndex].setPressed(false);
                        } catch (Exception e) {
                        }
                    }
                }, 100L);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    private void playPadSoundImmediate(int index) {
        try {
            AudioEngine.SampleData sampleData = this.samples[index];
            if (sampleData == null || !sampleData.loaded) {
                return;
            }
            this.audioEngine.playSample(index, sampleData, this.padVolume[index], this.padPitch[index], 0, this.padDelayOn[index], this.padDelayTime[index], this.padDelayLevel[index], this.padEqLow[index], this.padEqMid[index], this.padEqHigh[index], this.padChokeGroup[index], 0.0f, 0.0f);
        } catch (Exception e) {
        }
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        hideSystemUI();
        Toast.makeText(this, "Modified build loaded", Toast.LENGTH_SHORT).show();
        initPresets();
        setupMidi();
        getWindow().getDecorView().setSoundEffectsEnabled(false);
        this.prefs = getSharedPreferences(PREF_NAME, 0);
        this.txtKitName = (TextView) findViewById(R.id.txtKitName);
        this.txtSelectedPad = (TextView) findViewById(R.id.txtSelectedPad);
        this.txtMidiStatus = (TextView) findViewById(R.id.txtMidiStatus);
        this.txtMidiStatus.setText("MIDI status: disconnected");
        this.btnEditMode = (Button) findViewById(R.id.btnEditMode);
        this.btnSaveKit = (Button) findViewById(R.id.btnSaveKit);
        this.btnLoadKit = (Button) findViewById(R.id.btnLoadKit);
        this.btnRenameKit = (Button) findViewById(R.id.btnRenameKit);
        this.btnPrevKit = (Button) findViewById(R.id.btnPrevKit);
        this.btnNextKit = (Button) findViewById(R.id.btnNextKit);
        this.btnEq = (Button) findViewById(R.id.btnEq);
        Button button = (Button) findViewById(R.id.btnLoops);
        this.btnLoops = button;
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() { // from class: com.pramod.octapadpromidi.MainActivity.4
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, (Class<?>) LoopsActivity.class);
                    MainActivity.this.startActivity(intent);
                }
            });
        }
        this.seekVolume = (SeekBar) findViewById(R.id.seekVolume);
        this.seekPitch = (SeekBar) findViewById(R.id.seekPitch);
        this.fxControlBar = findViewById(R.id.fxControlBar);
        this.advControlBar = findViewById(R.id.advControlBar);
        this.chkDelay = (CheckBox) findViewById(R.id.chkDelay);
        this.seekDelayTime = (SeekBar) findViewById(R.id.seekDelayTime);
        this.seekDelayLevel = (SeekBar) findViewById(R.id.seekDelayLevel);
        this.seekEqHigh = (SeekBar) findViewById(R.id.seekEqHigh);
        this.seekEqMid = (SeekBar) findViewById(R.id.seekEqMid);
        this.seekEqLow = (SeekBar) findViewById(R.id.seekEqLow);
        this.seekChokeGroup = (SeekBar) findViewById(R.id.seekChokeGroup);
        AudioEngine audioEngine = new AudioEngine(this);
        this.audioEngine = audioEngine;
        audioEngine.start();
        initPads();
        initSeekBars();
        this.editMode = this.prefs.getBoolean(KEY_EDIT_MODE, false);
        int i = this.prefs.getInt(KEY_KIT_INDEX, 1);
        this.kitIndex = i;
        if (i < 1) {
            this.kitIndex = 1;
        }
        loadKitFromMemory(this.kitIndex);
        updateEditButtonUI();
        this.btnEditMode.setOnClickListener(new View.OnClickListener() { // from class: com.pramod.octapadpromidi.MainActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                MainActivity.this.editMode = !r0.editMode;
                if (!MainActivity.this.editMode) {
                    MainActivity.this.copySourcePad = -1;
                    MainActivity.this.swapSourcePad = -1;
                }
                MainActivity.this.updateEditButtonUI();
                MainActivity.this.prefs.edit().putBoolean(MainActivity.KEY_EDIT_MODE, MainActivity.this.editMode).apply();
                MainActivity mainActivity = MainActivity.this;
                mainActivity.saveKitToMemory(mainActivity.kitIndex);
            }
        });
        this.btnRenameKit.setOnClickListener(new View.OnClickListener() { // from class: com.pramod.octapadpromidi.MainActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                MainActivity.this.renameKitDialog();
            }
        });
        this.btnPrevKit.setOnClickListener(new View.OnClickListener() { // from class: com.pramod.octapadpromidi.MainActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (MainActivity.this.kitIndex > 1) {
                    MainActivity mainActivity = MainActivity.this;
                    mainActivity.saveKitToMemory(mainActivity.kitIndex);
                    MainActivity.access$1210(MainActivity.this);
                    MainActivity.this.prefs.edit().putInt(MainActivity.KEY_KIT_INDEX, MainActivity.this.kitIndex).apply();
                    MainActivity mainActivity2 = MainActivity.this;
                    mainActivity2.loadKitFromMemory(mainActivity2.kitIndex);
                    return;
                }
                Toast.makeText(MainActivity.this, "Already First Kit!", 0).show();
            }
        });
        this.btnNextKit.setOnClickListener(new View.OnClickListener() { // from class: com.pramod.octapadpromidi.MainActivity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (MainActivity.this.kitIndex < 50) {
                    MainActivity mainActivity = MainActivity.this;
                    mainActivity.saveKitToMemory(mainActivity.kitIndex);
                    MainActivity.access$1208(MainActivity.this);
                    MainActivity.this.prefs.edit().putInt(MainActivity.KEY_KIT_INDEX, MainActivity.this.kitIndex).apply();
                    MainActivity mainActivity2 = MainActivity.this;
                    mainActivity2.loadKitFromMemory(mainActivity2.kitIndex);
                    return;
                }
                Toast.makeText(MainActivity.this, "Max Kit Limit Reached!", 0).show();
            }
        });
        this.btnLoadKit.setOnClickListener(new View.OnClickListener() { // from class: com.pramod.octapadpromidi.MainActivity.9
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
                intent.addFlags(1);
                intent.addFlags(2);
                intent.addFlags(64);
                MainActivity.this.startActivityForResult(intent, MainActivity.REQ_LOAD_FOLDER);
            }
        });
        this.btnSaveKit.setOnClickListener(new View.OnClickListener() { // from class: com.pramod.octapadpromidi.MainActivity.10
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                MainActivity.this.showSaveKitNameDialog();
            }
        });
        Button button2 = this.btnEq;
        if (button2 != null) {
            button2.setOnClickListener(new View.OnClickListener() { // from class: com.pramod.octapadpromidi.MainActivity.11
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    if (MainActivity.this.fxControlBar != null && MainActivity.this.advControlBar != null) {
                        if (MainActivity.this.fxControlBar.getVisibility() == 0) {
                            MainActivity.this.fxControlBar.setVisibility(8);
                            MainActivity.this.advControlBar.setVisibility(8);
                            MainActivity.this.btnEq.setBackgroundResource(R.drawable.btn_3d_dark);
                        } else {
                            MainActivity.this.fxControlBar.setVisibility(0);
                            MainActivity.this.advControlBar.setVisibility(0);
                            MainActivity.this.btnEq.setBackgroundResource(R.drawable.btn_3d_orange);
                        }
                    }
                }
            });
        }
    }

    private void initPads() {
        int[] padIds = {R.id.pad1, R.id.pad2, R.id.pad3, R.id.pad4, R.id.pad5, R.id.pad6, R.id.pad7, R.id.pad8};
        for (int i = 0; i < 8; i++) {
            this.pads[i] = (Button) findViewById(padIds[i]);
            this.padVolume[i] = 0.8f;
            this.padPitch[i] = 1.0f;
            this.padDelayOn[i] = false;
            this.padDelayTime[i] = 150.0f;
            this.padDelayLevel[i] = 0.5f;
            this.padEqHigh[i] = 0.0f;
            this.padEqMid[i] = 0.0f;
            this.padEqLow[i] = 0.0f;
            this.activePointerId[i] = -1;
            this.lastHitTime[i] = 0;
            this.pads[i].setSoundEffectsEnabled(false);
            this.pads[i].setHapticFeedbackEnabled(false);
            this.pads[i].setClickable(true);
            this.pads[i].setLongClickable(false);
            this.pads[i].setFocusable(false);
            this.pads[i].setFocusableInTouchMode(false);
            this.pads[i].setOnClickListener(null);
            this.pads[i].setOnTouchListener(new PadTouch(i));
        }
    }

    private void initSeekBars() {
        this.seekVolume.setMax(100);
        this.seekPitch.setMax(100);
        this.seekVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.pramod.octapadpromidi.MainActivity.12
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MainActivity.this.padVolume[MainActivity.this.selectedPad] = progress / 100.0f;
                MainActivity mainActivity = MainActivity.this;
                mainActivity.saveKitToMemory(mainActivity.kitIndex);
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar s) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar s) {
            }
        });
        this.seekPitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.pramod.octapadpromidi.MainActivity.13
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MainActivity.this.padPitch[MainActivity.this.selectedPad] = (progress / 100.0f) + 0.5f;
                MainActivity mainActivity = MainActivity.this;
                mainActivity.saveKitToMemory(mainActivity.kitIndex);
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar s) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar s) {
            }
        });
        this.chkDelay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.pramod.octapadpromidi.MainActivity.14
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.this.padDelayOn[MainActivity.this.selectedPad] = isChecked;
                MainActivity mainActivity = MainActivity.this;
                mainActivity.saveKitToMemory(mainActivity.kitIndex);
            }
        });
        this.seekDelayTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.pramod.octapadpromidi.MainActivity.15
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    MainActivity.this.padDelayTime[MainActivity.this.selectedPad] = progress;
                    MainActivity mainActivity = MainActivity.this;
                    mainActivity.saveKitToMemory(mainActivity.kitIndex);
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar s) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar s) {
            }
        });
        this.seekDelayLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.pramod.octapadpromidi.MainActivity.16
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    MainActivity.this.padDelayLevel[MainActivity.this.selectedPad] = progress / 100.0f;
                    MainActivity mainActivity = MainActivity.this;
                    mainActivity.saveKitToMemory(mainActivity.kitIndex);
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar s) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar s) {
            }
        });
        this.seekEqHigh.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.pramod.octapadpromidi.MainActivity.17
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    MainActivity.this.padEqHigh[MainActivity.this.selectedPad] = (progress - 100) * 0.15f;
                    MainActivity mainActivity = MainActivity.this;
                    mainActivity.saveKitToMemory(mainActivity.kitIndex);
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar s) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar s) {
            }
        });
        this.seekEqMid.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.pramod.octapadpromidi.MainActivity.18
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    MainActivity.this.padEqMid[MainActivity.this.selectedPad] = (progress - 100) * 0.15f;
                    MainActivity mainActivity = MainActivity.this;
                    mainActivity.saveKitToMemory(mainActivity.kitIndex);
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar s) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar s) {
            }
        });
        this.seekEqLow.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.pramod.octapadpromidi.MainActivity.19
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    MainActivity.this.padEqLow[MainActivity.this.selectedPad] = (progress - 100) * 0.15f;
                    MainActivity mainActivity = MainActivity.this;
                    mainActivity.saveKitToMemory(mainActivity.kitIndex);
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar s) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar s) {
            }
        });
        this.seekChokeGroup.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.pramod.octapadpromidi.MainActivity.20
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    MainActivity.this.padChokeGroup[MainActivity.this.selectedPad] = progress;
                    MainActivity mainActivity = MainActivity.this;
                    mainActivity.saveKitToMemory(mainActivity.kitIndex);
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar s) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar s) {
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEditButtonUI() {
        this.btnEditMode.setText(this.editMode ? "EDIT ON" : "EDIT OFF");
        this.btnEditMode.setBackgroundResource(this.editMode ? R.drawable.btn_3d_red : R.drawable.btn_3d_dark);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playPadSound(int index) {
        AudioEngine.SampleData sampleData = this.samples[index];
        if (sampleData == null) {
            Toast.makeText(this, "No WAV Selected!", 0).show();
        } else {
            this.audioEngine.playSample(index, sampleData, this.padVolume[index], this.padPitch[index], 0, this.padDelayOn[index], this.padDelayTime[index], this.padDelayLevel[index], this.padEqLow[index], this.padEqMid[index], this.padEqHigh[index], this.padChokeGroup[index], 0.0f, 0.0f);
        }
    }

    private class PadTouch implements View.OnTouchListener {
        int index;

        PadTouch(int i) {
            this.index = i;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getActionMasked();
            int pointerIndex = event.getActionIndex();
            int pointerId = event.getPointerId(pointerIndex);
            if (action != 0 && action != 5) {
                if (action == 1 || action == 6 || action == 3) {
                    if (MainActivity.this.activePointerId[this.index] == pointerId) {
                        MainActivity.this.activePointerId[this.index] = -1;
                        v.setPressed(false);
                    }
                    return true;
                }
                return false;
            }
            if (MainActivity.this.activePointerId[this.index] != -1) {
                return true;
            }
            long now = System.currentTimeMillis();
            if (now - MainActivity.this.lastHitTime[this.index] < MainActivity.HIT_BLOCK_MS) {
                return true;
            }
            MainActivity.this.lastHitTime[this.index] = now;
            MainActivity.this.activePointerId[this.index] = pointerId;
            v.setPressed(true);
            if (!MainActivity.this.editMode) {
                MainActivity.this.playPadSound(this.index);
            }
            MainActivity.this.selectedPad = this.index;
            if (!MainActivity.this.editMode || MainActivity.this.copySourcePad == -1 || MainActivity.this.copySourcePad == this.index) {
                if (!MainActivity.this.editMode || MainActivity.this.swapSourcePad == -1 || MainActivity.this.swapSourcePad == this.index) {
                    if (MainActivity.this.editMode) {
                        MainActivity.this.showEditPadOptions(this.index);
                    }
                    MainActivity.this.txtSelectedPad.setText("Selected: PAD " + (this.index + 1));
                    MainActivity.this.seekVolume.setProgress((int) (MainActivity.this.padVolume[this.index] * 100.0f));
                    MainActivity.this.seekPitch.setProgress((int) ((MainActivity.this.padPitch[this.index] - 0.5f) * 100.0f));
                    MainActivity.this.chkDelay.setChecked(MainActivity.this.padDelayOn[this.index]);
                    MainActivity.this.seekDelayTime.setProgress((int) MainActivity.this.padDelayTime[this.index]);
                    MainActivity.this.seekDelayLevel.setProgress((int) (MainActivity.this.padDelayLevel[this.index] * 100.0f));
                    MainActivity.this.seekEqHigh.setProgress(((int) (MainActivity.this.padEqHigh[this.index] / 0.15f)) + 100);
                    MainActivity.this.seekEqMid.setProgress(((int) (MainActivity.this.padEqMid[this.index] / 0.15f)) + 100);
                    MainActivity.this.seekEqLow.setProgress(((int) (MainActivity.this.padEqLow[this.index] / 0.15f)) + 100);
                    MainActivity.this.seekChokeGroup.setProgress(MainActivity.this.padChokeGroup[this.index]);
                    return true;
                }
                MainActivity mainActivity = MainActivity.this;
                mainActivity.swapPadSound(mainActivity.swapSourcePad, this.index);
                MainActivity.this.swapSourcePad = -1;
                MainActivity mainActivity2 = MainActivity.this;
                mainActivity2.saveKitToMemory(mainActivity2.kitIndex);
                return true;
            }
            MainActivity mainActivity3 = MainActivity.this;
            mainActivity3.copyPadSound(mainActivity3.copySourcePad, this.index);
            MainActivity.this.copySourcePad = -1;
            MainActivity mainActivity4 = MainActivity.this;
            mainActivity4.saveKitToMemory(mainActivity4.kitIndex);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showEditPadOptions(final int padIndex) {
        String copyText = this.copySourcePad == -1 ? "Pad Sound Copy (Select Source)" : "Pad Sound Copy (Paste Mode ON)";
        String swapText = this.swapSourcePad == -1 ? "Pad Sound Exchange (Select First Pad)" : "Pad Sound Exchange (Swap Mode ON)";
        String[] options = {"Pad Select Sound", copyText, swapText, "Clear Pad Sound"};
        new AlertDialog.Builder(this).setTitle("PAD " + (padIndex + 1) + " - EDIT OPTIONS").setItems(options, new DialogInterface.OnClickListener() { // from class: com.pramod.octapadpromidi.MainActivity.21
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                if (which != 0) {
                    if (which == 1) {
                        MainActivity.this.copySourcePad = padIndex;
                        MainActivity.this.swapSourcePad = -1;
                        Toast.makeText(MainActivity.this, "Copy Mode ON: Now tap target PAD to paste", 0).show();
                        return;
                    }
                    if (which == 2) {
                        MainActivity.this.swapSourcePad = padIndex;
                        MainActivity.this.copySourcePad = -1;
                        Toast.makeText(MainActivity.this, "Exchange Mode ON: Now tap second PAD to swap", 0).show();
                        return;
                    }
                    if (which == 3) {
                        MainActivity.this.selectedWavUris[padIndex] = null;
                        MainActivity.this.selectedRawResIds[padIndex] = 0;
                        MainActivity.this.samples[padIndex] = null;
                        MainActivity.this.padVolume[padIndex] = 0.8f;
                        MainActivity.this.padPitch[padIndex] = 1.0f;
                        MainActivity.this.padDelayOn[padIndex] = false;
                        MainActivity.this.padDelayTime[padIndex] = 150.0f;
                        MainActivity.this.padDelayLevel[padIndex] = 0.5f;
                        MainActivity.this.padEqHigh[padIndex] = 0.0f;
                        MainActivity.this.padEqMid[padIndex] = 0.0f;
                        MainActivity.this.padEqLow[padIndex] = 0.0f;
                        MainActivity.this.padChokeGroup[padIndex] = 0;
                        MainActivity mainActivity = MainActivity.this;
                        mainActivity.saveKitToMemory(mainActivity.kitIndex);
                        Toast.makeText(MainActivity.this, "PAD " + (padIndex + 1) + " Cleared!", 0).show();
                        return;
                    }
                    return;
                }
                Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT");
                intent.addCategory("android.intent.category.OPENABLE");
                intent.setType("audio/*");
                intent.addFlags(1);
                intent.addFlags(64);
                MainActivity.this.startActivityForResult(intent, MainActivity.REQ_PICK_SINGLE_WAV);
            }
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void copyPadSound(int fromPad, int toPad) {
        if (fromPad == toPad) {
            return;
        }
        Uri[] uriArr = this.selectedWavUris;
        Uri uri = uriArr[fromPad];
        uriArr[toPad] = uri;
        int[] iArr = this.selectedRawResIds;
        iArr[toPad] = iArr[fromPad];
        float[] fArr = this.padVolume;
        fArr[toPad] = fArr[fromPad];
        float[] fArr2 = this.padPitch;
        fArr2[toPad] = fArr2[fromPad];
        boolean[] zArr = this.padDelayOn;
        zArr[toPad] = zArr[fromPad];
        float[] fArr3 = this.padDelayTime;
        fArr3[toPad] = fArr3[fromPad];
        float[] fArr4 = this.padDelayLevel;
        fArr4[toPad] = fArr4[fromPad];
        float[] fArr5 = this.padEqHigh;
        fArr5[toPad] = fArr5[fromPad];
        float[] fArr6 = this.padEqMid;
        fArr6[toPad] = fArr6[fromPad];
        float[] fArr7 = this.padEqLow;
        fArr7[toPad] = fArr7[fromPad];
        int[] iArr2 = this.padChokeGroup;
        iArr2[toPad] = iArr2[fromPad];
        if (uri != null) {
            this.samples[toPad] = this.audioEngine.loadWavFromUri(toPad, uri);
        } else {
            int i = iArr[toPad];
            if (i != 0) {
                this.samples[toPad] = this.audioEngine.loadRawSound(toPad, i);
            } else {
                this.samples[toPad] = null;
            }
        }
        saveKitToMemory(this.kitIndex);
        Toast.makeText(this, "Copied PAD " + (fromPad + 1) + " -> PAD " + (toPad + 1), 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void swapPadSound(int padA, int padB) {
        if (padA == padB) {
            return;
        }
        Uri[] uriArr = this.selectedWavUris;
        Uri tempUri = uriArr[padA];
        uriArr[padA] = uriArr[padB];
        uriArr[padB] = tempUri;
        int[] iArr = this.selectedRawResIds;
        int tempRaw = iArr[padA];
        iArr[padA] = iArr[padB];
        iArr[padB] = tempRaw;
        float[] fArr = this.padVolume;
        float tempVol = fArr[padA];
        fArr[padA] = fArr[padB];
        fArr[padB] = tempVol;
        float[] fArr2 = this.padPitch;
        float tempPitch = fArr2[padA];
        fArr2[padA] = fArr2[padB];
        fArr2[padB] = tempPitch;
        boolean[] zArr = this.padDelayOn;
        boolean tempDlyOn = zArr[padA];
        zArr[padA] = zArr[padB];
        zArr[padB] = tempDlyOn;
        float[] fArr3 = this.padDelayTime;
        float tempDlyT = fArr3[padA];
        fArr3[padA] = fArr3[padB];
        fArr3[padB] = tempDlyT;
        float[] fArr4 = this.padDelayLevel;
        float tempDlyL = fArr4[padA];
        fArr4[padA] = fArr4[padB];
        fArr4[padB] = tempDlyL;
        float[] fArr5 = this.padEqHigh;
        float tempEqH = fArr5[padA];
        fArr5[padA] = fArr5[padB];
        fArr5[padB] = tempEqH;
        float[] fArr6 = this.padEqMid;
        float tempEqM = fArr6[padA];
        fArr6[padA] = fArr6[padB];
        fArr6[padB] = tempEqM;
        float[] fArr7 = this.padEqLow;
        float tempEqL = fArr7[padA];
        fArr7[padA] = fArr7[padB];
        fArr7[padB] = tempEqL;
        int[] iArr2 = this.padChokeGroup;
        int tempChoke = iArr2[padA];
        iArr2[padA] = iArr2[padB];
        iArr2[padB] = tempChoke;
        Uri uri = uriArr[padA];
        if (uri != null) {
            this.samples[padA] = this.audioEngine.loadWavFromUri(padA, uri);
        } else {
            int i = iArr[padA];
            if (i != 0) {
                this.samples[padA] = this.audioEngine.loadRawSound(padA, i);
            } else {
                this.samples[padA] = null;
            }
        }
        Uri uri2 = this.selectedWavUris[padB];
        if (uri2 != null) {
            this.samples[padB] = this.audioEngine.loadWavFromUri(padB, uri2);
        } else {
            int i2 = this.selectedRawResIds[padB];
            if (i2 != 0) {
                this.samples[padB] = this.audioEngine.loadRawSound(padB, i2);
            } else {
                this.samples[padB] = null;
            }
        }
        saveKitToMemory(this.kitIndex);
        Toast.makeText(this, "Swapped PAD " + (padA + 1) + " <-> PAD " + (padB + 1), 0).show();
    }

    @Override // android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1 || data == null || (uri = data.getData()) == null) {
            return;
        }
        try {
            if (requestCode == REQ_PICK_SINGLE_WAV) {
                int takeFlags = data.getFlags() & 3;
                getContentResolver().takePersistableUriPermission(uri, takeFlags);
                Uri[] uriArr = this.selectedWavUris;
                int i = this.selectedPad;
                uriArr[i] = uri;
                this.samples[i] = this.audioEngine.loadWavFromUri(i, uri);
                AudioEngine.SampleData sampleData = this.samples[this.selectedPad];
                if (sampleData != null) {
                    this.audioEngine.preloadSample(sampleData);
                }
                saveKitToMemory(this.kitIndex);
                Toast.makeText(this, "Sound Loaded & Saved!", 0).show();
                return;
            }
            if (requestCode == REQ_LOAD_FOLDER) {
                getContentResolver().takePersistableUriPermission(uri, 1);
                loadKitFromFolder(uri);
                saveKitToMemory(this.kitIndex);
                return;
            }
            if (requestCode == REQ_SAVE_FOLDER) {
                getContentResolver().takePersistableUriPermission(uri, 3);
                String str = this.pendingSaveKitName;
                if (str != null && str.length() > 0) {
                    String str2 = this.pendingSaveKitName;
                    this.currentKitName = str2;
                    this.txtKitName.setText(str2);
                }
                saveKitToFolder(uri);
                this.pendingSaveKitName = null;
                return;
            }
            if (requestCode == REQ_LIST_FOLDER) {
                getContentResolver().takePersistableUriPermission(uri, 3);
                this.prefs.edit().putString(KEY_LAST_LIST_FOLDER_URI, uri.toString()).apply();
                showKitListDialog(uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Permission Error: " + e.getMessage(), 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSaveKitNameDialog() {
        final EditText edt = new EditText(this);
        edt.setHint("Enter Kit Name");
        edt.setText(this.currentKitName);
        new AlertDialog.Builder(this).setTitle("Save Kit As").setView(edt).setPositiveButton("NEXT", new DialogInterface.OnClickListener() { // from class: com.pramod.octapadpromidi.MainActivity.22
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                String name = edt.getText().toString().trim();
                if (name.length() != 0) {
                    MainActivity.this.pendingSaveKitName = MainActivity.this.sanitizeFileName(name);
                    MainActivity.this.startActivityForResult(new Intent("android.intent.action.OPEN_DOCUMENT_TREE"), MainActivity.REQ_SAVE_FOLDER);
                } else {
                    Toast.makeText(MainActivity.this, "Kit name required!", 0).show();
                }
            }
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void renameKitDialog() {
        final EditText edt = new EditText(this);
        edt.setText(this.currentKitName);
        new AlertDialog.Builder(this).setTitle("Enter Kit Name").setView(edt).setPositiveButton("OK", new DialogInterface.OnClickListener() { // from class: com.pramod.octapadpromidi.MainActivity.23
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface d, int w) {
                MainActivity.this.currentKitName = edt.getText().toString().trim();
                if (MainActivity.this.currentKitName.length() == 0) {
                    MainActivity.this.currentKitName = "KIT " + MainActivity.this.kitIndex;
                }
                MainActivity.this.txtKitName.setText(MainActivity.this.currentKitName);
                MainActivity mainActivity = MainActivity.this;
                mainActivity.saveKitToMemory(mainActivity.kitIndex);
            }
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String sanitizeFileName(String name) {
        return name.replace("/", "_").replace("\\", "_").replace(":", "_").replace("*", "_").replace("?", "_").replace("\"", "_").replace("<", "_").replace(">", "_").replace("|", "_");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveKitToMemory(int kitNo) {
        SharedPreferences.Editor editor = this.prefs.edit();
        editor.putString("kit_name_" + kitNo, this.currentKitName);
        for (int i = 0; i < 8; i++) {
            editor.putFloat("kit_" + kitNo + "_vol_" + i, this.padVolume[i]);
            editor.putFloat("kit_" + kitNo + "_pitch_" + i, this.padPitch[i]);
            editor.putBoolean("kit_" + kitNo + "_dlyon_" + i, this.padDelayOn[i]);
            editor.putFloat("kit_" + kitNo + "_dlyt_" + i, this.padDelayTime[i]);
            editor.putFloat("kit_" + kitNo + "_dlyl_" + i, this.padDelayLevel[i]);
            editor.putFloat("kit_" + kitNo + "_eqh_" + i, this.padEqHigh[i]);
            editor.putFloat("kit_" + kitNo + "_eqm_" + i, this.padEqMid[i]);
            editor.putFloat("kit_" + kitNo + "_eql_" + i, this.padEqLow[i]);
            editor.putInt("kit_" + kitNo + "_choke_" + i, this.padChokeGroup[i]);
            if (this.selectedWavUris[i] != null) {
                editor.putString("kit_" + kitNo + "_uri_" + i, this.selectedWavUris[i].toString());
                editor.remove("kit_" + kitNo + "_raw_" + i);
            } else if (this.selectedRawResIds[i] != 0) {
                editor.remove("kit_" + kitNo + "_uri_" + i);
                editor.putInt("kit_" + kitNo + "_raw_" + i, this.selectedRawResIds[i]);
            } else {
                editor.remove("kit_" + kitNo + "_uri_" + i);
                editor.remove("kit_" + kitNo + "_raw_" + i);
            }
        }
        if (this.assistSoundUri != null) {
            editor.putString("kit_" + kitNo + "_assist_uri", this.assistSoundUri.toString());
        } else {
            editor.remove("kit_" + kitNo + "_assist_uri");
        }
        editor.apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadKitFromMemory(int kitNo) {
        if (kitNo <= this.presetKitNames.length) {
            this.currentPresetKit = kitNo - 1;
            this.currentKitName = this.prefs.getString("kit_name_" + kitNo, this.presetKitNames[this.currentPresetKit]);
        } else {
            this.currentKitName = this.prefs.getString("kit_name_" + kitNo, "KIT " + kitNo);
        }
        this.txtKitName.setText(this.currentKitName);
        for (int i = 0; i < 8; i++) {
            this.padVolume[i] = this.prefs.getFloat("kit_" + kitNo + "_vol_" + i, 0.8f);
            this.padPitch[i] = this.prefs.getFloat("kit_" + kitNo + "_pitch_" + i, 1.0f);
            this.padDelayOn[i] = this.prefs.getBoolean("kit_" + kitNo + "_dlyon_" + i, false);
            this.padDelayTime[i] = this.prefs.getFloat("kit_" + kitNo + "_dlyt_" + i, 150.0f);
            this.padDelayLevel[i] = this.prefs.getFloat("kit_" + kitNo + "_dlyl_" + i, 0.5f);
            this.padEqHigh[i] = this.prefs.getFloat("kit_" + kitNo + "_eqh_" + i, 0.0f);
            this.padEqMid[i] = this.prefs.getFloat("kit_" + kitNo + "_eqm_" + i, 0.0f);
            this.padEqLow[i] = this.prefs.getFloat("kit_" + kitNo + "_eql_" + i, 0.0f);
            this.padChokeGroup[i] = this.prefs.getInt("kit_" + kitNo + "_choke_" + i, 0);
            String uriStr = this.prefs.getString("kit_" + kitNo + "_uri_" + i, null);
            int rawResId = this.prefs.getInt("kit_" + kitNo + "_raw_" + i, 0);
            if (uriStr != null) {
                this.selectedWavUris[i] = Uri.parse(uriStr);
                this.selectedRawResIds[i] = 0;
                this.samples[i] = this.audioEngine.loadWavFromUri(i, this.selectedWavUris[i]);
                AudioEngine.SampleData sampleData = this.samples[i];
                if (sampleData != null) {
                    this.audioEngine.preloadSample(sampleData);
                }
            } else if (rawResId != 0) {
                this.selectedWavUris[i] = null;
                this.selectedRawResIds[i] = rawResId;
                this.samples[i] = this.audioEngine.loadRawSound(i, rawResId);
                AudioEngine.SampleData sampleData2 = this.samples[i];
                if (sampleData2 != null) {
                    this.audioEngine.preloadSample(sampleData2);
                }
            } else {
                this.selectedWavUris[i] = null;
                if (kitNo <= this.presetKitNames.length) {
                    this.selectedRawResIds[i] = this.presetKits[this.currentPresetKit][i];
                } else {
                    this.selectedRawResIds[i] = this.presetKits[0][i];
                }
                this.samples[i] = this.audioEngine.loadRawSound(i, this.selectedRawResIds[i]);
                AudioEngine.SampleData sampleData3 = this.samples[i];
                if (sampleData3 != null) {
                    this.audioEngine.preloadSample(sampleData3);
                }
            }
        }
        String assistUriStr = this.prefs.getString("kit_" + kitNo + "_assist_uri", null);
        if (assistUriStr != null) {
            this.assistSoundUri = Uri.parse(assistUriStr);
        } else {
            this.assistSoundUri = null;
        }
        this.seekVolume.setProgress((int) (this.padVolume[this.selectedPad] * 100.0f));
        this.seekPitch.setProgress((int) ((this.padPitch[this.selectedPad] - 0.5f) * 100.0f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadKitFromFolder(Uri folderUri) throws IOException {
        InputStream is;
        BufferedReader reader;
        StringBuilder sb;
        try {
            DocumentFile kitFolder = DocumentFile.fromTreeUri(this, folderUri);
            if (kitFolder == null) {
                Toast.makeText(this, "Folder not found!", 0).show();
                return;
            }
            for (int i = 0; i < 8; i++) {
                DocumentFile wav = kitFolder.findFile(KitManager.DEFAULT_WAV_NAMES[i]);
                if (wav != null) {
                    this.selectedWavUris[i] = wav.getUri();
                    this.selectedRawResIds[i] = 0;
                    this.samples[i] = this.audioEngine.loadWavFromUri(i, wav.getUri());
                    AudioEngine.SampleData sampleData = this.samples[i];
                    if (sampleData != null) {
                        this.audioEngine.preloadSample(sampleData);
                    }
                } else {
                    this.selectedWavUris[i] = null;
                    int[] iArr = this.selectedRawResIds;
                    int i2 = this.presetKits[this.currentPresetKit][i];
                    iArr[i] = i2;
                    this.samples[i] = this.audioEngine.loadRawSound(i, i2);
                    AudioEngine.SampleData sampleData2 = this.samples[i];
                    if (sampleData2 != null) {
                        this.audioEngine.preloadSample(sampleData2);
                    }
                }
            }
            String folderName = kitFolder.getName();
            if (folderName != null) {
                String strReplace = folderName.replace(".mcn", "");
                this.currentKitName = strReplace;
                this.txtKitName.setText(strReplace);
            }
            DocumentFile dataFile = kitFolder.findFile("kit_data.json");
            if (dataFile != null) {
                try {
                    is = getContentResolver().openInputStream(dataFile.getUri());
                } catch (Exception e) {
                    e = e;
                }
                if (is != null) {
                    BufferedReader reader2 = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb2 = new StringBuilder();
                    while (true) {
                        String line = reader2.readLine();
                        if (line == null) {
                            break;
                        }
                        try {
                            sb2.append(line);
                        } catch (Exception e2) {
                            e = e2;
                        }
                        e.printStackTrace();
                    }
                    is.close();
                    JSONObject jsonData = new JSONObject(sb2.toString());
                    JSONArray volArray = jsonData.optJSONArray("volume");
                    JSONArray pitchArray = jsonData.optJSONArray("pitch");
                    JSONArray dlyOnArray = jsonData.optJSONArray("delayOn");
                    JSONArray dlyTArray = jsonData.optJSONArray("delayTime");
                    JSONArray dlyLArray = jsonData.optJSONArray("delayLevel");
                    JSONArray eqHArray = jsonData.optJSONArray("eqHigh");
                    JSONArray eqMArray = jsonData.optJSONArray("eqMid");
                    JSONArray eqLArray = jsonData.optJSONArray("eqLow");
                    try {
                        JSONArray chokeArray = jsonData.optJSONArray("chokeGroup");
                        int i3 = 0;
                        while (true) {
                            DocumentFile dataFile2 = dataFile;
                            if (i3 >= 8) {
                                break;
                            }
                            if (volArray != null) {
                                try {
                                    reader = reader2;
                                    sb = sb2;
                                    this.padVolume[i3] = (float) volArray.getDouble(i3);
                                } catch (Exception e3) {
                                    e = e3;
                                }
                            } else {
                                reader = reader2;
                                sb = sb2;
                            }
                            if (pitchArray != null) {
                                this.padPitch[i3] = (float) pitchArray.getDouble(i3);
                            }
                            if (dlyOnArray != null) {
                                this.padDelayOn[i3] = dlyOnArray.getBoolean(i3);
                            }
                            if (dlyTArray != null) {
                                this.padDelayTime[i3] = (float) dlyTArray.getDouble(i3);
                            }
                            if (dlyLArray != null) {
                                this.padDelayLevel[i3] = (float) dlyLArray.getDouble(i3);
                            }
                            if (eqHArray != null) {
                                this.padEqHigh[i3] = (float) eqHArray.getDouble(i3);
                            }
                            if (eqMArray != null) {
                                this.padEqMid[i3] = (float) eqMArray.getDouble(i3);
                            }
                            if (eqLArray != null) {
                                this.padEqLow[i3] = (float) eqLArray.getDouble(i3);
                            }
                            if (chokeArray != null) {
                                this.padChokeGroup[i3] = chokeArray.getInt(i3);
                            }
                            i3++;
                            dataFile = dataFile2;
                            reader2 = reader;
                            sb2 = sb;
                        }
                    } catch (Exception e4) {
                        e = e4;
                    }
                }
            }
            this.seekVolume.setProgress((int) (this.padVolume[this.selectedPad] * 100.0f));
            this.seekPitch.setProgress((int) ((this.padPitch[this.selectedPad] - 0.5f) * 100.0f));
            saveKitToMemory(this.kitIndex);
            Toast.makeText(this, "Kit Loaded Successfully!", 0).show();
        } catch (Exception e5) {
            e5.printStackTrace();
            Toast.makeText(this, "Load Error: " + e5.getMessage(), 0).show();
        }
    }

    private void saveKitToFolder(Uri folderUri) throws JSONException, IOException {
        int i;
        try {
            DocumentFile root = DocumentFile.fromTreeUri(this, folderUri);
            if (root == null) {
                Toast.makeText(this, "Folder access error!", 0).show();
                return;
            }
            DocumentFile kitFolder = root.findFile(this.currentKitName + ".mcn");
            if (kitFolder == null) {
                kitFolder = root.createDirectory(this.currentKitName + ".mcn");
            }
            if (kitFolder == null) {
                Toast.makeText(this, "Cannot create kit folder!", 0).show();
                return;
            }
            int i2 = 0;
            while (true) {
                if (i2 >= 8) {
                    break;
                }
                if (this.selectedWavUris[i2] != null || this.selectedRawResIds[i2] != 0) {
                    DocumentFile old = kitFolder.findFile(KitManager.DEFAULT_WAV_NAMES[i2]);
                    if (old != null) {
                        old.delete();
                    }
                    DocumentFile dest = kitFolder.createFile("audio/wav", KitManager.DEFAULT_WAV_NAMES[i2]);
                    if (dest != null) {
                        Uri uri = this.selectedWavUris[i2];
                        if (uri != null) {
                            FileUtil.copyUriToUri(this, uri, dest.getUri());
                        } else {
                            int i3 = this.selectedRawResIds[i2];
                            if (i3 != 0) {
                                FileUtil.copyRawToUri(this, i3, dest.getUri());
                            }
                        }
                    }
                }
                i2++;
            }
            DocumentFile dataFile = kitFolder.findFile("kit_data.json");
            if (dataFile != null) {
                dataFile.delete();
            }
            DocumentFile dataFile2 = kitFolder.createFile("application/json", "kit_data.json");
            if (dataFile2 != null) {
                try {
                    JSONObject jsonData = new JSONObject();
                    JSONArray volArray = new JSONArray();
                    JSONArray pitchArray = new JSONArray();
                    JSONArray dlyOnArray = new JSONArray();
                    JSONArray dlyTArray = new JSONArray();
                    JSONArray dlyLArray = new JSONArray();
                    JSONArray eqHArray = new JSONArray();
                    JSONArray eqMArray = new JSONArray();
                    JSONArray eqLArray = new JSONArray();
                    JSONArray chokeArray = new JSONArray();
                    int i4 = 0;
                    for (i = 8; i4 < i; i = 8) {
                        DocumentFile root2 = root;
                        DocumentFile kitFolder2 = kitFolder;
                        try {
                            volArray.put(this.padVolume[i4]);
                            pitchArray.put(this.padPitch[i4]);
                            dlyOnArray.put(this.padDelayOn[i4]);
                            dlyTArray.put(this.padDelayTime[i4]);
                            dlyLArray.put(this.padDelayLevel[i4]);
                            eqHArray.put(this.padEqHigh[i4]);
                            eqMArray.put(this.padEqMid[i4]);
                            eqLArray.put(this.padEqLow[i4]);
                            chokeArray.put(this.padChokeGroup[i4]);
                            i4++;
                            root = root2;
                            kitFolder = kitFolder2;
                        } catch (Exception e) {
                            e = e;
                            e.printStackTrace();
                            Toast.makeText(this, "Kit Saved: " + this.currentKitName, 0).show();
                        }
                    }
                    jsonData.put("volume", volArray);
                    jsonData.put("pitch", pitchArray);
                    jsonData.put("delayOn", dlyOnArray);
                    jsonData.put("delayTime", dlyTArray);
                    jsonData.put("delayLevel", dlyLArray);
                    jsonData.put("eqHigh", eqHArray);
                    jsonData.put("eqMid", eqMArray);
                    jsonData.put("eqLow", eqLArray);
                    jsonData.put("chokeGroup", chokeArray);
                    OutputStream out = getContentResolver().openOutputStream(dataFile2.getUri());
                    if (out != null) {
                        out.write(jsonData.toString().getBytes());
                        out.close();
                    }
                } catch (Exception e2) {
                    e = e2;
                }
            }
            Toast.makeText(this, "Kit Saved: " + this.currentKitName, 0).show();
        } catch (Exception e3) {
            e3.printStackTrace();
            Toast.makeText(this, "Save Error: " + e3.getMessage(), 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openListFolderPicker() {
        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
        intent.addFlags(1);
        intent.addFlags(2);
        intent.addFlags(64);
        startActivityForResult(intent, REQ_LIST_FOLDER);
    }

    private void scanForMcnFolders(DocumentFile folder, ArrayList<DocumentFile> kitFolders, ArrayList<String> kitNames) {
        String name;
        for (DocumentFile file : folder.listFiles()) {
            if (file != null && (name = file.getName()) != null && file.isDirectory()) {
                if (name.toLowerCase().endsWith(".mcn")) {
                    kitFolders.add(file);
                    kitNames.add(name.substring(0, name.length() - 4));
                } else {
                    scanForMcnFolders(file, kitFolders, kitNames);
                }
            }
        }
    }

    private void showKitListDialog(Uri folderUri) {
        try {
            DocumentFile root = DocumentFile.fromTreeUri(this, folderUri);
            if (root != null && root.exists() && root.isDirectory()) {
                final ArrayList<DocumentFile> kitFolders = new ArrayList<>();
                ArrayList<String> kitNames = new ArrayList<>();
                scanForMcnFolders(root, kitFolders, kitNames);
                if (kitNames.size() == 0) {
                    Toast.makeText(this, "No .mcn kit folders found in this folder!", 0).show();
                    return;
                } else {
                    String[] items = (String[]) kitNames.toArray(new String[0]);
                    new AlertDialog.Builder(this).setTitle("Select Kit").setItems(items, new DialogInterface.OnClickListener() { // from class: com.pramod.octapadpromidi.MainActivity.25
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int which) throws IOException {
                            DocumentFile selectedKitFolder = (DocumentFile) kitFolders.get(which);
                            MainActivity.this.loadKitFromFolder(selectedKitFolder.getUri());
                            MainActivity mainActivity = MainActivity.this;
                            mainActivity.saveKitToMemory(mainActivity.kitIndex);
                        }
                    }).setNeutralButton("Change Folder", new DialogInterface.OnClickListener() { // from class: com.pramod.octapadpromidi.MainActivity.24
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.openListFolderPicker();
                        }
                    }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
                    return;
                }
            }
            Toast.makeText(this, "Invalid folder! Choose again.", 0).show();
            openListFolderPicker();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "List Error: " + e.getMessage(), 0).show();
        }
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        saveKitToMemory(this.kitIndex);
    }

    @Override // android.app.Activity
    protected void onStop() {
        super.onStop();
        saveKitToMemory(this.kitIndex);
    }

    @Override // android.app.Activity
    protected void onDestroy() throws IOException {
        super.onDestroy();
        saveKitToMemory(this.kitIndex);
        closeMidiDevice();
        try {
            AudioEngine audioEngine = this.audioEngine;
            if (audioEngine != null) {
                audioEngine.stop();
                this.audioEngine = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
