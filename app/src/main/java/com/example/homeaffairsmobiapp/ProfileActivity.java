package com.example.homeaffairsmobiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private EditText displayNameEditText, emailEditText;
    private de.hdodenhof.circleimageview.CircleImageView profileImageView;
    private Button updateProfileButton, signOutButton;
    private FirebaseUser user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        displayNameEditText = findViewById(R.id.displayName);
        emailEditText = findViewById(R.id.email);
        profileImageView = findViewById(R.id.profileImageView);
        updateProfileButton = findViewById(R.id.updateProfile);
        signOutButton = findViewById(R.id.signOut);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if (user != null) {
            loadUserProfile();
        }

        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadUserProfile() {
        displayNameEditText.setText(user.getDisplayName());
        emailEditText.setText(user.getEmail());
        Uri photoUrl = user.getPhotoUrl();
        if (photoUrl != null) {
            Glide.with(this).load(photoUrl).into(profileImageView);
        }

        // Load additional user data from Firestore if needed
        db.collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    // Example: Load additional fields like phone number
                    String phone = task.getResult().getString("phone");
                    // Set phone number to a TextView or EditText if you have one
                } else {
                    Log.d(TAG, "Failed to load user data", task.getException());
                }
            }
        });
    }

    private void updateProfile() {
        String displayName = displayNameEditText.getText().toString();
        String email = emailEditText.getText().toString();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                            Toast.makeText(ProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        user.updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                            Toast.makeText(ProfileActivity.this, "Email updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Update additional user data in Firestore if needed
        db.collection("users").document(user.getUid()).update("displayName", displayName, "email", email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User Firestore data updated.");
                        } else {
                            Log.d(TAG, "Failed to update Firestore data", task.getException());
                        }
                    }
                });
    }
}
