package com.mcmah113.mcmah113memorygame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;
import java.util.Arrays;

public class Start extends AppCompatActivity {
    private final int[] numCardsList = {4,6,8,10};
    private final String[] categoriesList = {"Fruits","Vegetables","Birds","Animals","Fish","Cars","Colours","Plants"};

    private static int selectedNumCards;
    private static ArrayList<String> selectedCategories;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        selectedNumCards = numCardsList[0];
        selectedCategories = new ArrayList<>();
        selectedCategories.addAll(Arrays.asList(categoriesList));

        final Button buttonGame = findViewById(R.id.buttonGame);
        final Button buttonSettings = findViewById(R.id.buttonSettings);
        final Button buttonExit = findViewById(R.id.buttonExit);

        buttonGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Game.class);
                intent.putExtra("numCardsList", numCardsList);
                intent.putExtra("categoriesList", categoriesList);
                intent.putExtra("selectedNumCards", selectedNumCards);
                intent.putExtra("selectedCategories", selectedCategories);
                startActivityForResult(intent,1);
            }
        });

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Settings.class);
                intent.putExtra("numCardsList", numCardsList);
                intent.putExtra("categoriesList", categoriesList);
                intent.putExtra("selectedNumCards", selectedNumCards);
                intent.putExtra("selectedCategories", selectedCategories);
                startActivityForResult(intent,1);
            }
        });

        buttonExit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.exit(0);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                selectedNumCards = data.getIntExtra("selectedNumCards",numCardsList[0]);
                selectedCategories = data.getStringArrayListExtra("selectedCategories");
            }
        }
    }
}
