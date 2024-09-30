package com.example.homeaffairsmobiapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserActivity extends AppCompatActivity {

    private static final String TAG = "UserActivity";

    private Button signOutButton;
    private Button selectServiceButton;
    private Button settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initializeViews();
        setupButtonListeners();
        checkUserProfile();
    }

    private void initializeViews() {
        signOutButton = findViewById(R.id.signOutButton);
        selectServiceButton = findViewById(R.id.selectServiceButton);
        settingsButton = findViewById(R.id.settingsButton);
    }

    private void setupButtonListeners() {
        signOutButton.setOnClickListener(v -> signOut());
        selectServiceButton.setOnClickListener(v -> navigateToServiceSelection());
        settingsButton.setOnClickListener(v -> navigateToSettings());
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(UserActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void navigateToServiceSelection() {
        startActivity(new Intent(UserActivity.this, ServiceSelectionActivity.class));
    }

    private void navigateToSettings() {
        startActivity(new Intent(UserActivity.this, SettingsActivity.class));
    }

    private void checkUserProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(user.getUid()).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot document = task.getResult();
                            if (!document.exists() || document.getString("firstName") == null) {
                                navigateToEditProfile();
                            }
                        } else {
                            Log.d(TAG, "Failed to load user data", task.getException());
                        }
                    });
        }
    }

    private void navigateToEditProfile() {
        Intent intent = new Intent(UserActivity.this, EditProfileActivity.class);
        startActivity(intent);
    }
}
