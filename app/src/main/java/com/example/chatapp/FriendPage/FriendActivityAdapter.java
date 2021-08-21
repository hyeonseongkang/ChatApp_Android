package com.example.chatapp.FriendPage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Image;
import com.example.chatapp.R;
import com.example.chatapp.User;

import java.util.List;

public class FriendActivityAdapter  extends RecyclerView.Adapter<FriendActivityAdapter.MyViewHolder>{

    private List<User> userList;
    static public View.OnClickListener onClick;
    private Image image = new Image();

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView profile;

        public MyViewHolder (View v, OnItemClickListener listener) {
            super(v);

            name = (TextView) v.findViewById(R.id.name);
            profile = (ImageView) v.findViewById(R.id.profile);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public FriendActivityAdapter(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public FriendActivityAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_friend_adapter, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(v, mListener);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(userList.get(position).getName());
        holder.profile.setImageBitmap(image.StringToBitMap(userList.get(position).getProfile()));
    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }

}
