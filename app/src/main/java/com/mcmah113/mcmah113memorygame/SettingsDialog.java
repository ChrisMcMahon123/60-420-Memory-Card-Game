package com.mcmah113.mcmah113memorygame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class SettingsDialog extends DialogFragment {
    public interface OnCompleteListener {
        void onComplete(Bundle callbackData);
    }

    public SettingsDialog() {

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final OnCompleteListener dialogCompleteListenerSettings = (OnCompleteListener) getActivity();

        final AlertDialog.Builder dialogSettings = new AlertDialog.Builder(getActivity());

        final LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        linearLayout.setPadding(5,5,5,5);

        String dialogType = getArguments().getString("type");

        switch (dialogType) {
            case "numCards":
                //radio group of how many unique cards to show
                final int numCardsList[] = getArguments().getIntArray("numCardsList");
                final int selectedNumCards = getArguments().getInt("selectedNumCards");

                final RadioGroup radioGroup = new RadioGroup(getContext());

                String radioText;
                for(int radioButtonOption : numCardsList) {
                    RadioButton radioButton = new RadioButton(getContext());

                    if(radioButtonOption == selectedNumCards) {
                        radioButton.setChecked(true);
                    }

                    radioText = "" + radioButtonOption + " Unique Cards";
                    radioButton.setText(radioText);
                    radioButton.setTextSize(20);
                    radioButton.setId(radioButtonOption);
                    radioGroup.addView(radioButton);
                }

                linearLayout.addView(radioGroup);

                dialogSettings.setView(linearLayout);
                dialogSettings.setTitle("Number of Choices");
                dialogSettings.setMessage("Select how many unique cards will be used");
                dialogSettings.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle callbackData = new Bundle();
                        callbackData.putString("dialog", "numCards");
                        callbackData.putInt("numCardsSelected", radioGroup.getCheckedRadioButtonId());
                        dialogCompleteListenerSettings.onComplete(callbackData);
                    }
                });
                dialogSettings.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle callbackData = new Bundle();
                        callbackData.putString("dialog", "");
                        dialogCompleteListenerSettings.onComplete(callbackData);
                    }
                });
                break;
            case "categories":
                //what categories to be used in the game
                final String categoriesList[] = getArguments().getStringArray("categoriesList");
                final ArrayList<String> selectedCategories = getArguments().getStringArrayList("selectedCategories");

                final CheckBox checkBoxArray[] = new CheckBox[categoriesList.length];

                int i = 0;
                for(String checkBoxOption : categoriesList) {
                    checkBoxArray[i] = new CheckBox(getContext());

                    if(0 <= selectedCategories.indexOf(checkBoxOption)) {
                        checkBoxArray[i].setChecked(true);
                    }

                    checkBoxArray[i].setTextSize(20);
                    checkBoxArray[i].setText(checkBoxOption);
                    linearLayout.addView(checkBoxArray[i]);
                    i++;
                }

                dialogSettings.setView(linearLayout);
                dialogSettings.setTitle("Categories");
                dialogSettings.setMessage("Select the categories the game will use");
                dialogSettings.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //get the list of checkboxes the user selected
                        ArrayList<String> categoriesSelected = new ArrayList<>();

                        for(CheckBox checkBoxItem : checkBoxArray) {
                            if(checkBoxItem.isChecked()) {
                                    categoriesSelected.add(checkBoxItem.getText().toString());
                            }
                        }

                        Bundle callbackData = new Bundle();
                        callbackData.putString("dialog", "categories");
                        callbackData.putStringArrayList("categoriesSelected", categoriesSelected);
                        dialogCompleteListenerSettings.onComplete(callbackData);
                    }
                });
                dialogSettings.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle callbackData = new Bundle();
                        callbackData.putString("dialog", "");
                        dialogCompleteListenerSettings.onComplete(callbackData);
                    }
                });
                break;
            default:
                break;
        }

        return dialogSettings.create();
    }
}
