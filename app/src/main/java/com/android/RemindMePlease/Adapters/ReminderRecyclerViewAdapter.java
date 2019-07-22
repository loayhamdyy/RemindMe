package com.android.RemindMePlease.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.RemindMePlease.Activities.Reminder_Activity;
import com.android.RemindMePlease.Fragments.NewReminder;
import com.android.RemindMePlease.Models.AppData;
import com.android.RemindMePlease.Models.Reminder;
import com.android.RemindMePlease.R;
import java.util.ArrayList;



public class ReminderRecyclerViewAdapter extends RecyclerView.Adapter<ReminderRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<Reminder> userReminders;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView reminderDescription,reminderDetails,itemOptions;
        private ImageView isImportant;

        MyViewHolder(View view) {

            super(view);

            reminderDescription =  view.findViewById(R.id.reminderDescription);
            reminderDetails =  view.findViewById(R.id.reminderDetails);
            isImportant = view.findViewById(R.id.important);
            itemOptions = view.findViewById(R.id.itemOptions);
        }
    }

    public ReminderRecyclerViewAdapter(ArrayList<Reminder> userReminders,Context context) {
        this.userReminders=userReminders;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_item, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position){
        holder.reminderDescription.setText(userReminders.get(position).getReminderDescription());

        String date = AppData.getMonths()[userReminders.get(position).getMonth()]+", "+userReminders.get(position).getDay()+", "+userReminders.get(position).getYear();
        holder.reminderDetails.setText(date);

        if(userReminders.get(position).isImportant())
            holder.isImportant.setVisibility(View.VISIBLE);
        else
            holder.isImportant.setVisibility(View.GONE);

        holder.itemOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(context, holder.itemOptions);
                popup.inflate(R.menu.reminder_options);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.EditReminder:
                                NewReminder newReminder = new NewReminder();
                                Bundle bundle = new Bundle();
                                bundle.putInt("ReminderID",position);
                                newReminder.setArguments(bundle);
                                newReminder.show(((Reminder_Activity)context).getSupportFragmentManager(),"NewReminder");
                                break;
                            case R.id.DeleteReminder:
                                ReminderDBAdapter.getInstance(context).deleteReminderById(userReminders.get(position));
                                userReminders.remove(position);
                                ((Reminder_Activity)context).updateRecyclerView();
                                break;
                        }
                        return false;
                    }
                });
                popup.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return userReminders.size();
    }
}