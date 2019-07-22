package com.android.RemindMePlease.Fragments;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.android.RemindMePlease.Activities.Reminder_Activity;
import com.android.RemindMePlease.Adapters.ReminderDBAdapter;
import com.android.RemindMePlease.Models.AppData;
import com.android.RemindMePlease.Models.Reminder;
import com.android.RemindMePlease.R;
import java.util.Date;



public class NewReminder extends DialogFragment {
    private View view;
    private TimePicker timePicker;
    private DatePicker datePicker;
    private Button addOrEdit,cancel;
    private EditText reminderDescription;
    private CheckBox isImportant;
    private TextView charCount;
    private int reminderID = -1;
    private boolean isEditing = false;
    private final int maxCharCount = 20;
    private boolean addOrEditButtonEnabled = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_reminder_pop_up, container, false);
        findViewByID();
        onClickListeners();
        reminderDescriptionTextChangedListener();
        reminderID = getArguments().getInt("ReminderID");
        editUI();
        return view;
    }

    private void checkAddOrEditButtonAllowability(int charactersLeft){
        if(charactersLeft!=maxCharCount){
            enableButton();
        }
        else
            disableButton();
    }

    private void disableButton(){
        addOrEditButtonEnabled = false;
        addOrEdit.setTextColor(ContextCompat.getColor(getActivity(), R.color.LightGrey));
        addOrEdit.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.disabled_button));
    }

    private void enableButton(){
        addOrEditButtonEnabled = true;
        addOrEdit.setTextColor(ContextCompat.getColor(getActivity(), R.color.Black));
        addOrEdit.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.enabled_button));
    }

    private void reminderDescriptionTextChangedListener(){
        reminderDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int charactersLeft=maxCharCount - s.toString().length();

                int numLines=reminderDescription.getLineCount();
                if(numLines>1){
                    int length = reminderDescription.getText().length();
                    if(length>0){
                        reminderDescription.getText().delete(length - 1, length);
                        charactersLeft+=1;
                    }
                }
                charCount.setText(String.valueOf(charactersLeft));
                checkAddOrEditButtonAllowability(charactersLeft);
            }
        });
    }


    private void editUI(){
        datePicker.setMinDate(new Date().getTime());

        if(reminderID!=-1){
            addOrEdit.setText(getActivity().getString(R.string.Edit));
            isEditing = true;

            Reminder reminder = AppData.getAllUserReminders().get(reminderID);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timePicker.setHour(reminder.getHour());
                timePicker.setMinute(reminder.getMinute());
            }
            isImportant.setChecked(reminder.isImportant());
            reminderDescription.setText(reminder.getReminderDescription());

            if(reminderDescription.getText().toString().length()!=0)
                addOrEditButtonEnabled = true;
        }

        if(addOrEditButtonEnabled)
            enableButton();
        else
            disableButton();
    }

    private void findViewByID(){
        addOrEdit = view.findViewById(R.id.AddReminder);
        cancel = view.findViewById(R.id.Cancel);
        timePicker = view.findViewById(R.id.TimePicker);
        datePicker = view.findViewById(R.id.DatePicker);
        reminderDescription = view.findViewById(R.id.ReminderDescription);
        isImportant = view.findViewById(R.id.IsImportant);
        charCount = view.findViewById(R.id.CharCount);
    }

    private void onClickListeners () {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        addOrEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addOrEditButtonEnabled) {
                    Reminder reminder;
                    if(!isEditing){
                        reminder = new Reminder(reminderDescription.getText().toString(),isImportant.isChecked());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            reminder.setHour(timePicker.getHour());
                            reminder.setMinute(timePicker.getMinute());
                            reminder.setDay(datePicker.getDayOfMonth());
                            reminder.setMonth(datePicker.getMonth());
                            reminder.setYear(datePicker.getYear());
                        }

                        AppData.getAllUserReminders().add(reminder);
                        ReminderDBAdapter.getInstance(getActivity()).insertReminder(reminder);



                    }else{
                        reminder = AppData.getAllUserReminders().get(reminderID);
                        reminder.setReminderDescription(reminderDescription.getText().toString());
                        reminder.setImportant(isImportant.isChecked());
                        reminder.setDay(datePicker.getDayOfMonth());
                        reminder.setMonth(datePicker.getMonth());
                        reminder.setYear(datePicker.getYear());

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            reminder.setHour(timePicker.getHour());
                            reminder.setMinute(timePicker.getMinute());
                        }

                        ReminderDBAdapter.getInstance(getActivity()).updateReminder(reminder);

                        ((Reminder_Activity)getActivity()).updateRecyclerView();
                    }
                        dismiss();
                }else
                    Toast.makeText(getActivity(), "Please Enter Reminder Description!" , Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof Reminder_Activity) {
            ((Reminder_Activity) activity).handleDialogClose(dialog);
        }
    }
}
