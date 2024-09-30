package com.example.homeaffairsmobiapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;

public class BookingConfirmationActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private Booking booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmation);

        db = FirebaseFirestore.getInstance();

        // Retrieve the Booking object from the intent
        booking = (Booking) getIntent().getSerializableExtra("booking");

        if (booking != null) {
            // Save booking to Firestore
            saveBookingToFirestore();

            // Display booking details
            showBookingDetailsDialog();
        } else {
            // Handle the case where booking is null
            Toast.makeText(this, "Booking details are missing. Please try again.", Toast.LENGTH_LONG).show();
            finish(); // Close the current activity
        }
    }

    private void saveBookingToFirestore() {
        db.collection("bookings").document(booking.getBookingId())
                .set(booking)
                .addOnSuccessListener(aVoid -> Toast.makeText(BookingConfirmationActivity.this, "Booking saved successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(BookingConfirmationActivity.this, "Failed to save booking: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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
        builder.setTitle("Booking Confirmation");
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }
}
