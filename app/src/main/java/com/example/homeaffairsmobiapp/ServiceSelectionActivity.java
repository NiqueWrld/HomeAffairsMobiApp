package com.example.homeaffairsmobiapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ServiceSelectionActivity extends AppCompatActivity {

    private Spinner serviceSpinner;
    private Button proceedButton;
    private String selectedService;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_selection);

        serviceSpinner = findViewById(R.id.serviceSpinner);
        proceedButton = findViewById(R.id.proceedButton);

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Set up the spinner with service options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.services_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceSpinner.setAdapter(adapter);

        serviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedService = parent.getItemAtPosition(position).toString();
                Toast.makeText(ServiceSelectionActivity.this, "Selected: " + selectedService, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseUser != null) {
                    // Create a new booking document with auto-generated ID
                    DocumentReference newBookingRef = db.collection("bookings").document();
                    String bookingId = newBookingRef.getId();

                    // Create a Booking object with only the service selected
                    Booking booking = new Booking(bookingId, firebaseUser.getUid(), selectedService, null, null, null);

                    // Save the booking to Firestore
                    newBookingRef.set(booking).addOnSuccessListener(aVoid -> {
                        // Navigate to TimeSlotActivity with the Booking object
                        Intent intent = new Intent(ServiceSelectionActivity.this, TimeSlotActivity.class);
                        intent.putExtra("booking", booking);
                        startActivity(intent);
                    }).addOnFailureListener(e -> {
                        Toast.makeText(ServiceSelectionActivity.this, "Failed to create booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }
}
