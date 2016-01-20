package com.example.zhanna.findwaldo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    Button continueBtn;
    ImageView imageToShow;

    public static ArrayList<CellContent> icons = new ArrayList<CellContent>();
    CellContent iconToFind;
    int posToFind;
    String iconName;
    TextView iconNameTextView;

    public static HashMap<CellContent, Integer> iconsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateArrayList();
        SharedPreferences prefs = getSharedPreferences(DisplayGrid.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("FIRSTTIME", true);
        editor.commit();
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

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences(DisplayGrid.MyPREFERENCES, Context.MODE_PRIVATE);
        if (iconsMap.isEmpty() && !prefs.getBoolean("FIRSTTIME", true)) {
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
                    imageToShow = (ImageView) findViewById(R.id.icon_img);
                    imageToShow.setImageResource(key.getDrawableID());
                    iconName = key.getName();
                    iconNameTextView = (TextView) findViewById(R.id.icon_text_view);
                    iconNameTextView.setText(key.getName());
                    Log.d("AAA posToFind", "" + posToFind);
                    Log.d("AAA keyName", "" + key.getName());
                    Log.d("AAA iconToFind", "" + iconToFind.getName());
                    break findKey;
                }
                index++;
            }

            for (MainActivity.CellContent c : icons) {
                Log.d("FFF AA", c.getName() + " ");
            }

            continueBtn = (Button) findViewById(R.id.continue_btn);

            iconsMap.remove(temp);
            for (CellContent c : iconsMap.keySet()) {
                Log.d("iconsMap", iconsMap.get(c) + "");
            }
            Log.d("iconsMapSize", iconsMap.size() + "");
            continueBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), DisplayGrid.class);
                    intent.putExtra("iconToFind", iconToFind);
                    intent.putExtra("positionToPlace", posToFind);
                    intent.putExtra("iconName", iconName);
                    startActivity(intent);
                    Log.d("AAA BB posToFind", "" + posToFind);
                    Log.d("AAA BB iconToFind", "" + iconToFind.getName());
                }
            });
        }

    }
}
