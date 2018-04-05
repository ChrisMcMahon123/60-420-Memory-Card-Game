package com.mcmah113.mcmah113memorygame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class EndGameDialog extends DialogFragment {
    public interface OnCompleteListener {
        void onComplete(Bundle callbackData);
    }

    public EndGameDialog() {

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final OnCompleteListener dialogCompleteListenerSettings = (OnCompleteListener) getActivity();

        final AlertDialog.Builder dialogWin = new AlertDialog.Builder(getActivity());

        int score = getArguments().getInt("score");
        String time = getArguments().getString("time");
        String dialogType = getArguments().getString("type");

        dialogWin.setTitle("You " + dialogType + "!!!");
        dialogWin.setMessage("Score: " + score +"\n" + "Time: " + time);
        dialogWin.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Bundle callbackData = new Bundle();
                callbackData.putString("dialog", "newGame");
                dialogCompleteListenerSettings.onComplete(callbackData);
            }
        });
        dialogWin.setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Bundle callbackData = new Bundle();
                callbackData.putString("dialog", "mainMenu");
                dialogCompleteListenerSettings.onComplete(callbackData);
            }
        });

        return dialogWin.create();
    }
}
