package com.example.smartlighting;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private Button btnLivingRoom;
    private Button btnKitchen;
    private Button btnBedroom;
    private Button btnSave;
    private RadioGroup rbtns;
    private RadioButton rbtnDay;
    private RadioButton rbtnEvening;
    private RadioButton rbtnNight;
    private ListView listView;
    private ImageView imgLivingRoom;
    private ImageView imgKitchen;
    private ImageView imgBedroom;

    private final String[] COLORS = new String[]{"Biały", "Źółty", "Niebieski"};
    private final String[] ROOMS = new String[]{"Salon", "Kuchnia", "Sypialnia"};
    private final String[] MODES = new String[]{"Tryb Dzienny", "Tryb Wieczorny", "Tryb Nocny"};
    private final int[] PATHS = new int[]{
            R.drawable.star_white,
            R.drawable.star_yellow,
            R.drawable.star_blue
    };
    private int[] colorsId;
    private ImageView[] imgs;
    private Button[] btns;

    private int modeId;

    private final String PREFS = "last_scene";
    private final String MODE_KEY = "mode";

    private ArrayList<String> scenes;
    private ArrayAdapter<String> adapter;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);

        modeId = sharedPreferences.getInt(MODE_KEY, 1);
        colorsId = new int[]{
                sharedPreferences.getInt(ROOMS[0], 1),
                sharedPreferences.getInt(ROOMS[1], 1),
                sharedPreferences.getInt(ROOMS[2], 1),
        };

        initializeViews();

        imgs = new ImageView[]{
                imgLivingRoom,
                imgKitchen,
                imgBedroom
        };

        btns = new Button[]{
                btnLivingRoom,
                btnKitchen,
                btnBedroom
        };

        btnLivingRoom.setOnClickListener(v -> changeColor(0));
        btnKitchen.setOnClickListener(v -> changeColor(1));
        btnBedroom.setOnClickListener(v -> changeColor(2));

        rbtns.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rbtnDay.isChecked()) {
                    modeId = 0;
                } else if (rbtnEvening.isChecked()) {
                    modeId = 1;
                } else if (rbtnNight.isChecked()) {
                    modeId = 2;
                }

                for (ImageView img : imgs) {
                    img.setImageResource(PATHS[modeId]);
                }

                for (int id=0; id<btns.length; id++) {
                    btns[id].setText(ROOMS[id]+": "+COLORS[modeId]);
                    colorsId[id] = modeId;
                }
            }
        });

        scenes = new ArrayList<String>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, scenes);
        listView.setAdapter(adapter);

        btnSave.setOnClickListener(v -> save());


        add();

        for (int id=0; id<btns.length; id++) {
            imgs[id].setImageResource(PATHS[colorsId[id]]);
        }

        for (int id=0; id<btns.length; id++) {
            btns[id].setText(ROOMS[id]+": "+COLORS[colorsId[id]]);
        }
    }

    private void add() {
        String str = MODES[modeId] + ":\n\t" +
                btnLivingRoom.getText().toString() + ",\n\t" +
                btnKitchen.getText().toString() + ",\n\t" +
                btnBedroom.getText().toString();

//        scenes.add(str);
//        adapter.notifyDataSetChanged();
        adapter.insert(str,0 );
    }

    private void save() {
        add();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(MODE_KEY, modeId);
        editor.putInt(ROOMS[0], colorsId[0]);
        editor.putInt(ROOMS[1], colorsId[1]);
        editor.putInt(ROOMS[2], colorsId[2]);

        Toast.makeText(this, "Dodane", Toast.LENGTH_SHORT).show();
    }

    private void changeColor(int id) {
        colorsId[id] = ++colorsId[id] % 3;
        imgs[id].setImageResource(PATHS[colorsId[id]]);
        btns[id].setText(ROOMS[id]+": "+COLORS[colorsId[id]]);
    }

    private void initializeViews() {
        btnLivingRoom = findViewById(R.id.livingRoom);
        btnKitchen = findViewById(R.id.kitchen);
        btnBedroom = findViewById(R.id.bedroom);
        btnSave = findViewById(R.id.save);
        rbtns = findViewById(R.id.rbtns);
        rbtnDay = findViewById(R.id.day);
        rbtnEvening = findViewById(R.id.evening);
        rbtnNight = findViewById(R.id.night);
        listView = findViewById(R.id.listView);
        imgLivingRoom = findViewById(R.id.imgLivingRoom);
        imgKitchen = findViewById(R.id.imgKitchen);
        imgBedroom = findViewById(R.id.imgBedroom);
    }
}