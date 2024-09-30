package com.example.homeaffairsmobiapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Toast;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AcceptedBookingsActivity extends AppCompatActivity implements BookingAdapter.OnBookingStatusChangeListener {

    private RecyclerView acceptedBookingsRecyclerView;
    private BookingAdapter bookingAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_bookings);

        acceptedBookingsRecyclerView = findViewById(R.id.acceptedBookingsRecyclerView);
        db = FirebaseFirestore.getInstance();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        Query query = db.collection("bookings")
                .whereEqualTo("status", "accepted");

        FirestoreRecyclerOptions<Booking> options = new FirestoreRecyclerOptions.Builder<Booking>()
                .setQuery(query, Booking.class)
                .build();

        bookingAdapter = new BookingAdapter(options, this);
        acceptedBookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        acceptedBookingsRecyclerView.setAdapter(bookingAdapter);
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
        // Handle status changes if needed
        Toast.makeText(this, "Booking status changed to: " + newStatus, Toast.LENGTH_SHORT).show();
    }
}
