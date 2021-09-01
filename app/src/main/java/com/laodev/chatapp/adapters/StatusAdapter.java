package com.laodev.chatapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.laodev.chatapp.R;
import com.laodev.chatapp.fragments.MyStatusFragment;
import com.laodev.chatapp.models.Group;
import com.laodev.chatapp.models.Status;
import com.laodev.chatapp.models.User;
import com.laodev.chatapp.utils.Helper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Status> dataList;
    private MyStatusFragment myStatusFragment;


    public StatusAdapter(Context context, ArrayList<Status> dataList, MyStatusFragment aStatusFragment) {
        this.context = context;
        this.dataList = dataList;
        this.myStatusFragment = aStatusFragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.setData(dataList.get(position));
        holder.itemView.setOnClickListener(v -> myStatusFragment.navigateStatusStories(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView status, name, lastMessage, time;
        private ImageView image, myUserImageOnline;
        private RelativeLayout user_details_container;

        MyViewHolder(View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.emotion);
            name = itemView.findViewById(R.id.user_name);
            time = itemView.findViewById(R.id.time);
            lastMessage = itemView.findViewById(R.id.message);
            image = itemView.findViewById(R.id.user_image);
            user_details_container = itemView.findViewById(R.id.user_details_container);
            myUserImageOnline = itemView.findViewById(R.id.user_image_online);
        }

        private void setData(Status chat) {
            User chatUser = chat.getUser();
            Group chatGroup = chat.getGroup();
            if (chatUser != null && chatUser.getImage() != null && !chatUser.getImage().equalsIgnoreCase("")) {
                Picasso.get()
                        .load(chatUser.getImage())
                        .tag(this)
                        .placeholder(R.drawable.ic_avatar)
                        .into(image);

            } else if (chatGroup != null && chatGroup.getImage() != null && !chatGroup.getImage().equalsIgnoreCase("")) {
                Picasso.get()
                        .load(chatGroup.getImage())
                        .tag(this)
                        .placeholder(R.drawable.ic_avatar)
                        .into(image);
            }

            name.setText(chatUser != null ? chatUser.getNameToDisplay() : chatGroup.getName());
            name.setCompoundDrawablesWithIntrinsicBounds(0, 0, !chat.isRead() ? R.drawable.ring_blue : 0, 0);
            status.setText(chatUser != null ? chatUser.getStatus() : chatGroup.getStatus());
            time.setText(Helper.getTimeAgo(chat.getTimeUpdated(), context));
            lastMessage.setText(chat.getLastMessage());
            lastMessage.setTextColor(ContextCompat.getColor(context, !chat.isRead() ? R.color.textColorPrimary : R.color.textColorSecondary));

            user_details_container.setBackgroundColor(ContextCompat.getColor(context, (chat.isSelected() ? R.color.bg_gray : R.color.colorIcon)));

            try {
                if (chatUser != null && chatUser.isOnline()) {
                    myUserImageOnline.setVisibility(View.VISIBLE);
                    lastMessage.setCompoundDrawablesWithIntrinsicBounds(0, 0, chatUser.isOnline() ? R.drawable.ring_green : 0, 0);
                } else {
                    myUserImageOnline.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
