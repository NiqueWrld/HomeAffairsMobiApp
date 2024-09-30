package com.example.homeaffairsmobiapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private Button viewAppointmentsButton, viewAnalyticsButton, signOutButton;
    private RecyclerView bookingsRecyclerView;
    private BookingAdapter bookingAdapter;
    private List<Booking> bookingList;
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
        bookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(bookingList, this);

        bookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingsRecyclerView.setAdapter(bookingAdapter);

        fetchBookings();

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

    private void fetchBookings() {
        db.collection("bookings")
                .whereEqualTo("status", "pending")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        bookingList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Booking booking = document.toObject(Booking.class);
                            bookingList.add(booking);
                        }
                        bookingAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(AdminActivity.this, "Error fetching bookings", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
