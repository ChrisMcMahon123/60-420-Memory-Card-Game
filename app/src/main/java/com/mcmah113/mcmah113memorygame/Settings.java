package com.mcmah113.mcmah113memorygame;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class Settings extends AppCompatActivity implements SettingsDialog.OnCompleteListener {
    private int numCardsList[];
    private String categoriesList[];
    private int selectedNumCards;
    private ArrayList<String> selectedCategories;

    private String[] elementsArray = {
            "Number of unique cards" + "\n" +
                    "Display 4, 6, 8 or 10",
            "Categories" + "\n" +
                    "Categories that will appear in the game"
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Bundle bundleExtras = getIntent().getExtras();
        numCardsList = bundleExtras.getIntArray("numCardsList");
        categoriesList = bundleExtras.getStringArray("categoriesList");
        selectedNumCards = bundleExtras.getInt("selectedNumCards");
        selectedCategories  = bundleExtras.getStringArrayList("selectedCategories");

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.layout_listview, elementsArray);

        final ListView listViewSettings = findViewById(R.id.listViewSettings);
        listViewSettings.setAdapter(adapter);
        listViewSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //list element the user selected
                Bundle args = new Bundle();

                if(position == 0) {
                    args.putIntArray("numCardsList",numCardsList);
                    args.putInt("selectedNumCards",selectedNumCards);
                    args.putString("type","numCards");
                }
                else if(position == 1) {
                    args.putStringArray("categoriesList",categoriesList);
                    args.putStringArrayList("selectedCategories",selectedCategories);
                    args.putString("type","categories");
                }

                DialogFragment settingsDialog = new SettingsDialog();
                settingsDialog.setArguments(args);
                settingsDialog.show(getFragmentManager(), "Settings Dialog");
            }
        });
    }

    public void onComplete(Bundle callbackData) {
        String dialogName = callbackData.getString("dialog");

        if(dialogName.equals("numCards")) {
            selectedNumCards = callbackData.getInt("numCardsSelected");

        }
        else if(dialogName.equals("categories")) {
            if(callbackData.getStringArrayList("categoriesSelected").size() == 0) {
                Toast.makeText(this, "No categories selected, applying default: All categories", Toast.LENGTH_LONG).show();
                selectedCategories = new ArrayList<>();
                selectedCategories.addAll(Arrays.asList(categoriesList));
            }
            else {
                selectedCategories = callbackData.getStringArrayList("categoriesSelected");
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            //android UI back button in the tab bar
            returnToMain();
        }

        return true;
    }

    public void onBackPressed() {
        //android back button has been pressed
        returnToMain();
    }

    public void returnToMain(){
        Intent intent = new Intent();
        intent.putExtra("selectedNumCards",selectedNumCards);
        intent.putExtra("selectedCategories",selectedCategories);
        setResult(RESULT_OK, intent);
        finish();
    }
}
