package com.example.homeaffairsmobiapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class AcceptedBookingsActivity extends AppCompatActivity {

    private RecyclerView acceptedBookingsRecyclerView;
    private BookingAdapter bookingAdapter;
    private List<Booking> acceptedBookingList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_bookings);

        acceptedBookingsRecyclerView = findViewById(R.id.acceptedBookingsRecyclerView);
        db = FirebaseFirestore.getInstance();
        acceptedBookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(acceptedBookingList, this);

        acceptedBookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        acceptedBookingsRecyclerView.setAdapter(bookingAdapter);

        fetchAcceptedBookings();
    }

    private void fetchAcceptedBookings() {
        db.collection("bookings")
                .whereEqualTo("status", "accepted")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        acceptedBookingList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Booking booking = document.toObject(Booking.class);
                            acceptedBookingList.add(booking);
                        }
                        bookingAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(AcceptedBookingsActivity.this, "Error fetching accepted bookings", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
