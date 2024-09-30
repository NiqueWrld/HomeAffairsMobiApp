package com.example.homeaffairsmobiapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> bookings;
    private Context context;
    private FirebaseFirestore db;

    public BookingAdapter(List<Booking> bookings, Context context) {
        this.bookings = bookings;
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.serviceName.setText("Service: " + booking.getService());
        holder.timeSlot.setText("Time Slot: " + booking.getTimeSlot());
        holder.userId.setText("User ID: " + booking.getUserId());
        holder.status.setText("Status: " + booking.getStatus()); // Display the status

        // Only show accept/decline buttons if status is "pending"
        if ("pending".equalsIgnoreCase(booking.getStatus())) {
            holder.acceptButton.setVisibility(View.VISIBLE);
            holder.declineButton.setVisibility(View.VISIBLE);

            holder.acceptButton.setOnClickListener(v -> updateBookingStatus(booking, "accepted"));
            holder.declineButton.setOnClickListener(v -> updateBookingStatus(booking, "declined"));
        } else {
            holder.acceptButton.setVisibility(View.GONE);
            holder.declineButton.setVisibility(View.GONE);
        }
    }

    private void updateBookingStatus(Booking booking, String status) {
        db.collection("bookings").document(booking.getBookingId())
                .update("status", status)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Booking " + status, Toast.LENGTH_SHORT).show();
                    booking.setStatus(status); // Update the local object
                    notifyDataSetChanged(); // Refresh the entire list
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to update booking: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView serviceName, timeSlot, userId, status;
        Button acceptButton, declineButton;

        BookingViewHolder(View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.serviceName);
            timeSlot = itemView.findViewById(R.id.timeSlot);
            userId = itemView.findViewById(R.id.userId);
            status = itemView.findViewById(R.id.status); // Add this line
            acceptButton = itemView.findViewById(R.id.acceptButton);
            declineButton = itemView.findViewById(R.id.declineButton);
        }
    }
}

