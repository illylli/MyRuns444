package com.example.yuzhong.myruns4;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created by Yuzhong on 2016/4/7.
 */
public class StartFragment extends Fragment{
    private Button mButtonStart;
    private Button mSyncButton;
    private Spinner mInputspinner;
    private Spinner mActivityspinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.start_fragment
                , container, false);

        mInputspinner = (Spinner) view.findViewById(R.id.input_type);

        ArrayAdapter<CharSequence> inputAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.InputTypeSpinner, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        inputAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mInputspinner.setAdapter(inputAdapter);

        mActivityspinner = (Spinner) view.findViewById(R.id.activity_type);

        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.ActivityTypeSpinner, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mActivityspinner.setAdapter(activityAdapter);

        mButtonStart = (Button) view.findViewById(R.id.StartButton);
        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                Log.d("CS65", mInputspinner.getSelectedItem().toString());
                if (mInputspinner.getSelectedItem().toString().equals("Manual Entry")) {
                    i = new Intent(getActivity(),ManualActivity.class);
                    i.putExtra("input_type", mInputspinner.getSelectedItem().toString());
                    i.putExtra("activity_type", mActivityspinner.getSelectedItem().toString());
                } else if(mInputspinner.getSelectedItem().toString().equals("GPS")){
                    i = new Intent(getActivity(),MapDisplayActivity.class);
                    i.putExtra("input_type", mInputspinner.getSelectedItem().toString());
                    i.putExtra("activity_type", mActivityspinner.getSelectedItem().toString());
                } else{
                    i = new Intent(getActivity(),MapDisplayActivity.class);
                    i.putExtra("activity_type", "Unknown");
                    i.putExtra("input_type", mInputspinner.getSelectedItem().toString());
                }
                startActivity(i);
            }
        });
        return view;
    }
}
