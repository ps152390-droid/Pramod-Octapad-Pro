package com.pramod.octapadpromidi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/* loaded from: classes3.dex */
public class ActivationActivity extends Activity {
    private static final String KEY_IS_ACTIVATED = "is_activated";
    private static final String PREF_NAME = "OctapadSettings";
    private Button btnActivate;
    private EditText editActivationKey;
    private SharedPreferences prefs;
    private ProgressBar progressBar;

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, 0);
        this.prefs = sharedPreferences;
        if (sharedPreferences.getBoolean(KEY_IS_ACTIVATED, false)) {
            startMainActivity();
            return;
        }
        setContentView(R.layout.activity_activation);
        this.editActivationKey = (EditText) findViewById(R.id.editActivationKey);
        this.btnActivate = (Button) findViewById(R.id.btnActivate);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.btnActivate.setOnClickListener(new View.OnClickListener() { // from class: com.pramod.octapadpromidi.ActivationActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) throws DatabaseException {
                ActivationActivity.this.activateApp();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void activateApp() throws DatabaseException {
        String key = this.editActivationKey.getText().toString().trim();
        if (key.isEmpty()) {
            Toast.makeText(this, "Please enter an activation key", 0).show();
            return;
        }
        this.progressBar.setVisibility(0);
        this.btnActivate.setEnabled(false);
        final String deviceId = Settings.Secure.getString(getContentResolver(), "android_id");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("activation_keys").child(key);
        ref.addListenerForSingleValueEvent(new ValueEventListener() { // from class: com.pramod.octapadpromidi.ActivationActivity.2
            @Override // com.google.firebase.database.ValueEventListener
            public void onDataChange(DataSnapshot snapshot) {
                String str;
                ActivationActivity.this.progressBar.setVisibility(8);
                ActivationActivity.this.btnActivate.setEnabled(true);
                if (snapshot.exists()) {
                    String status = (String) snapshot.child(NotificationCompat.CATEGORY_STATUS).getValue(String.class);
                    String dbDeviceId = (String) snapshot.child("device_id").getValue(String.class);
                    if ("unused".equals(status)) {
                        snapshot.getRef().child(NotificationCompat.CATEGORY_STATUS).setValue("used");
                        snapshot.getRef().child("device_id").setValue(deviceId);
                        ActivationActivity.this.successAndProceed();
                        return;
                    } else if ("used".equals(status) && (str = deviceId) != null && str.equals(dbDeviceId)) {
                        ActivationActivity.this.successAndProceed();
                        return;
                    } else {
                        Toast.makeText(ActivationActivity.this, "Key is already used on another device!", 1).show();
                        return;
                    }
                }
                Toast.makeText(ActivationActivity.this, "Invalid Activation Key!", 1).show();
            }

            @Override // com.google.firebase.database.ValueEventListener
            public void onCancelled(DatabaseError error) {
                ActivationActivity.this.progressBar.setVisibility(8);
                ActivationActivity.this.btnActivate.setEnabled(true);
                Toast.makeText(ActivationActivity.this, "Database Error: " + error.getMessage(), 0).show();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void successAndProceed() {
        this.prefs.edit().putBoolean(KEY_IS_ACTIVATED, true).apply();
        Toast.makeText(this, "Activation Successful!", 0).show();
        startMainActivity();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, (Class<?>) MainActivity.class);
        startActivity(intent);
        finish();
    }
}
