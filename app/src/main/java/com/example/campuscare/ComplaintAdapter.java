package com.example.campuscare;

import android.content.Context;

import android.graphics.Color;

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

    public interface ProofUploadCallback {

        void onUploadProof(ComplaintModel complaint, int position);

    }

    private List<ComplaintModel> complaintList;

    private String loggedInRole;

    private ProofUploadCallback proofCallback;

    public ComplaintAdapter(List<ComplaintModel> complaintList, String loggedInRole, ProofUploadCallback callback) {

        this.complaintList = complaintList;

        this.loggedInRole = loggedInRole;

        this.proofCallback = callback;

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

        holder.tvStatus.setText("Status: " + complaint.getStatus());

        if (complaint.getImageUri() != null && !complaint.getImageUri().isEmpty()) {

            holder.imgComplaintItem.setImageURI(Uri.parse(complaint.getImageUri()));

        } else {

            holder.imgComplaintItem.setImageResource(android.R.drawable.ic_menu_gallery);

        }

        if (complaint.getResolutionImageUri() != null && !complaint.getResolutionImageUri().isEmpty()) {

            holder.imgResolutionItem.setImageURI(Uri.parse(complaint.getResolutionImageUri()));

            holder.imgResolutionItem.setVisibility(View.VISIBLE);

        } else {

            holder.imgResolutionItem.setImageResource(android.R.drawable.ic_menu_camera);

        }

        // Role-based button control

        if (loggedInRole.equals("Resolver")) {

            if (complaint.getStatus().equals("Pending")) {

                if (complaint.getResolutionImageUri() == null || complaint.getResolutionImageUri().isEmpty()) {

                    holder.btnAction.setText("1. Attach Proof Photo");

                    holder.btnAction.setBackgroundColor(Color.parseColor("#FF9800"));

                } else {

                    holder.btnAction.setText("2. Mark Done");

                    holder.btnAction.setBackgroundColor(Color.parseColor("#4CAF50"));

                }

                holder.btnAction.setVisibility(View.VISIBLE);

                holder.btnAction.setEnabled(true);

            } else {

                holder.btnAction.setText("Completed");

                holder.btnAction.setEnabled(false);

                holder.btnAction.setVisibility(View.VISIBLE);

            }

        } else if (loggedInRole.equals("Admin")) {

            holder.btnAction.setText("Admin Delete");

            holder.btnAction.setBackgroundColor(Color.parseColor("#F44336"));

            holder.btnAction.setVisibility(View.VISIBLE);

            holder.btnAction.setEnabled(true);

        } else {

            if (complaint.getStatus().equals("Work Completed - Awaiting Acceptance")) {

                holder.btnAction.setText("Accept & Close Ticket");

                holder.btnAction.setBackgroundColor(Color.parseColor("#2196F3"));

                holder.btnAction.setVisibility(View.VISIBLE);

                holder.btnAction.setEnabled(true);

            } else if (complaint.getStatus().equals("Accepted / Closed")) {

                holder.btnAction.setText("Closed");

                holder.btnAction.setEnabled(false);

                holder.btnAction.setVisibility(View.VISIBLE);

            } else {

                holder.btnAction.setVisibility(View.GONE);

            }

        }

        holder.btnAction.setOnClickListener(v -> {

            Context context = v.getContext();

            DatabaseHelper db = new DatabaseHelper(context);

            if (loggedInRole.equals("Resolver")) {

                if (complaint.getResolutionImageUri() == null || complaint.getResolutionImageUri().isEmpty()) {

                    if (proofCallback != null) {

                        proofCallback.onUploadProof(complaint, position);

                    }

                } else {

                    boolean success = db.updateByResolver(complaint.getId(), "Work Completed - Awaiting Acceptance", complaint.getResolutionImageUri());

                    if (success) {

                        complaint.setStatus("Work Completed - Awaiting Acceptance");

                        notifyItemChanged(position);

                        Toast.makeText(context, "Work marked complete. Awaiting reporter acceptance.", Toast.LENGTH_SHORT).show();

                    }

                }

            } else if (loggedInRole.equals("Admin")) {

                boolean success = db.deleteComplaint(complaint.getId());

                if (success) {

                    complaintList.remove(position);

                    notifyItemRemoved(position);

                    notifyItemRangeChanged(position, complaintList.size());

                    Toast.makeText(context, "Complaint deleted by Admin", Toast.LENGTH_SHORT).show();

                }

            } else {

                boolean success = db.acceptResolution(complaint.getId(), "Accepted / Closed");

                if (success) {

                    complaint.setStatus("Accepted / Closed");

                    notifyItemChanged(position);

                    Toast.makeText(context, "Ticket accepted and closed successfully!", Toast.LENGTH_SHORT).show();

                }

            }

        });

    }

    @Override

    public int getItemCount() {

        return complaintList.size();

    }

    public static class ComplaintViewHolder extends RecyclerView.ViewHolder {

        TextView tvCategory, tvLocation, tvReporter, tvStatus;

        ImageView imgComplaintItem, imgResolutionItem;

        Button btnAction;

        public ComplaintViewHolder(@NonNull View itemView) {

            super(itemView);

            tvCategory = itemView.findViewById(R.id.tvCategory);

            tvLocation = itemView.findViewById(R.id.tvLocation);

            tvReporter = itemView.findViewById(R.id.tvReporter);

            tvStatus = itemView.findViewById(R.id.tvStatus);

            imgComplaintItem = itemView.findViewById(R.id.imgComplaintItem);

            imgResolutionItem = itemView.findViewById(R.id.imgResolutionItem);

            btnAction = itemView.findViewById(R.id.btnAction);

        }

    }

}
