package com.example.homeaffairsmobiapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import android.net.Uri;


public class AdminActivity extends AppCompatActivity implements BookingAdapter.OnBookingStatusChangeListener {

    private Button viewAppointmentsButton, viewAnalyticsButton, signOutButton;
    private RecyclerView bookingsRecyclerView;
    private BookingAdapter bookingAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        viewAppointmentsButton = findViewById(R.id.viewAppointmentsButton);
        viewAnalyticsButton = findViewById(R.id.viewAnalyticsButton);
        signOutButton = findViewById(R.id.signOutButton);
        bookingsRecyclerView = findViewById(R.id.bookingsRecyclerView);

        db = FirebaseFirestore.getInstance();
        setupRecyclerView();

        viewAppointmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AcceptedBookingsActivity.class);
                startActivity(intent);
            }
        });

        viewAnalyticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminActivity.this, "Viewing Analytics", Toast.LENGTH_SHORT).show();
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupRecyclerView() {
        Query query = db.collection("bookings")
                .whereEqualTo("status", "pending");

        FirestoreRecyclerOptions<Booking> options = new FirestoreRecyclerOptions.Builder<Booking>()
                .setQuery(query, Booking.class)
                .build();

        bookingAdapter = new BookingAdapter(options, this);
        bookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingsRecyclerView.setAdapter(bookingAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bookingAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bookingAdapter.stopListening();
    }

    @Override
    public void onBookingStatusChanged(Booking booking, String newStatus) {
        if (newStatus.equals("accepted")) {
            sendConfirmationEmail(booking);
        }
    }

    private void sendConfirmationEmail(Booking booking) {
        Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
        selectorIntent.setData(Uri.parse("mailto:"));

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{booking.getUserEmail()});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Booking Confirmation");

        String emailBody = "Dear " + booking.getFullName() + ",\n\n" +
                "Your booking has been confirmed:\n" +
                "Service: " + booking.getService() + "\n" +
                "Time Slot: " + booking.getTimeSlot() + "\n" +
                "Booking ID: " + booking.getBookingId() + "\n\n" +
                "Thank you for using our service.";

        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
        emailIntent.setSelector(selectorIntent);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
            // Redirect to AcceptedBookingsActivity after sending the email
            Intent intent = new Intent(AdminActivity.this, AcceptedBookingsActivity.class);
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AdminActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}

