package com.example.zhanna.findwaldo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST = 12;

    Button continueBtn;
    ImageView imageToShow;

    public static ArrayList<CellContent> icons = new ArrayList<CellContent>();
    CellContent iconToFind;
    int posToFind;
    String iconName;
    TextView iconNameTextView, findIconText;

    public static HashMap<CellContent, Integer> iconsMap = new HashMap<>();

    Long startTime, endTime, diff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateArrayList();
        SharedPreferences prefs = getSharedPreferences(DisplayGrid.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("FIRSTTIME", true);
        editor.commit();

        requestPermissions();

    }

    public void initializeHash() {
        SharedPreferences prefs = getSharedPreferences(DisplayGrid.MyPREFERENCES, Context.MODE_PRIVATE);
        if (iconsMap.isEmpty()) {
            ArrayList<Integer> pos = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
                    11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23));
            int i = 0;
            Collections.shuffle(pos, new Random(System.nanoTime()));
            for (CellContent c : icons) {
                iconsMap.put(c, pos.get(i));
                i++;
            }
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("FIRSTTIME", false);
            editor.commit();
        }
    }

    // create an ArrayList of 24 elements
    public void populateArrayList() {
        icons.clear();
        icons.add(new CellContent("Adobe Acrobat", R.drawable.adobereader));
        icons.add(new CellContent("Angry Birds", R.drawable.angrybirds));
        icons.add(new CellContent("Candy Crush Saga", R.drawable.candycrush));
        icons.add(new CellContent("ChatON", R.drawable.chaton));
        icons.add(new CellContent("Chrome", R.drawable.chrome));
        icons.add(new CellContent("Drive", R.drawable.drive));
        icons.add(new CellContent("Dropbox", R.drawable.dropbox));
        icons.add(new CellContent("Facebook", R.drawable.facebook));
        icons.add(new CellContent("Fruit Ninja", R.drawable.fruitninja));
        icons.add(new CellContent("Gmail", R.drawable.gmail));
        icons.add(new CellContent("Maps", R.drawable.googlemaps));
        icons.add(new CellContent("Google+", R.drawable.googleplus));
        icons.add(new CellContent("Hangouts", R.drawable.hangouts));
        icons.add(new CellContent("Instagram", R.drawable.instagram));
        icons.add(new CellContent("Line", R.drawable.line));
        icons.add(new CellContent("Messenger", R.drawable.messenger));
        icons.add(new CellContent("Shazam", R.drawable.shazam));
        icons.add(new CellContent("Skype", R.drawable.skype));
        icons.add(new CellContent("Temple Run", R.drawable.templerun));
        icons.add(new CellContent("Translate", R.drawable.translate));
        icons.add(new CellContent("Twitter", R.drawable.twitter));
        icons.add(new CellContent("Viber", R.drawable.viber));
        icons.add(new CellContent("WhatsApp", R.drawable.whatsapp));
        icons.add(new CellContent("YouTube", R.drawable.youtube));
    }

    public static class CellContent implements Serializable {

        private int drawableID;
        private String name;

        public CellContent(String nName, int nDrawableID) {
            name = nName;
            drawableID = nDrawableID;
        }

        public String getName() {
            return name;
        }

        public int getDrawableID() {
            return drawableID;
        }

        @Override
        public boolean equals(Object o) {
            // compare drawable id
            if (o.getClass().equals(this.getClass())) {
                CellContent obj = (CellContent) o;
                if (this.getDrawableID() == obj.getDrawableID()) {
                    return true;
                }
            }
            return false;
        }
    }

//    Handler handler;
    @Override
    protected void onResume() {
        super.onResume();
        continueBtn = (Button) findViewById(R.id.continue_btn);
        iconNameTextView = (TextView) findViewById(R.id.icon_text_view);
        imageToShow = (ImageView) findViewById(R.id.icon_img);
        findIconText = (TextView) findViewById(R.id.find_icon_text);

        continueBtn.setVisibility(View.VISIBLE);
        iconNameTextView.setVisibility(View.VISIBLE);
        imageToShow.setVisibility(View.VISIBLE);
        findIconText.setVisibility(View.VISIBLE);

        startTime = System.currentTimeMillis();

        SharedPreferences prefs = getSharedPreferences(DisplayGrid.MyPREFERENCES, Context.MODE_PRIVATE);
        if (iconsMap.isEmpty() && !prefs.getBoolean("FIRSTTIME", true)) {
            Intent intentSensorService = new Intent(this, SensorsService.class);
            stopService(intentSensorService);
            finish();
        } else {
            initializeHash();

            Random r = new Random(System.nanoTime());
            int random  = r.nextInt(iconsMap.size());

            CellContent temp = new CellContent("", 0);
            int index = 0;
            findKey:
            for (CellContent key : iconsMap.keySet()) {
                if (index==random) {
                    posToFind = iconsMap.get(key);
                    iconToFind = key;
                    temp = key;
                    imageToShow.setImageResource(key.getDrawableID());
                    iconName = key.getName();
                    iconNameTextView.setText(key.getName());
                    break findKey;
                }
                index++;
            }


            iconsMap.remove(temp);

//            handler = new Handler();
//            handler.postDelayed(new Runnable() {
//
//                public void run() {
//                    Intent intent = new Intent(getBaseContext(), DisplayGrid.class);
//                    intent.putExtra("iconToFind", iconToFind);
//                    intent.putExtra("positionToPlace", posToFind);
//                    intent.putExtra("iconName", iconName);
//                    startActivity(intent);
//                }
//
//            }, 1000);

            continueBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    endTime = System.currentTimeMillis();
                    diff = endTime-startTime;


                    //WITHOUT BLANK SCREEN
                    Intent intent = new Intent(getBaseContext(), DisplayGrid.class);
                    intent.putExtra("iconToFind", iconToFind);
                    intent.putExtra("positionToPlace", posToFind);
                    intent.putExtra("iconName", iconName);
                    intent.putExtra("timeToRemember", diff);
                    startActivity(intent);

                    Log.d("timeToRemember", ""+diff);

                    //BLANK SCREEN
//                    continueBtn.setVisibility(View.INVISIBLE);
//                    iconNameTextView.setVisibility(View.INVISIBLE);
//                    imageToShow.setVisibility(View.INVISIBLE);
//                    findIconText.setVisibility(View.INVISIBLE);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            Intent intent = new Intent(getBaseContext(), DisplayGrid.class);
//                            intent.putExtra("iconToFind", iconToFind);
//                            intent.putExtra("positionToPlace", posToFind);
//                            intent.putExtra("iconName", iconName);
//                            intent.putExtra("timeToRemember", diff);
//                            startActivity(intent);
//
//                        }
//                    },
//                    300);


                }
            });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intentSensorService = new Intent(this, SensorsService.class);
                    startService(intentSensorService);

                } else {
                    requestPermissions();

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void requestPermissions()
    {
//        Log.d("TAG", "Whatever1");
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
//            Log.d("TAG", "Whatever2");

            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST);

        }
        else
        {
//            Log.d("TAG", "Whatever3");
            Intent intentSensorService = new Intent(this, SensorsService.class);
            startService(intentSensorService);
        }
    }


}
