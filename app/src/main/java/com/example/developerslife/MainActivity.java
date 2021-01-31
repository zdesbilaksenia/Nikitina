package com.example.developerslife;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    int currentGIF = 0;
    int currentTab = 0;

    LiveData<ArrayList<Result>> list;
    Map<Integer, ArrayList<Result>> hashMap = new HashMap<Integer, ArrayList<Result>>();
    int[] pages = new int[] {0,0,0};
    String[] cat = new String[] {"latest", "hot", "top"};

    Logic model;
    ImageButton previous;
    ImageButton next;
    ImageView gif;
    TextView description;
    TextView empty;
    Parsing parser;
    ProgressBar progressBar;
    Animation anim;
    Button restart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentGIF = 0;
                previous.setVisibility(View.INVISIBLE);
                currentTab = tab.getPosition();
                if (hashMap.get(tab.getPosition()) == null) {
                    progressBar.setVisibility(View.VISIBLE);
                    parser.ParseJSON(cat[tab.getPosition()], 0);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    loadGIF();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }
        });

        gif = findViewById(R.id.GIFcontainer);
        description = findViewById(R.id.description);
        progressBar = findViewById(R.id.progress_bar);
        anim = AnimationUtils.loadAnimation(this, R.anim.scale);

        model = new ViewModelProvider(this).get(Logic.class);
        list = model.getLiveData("latest", 0);list.observe(this, new Observer<ArrayList<Result>>() {
            @Override
            public void onChanged(ArrayList<Result> results) {
                if (results == null){
                    loadError();
                } else {
                    ArrayList<Result> temp = new ArrayList<>();
                    if (hashMap.get(currentTab) != null) {
                        temp = hashMap.get(currentTab);
                    }
                    for (int i = 0; i < results.size(); i++) {
                        temp.add(results.get(i));
                    }
                    hashMap.put(currentTab, temp);
                    loadGIF();
                }
            }
        });

        parser = new Parsing(model);
        empty = findViewById(R.id.empty);

        progressBar.setVisibility(View.VISIBLE);
        parser.ParseJSON("latest", 0);

        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(anim);
                progressBar.setVisibility(View.VISIBLE);
                currentGIF++;
                if (currentGIF == hashMap.get(currentTab).size()){
                    pages[currentTab]++;
                    gif.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    parser.ParseJSON(cat[currentTab], pages[currentTab]);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    loadGIF();
                }
                previous.setVisibility(View.VISIBLE);
            }
        });

        previous = findViewById(R.id.previous);
        previous.setVisibility(View.INVISIBLE);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(anim);
                currentGIF--;
                progressBar.setVisibility(View.VISIBLE);
                loadGIF();
                if (currentGIF == 0) v.setVisibility(View.INVISIBLE);
            }
        });

    }

    public void loadGIF(){

        gif.setVisibility(View.VISIBLE);
        restart.setVisibility(View.INVISIBLE);

        if (hashMap.get(currentTab).size() != 0) {

            description.setVisibility(View.VISIBLE);
            next.setVisibility(View.VISIBLE);
            empty.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

            if (currentGIF != 0) previous.setVisibility(View.VISIBLE);

            Glide.with(MainActivity.this).asGif().load(hashMap.get(currentTab).get(currentGIF).getGifURL()).into(gif);
            description.setText(hashMap.get(currentTab).get(currentGIF).getDescription());

        } else {

            empty.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            gif.setVisibility(View.INVISIBLE);
            description.setVisibility(View.INVISIBLE);
            next.setVisibility(View.INVISIBLE);
            previous.setVisibility(View.INVISIBLE);

            empty.setText("Здесь ничего нет");

        }
    }

    public void loadError(){

        restart = findViewById(R.id.restart);
        restart.setVisibility(View.VISIBLE);
        next.setVisibility(View.INVISIBLE);
        previous.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        gif.setVisibility(View.VISIBLE);

        gif.setImageResource(R.drawable.error);
        description.setText("Соединение с интернетом отсутствует");
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parser.ParseJSON(cat[currentTab], pages[currentTab]);
                v.setVisibility(View.INVISIBLE);
            }
        });

    }

}