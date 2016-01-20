package com.example.zhanna.findwaldo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
*  viewTouchX, viewTouchY - X, Y start position of the touched view
*  viewCenterX, viewCenterY - Center of the touched view/grid
*  touchX, touchY - coordinates of the actual touch
*
* **/

public class DisplayGrid extends AppCompatActivity {

    final String WORKING_DIRECTORY = "/FindWaldoData/";
    final String HEADER = "TimeStamp,Date,Participant,Session,Group,Condition,"
            + "Time(ms),ActualGridPosition,SelectedGridPosition,PassedDrawableID,SelectedDrawableID,PassedIconName,SelectedIconName,StartViewTouchX,StartViewTouchY,IconCenterX,IconCenterY,TouchX,TouchY,WrongHit\n";
    public static final String MyPREFERENCES = "MyPrefs";

    int counter = 0;
    File file;
    BufferedWriter bufferedWriter;
    StringBuilder stringBuilder;

    SharedPreferences prefs;
    String participantCode, sessionCode, groupCode, conditionCode;

    GridView gridView;
    GridViewCustomAdapter gridViewCustomAdapter;

    MainActivity.CellContent passedIcon;
    ArrayList<MainActivity.CellContent> iconsCopy = new ArrayList<MainActivity.CellContent>(MainActivity.icons);
    int passedPosition;

    Long startTime, endTime, diff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_grid);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            passedIcon = (MainActivity.CellContent) extras.getSerializable("iconToFind");
            passedPosition = extras.getInt("positionToPlace");
        }
        Log.d("AAA CC passedIcon", passedIcon.getName());
        Log.d("AAA CC passedPosition", "" + passedPosition);
        for (MainActivity.CellContent c : iconsCopy) {
            Log.d("FFF BB", c.getName() + " ");
        }

        startTime = System.currentTimeMillis();

        prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        participantCode = prefs.getString("participantCode", "");
        sessionCode = prefs.getString("sessionCode", "");
        groupCode = prefs.getString("groupCode", "");
        conditionCode = prefs.getString("conditionCode", "");
        Log.d("prefs", participantCode + sessionCode + groupCode + conditionCode);

        gridView = (GridView) findViewById(R.id.gridViewCustom);
        // Create the Custom Adapter Object
        gridViewCustomAdapter = new GridViewCustomAdapter(this, MainActivity.icons, passedPosition, passedIcon);
        // Set the Adapter to GridView
        gridView.setAdapter(gridViewCustomAdapter);

        File dataDirectory = new File(Environment.getExternalStorageDirectory() +
                WORKING_DIRECTORY);
        if (!dataDirectory.exists() && !dataDirectory.mkdirs()) {
            Log.e("MYDEBUG", "Failed to create directory: " + WORKING_DIRECTORY);
            Toast.makeText(this, "Couldn't create directory", Toast.LENGTH_SHORT).show();
            System.exit(0);
        }

        String base = "FindWaldo-" + participantCode + "-" + sessionCode + "-" +
                groupCode + "-" + conditionCode;

        file = new File(dataDirectory, base + ".csv");

        try {

            bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            if (!prefs.getBoolean("HEADERS", false)) {
                bufferedWriter.append(HEADER, 0, HEADER.length());
                bufferedWriter.flush();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("HEADERS", true);
                editor.commit();
            }

        } catch (IOException e) {
            Log.i("MYDEBUG", "Error opening data files! Exception: " + e.toString());
            System.exit(0);
        }

        gridView.setOnTouchListener(new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            // check that the even onUP (when the release the finger)
                                            //
                                            switch (event.getAction()) {
                                                case MotionEvent.ACTION_DOWN:
                                                    Log.d("DOWN", "DOWN");
                                                    break;
                                                case MotionEvent.ACTION_MOVE:

                                                    break;

                                                case MotionEvent.ACTION_UP:
                                                    Log.d("UP", "UP");
                                                    //record x, y
                                                    float x = event.getX();
                                                    float y = event.getY();
                                                    SharedPreferences.Editor editor = prefs.edit();
                                                    editor.putFloat("TouchX", x);
                                                    editor.putFloat("TouchY", y);
                                                    editor.commit();

                                                    // log them and see if the values are the same if hit the center
                                                    Log.d("TouchX", "" + x);
                                                    Log.d("TouchY", "" + y);
                                                    break;
                                            }
                                            return false;
                                        }
                                    }

        );


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.CellContent selected = iconsCopy.get(position); // iconsCopy[position] is another icon, should just remove selected item from here
                Log.d("AAA DD selectedIcon", selected.getName() + " " + position + " " + passedPosition);
                Log.d("AAA DD position", position+"");
                // get shared prefs: x and y
                float touchX = prefs.getFloat("TouchX", 0);
                float touchY = prefs.getFloat("TouchY", 0);
                // get center of the view (cell of grid)
                float viewCenterX = view.getWidth() / 2;
                float viewCenterY = view.getHeight() / 2;
                // coordinates of the iconview
                float viewTouchX = view.getX();
                float viewTouchY = view.getY();
                Log.d("coordinates", ""+viewTouchX+" "+viewTouchY);

                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();
                String date = DateFormat.getDateTimeInstance().format(new Date());
                endTime = System.currentTimeMillis();
                diff = endTime - startTime;

                stringBuilder = new StringBuilder();
                if (passedPosition == position) {
                    // StartViewTouchX,StartViewTouchY,IconCenterX,IconCenterY,TouchX,TouchY
                    Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                    stringBuilder.append(String.format("%s,%s,%s,%s,%s,%s,%s,%d,%d,%s,%s,%s,%s,%f,%f,%f,%f,%f,%f,%d\n", ts, date, participantCode,
                            sessionCode, groupCode, conditionCode, diff.toString(), passedPosition, position, passedIcon.getDrawableID(),selected.getDrawableID(),
                            passedIcon.getName(),selected.getName(),
                            viewTouchX, viewTouchY, viewCenterX, viewCenterY, touchX, touchY,counter));
                    try {
                        bufferedWriter.write(stringBuilder.toString(), 0, stringBuilder.length());
                        bufferedWriter.flush();
                    } catch (IOException e) {
                        Log.d("MYDEBUG", "ERROR WRITING TO DATA FILES: e = " + e);
                    }
                    stringBuilder.delete(0, stringBuilder.length());
                    finish();
                } else {
                    stringBuilder.append(String.format("%s,%s,%s,%s,%s,%s,%s,%d,%d,%s,%s,%s,%s,%f,%f,%f,%f,%f,%f,%d\n", ts, date, participantCode,
                            sessionCode, groupCode, conditionCode, diff.toString(), passedPosition, position, passedIcon.getDrawableID(), selected.getDrawableID(),
                            passedIcon.getName(), selected.getName(),
                            viewTouchX, viewTouchY, viewCenterX, viewCenterY, touchX, touchY,counter));
                    try {
                        bufferedWriter.write(stringBuilder.toString(), 0, stringBuilder.length());
                        bufferedWriter.flush();
                    } catch (IOException e) {
                        Log.d("MYDEBUG", "ERROR WRITING TO DATA FILES: e = " + e);
                    }
                    stringBuilder.delete(0, stringBuilder.length());
                    Toast.makeText(getApplicationContext(), "Incorrect", Toast.LENGTH_SHORT).show();
                    counter++;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            Log.d("MYDEBUG", "ERROR CLOSING THE DATA FILES: e = " + e);
        }
        finish();
    }


}
