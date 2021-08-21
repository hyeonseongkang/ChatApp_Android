package com.example.chatapp.ChatPage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;

import java.util.List;

public class ChatActivityAdapter extends RecyclerView.Adapter<ChatActivityAdapter.MyViewHolder> {

    private List<Chat> chatDataList;
    private String user;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView time, message;
        public MyViewHolder(View v) {
            super(v);

            message = (TextView) v.findViewById(R.id.message);
            time = (TextView) v.findViewById(R.id.time);
        }
    }

    public ChatActivityAdapter(List<Chat> chatDataList, String user) {
        this.chatDataList = chatDataList;
        this.user = user;
    }

    @Override
    public ChatActivityAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (viewType == 1) {
            v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_chat_adapter_my, parent, false);
        } else {
            v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_chat_adapter_user, parent, false);
        }

        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (chatDataList.get(position).getUser().equals(user)) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.message.setText(chatDataList.get(position).getMessage());
        holder.time.setText(chatDataList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return chatDataList == null ? 0 : chatDataList.size();
    }

}
