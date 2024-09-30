package com.example.homeaffairsmobiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailTextView, passwordTextView;
    private Button registerButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.password);
        registerButton = findViewById(R.id.register);
        progressBar = findViewById(R.id.progressBar);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });
    }

    private void registerNewUser() {
        progressBar.setVisibility(View.VISIBLE);
        String email = emailTextView.getText().toString();
        String password = passwordTextView.getText().toString();

        if (email.isEmpty()) {
            emailTextView.setError("Please enter email");
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (password.isEmpty()) {
            passwordTextView.setError("Please enter password");
            progressBar.setVisibility(View.GONE);
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            saveUserRole();
                            Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void saveUserRole() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        Map<String, Object> user = new HashMap<>();
        user.put("role", "user"); // Default role
        db.collection("users").document(uid).set(user);
    }
}