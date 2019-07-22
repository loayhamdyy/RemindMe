package com.android.RemindMePlease.Activities;

import android.support.v4.app.FragmentTransaction;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import com.android.RemindMePlease.Adapters.ReminderDBAdapter;
import com.android.RemindMePlease.Adapters.ReminderRecyclerViewAdapter;
import com.android.RemindMePlease.Fragments.NewReminder;
import com.android.RemindMePlease.Models.AppData;
import com.android.RemindMePlease.R;
import com.android.RemindMePlease.Utils.MyDialogCloseListener;

public class Reminder_Activity extends AppCompatActivity implements MyDialogCloseListener {
    private RecyclerView userReminders;
    private ReminderRecyclerViewAdapter remindersAdapter;
    private ReminderDBAdapter reminderDBAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        findViewByID();
        initializeMenuToolbar();
        initializeDB();
        fetchUserReminders();
        configureRecyclerViews();
    }

    private void initializeDB(){
        reminderDBAdapter = ReminderDBAdapter.getInstance(this);
        reminderDBAdapter.open();
    }

    private void fetchUserReminders(){
        reminderDBAdapter.fetchAllReminders();
    }

    private void findViewByID(){
        userReminders = findViewById(R.id.UserRemindersRecyclerView);
    }

    private void initializeMenuToolbar(){
        try{
            getSupportActionBar().show();
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean  onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reminder_toolbar, menu);
        super.onCreateOptionsMenu(menu);
        MenuItem options = menu.findItem(R.id.action_settings);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.NewReminder:
                NewReminder newReminder = new NewReminder();
                Bundle bundle = new Bundle();
                bundle.putInt("ReminderID",-1);
                newReminder.setArguments(bundle);
                newReminder.show(getSupportFragmentManager(),"NewReminder");
                return true;
            case R.id.Exit:
                reminderDBAdapter.close();
                finishAffinity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void configureRecyclerViews(){
        remindersAdapter = new ReminderRecyclerViewAdapter(AppData.getAllUserReminders(),this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        userReminders.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(this.getResources().getDrawable(R.drawable.rectangle));
        userReminders.addItemDecoration(dividerItemDecoration);

        userReminders.setAdapter(remindersAdapter);
    }

    public void updateRecyclerView(){
        remindersAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume(){
        super.onResume();
        updateRecyclerView();
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        updateRecyclerView();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        reminderDBAdapter.close();
    }
}
