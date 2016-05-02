package com.example.yuzhong.myruns4;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ManualActivity extends Activity {
   private ListView list ;
    Calendar mDateAndTime = Calendar.getInstance();
    private HistoryEntry mHistroyEntry;
    private String mDate;
    private String mTime;
    private final double MILESCONVERTTOKILOMETERS = 1.609344;
//    TextView mDisplayDateTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        Intent intent = getIntent();
        mHistroyEntry = new HistoryEntry();
        mHistroyEntry.setmInputType(intent.getStringExtra("input_type"));
        mHistroyEntry.setmActivityType(intent.getStringExtra("activity_type"));
        mHistroyEntry.setmDateTime(new SimpleDateFormat("HH:mm:ss MMM dd yyyy").format(new Date()));
        mDate = new SimpleDateFormat("MMM dd yyyy").format(new Date());
        mTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        mHistroyEntry.setmDuration(0);

        list = (ListView) findViewById(R.id.ManualList);
        ArrayAdapter<CharSequence> manualInputAdapter = ArrayAdapter.createFromResource(this,
                R.array.ManualInput, android.R.layout.simple_list_item_1);
        list.setAdapter(manualInputAdapter);
        // choose different dialogues
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (position == 0) {
                    onDateClicked();
                }

                else if (position == 1) {
                    onTimeClicked();
                }

                else {
                    onInputClicked(position);
                }
            }
        });
    }

    public void onSaveClick(View v){
        mHistroyEntry.setmDateTime(mTime + " " + mDate);
        SaveToDatabase saveToDatabase = new SaveToDatabase(this, mHistroyEntry);
        saveToDatabase.execute();

//        HistoryFragment fragment = (HistoryFragment) getFragmentManager().findFragmentById(1);
//        fragment.refresh();

        finish();
    }

    public void onCancelClick(View v){
        finish();
    }

    public void onDateClicked(){

        DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mDateAndTime.set(Calendar.YEAR, year);
                mDateAndTime.set(Calendar.MONTH, monthOfYear);
                mDateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mDate = new SimpleDateFormat("MMM dd yyyy").format(mDateAndTime.getTime());
            }
        };
        new DatePickerDialog(this, mDateListener,
                mDateAndTime.get(Calendar.YEAR),
                mDateAndTime.get(Calendar.MONTH),
                mDateAndTime.get(Calendar.DAY_OF_MONTH)).show();
    }
    public void onTimeClicked() {

        TimePickerDialog.OnTimeSetListener mTimeListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mDateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mDateAndTime.set(Calendar.MINUTE, minute);
                mTime = new SimpleDateFormat("HH:mm:ss").format(mDateAndTime.getTimeInMillis());
            }
        };
        new TimePickerDialog(this, mTimeListener,
                mDateAndTime.get(Calendar.HOUR_OF_DAY),
                mDateAndTime.get(Calendar.MINUTE), true).show();
    }

    public void onInputClicked(int pos){
        showEditBox(pos);
    }

    void showEditBox(int pos) {
        DialogFragment newFragment = MyEditBoxFragment
                .newInstance(pos);
        newFragment.show(getFragmentManager(), "dialog");
    }

    public void doPositiveClick(int title, EditText editText) {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Positive click!");
        switch (title) {
            case 2: //Duration
                double num = Double.parseDouble(editText.getText().toString());
                int intNum = (int) (num * 60);
                mHistroyEntry.setmDuration(intNum);
                break;
            case 3: //Distance
                String unit = PreferenceManager.getDefaultSharedPreferences(this).getString("list_preference", "Miles");
                Double distance = Double.parseDouble(editText.getText().toString());
                if(unit.equals("Imperial(Miles)")) distance *= MILESCONVERTTOKILOMETERS;

                mHistroyEntry.setmDistance(distance);
                break;
            case 4:
                mHistroyEntry.setmCalorie(Integer.parseInt(editText.getText().toString()));
                break;
            case 5:
                mHistroyEntry.setmHeartRate(Integer.parseInt(editText.getText().toString()));
                break;
            case 6:
                mHistroyEntry.setmComment(editText.getText().toString());
                break;
            default:
                break;
        }
    }

    public void doNegativeClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Negative click!");
    }

    public static class MyEditBoxFragment extends DialogFragment {

        public static MyEditBoxFragment newInstance(int title) {
            MyEditBoxFragment frag = new MyEditBoxFragment();
            Bundle args = new Bundle();
            args.putInt("title", title);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final int title = getArguments().getInt("title");
            final EditText editText = new EditText(getActivity());
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            switch (title) {
                case 2:
                    alert.setTitle("Duration");
                    break;
                case 3:
                    alert.setTitle("Distance");
                    break;
                case 4:
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    alert.setTitle("Calories");
                    break;
                case 5:
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    alert.setTitle("Heart Rate");
                    break;
                case 6:
                    alert.setTitle("Comment");
                    editText.setHint("How did it go? Notes here.");
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                default:
                    break;
            }
            
            alert.setView(editText);
            alert.setPositiveButton(R.string.alert_dialog_ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            ((ManualActivity) getActivity())
                                    .doPositiveClick(title, editText);
                        }
                    });
            alert.setNegativeButton(R.string.alert_dialog_cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            ((ManualActivity) getActivity())
                                    .doNegativeClick();
                        }
                    });
            return alert.create();
        }
    }
}

