package com.example.ulas_.certainlynottwitter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SimpleRecyclerAdapter extends RecyclerView.Adapter<SimpleRecyclerAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView item_username_lbl;
        public TextView item_message_lbl;
        public TextView item_date_lbl;
        public ImageView imageAvatarItem;
        public CardView card_view;


        public ViewHolder(View itemView) {
            super(itemView);
            item_message_lbl = itemView.findViewById(R.id.item_message_lbl);
            item_username_lbl = itemView.findViewById(R.id.item_username_lbl);
            item_date_lbl = itemView.findViewById(R.id.item_date_lbl);
            imageAvatarItem = itemView.findViewById(R.id.imageAvatarItem);
        }

    }

    private List<Messages> MessagesList;
    CustomItemClickListener listener;

    public  SimpleRecyclerAdapter(List<Messages> messages_list,CustomItemClickListener listener){
        this.MessagesList=messages_list;
        this.listener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout,parent,false);
        final ViewHolder view_holder = new ViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v,view_holder.getPosition());
            }
        });
        return view_holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.item_username_lbl.setText(MessagesList.get(position).getUsername());
        holder.item_message_lbl.setText(MessagesList.get(position).getMessage());
        holder.item_date_lbl.setText(MessagesList.get(position).getDate());

        switch (MessagesList.get(position).getAvatarIndex()){
            case 0:
                holder.imageAvatarItem.setImageResource(R.drawable.avatar1);
                break;
            case 1:
                holder.imageAvatarItem.setImageResource(R.drawable.avatar2);
                break;
            case 2:
                holder.imageAvatarItem.setImageResource(R.drawable.avatar3);
                break;
            case 3:
                holder.imageAvatarItem.setImageResource(R.drawable.avatar4);
                break;
        }


    }

    @Override
    public int getItemCount() {
        return MessagesList.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
