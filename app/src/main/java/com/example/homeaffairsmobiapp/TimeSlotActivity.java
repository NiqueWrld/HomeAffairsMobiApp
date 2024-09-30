package com.example.homeaffairsmobiapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

public class TimeSlotActivity extends AppCompatActivity {

    private Spinner timeSlotSpinner;
    private Booking booking;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slot2);

        timeSlotSpinner = findViewById(R.id.timeSlotSpinner);
        db = FirebaseFirestore.getInstance();

        // Retrieve the Booking object from the intent
        booking = (Booking) getIntent().getSerializableExtra("booking");

        // Retrieve time slots from strings.xml
        String[] timeSlots = getResources().getStringArray(R.array.time_slots_array);

        // Set up the Spinner with time slots
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeSlots);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSlotSpinner.setAdapter(adapter);

        timeSlotSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTimeSlot = parent.getItemAtPosition(position).toString();
                Toast.makeText(TimeSlotActivity.this, "Selected: " + selectedTimeSlot, Toast.LENGTH_SHORT).show();

                // Update the Booking object with the selected time slot
                booking.setTimeSlot(selectedTimeSlot);

                // Update the booking in Firestore
                updateBookingInFirestore(selectedTimeSlot);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void updateBookingInFirestore(String selectedTimeSlot) {
        db.collection("bookings").document(booking.getBookingId())
                .update(
                        "timeSlot", selectedTimeSlot,
                        "status", "pending"
                )
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(TimeSlotActivity.this, "Booking updated successfully!", Toast.LENGTH_SHORT).show();
                    showBookingDetailsDialog();
                })
                .addOnFailureListener(e -> Toast.makeText(TimeSlotActivity.this, "Failed to update booking: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void showBookingDetailsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_booking_details, null);

        TextView serviceName = dialogView.findViewById(R.id.serviceName);
        TextView timeSlotText = dialogView.findViewById(R.id.timeSlotText);
        TextView userIdText = dialogView.findViewById(R.id.userIdText);

        serviceName.setText("Service: " + booking.getService());
        timeSlotText.setText("Time Slot: " + booking.getTimeSlot());
        userIdText.setText("User ID: " + booking.getUserId());

        builder.setView(dialogView);
        builder.setTitle("Booking Details");
        builder.setPositiveButton("Confirm", (dialog, which) -> {
            dialog.dismiss();
            // Send booking to admin dashboard
            sendBookingToAdminDashboard();
        });
        builder.show();
    }

    private void sendBookingToAdminDashboard() {
        // Update the booking status to "pending" in Firestore
        db.collection("bookings").document(booking.getBookingId())
                .update("status", "pending")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(TimeSlotActivity.this, "Booking sent to admin for approval", Toast.LENGTH_SHORT).show();
                    // Navigate back to the main activity or user dashboard
                    Intent intent = new Intent(TimeSlotActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(TimeSlotActivity.this, "Failed to send booking to admin: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
