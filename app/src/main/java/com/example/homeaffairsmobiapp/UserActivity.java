package com.example.homeaffairsmobiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserActivity extends AppCompatActivity {

    private static final String TAG = "UserActivity";
    private Button signOutButton, selectServiceButton, settingsButton;
    private Spinner serviceSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        signOutButton = findViewById(R.id.signOutButton);
        selectServiceButton = findViewById(R.id.selectServiceButton);
        settingsButton = findViewById(R.id.settingsButton);
        serviceSpinner = findViewById(R.id.serviceSpinner);

        // Set up the spinner with service options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.services_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceSpinner.setAdapter(adapter);

        checkUserProfile(); // Check if the user profile is complete

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        selectServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ServiceSelectionActivity
                startActivity(new Intent(UserActivity.this, ServiceSelectionActivity.class));
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to SettingsActivity
                startActivity(new Intent(UserActivity.this, SettingsActivity.class));
            }
        });
    }

    private void checkUserProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(user.getUid()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists() || document.getString("firstName") == null) {
                        // Prompt user to enter profile information
                        Intent intent = new Intent(UserActivity.this, EditProfileActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Log.d(TAG, "Failed to load user data", task.getException());
                }
            });
        }
    }
}
