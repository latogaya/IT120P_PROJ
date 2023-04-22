package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatfunction.MessageActivity;
import com.example.chatfunction.R;
import java.util.List;
import Models.UserModel;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.MyViewHolder> {

    private Context mContext;
    private List<UserModel> mUsers;

    public InboxAdapter(Context mContext, List<UserModel> mUsers){
        this.mUsers = mUsers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public InboxAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.inbox_row, parent, false);
        return new InboxAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InboxAdapter.MyViewHolder holder, int position) {

        UserModel user = mUsers.get(position);
        holder.txtName.setText(user.getUsername());

        if (user.getImageURL().equals("default")){
            holder.profile_pic.setImageResource(R.mipmap.ic_launcher);
        } else{
            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_pic);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtName, txtMsg, txtTime;
        ImageView profile_pic;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            profile_pic = itemView.findViewById(R.id.profilePic);
            txtName = itemView.findViewById(R.id.chatnameTV);
            txtMsg = itemView.findViewById(R.id.lastChatMsgTV);
            txtTime = itemView.findViewById(R.id.timeLastMsgTV);
        }
    }

}
