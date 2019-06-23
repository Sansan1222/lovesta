package com.example.lowestaachat.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lowestaachat.R;
import com.example.lowestaachat.models.ModelChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder>{

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    Context context;
    List<ModelChat> chatList;
    String imageUr1;
    FirebaseUser fUser;


    public AdapterChat(Context context, List<ModelChat> chatList, String imageUr1) {
        this.context = context;
        this.chatList = chatList;
        this.imageUr1 = imageUr1;


    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (i == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_right,viewGroup,false);
            return new MyHolder(view);

        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left,viewGroup,false);
            return new MyHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myholder, int i) {
        String message = chatList.get(i).getMessage();
        String timeStamp = chatList.get(i).getTimestamp();

        //convert time stamp to dd/mm/yyyy hh:mm am/pm
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa",cal).toString();

        myholder.messageTv.setText(message);
        myholder.timeTv.setText(dateTime);

        try {
            Picasso.get().load(imageUr1).into(myholder.profileIv);
        }catch (Exception e){

        }
        if (i== chatList.size()-1){
            if (chatList.get(i).isSeen()){
                myholder.isSeenTv.setText("Görüldü");
            }else {
                myholder.isSeenTv.setText("İletildi");
            }
        }else {
            myholder.isSeenTv.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSender().equals(fUser.getUid())){
            return  MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }

    //view
    class MyHolder extends RecyclerView.ViewHolder{

        //views

        ImageView profileIv;
        TextView messageTv, timeTv, isSeenTv;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //init views
            profileIv = itemView.findViewById(R.id.profileIv);
            messageTv = itemView.findViewById(R.id.messageTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            isSeenTv = itemView.findViewById(R.id.isSeentTv);

        }
    }
}
