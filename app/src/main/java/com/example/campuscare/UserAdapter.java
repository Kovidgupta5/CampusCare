package com.example.campuscare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<UserModel> userList;

    public UserAdapter(List<UserModel> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserModel user = userList.get(position);
        holder.tvUsername.setText(user.getUsername());
        holder.tvRole.setText("Role: " + user.getRole());

        // Protect admin account from accidental removal
        if (user.getRole().equals("Admin")) {
            holder.btnDeleteUser.setVisibility(View.GONE);
        } else {
            holder.btnDeleteUser.setVisibility(View.VISIBLE);
        }

        holder.btnDeleteUser.setOnClickListener(v -> {
            Context context = v.getContext();
            DatabaseHelper db = new DatabaseHelper(context);
            boolean success = db.deleteUser(user.getUserId());
            if (success) {
                userList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, userList.size());
                Toast.makeText(context, "User removed successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to remove user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvRole;
        Button btnDeleteUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvRole = itemView.findViewById(R.id.tvRole);
            btnDeleteUser = itemView.findViewById(R.id.btnDeleteUser);
        }
    }
}