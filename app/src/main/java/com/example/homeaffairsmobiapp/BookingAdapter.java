package com.example.homeaffairsmobiapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

public class BookingAdapter extends FirestoreRecyclerAdapter<Booking, BookingAdapter.BookingViewHolder> {

    private OnBookingStatusChangeListener listener;
    private FirebaseFirestore db;

    public interface OnBookingStatusChangeListener {
        void onBookingStatusChanged(Booking booking, String newStatus);
    }

    public BookingAdapter(@NonNull FirestoreRecyclerOptions<Booking> options, OnBookingStatusChangeListener listener) {
        super(options);
        this.listener = listener;
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onBindViewHolder(@NonNull BookingViewHolder holder, int position, @NonNull Booking booking) {
        holder.serviceName.setText("Service: " + booking.getService());
        holder.timeSlot.setText("Time Slot: " + booking.getTimeSlot());
        holder.userName.setText("Name: " + booking.getFullName());
        holder.userEmail.setText("Email: " + booking.getUserEmail());
        holder.status.setText("Status: " + booking.getStatus());

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

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    private void updateBookingStatus(Booking booking, String status) {
        db.collection("bookings").document(booking.getBookingId())
                .update("status", status)
                .addOnSuccessListener(aVoid -> {
                    if (listener != null) {
                        listener.onBookingStatusChanged(booking, status);
                    }
                });
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView serviceName, timeSlot, userName, userEmail, status;
        Button acceptButton, declineButton;

        BookingViewHolder(View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.serviceName);
            timeSlot = itemView.findViewById(R.id.timeSlot);
            userName = itemView.findViewById(R.id.userName);
            userEmail = itemView.findViewById(R.id.userEmail);
            status = itemView.findViewById(R.id.status);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            declineButton = itemView.findViewById(R.id.declineButton);
        }
    }
}
