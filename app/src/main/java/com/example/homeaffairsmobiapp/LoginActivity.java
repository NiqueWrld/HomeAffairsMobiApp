package com.example.homeaffairsmobiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailTextView, passwordTextView;
    private Button loginButton, registerButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);
        progressBar = findViewById(R.id.progressBar);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserAccount();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void loginUserAccount() {
        progressBar.setVisibility(View.VISIBLE);
        String email = emailTextView.getText().toString();
        String password = passwordTextView.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTextView.setError("Please enter a valid email");
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (password.isEmpty()) {
            passwordTextView.setError("Please enter password");
            progressBar.setVisibility(View.GONE);
            return;
        }

        // Hardcoded admin credentials
        if (email.equals("admin@example.com") && password.equals("admin123")) {
            progressBar.setVisibility(View.GONE);
            // Directly navigate to AdminActivity
            startActivity(new Intent(LoginActivity.this, AdminActivity.class));
            finish();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            checkUserRole();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void checkUserRole() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        db.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot -> {
            String role = documentSnapshot.getString("role");
            if ("admin".equals(role)) {
                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
            } else {
                startActivity(new Intent(LoginActivity.this, UserActivity.class));
            }
            finish();
        });
    }
}
