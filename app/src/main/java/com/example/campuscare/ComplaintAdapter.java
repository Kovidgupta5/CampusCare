package com.example.campuscare;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder> {

    private List<ComplaintModel> complaintList;

    public ComplaintAdapter(List<ComplaintModel> complaintList) {
        this.complaintList = complaintList;
    }

    @NonNull
    @Override
    public ComplaintViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_complaint, parent, false);
        return new ComplaintViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintViewHolder holder, int position) {
        ComplaintModel complaint = complaintList.get(position);
        holder.tvCategory.setText(complaint.getCategory());
        holder.tvLocation.setText("Location: " + complaint.getLocation());
        holder.tvReporter.setText("By: " + complaint.getName());
        holder.tvSeverity.setText("Severity: " + complaint.getSeverity());
        holder.tvStatus.setText("Status: " + complaint.getStatus());

        // Display photo thumbnail if available
        if (complaint.getImageUri() != null && !complaint.getImageUri().isEmpty()) {
            holder.imgComplaintItem.setImageURI(Uri.parse(complaint.getImageUri()));
        } else {
            holder.imgComplaintItem.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        // Handle Resolve button click (Faculty action)
        holder.btnResolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                DatabaseHelper db = new DatabaseHelper(context);
                boolean success = db.updateStatus(complaint.getId(), "Resolved");
                if (success) {
                    complaint.setStatus("Resolved");
                    notifyItemChanged(position);
                    Toast.makeText(context, "Ticket marked as Resolved!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to update status", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return complaintList.size();
    }

    public static class ComplaintViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvLocation, tvReporter, tvSeverity, tvStatus;
        ImageView imgComplaintItem;
        Button btnResolve;

        public ComplaintViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvReporter = itemView.findViewById(R.id.tvReporter);
            tvSeverity = itemView.findViewById(R.id.tvSeverity);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            imgComplaintItem = itemView.findViewById(R.id.imgComplaintItem);
            btnResolve = itemView.findViewById(R.id.btnResolve);
        }
    }
}