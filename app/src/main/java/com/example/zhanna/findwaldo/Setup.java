package com.example.zhanna.findwaldo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class Setup extends AppCompatActivity {

    private Spinner spinParticipant, spinSession, spinGroup, spinCondition, spinBlock;

    String[] participantCode = {"P01", "P02", "P03", "P04", "P05", "P06", "P07", "P08",
            "P09", "P10", "P11", "P12", "P13", "P14", "P15", "P16", "P17", "P18", "P19", "P20",
            "P21", "P22", "P23", "P24", "P25", "P99"};
    String[] sessionCode = {"S01", "S02"};
    String[] groupCode = {"G01", "G02"};
    String[] conditionCode = {"C01", "C02"};
    String[] blockCode = {"B01", "B02", "B03", "B04", "B05", "B06", "B07", "B08", "B09", "B10"};

    SharedPreferences sharedPrefs;

    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        sharedPrefs = this.getPreferences(MODE_PRIVATE);

        participantCode[0] = sharedPrefs.getString("participantCode", participantCode[0]);
        sessionCode[0] = sharedPrefs.getString("sessionCode", sessionCode[0]);

        groupCode[0] = sharedPrefs.getString("groupCode", groupCode[0]);
        conditionCode[0] = sharedPrefs.getString("conditionCode", conditionCode[0]);
        blockCode[0] = sharedPrefs.getString("blockCode", blockCode[0]);

        // get references to widget elements
        spinParticipant = (Spinner) findViewById(R.id.paramParticipant);
        spinSession = (Spinner) findViewById(R.id.paramSession);
        spinGroup = (Spinner) findViewById(R.id.paramGroup);
        spinCondition = (Spinner) findViewById(R.id.paramCondition);
        spinBlock = (Spinner) findViewById(R.id.paramBlock);

        // initialise spinner adapters

        ArrayAdapter<CharSequence> adapterPC = new ArrayAdapter<CharSequence>(this, R.layout
                .spinnerstyle,
                participantCode);
        spinParticipant.setAdapter(adapterPC);

        ArrayAdapter<CharSequence> adapterSS = new ArrayAdapter<CharSequence>(this, R.layout
                .spinnerstyle, sessionCode);
        spinSession.setAdapter(adapterSS);

        ArrayAdapter<CharSequence> adapterG = new ArrayAdapter<CharSequence>(this, R.layout
                .spinnerstyle, groupCode);
        spinGroup.setAdapter(adapterG);

        ArrayAdapter<CharSequence> adapterC = new ArrayAdapter<CharSequence>(this, R.layout
                .spinnerstyle, conditionCode);
        spinCondition.setAdapter(adapterC);

        ArrayAdapter<CharSequence> adapterB = new ArrayAdapter<CharSequence>(this, R.layout
                .spinnerstyle, blockCode);
        spinBlock.setAdapter(adapterB);


    }

    public void clickOK(View view) {

        clickSave(view);
        SharedPreferences prefs;
        prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("HEADERS", false);
        editor.commit();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);

    }

    public void clickSave(View view) {
        SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).edit();
        editor.putString("participantCode", participantCode[spinParticipant.getSelectedItemPosition()]);
        editor.putString("sessionCode", sessionCode[spinSession.getSelectedItemPosition()]);
        editor.putString("groupCode", groupCode[spinGroup.getSelectedItemPosition()]);
        editor.putString("conditionCode", conditionCode[spinCondition.getSelectedItemPosition()]);
        editor.putString("blockCode", blockCode[spinBlock.getSelectedItemPosition()]);
        editor.commit();
        Toast.makeText(this, "Preferences saved!", Toast.LENGTH_SHORT).show();
    }

    public void clickExit(View view) {
//        super.onDestroy(); // cleanup
        this.finish(); // terminate
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


}
