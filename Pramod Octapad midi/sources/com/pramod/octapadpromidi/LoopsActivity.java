package com.pramod.octapadpromidi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.media.audiofx.PresetReverb;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class LoopsActivity extends Activity {
    private static final String KEY_LOOP_INDEX = "current_loop_index";
    private static final int LOOP_PAD_COUNT = 8;
    private static final int MAX_LOOPS = 50;
    private static final String PREF_NAME = "OctapadSettings";
    private static final int REQ_LOAD_LOOP_FOLDER = 6003;
    private static final int REQ_PICK_LOOP_WAV = 6001;
    private static final int REQ_SAVE_LOOP_FOLDER = 6002;
    private View advancedControlPanel;
    private Button btnAdvancedLoops;
    private Button btnBack;
    private Button btnEditLoops;
    private Button btnLoadLoop;
    private Button btnNextLoop;
    private Button btnPrevLoop;
    private Button btnRenameLoop;
    private Button btnSaveLoop;
    private Button btnSetBpm;
    private Button btnTapTempo;
    private Button btnTempoMinus;
    private Button btnTempoPlus;
    private CheckBox chkMultiMode;
    private CheckBox chkOneShotMode;
    private EditText editCustomBpm;
    private PresetReverb globalReverb;
    private MidiManager midiManager;
    private MidiOutputPort midiOutputPort;
    private MidiDevice openedMidiDevice;
    private SharedPreferences prefs;
    private SeekBar seekLoopReverb;
    private SeekBar seekMasterVolume;
    private SeekBar seekPitch;
    private SeekBar seekTempo;
    private TextView txtLoopChannel;
    private TextView txtLoopReverbVal;
    private TextView txtLoopStatus;
    private TextView txtMidiStatus;
    private TextView txtMasterVolVal;
    private TextView txtPitchVal;
    private TextView txtTempoVal;
    private Button[] loopPads = new Button[8];
    private String currentLoopName = "LOOP 1";
    private String pendingSaveLoopName = null;
    private int loopChannelIndex = 1;
    private float currentSpeed = 1.0f;
    private float currentPitch = 1.0f;
    private float masterVolume = 1.0f;
    private int reverbLevel = 0;
    private boolean isMultiMode = false;
    private boolean isOneShotMode = false;
    private long[] tapTimes = new long[4];
    private int tapIndex = 0;
    private boolean editMode = false;
    private int selectedPad = 0;
    private Uri[] loopUris = new Uri[8];
    private MediaPlayer[] mediaPlayers = new MediaPlayer[8];

    static /* synthetic */ int access$508(LoopsActivity x0) {
        int i = x0.loopChannelIndex;
        x0.loopChannelIndex = i + 1;
        return i;
    }

    static /* synthetic */ int access$510(LoopsActivity x0) {
        int i = x0.loopChannelIndex;
        x0.loopChannelIndex = i - 1;
        return i;
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) throws IllegalStateException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loops);
        hideSystemUI();
        setupMidi();
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, 0);
        this.prefs = sharedPreferences;
        this.loopChannelIndex = sharedPreferences.getInt(KEY_LOOP_INDEX, 1);
        this.btnBack = (Button) findViewById(R.id.btnBack);
        this.btnEditLoops = (Button) findViewById(R.id.btnEditLoops);
        this.btnAdvancedLoops = (Button) findViewById(R.id.btnAdvancedLoops);
        this.advancedControlPanel = findViewById(R.id.advancedControlPanel);
        this.txtLoopStatus = (TextView) findViewById(R.id.txtLoopStatus);
        this.txtMidiStatus = (TextView) findViewById(R.id.txtMidiStatus);
        if (this.txtMidiStatus != null) {
            this.txtMidiStatus.setText("MIDI status: disconnected");
        }
        this.btnPrevLoop = (Button) findViewById(R.id.btnPrevLoop);
        this.btnNextLoop = (Button) findViewById(R.id.btnNextLoop);
        this.txtLoopChannel = (TextView) findViewById(R.id.txtLoopChannel);
        this.btnRenameLoop = (Button) findViewById(R.id.btnRenameLoop);
        this.btnSaveLoop = (Button) findViewById(R.id.btnSaveLoop);
        this.btnLoadLoop = (Button) findViewById(R.id.btnLoadLoop);
        this.btnTempoMinus = (Button) findViewById(R.id.btnTempoMinus);
        this.btnTempoPlus = (Button) findViewById(R.id.btnTempoPlus);
        this.seekTempo = (SeekBar) findViewById(R.id.seekTempo);
        this.seekPitch = (SeekBar) findViewById(R.id.seekPitch);
        this.txtTempoVal = (TextView) findViewById(R.id.txtTempoVal);
        this.txtPitchVal = (TextView) findViewById(R.id.txtPitchVal);
        this.editCustomBpm = (EditText) findViewById(R.id.editCustomBpm);
        this.btnSetBpm = (Button) findViewById(R.id.btnSetBpm);
        this.seekMasterVolume = (SeekBar) findViewById(R.id.seekMasterVolume);
        this.seekLoopReverb = (SeekBar) findViewById(R.id.seekLoopReverb);
        this.txtMasterVolVal = (TextView) findViewById(R.id.txtMasterVolVal);
        this.txtLoopReverbVal = (TextView) findViewById(R.id.txtLoopReverbVal);
        this.chkMultiMode = (CheckBox) findViewById(R.id.chkMultiMode);
        this.chkOneShotMode = (CheckBox) findViewById(R.id.chkOneShotMode);
        this.btnTapTempo = (Button) findViewById(R.id.btnTapTempo);
        String string = this.prefs.getString("loop_name_ch_" + this.loopChannelIndex, "LOOP " + this.loopChannelIndex);
        this.currentLoopName = string;
        this.txtLoopChannel.setText(string);
        this.masterVolume = this.prefs.getFloat("loop_master_volume", 1.0f);
        this.reverbLevel = this.prefs.getInt("loop_reverb_level", 0);
        this.isMultiMode = this.prefs.getBoolean("loop_multi_mode", false);
        this.isOneShotMode = this.prefs.getBoolean("loop_one_shot_mode", false);
        SeekBar seekBar = this.seekMasterVolume;
        if (seekBar != null) {
            seekBar.setProgress((int) (this.masterVolume * 100.0f));
        }
        SeekBar seekBar2 = this.seekLoopReverb;
        if (seekBar2 != null) {
            seekBar2.setProgress(this.reverbLevel);
        }
        CheckBox checkBox = this.chkMultiMode;
        if (checkBox != null) {
            checkBox.setChecked(this.isMultiMode);
        }
        CheckBox checkBox2 = this.chkOneShotMode;
        if (checkBox2 != null) {
            checkBox2.setChecked(this.isOneShotMode);
        }
        setupReverb();
        setupControls();
        initPads();
        loadLoopsFromMemory();
        this.btnBack.setOnClickListener(new View.OnClickListener() { // from class: com.pramod.octapadpromidi.LoopsActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                LoopsActivity.this.finish();
            }
        });
        this.btnEditLoops.setOnClickListener(new View.OnClickListener() { // from class: com.pramod.octapadpromidi.LoopsActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                LoopsActivity.this.editMode = !r0.editMode;
                LoopsActivity.this.btnEditLoops.setText(LoopsActivity.this.editMode ? "EDIT ON" : "EDIT OFF");
                LoopsActivity.this.btnEditLoops.setBackgroundResource(LoopsActivity.this.editMode ? R.drawable.btn_3d_red : R.drawable.btn_3d_dark);
            }
        });
        Button button = this.btnAdvancedLoops;
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() { // from class: com.pramod.octapadpromidi.LoopsActivity.3
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    if (LoopsActivity.this.advancedControlPanel != null) {
                        if (LoopsActivity.this.advancedControlPanel.getVisibility() == 0) {
                            LoopsActivity.this.advancedControlPanel.setVisibility(8);
                            LoopsActivity.this.btnAdvancedLoops.setBackgroundResource(R.drawable.btn_3d_dark);
                        } else {
                            LoopsActivity.this.advancedControlPanel.setVisibility(0);
                            LoopsActivity.this.btnAdvancedLoops.setBackgroundResource(R.drawable.btn_3d_orange);
                        }
                    }
                }
            });
        }
        Button button2 = this.btnTapTempo;
        if (button2 != null) {
            button2.setOnClickListener(new View.OnClickListener() { // from class: com.pramod.octapadpromidi.LoopsActivity.4
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    LoopsActivity.this.handleTapTempo();
                }
            });
        }
        this.btnPrevLoop.setOnClickListener(new View.OnClickListener() { // from class: com.pramod.octapadpromidi.LoopsActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) throws IllegalStateException {
                if (LoopsActivity.this.loopChannelIndex > 1) {
                    LoopsActivity.this.saveLoopsToMemory();
                    LoopsActivity.access$510(LoopsActivity.this);
                    LoopsActivity.this.prefs.edit().putInt(LoopsActivity.KEY_LOOP_INDEX, LoopsActivity.this.loopChannelIndex).apply();
                    LoopsActivity loopsActivity = LoopsActivity.this;
                    loopsActivity.currentLoopName = loopsActivity.prefs.getString("loop_name_ch_" + LoopsActivity.this.loopChannelIndex, "LOOP " + LoopsActivity.this.loopChannelIndex);
                    LoopsActivity.this.txtLoopChannel.setText(LoopsActivity.this.currentLoopName);
                    LoopsActivity.this.loadLoopsFromMemory();
                    return;
                }
                Toast.makeText(LoopsActivity.this, "Already First Loop Channel!", 0).show();
            }
        });
        this.btnNextLoop.setOnClickListener(new View.OnClickListener() { // from class: com.pramod.octapadpromidi.LoopsActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) throws IllegalStateException {
                if (LoopsActivity.this.loopChannelIndex < 50) {
                    LoopsActivity.this.saveLoopsToMemory();
                    LoopsActivity.access$508(LoopsActivity.this);
                    LoopsActivity.this.prefs.edit().putInt(LoopsActivity.KEY_LOOP_INDEX, LoopsActivity.this.loopChannelIndex).apply();
                    LoopsActivity loopsActivity = LoopsActivity.this;
                    loopsActivity.currentLoopName = loopsActivity.prefs.getString("loop_name_ch_" + LoopsActivity.this.loopChannelIndex, "LOOP " + LoopsActivity.this.loopChannelIndex);
                    LoopsActivity.this.txtLoopChannel.setText(LoopsActivity.this.currentLoopName);
                    LoopsActivity.this.loadLoopsFromMemory();
                    return;
                }
                Toast.makeText(LoopsActivity.this, "Max Loop Channel Reached!", 0).show();
            }
        });
        Button button3 = this.btnRenameLoop;
        if (button3 != null) {
            button3.setOnClickListener(new View.OnClickListener() { // from class: com.pramod.octapadpromidi.LoopsActivity.7
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    LoopsActivity.this.renameLoopDialog();
                }
            });
        }
        Button button4 = this.btnSaveLoop;
        if (button4 != null) {
            button4.setOnClickListener(new View.OnClickListener() { // from class: com.pramod.octapadpromidi.LoopsActivity.8
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    LoopsActivity.this.showSaveLoopNameDialog();
                }
            });
        }
        Button button5 = this.btnLoadLoop;
        if (button5 != null) {
            button5.setOnClickListener(new View.OnClickListener() { // from class: com.pramod.octapadpromidi.LoopsActivity.9
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
                    intent.addFlags(1);
                    LoopsActivity.this.startActivityForResult(intent, LoopsActivity.REQ_LOAD_LOOP_FOLDER);
                }
            });
        }
        this.btnTempoMinus.setOnClickListener(new View.OnClickListener() { // from class: com.pramod.octapadpromidi.LoopsActivity.10
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                int progress = LoopsActivity.this.seekTempo.getProgress();
                if (progress > 0) {
                    LoopsActivity.this.seekTempo.setProgress(progress - 1);
                }
            }
        });
        this.btnTempoPlus.setOnClickListener(new View.OnClickListener() { // from class: com.pramod.octapadpromidi.LoopsActivity.11
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                int progress = LoopsActivity.this.seekTempo.getProgress();
                if (progress < LoopsActivity.this.seekTempo.getMax()) {
                    LoopsActivity.this.seekTempo.setProgress(progress + 1);
                }
            }
        });
        Button button6 = this.btnSetBpm;
        if (button6 != null && this.editCustomBpm != null) {
            button6.setOnClickListener(new View.OnClickListener() { // from class: com.pramod.octapadpromidi.LoopsActivity.12
                @Override // android.view.View.OnClickListener
                public void onClick(View v) throws NumberFormatException {
                    String bpmText = LoopsActivity.this.editCustomBpm.getText().toString();
                    if (!bpmText.isEmpty()) {
                        try {
                            float bpm = Float.parseFloat(bpmText);
                            float speed = bpm / 120.0f;
                            float speed2 = Math.max(0.1f, Math.min(2.0f, speed));
                            if (LoopsActivity.this.seekTempo != null) {
                                LoopsActivity.this.seekTempo.setProgress((int) (100.0f * speed2));
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(LoopsActivity.this, "Invalid BPM", 0).show();
                        }
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleTapTempo() {
        long now = System.currentTimeMillis();
        long[] jArr = this.tapTimes;
        int i = this.tapIndex;
        jArr[i] = now;
        this.tapIndex = (i + 1) % 4;
        int validTaps = 0;
        long totalDelta = 0;
        for (int i2 = 0; i2 < 3; i2++) {
            int i3 = this.tapIndex;
            int current = (((i3 - 1) - i2) + 4) % 4;
            int previous = (((i3 - 2) - i2) + 4) % 4;
            long[] jArr2 = this.tapTimes;
            long delta = jArr2[current] - jArr2[previous];
            if (delta <= 250 || delta >= 2000) {
                if (delta != 0) {
                    break;
                }
            } else {
                totalDelta += delta;
                validTaps++;
            }
        }
        if (validTaps > 0) {
            long avgDelta = totalDelta / validTaps;
            float bpm = 60000.0f / avgDelta;
            float speed = bpm / 120.0f;
            float speed2 = Math.max(0.1f, Math.min(2.0f, speed));
            SeekBar seekBar = this.seekTempo;
            if (seekBar != null) {
                seekBar.setProgress((int) (100.0f * speed2));
            }
        }
    }

    private void setupReverb() {
        try {
            PresetReverb presetReverb = this.globalReverb;
            if (presetReverb != null) {
                presetReverb.release();
            }
            this.globalReverb = new PresetReverb(0, 0);
            updateReverbLevel(this.reverbLevel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateReverbLevel(int progress) throws IllegalStateException, UnsupportedOperationException, IllegalArgumentException {
        short preset;
        PresetReverb presetReverb = this.globalReverb;
        if (presetReverb != null) {
            try {
                if (progress == 0) {
                    presetReverb.setEnabled(false);
                    TextView textView = this.txtLoopReverbVal;
                    if (textView != null) {
                        textView.setText("OFF");
                    }
                } else {
                    presetReverb.setEnabled(true);
                    if (progress < 20) {
                        preset = 1;
                    } else if (progress < 40) {
                        preset = 2;
                    } else if (progress < 60) {
                        preset = 3;
                    } else {
                        preset = progress < 80 ? (short) 4 : (short) 5;
                    }
                    this.globalReverb.setPreset(preset);
                    TextView textView2 = this.txtLoopReverbVal;
                    if (textView2 != null) {
                        textView2.setText(progress + "%");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setupControls() {
        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() { // from class: com.pramod.octapadpromidi.LoopsActivity.13
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) throws IllegalStateException, UnsupportedOperationException, IllegalArgumentException {
                float value = Math.max(0.1f, progress / 100.0f);
                if (seekBar.getId() == R.id.seekTempo) {
                    LoopsActivity.this.currentSpeed = value;
                    if (LoopsActivity.this.txtTempoVal != null) {
                        LoopsActivity.this.txtTempoVal.setText(String.format("%.1fx", Float.valueOf(LoopsActivity.this.currentSpeed)));
                    }
                    LoopsActivity.this.updateAllActiveLoops();
                    return;
                }
                if (seekBar.getId() == R.id.seekPitch) {
                    LoopsActivity.this.currentPitch = value;
                    if (LoopsActivity.this.txtPitchVal != null) {
                        LoopsActivity.this.txtPitchVal.setText(String.format("%.1fx", Float.valueOf(LoopsActivity.this.currentPitch)));
                    }
                    LoopsActivity.this.updateAllActiveLoops();
                    return;
                }
                if (seekBar.getId() == R.id.seekMasterVolume) {
                    LoopsActivity.this.masterVolume = progress / 100.0f;
                    if (LoopsActivity.this.txtMasterVolVal != null) {
                        LoopsActivity.this.txtMasterVolVal.setText(progress + "%");
                    }
                    LoopsActivity.this.prefs.edit().putFloat("loop_master_volume", LoopsActivity.this.masterVolume).apply();
                    LoopsActivity.this.updateAllActiveLoops();
                    return;
                }
                if (seekBar.getId() == R.id.seekLoopReverb) {
                    LoopsActivity.this.reverbLevel = progress;
                    LoopsActivity.this.prefs.edit().putInt("loop_reverb_level", LoopsActivity.this.reverbLevel).apply();
                    LoopsActivity loopsActivity = LoopsActivity.this;
                    loopsActivity.updateReverbLevel(loopsActivity.reverbLevel);
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };
        SeekBar seekBar = this.seekTempo;
        if (seekBar != null) {
            seekBar.setOnSeekBarChangeListener(listener);
        }
        SeekBar seekBar2 = this.seekPitch;
        if (seekBar2 != null) {
            seekBar2.setOnSeekBarChangeListener(listener);
        }
        SeekBar seekBar3 = this.seekMasterVolume;
        if (seekBar3 != null) {
            seekBar3.setOnSeekBarChangeListener(listener);
        }
        SeekBar seekBar4 = this.seekLoopReverb;
        if (seekBar4 != null) {
            seekBar4.setOnSeekBarChangeListener(listener);
        }
        CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.pramod.octapadpromidi.LoopsActivity.14
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.getId() == R.id.chkMultiMode) {
                    LoopsActivity.this.isMultiMode = isChecked;
                    LoopsActivity.this.prefs.edit().putBoolean("loop_multi_mode", LoopsActivity.this.isMultiMode).apply();
                } else if (buttonView.getId() == R.id.chkOneShotMode) {
                    LoopsActivity.this.isOneShotMode = isChecked;
                    LoopsActivity.this.prefs.edit().putBoolean("loop_one_shot_mode", LoopsActivity.this.isOneShotMode).apply();
                    for (int i = 0; i < 8; i++) {
                        if (LoopsActivity.this.mediaPlayers[i] != null) {
                            LoopsActivity.this.mediaPlayers[i].setLooping(!LoopsActivity.this.isOneShotMode);
                        }
                    }
                }
            }
        };
        CheckBox checkBox = this.chkMultiMode;
        if (checkBox != null) {
            checkBox.setOnCheckedChangeListener(checkListener);
        }
        CheckBox checkBox2 = this.chkOneShotMode;
        if (checkBox2 != null) {
            checkBox2.setOnCheckedChangeListener(checkListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAllActiveLoops() {
        for (int i = 0; i < 8; i++) {
            MediaPlayer mediaPlayer = this.mediaPlayers[i];
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                applyPlaybackParams(this.mediaPlayers[i]);
            }
        }
    }

    private void applyPlaybackParams(MediaPlayer mp) {
        if (mp != null) {
            try {
                float f = this.masterVolume;
                mp.setVolume(f, f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (Build.VERSION.SDK_INT >= 23 && mp != null) {
            try {
                PlaybackParams params = mp.getPlaybackParams();
                params.setSpeed(this.currentSpeed);
                params.setPitch(this.currentPitch);
                mp.setPlaybackParams(params);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private void initPads() {
        int[] padIds = {R.id.loopPad1, R.id.loopPad2, R.id.loopPad3, R.id.loopPad4, R.id.loopPad5, R.id.loopPad6, R.id.loopPad7, R.id.loopPad8};
        for (int i = 0; i < 8; i++) {
            this.loopPads[i] = (Button) findViewById(padIds[i]);
            this.loopPads[i].setSoundEffectsEnabled(false);
            final int index = i;
            this.loopPads[i].setOnTouchListener(new View.OnTouchListener() { // from class: com.pramod.octapadpromidi.LoopsActivity.15
                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View v, MotionEvent event) throws IllegalStateException {
                    if (event.getAction() == 0) {
                        v.setPressed(true);
                        LoopsActivity.this.handlePadClick(index);
                        return true;
                    }
                    if (event.getAction() != 1 && event.getAction() != 3) {
                        return false;
                    }
                    v.setPressed(false);
                    return true;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePadClick(int index) throws IllegalStateException {
        this.selectedPad = index;
        if (this.editMode) {
            showEditOptions(index);
        } else {
            toggleLoop(index);
        }
    }

    private void toggleLoop(final int index) throws IllegalStateException {
        MediaPlayer mediaPlayer;
        if (this.loopUris[index] == null) {
            this.txtLoopStatus.setText("LOOP " + (index + 1) + " IS EMPTY");
            return;
        }
        MediaPlayer mp = this.mediaPlayers[index];
        if (mp != null && mp.isPlaying()) {
            mp.pause();
            mp.seekTo(0);
            this.txtLoopStatus.setText("LOOP " + (index + 1) + " STOPPED");
            this.loopPads[index].setBackgroundResource(R.drawable.pad_black_selector);
            return;
        }
        if (mp == null) {
            // Ensure the loop is preloaded to minimize start latency
            preloadLoop(index);
            mp = this.mediaPlayers[index];
            if (mp == null) {
                this.txtLoopStatus.setText("ERROR LOADING LOOP " + (index + 1));
                return;
            }
        }
        mp.setLooping(!this.isOneShotMode);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.pramod.octapadpromidi.LoopsActivity.16
            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mediaPlayer2) throws IllegalStateException {
                if (LoopsActivity.this.isOneShotMode) {
                    LoopsActivity.this.loopPads[index].setBackgroundResource(R.drawable.pad_black_selector);
                    LoopsActivity.this.txtLoopStatus.setText("LOOP " + (index + 1) + " FINISHED");
                    mediaPlayer2.seekTo(0);
                }
            }
        });
        applyPlaybackParams(mp);
        PresetReverb presetReverb = this.globalReverb;
        if (presetReverb != null && presetReverb.getEnabled()) {
            try {
                mp.attachAuxEffect(this.globalReverb.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mp.start();
        this.txtLoopStatus.setText("PLAYING LOOP " + (index + 1));
        this.loopPads[index].setBackgroundResource(R.drawable.pad_blue_glow_selector);
        if (!this.isMultiMode) {
            for (int i = 0; i < 8; i++) {
                if (i != index && (mediaPlayer = this.mediaPlayers[i]) != null && mediaPlayer.isPlaying()) {
                    this.mediaPlayers[i].pause();
                    this.mediaPlayers[i].seekTo(0);
                    this.loopPads[i].setBackgroundResource(R.drawable.pad_black_selector);
                }
            }
        }
    }

    private void showEditOptions(final int index) {
        String[] options = {"Select Loop Audio", "Clear Loop"};
        new AlertDialog.Builder(this).setTitle("EDIT LOOP " + (index + 1)).setItems(options, new DialogInterface.OnClickListener() { // from class: com.pramod.octapadpromidi.LoopsActivity.17
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) throws IllegalStateException {
                if (which == 0) {
                    Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT");
                    intent.addCategory("android.intent.category.OPENABLE");
                    intent.setType("audio/*");
                    intent.addFlags(1);
                    intent.addFlags(64);
                    LoopsActivity.this.startActivityForResult(intent, LoopsActivity.REQ_PICK_LOOP_WAV);
                    return;
                }
                if (which == 1) {
                    LoopsActivity.this.clearLoop(index);
                }
            }
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearLoop(int index) throws IllegalStateException {
        MediaPlayer mediaPlayer = this.mediaPlayers[index];
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                this.mediaPlayers[index].stop();
            }
            this.mediaPlayers[index].release();
            this.mediaPlayers[index] = null;
        }
        this.loopUris[index] = null;
        this.loopPads[index].setBackgroundResource(R.drawable.pad_black_selector);
        saveLoopsToMemory();
        Toast.makeText(this, "Loop " + (index + 1) + " Cleared!", 0).show();
    }

    @Override // android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != -1 || data == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        Uri uri = data.getData();
        if (uri == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (requestCode == REQ_PICK_LOOP_WAV) {
            int takeFlags = data.getFlags() & 3;
            try {
                getContentResolver().takePersistableUriPermission(uri, takeFlags);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            Uri[] uriArr = this.loopUris;
            int i = this.selectedPad;
            uriArr[i] = uri;
            MediaPlayer mediaPlayer = this.mediaPlayers[i];
            if (mediaPlayer != null) {
                mediaPlayer.release();
                this.mediaPlayers[this.selectedPad] = null;
            }
            this.mediaPlayers[this.selectedPad] = MediaPlayer.create(this, uri);
            MediaPlayer mediaPlayer2 = this.mediaPlayers[this.selectedPad];
            if (mediaPlayer2 != null) {
                mediaPlayer2.setLooping(true);
            }
            saveLoopsToMemory();
            Toast.makeText(this, "Loop Audio Loaded!", 0).show();
        } else if (requestCode == REQ_SAVE_LOOP_FOLDER) {
            try {
                getContentResolver().takePersistableUriPermission(uri, 3);
                String str = this.pendingSaveLoopName;
                if (str != null && str.length() > 0) {
                    String str2 = this.pendingSaveLoopName;
                    this.currentLoopName = str2;
                    this.txtLoopChannel.setText(str2);
                    this.prefs.edit().putString("loop_name_ch_" + this.loopChannelIndex, this.currentLoopName).apply();
                }
                saveLoopToFolder(uri);
                this.pendingSaveLoopName = null;
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else if (requestCode == REQ_LOAD_LOOP_FOLDER) {
            try {
                getContentResolver().takePersistableUriPermission(uri, 1);
                loadLoopFromFolder(uri);
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void renameLoopDialog() {
        final EditText edt = new EditText(this);
        edt.setText(this.currentLoopName);
        new AlertDialog.Builder(this).setTitle("Enter Loop Name").setView(edt).setPositiveButton("OK", new DialogInterface.OnClickListener() { // from class: com.pramod.octapadpromidi.LoopsActivity.18
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface d, int w) {
                LoopsActivity.this.currentLoopName = edt.getText().toString().trim();
                if (LoopsActivity.this.currentLoopName.length() == 0) {
                    LoopsActivity.this.currentLoopName = "LOOP " + LoopsActivity.this.loopChannelIndex;
                }
                LoopsActivity.this.txtLoopChannel.setText(LoopsActivity.this.currentLoopName);
                LoopsActivity.this.prefs.edit().putString("loop_name_ch_" + LoopsActivity.this.loopChannelIndex, LoopsActivity.this.currentLoopName).apply();
            }
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSaveLoopNameDialog() {
        final EditText edt = new EditText(this);
        edt.setHint("Enter Loop Group Name");
        edt.setText(this.currentLoopName);
        new AlertDialog.Builder(this).setTitle("Save Loop Group As").setView(edt).setPositiveButton("NEXT", new DialogInterface.OnClickListener() { // from class: com.pramod.octapadpromidi.LoopsActivity.19
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                String name = edt.getText().toString().trim();
                if (name.length() != 0) {
                    LoopsActivity.this.pendingSaveLoopName = LoopsActivity.this.sanitizeFileName(name);
                    LoopsActivity.this.startActivityForResult(new Intent("android.intent.action.OPEN_DOCUMENT_TREE"), LoopsActivity.REQ_SAVE_LOOP_FOLDER);
                } else {
                    Toast.makeText(LoopsActivity.this, "Name required!", 0).show();
                }
            }
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String sanitizeFileName(String name) {
        return name.replaceAll("[\\\\/:*?\"<>|]", "_");
    }

    private void saveLoopToFolder(Uri folderUri) throws JSONException, IOException {
        try {
            DocumentFile root = DocumentFile.fromTreeUri(this, folderUri);
            if (root == null) {
                Toast.makeText(this, "Folder access error!", 0).show();
                return;
            }
            DocumentFile loopFolder = root.findFile(this.currentLoopName + "_loop.mcn");
            if (loopFolder == null) {
                loopFolder = root.createDirectory(this.currentLoopName + "_loop.mcn");
            }
            if (loopFolder == null) {
                Toast.makeText(this, "Cannot create loop folder!", 0).show();
                return;
            }
            for (int i = 0; i < 8; i++) {
                if (this.loopUris[i] != null) {
                    String fileName = "loop_pad_" + (i + 1) + ".wav";
                    DocumentFile old = loopFolder.findFile(fileName);
                    if (old != null) {
                        old.delete();
                    }
                    DocumentFile dest = loopFolder.createFile("audio/wav", fileName);
                    if (dest != null) {
                        FileUtil.copyUriToUri(this, this.loopUris[i], dest.getUri());
                    }
                }
            }
            DocumentFile dataFile = loopFolder.findFile("loop_data.json");
            if (dataFile != null) {
                dataFile.delete();
            }
            DocumentFile dataFile2 = loopFolder.createFile("application/json", "loop_data.json");
            if (dataFile2 != null) {
                try {
                    JSONObject jsonData = new JSONObject();
                    jsonData.put("speed", this.currentSpeed);
                    jsonData.put("pitch", this.currentPitch);
                    jsonData.put("masterVolume", this.masterVolume);
                    jsonData.put("reverbLevel", this.reverbLevel);
                    jsonData.put("isMultiMode", this.isMultiMode);
                    jsonData.put("isOneShotMode", this.isOneShotMode);
                    OutputStream out = getContentResolver().openOutputStream(dataFile2.getUri());
                    if (out != null) {
                        out.write(jsonData.toString().getBytes());
                        out.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(this, "Loop Saved Successfully!", 0).show();
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "Save Error: " + e2.getMessage(), 0).show();
        }
    }

    private void loadLoopFromFolder(Uri folderUri) throws IOException {
        InputStream in;
        try {
            DocumentFile loopFolder = DocumentFile.fromTreeUri(this, folderUri);
            if (loopFolder != null && loopFolder.isDirectory()) {
                String folderName = loopFolder.getName();
                if (folderName != null && folderName.endsWith("_loop.mcn")) {
                    String strReplace = folderName.replace("_loop.mcn", "");
                    this.currentLoopName = strReplace;
                    this.txtLoopChannel.setText(strReplace);
                    this.prefs.edit().putString("loop_name_ch_" + this.loopChannelIndex, this.currentLoopName).apply();
                }
                for (int i = 0; i < 8; i++) {
                    this.loopUris[i] = null;
                    String fileName = "loop_pad_" + (i + 1) + ".wav";
                    DocumentFile wav = loopFolder.findFile(fileName);
                    if (wav != null) {
                        this.loopUris[i] = wav.getUri();
                    }
                }
                DocumentFile dataFile = loopFolder.findFile("loop_data.json");
                if (dataFile != null) {
                    try {
                        in = getContentResolver().openInputStream(dataFile.getUri());
                    } catch (Exception e) {
                        e = e;
                    }
                    if (in != null) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder sb = new StringBuilder();
                        while (true) {
                            String line = reader.readLine();
                            if (line == null) {
                                break;
                            }
                            try {
                                sb.append(line);
                            } catch (Exception e2) {
                                e = e2;
                            }
                            e.printStackTrace();
                        }
                        in.close();
                        JSONObject jsonData = new JSONObject(sb.toString());
                        if (jsonData.has("speed")) {
                            try {
                                this.currentSpeed = (float) jsonData.getDouble("speed");
                            } catch (Exception e3) {
                                e = e3;
                            }
                        }
                        if (jsonData.has("pitch")) {
                            this.currentPitch = (float) jsonData.getDouble("pitch");
                        }
                        if (jsonData.has("masterVolume")) {
                            this.masterVolume = (float) jsonData.getDouble("masterVolume");
                        }
                        if (jsonData.has("reverbLevel")) {
                            this.reverbLevel = jsonData.getInt("reverbLevel");
                        }
                        if (jsonData.has("isMultiMode")) {
                            this.isMultiMode = jsonData.getBoolean("isMultiMode");
                        }
                        if (jsonData.has("isOneShotMode")) {
                            this.isOneShotMode = jsonData.getBoolean("isOneShotMode");
                        }
                    }
                }
                SeekBar seekBar = this.seekTempo;
                if (seekBar != null) {
                    seekBar.setProgress((int) (this.currentSpeed * 100.0f));
                }
                SeekBar seekBar2 = this.seekPitch;
                if (seekBar2 != null) {
                    seekBar2.setProgress((int) (this.currentPitch * 100.0f));
                }
                SeekBar seekBar3 = this.seekMasterVolume;
                if (seekBar3 != null) {
                    seekBar3.setProgress((int) (this.masterVolume * 100.0f));
                }
                SeekBar seekBar4 = this.seekLoopReverb;
                if (seekBar4 != null) {
                    seekBar4.setProgress(this.reverbLevel);
                }
                CheckBox checkBox = this.chkMultiMode;
                if (checkBox != null) {
                    checkBox.setChecked(this.isMultiMode);
                }
                CheckBox checkBox2 = this.chkOneShotMode;
                if (checkBox2 != null) {
                    checkBox2.setChecked(this.isOneShotMode);
                }
                updateReverbLevel(this.reverbLevel);
                saveLoopsToMemory();
                loadLoopsFromMemory();
                Toast.makeText(this, "Loop Loaded Successfully!", 0).show();
                return;
            }
            Toast.makeText(this, "Invalid folder!", 0).show();
        } catch (Exception e4) {
            e4.printStackTrace();
            Toast.makeText(this, "Load Error: " + e4.getMessage(), 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveLoopsToMemory() {
        SharedPreferences.Editor editor = this.prefs.edit();
        for (int i = 0; i < 8; i++) {
            if (this.loopUris[i] != null) {
                editor.putString("loop_uri_ch_" + this.loopChannelIndex + "_" + i, this.loopUris[i].toString());
            } else {
                editor.remove("loop_uri_ch_" + this.loopChannelIndex + "_" + i);
            }
        }
        editor.apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadLoopsFromMemory() throws IllegalStateException {
        for (int i = 0; i < 8; i++) {
            MediaPlayer mediaPlayer = this.mediaPlayers[i];
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    this.mediaPlayers[i].stop();
                }
                this.mediaPlayers[i].release();
                this.mediaPlayers[i] = null;
            }
            this.loopUris[i] = null;
            Button button = this.loopPads[i];
            if (button != null) {
                button.setBackgroundResource(R.drawable.pad_black_selector);
            }
        }
        TextView textView = this.txtLoopStatus;
        if (textView != null) {
            textView.setText("TAP A PAD TO PLAY/STOP LOOP");
        }
        for (int i2 = 0; i2 < 8; i2++) {
            String uriStr = this.prefs.getString("loop_uri_ch_" + this.loopChannelIndex + "_" + i2, null);
            if (uriStr != null) {
                this.loopUris[i2] = Uri.parse(uriStr);
                try {
                    this.mediaPlayers[i2] = MediaPlayer.create(this, this.loopUris[i2]);
                    MediaPlayer mediaPlayer2 = this.mediaPlayers[i2];
                    if (mediaPlayer2 != null) {
                        mediaPlayer2.setLooping(true);
                    }
                    // Warm/preload the MediaPlayer to reduce first-start latency
                    preloadLoop(i2);
                } catch (Exception e) {
                    e.printStackTrace();
                    this.loopUris[i2] = null;
                }
            }
        }
    }

                    this.mediaPlayers[this.selectedPad] = MediaPlayer.create(this, uri);
                    MediaPlayer mediaPlayer2 = this.mediaPlayers[this.selectedPad];
                    if (mediaPlayer2 != null) {
                        mediaPlayer2.setLooping(true);
                    }
                    // Warm the newly selected loop to reduce latency on first play
                    preloadLoop(this.selectedPad);
            }
            MediaPlayer mp = this.mediaPlayers[index];
            if (mp == null) {
                mp = MediaPlayer.create(this, uri);
                if (mp == null) {
                    return;
                }
                mp.setLooping(!this.isOneShotMode);
                this.mediaPlayers[index] = mp;
            }
            try {
                // Start/pause with zero volume to prime the decoder without audible output
                mp.setVolume(0.0f, 0.0f);
                applyPlaybackParams(mp);
                mp.start();
                mp.pause();
                mp.seekTo(0);
            } catch (Exception e) {
            }
            mp.setVolume(this.masterVolume, this.masterVolume);
        } catch (Exception e2) {
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

    @Override // android.app.Activity
    protected void onPause() throws IllegalStateException {
        super.onPause();
        for (int i = 0; i < 8; i++) {
            MediaPlayer mediaPlayer = this.mediaPlayers[i];
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                this.mediaPlayers[i].pause();
                this.loopPads[i].setBackgroundResource(R.drawable.pad_black_selector);
            }
        }
    }

    @Override // android.app.Activity
    protected void onDestroy() throws IllegalStateException, IOException {
        super.onDestroy();
        for (int i = 0; i < 8; i++) {
            MediaPlayer mediaPlayer = this.mediaPlayers[i];
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    this.mediaPlayers[i].stop();
                }
                this.mediaPlayers[i].release();
                this.mediaPlayers[i] = null;
            }
        }
        closeMidiDevice();
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
            this.midiManager.registerDeviceCallback(new MidiManager.DeviceCallback() { // from class: com.pramod.octapadpromidi.LoopsActivity.20
                @Override // android.media.midi.MidiManager.DeviceCallback
                public void onDeviceAdded(MidiDeviceInfo device) {
                    LoopsActivity.this.openMidiDevice(device);
                }

                @Override // android.media.midi.MidiManager.DeviceCallback
                public void onDeviceRemoved(MidiDeviceInfo device) throws IOException {
                    if (LoopsActivity.this.openedMidiDevice != null && LoopsActivity.this.openedMidiDevice.getInfo().getId() == device.getId()) {
                        LoopsActivity.this.closeMidiDevice();
                        if (LoopsActivity.this.txtMidiStatus != null) {
                            LoopsActivity.this.txtMidiStatus.setText("MIDI disconnected");
                        }
                    }
                }
            }, new Handler(Looper.getMainLooper()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openMidiDevice(MidiDeviceInfo info) {
        if (Build.VERSION.SDK_INT >= 23 && info.getOutputPortCount() > 0) {
            this.midiManager.openDevice(info, new MidiManager.OnDeviceOpenedListener() { // from class: com.pramod.octapadpromidi.LoopsActivity.21
                @Override // android.media.midi.MidiManager.OnDeviceOpenedListener
                public void onDeviceOpened(MidiDevice device) {
                    LoopsActivity.this.openedMidiDevice = device;
                    LoopsActivity.this.midiOutputPort = device.openOutputPort(0);
                    if (LoopsActivity.this.midiOutputPort != null) {
                        if (LoopsActivity.this.txtMidiStatus != null) {
                            LoopsActivity.this.txtMidiStatus.setText("MIDI connected");
                        }
                        LoopsActivity.this.midiOutputPort.connect(new MidiReceiver() { // from class: com.pramod.octapadpromidi.LoopsActivity.21.1
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
                                        byte note = (byte) value;
                                        byte velocity = msg[i + 1];
                                        if ((velocity & 0xFF) > 0) {
                                            LoopsActivity.this.handleMidiNoteOn(note, velocity);
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
                        ((TextView) LoopsActivity.this.findViewById(R.id.txtMidiStatus)).setText("MIDI connected");
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
        runOnUiThread(new Runnable() { // from class: com.pramod.octapadpromidi.LoopsActivity.22
            @Override // java.lang.Runnable
            public void run() throws IllegalStateException {
                int i = finalPadIndex;
                if (i >= 0 && i < 8) {
                    LoopsActivity.this.loopPads[finalPadIndex].setPressed(true);
                    LoopsActivity.this.handlePadClick(finalPadIndex);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.pramod.octapadpromidi.LoopsActivity.22.1
                        @Override // java.lang.Runnable
                        public void run() {
                            LoopsActivity.this.loopPads[finalPadIndex].setPressed(false);
                        }
                    }, 100L);
                }
            }
        });
    }
}
