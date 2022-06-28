package com.apps.timemanagmentapp.adapters;


import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.apps.timemanagmentapp.R;
import com.apps.timemanagmentapp.objects.TaskObj;

import java.util.List;

public class TasksItemsAdapter extends RecyclerView.Adapter<TasksItemsAdapter.View_Holder> {
    private TasksItemsAdapter.OnitemClickListener mListener;

    public void setOnItemClick() {

    }

    public interface OnitemClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);


    }
    public void setOnItemClick(TasksItemsAdapter.OnitemClickListener listener) {
        mListener = listener;
    }
LayoutInflater layoutInflater;
    List<TaskObj> users;
    public TasksItemsAdapter(Context ctx, List<TaskObj> users) {
        this.layoutInflater= LayoutInflater.from(ctx);
        this.users = users;
    }
    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.tasks_items,parent,false);
        return new View_Holder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {
        TaskObj currentItem = users.get(position);
        holder.title.setText(users.get(position).getTitle());
        holder.date.setText(users.get(position).getDate());
        holder.img.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24);
        holder.imgDelete.setImageResource(R.drawable.ic_baseline_delete_24);
        holder.imgEdit.setImageResource(R.drawable.ic_baseline_edit_24);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
    class View_Holder extends RecyclerView.ViewHolder{
        TextView title,date;
        ImageView img,imgDelete,imgEdit;
        public View_Holder(@NonNull View itemView,final TasksItemsAdapter.OnitemClickListener listener) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.tvTitle);
            date=(TextView)itemView.findViewById(R.id.tvDates);
            img=(ImageView)itemView.findViewById(R.id.imageView);
            imgDelete=(ImageView)itemView.findViewById(R.id.imgDelete);
            imgEdit=(ImageView)itemView.findViewById(R.id.imgEdit) ;
            imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEditClick(position);
                        }
                    }
                }
            });
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }
}


